package com.example.bloodpressure.domain

import android.content.Context
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.data.CSVGenerationStatus
import com.example.bloodpressure.data.Record
import com.example.bloodpressure.data.RecordStatus
import com.example.bloodpressure.data.csv.CSV
import com.example.bloodpressure.data.sql.records.RecordsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class BloodPressureViewModel(
    private val repository: RecordsRepository,
    val navController: NavHostController,
    val context: Context,
): ViewModel() {
    private val TAG = "ViewModel"

    private val _state = MutableStateFlow(BloodPressureState())
    private val _selectedState = MutableStateFlow(SelectedState())

    val state = combine(
        _state,
        _selectedState,
    ){state, selectedState ->
        state.copy(
            records = repository.getRequiredRecords(selectedState.selectedRecords).first(),
            isNewSelected = selectedState.isNewSelected,
            isExportedSelected = selectedState.isExportedSelected,
            isArchivedSelected = selectedState.isArchivedSelected,
            hasNewBeenClicked = selectedState.hasNewBeenClicked
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), BloodPressureState())

    fun setNewToFalse(){
        _selectedState.update {
            it.copy(
                hasNewBeenClicked = false,
                isNewSelected = false
            )
        }
    }

    var newRecord: Record? by mutableStateOf(null)
        private set

    fun setPermissionValue(bool: Boolean){
        _state.update { it.copy(
            hasPermission = bool
        ) }
    }

    @RequiresApi(30)
    fun onEvent(event: BloodPressureEvent){
        when(event){
            BloodPressureEvent.DeleteRecord -> {
                viewModelScope.launch {
                    _state.value.selectedRecord?.let { record->
                        _state.update { it.copy(
                            isSelectedRecordSheetOpen = false,
                            selectedRecord = null
                        ) }
                        repository.deleteRecord(record)
                    }
                }
            }
            BloodPressureEvent.DismissRecord -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        isSelectedRecordSheetOpen = false,
                        isAddRecordSheetOpen = false,
                        selectedRecord = null,
                        systolicError = null,
                        diastolicError = null,
                        pulseError = null
                    ) }
                }
                navController.navigate("start-screen")
                _selectedState.update {
                    it.copy(
                        hasNewBeenClicked = false,
                        isNewSelected = false
                    )
                }
            }
            is BloodPressureEvent.EditRecord -> {
                _state.update {it.copy(
                    selectedRecord = null,
                    isSelectedRecordSheetOpen = false,
                )}
                newRecord = event.record
                navController.navigate("add-record-screen")
            }
            BloodPressureEvent.OnAddNewRecordClicked -> {
                newRecord = Record(
                    systolicPressure = "",
                    diastolicPressure = "",
                    pulse = "",
                )
                navController.navigate("add-record-screen")
            }
            is BloodPressureEvent.OnDiastolicPressureChanged -> {
                newRecord = newRecord?.copy(
                    diastolicPressure = event.value
                )
            }
            BloodPressureEvent.OnExportAsCSVClicked -> {

                _state.update { it.copy(
                    selectedRecord = null,
                    isSelectedRecordSheetOpen = false,
                    generatingCsv = CSVGenerationStatus.GENERATING
                ) }

                val csv = CSV(
                    state = _state.value,
                    onEvent = this::onEvent
                )
                viewModelScope.launch {
                    supervisorScope {
                        repository.exportedRecords.first().forEach {
                            async { repository.insertNewRecord(
                                it.copy(
                                    recordStatus = RecordStatus.ARCHIVED
                                )
                            ) }
                        }
                    }
                    csv.generateData()
                    csv.saveData(context = context)
                }

                navController.navigate("export-as-csv")
            }
            is BloodPressureEvent.OnPulseChanged -> {
                newRecord = newRecord?.copy(
                    pulse = event.value
                )
            }
            is BloodPressureEvent.OnSystolicPressureChanged -> {
                newRecord = newRecord?.copy(
                    systolicPressure = event.value
                )
            }
            BloodPressureEvent.SaveRecord -> {
                newRecord?.let{record->
                    val result = RecordValidator.validateRecord(record)
                    val errors = listOfNotNull(
                        result.pulseError,
                        result.diastolicPressureError,
                        result.systolicPressureError
                    )

                    if(errors.isEmpty()){
                        _state.update {it.copy(
                            systolicError = null,
                            diastolicError = null,
                            pulseError = null,
                            isSelectedRecordSheetOpen = false
                        ) }

                        navController.navigate("start-screen")
                        _selectedState.update {
                            it.copy(
                                hasNewBeenClicked = false,
                                isNewSelected = true
                            )
                        }

                        viewModelScope.launch {
                            repository.insertNewRecord(record)
                            newRecord = null
                        }
                    }else{
                        _state.update { it.copy(
                            diastolicError = result.diastolicPressureError,
                            pulseError = result.pulseError,
                            systolicError = result.systolicPressureError
                        ) }
                    }
                }
            }
            is BloodPressureEvent.SelectRecord -> {
                _state.update{it.copy(
                    selectedRecord = event.record,
                    isSelectedRecordSheetOpen = true
                )}
            }

            BloodPressureEvent.OnActionsClicked -> {
                navController.navigate("actions-page")
                _state.update { it.copy(
                    selectedRecord = null,
                    isSelectedRecordSheetOpen = false,
                ) }
            }
            BloodPressureEvent.OnDoneClickedCSV -> {
                navController.navigate("start-screen")
                _selectedState.update {
                    it.copy(
                        hasNewBeenClicked = false,
                        isNewSelected = false
                    )
                }
                viewModelScope.launch {
                    delay(1000)
                    _state.update {
                        it.copy(
                            generatingCsv = CSVGenerationStatus.NOT
                        )
                    }
                }
            }

            is BloodPressureEvent.ONCSVGenerated -> {
                _state.update { it.copy(
                    generatingCsv = event.status
                ) }
            }

            is BloodPressureEvent.PermissionProvided -> {
                _state.update { it.copy(
                    hasPermission = event.value
                ) }
            }

            is BloodPressureEvent.ArchivedSelected -> {
                _selectedState.update {state->
                    state.copy(
                        isArchivedSelected = event.value,
                        selectedRecords =
                            if(event.value){state.selectedRecords + RecordStatus.ARCHIVED}
                            else{state.selectedRecords - RecordStatus.ARCHIVED}
                    )
                }
                Log.d(TAG, _selectedState.value.selectedRecords.toString())
            }
            is BloodPressureEvent.ExportedSelected -> {
                _selectedState.update { state->
                    state.copy(
                        isExportedSelected = event.value,
                        selectedRecords =
                            if(event.value) {state.selectedRecords + RecordStatus.EXPORTED}
                            else {state.selectedRecords - RecordStatus.EXPORTED}
                    )
                }
                Log.d(TAG, _selectedState.value.selectedRecords.toString())
            }
            is BloodPressureEvent.NewSelected -> {
                _selectedState.update { state->
                    state.copy(
                        isNewSelected = event.value,
                        selectedRecords =
                            if(event.value){state.selectedRecords + RecordStatus.NEW}
                            else{state.selectedRecords - RecordStatus.NEW}
                    )
                }
                Log.d(TAG, _selectedState.value.selectedRecords.toString())
            }

            is BloodPressureEvent.ArchiveRecord -> {
                if(event.record == null) {
                    viewModelScope.launch {
                        repository.exportedRecords.first().forEach {
                            Log.d(TAG, "${it.id}")
                            repository.insertNewRecord(record =
                                it.copy(
                                    recordStatus = RecordStatus.ARCHIVED
                                )
                            )
                        }
                    }
                }else if(event.record.recordStatus != RecordStatus.ARCHIVED){
                    _state.update {
                        it.copy(
                            isSelectedRecordSheetOpen = false,
                        )
                    }
                    viewModelScope.launch {
                        _state.value.selectedRecord?.let {
                            repository.insertNewRecord(
                                record = it.copy(
                                    recordStatus = RecordStatus.ARCHIVED
                                )
                            )
                        }
                        delay(300) //Animation delay
                        _state.update {
                            it.copy(
                                selectedRecord = null
                            )
                        }
                    }
                }else if(event.record.recordStatus == RecordStatus.ARCHIVED){
                    _state.update {
                        it.copy(
                            isSelectedRecordSheetOpen = false
                        )
                    }
                    viewModelScope.launch{
                        _state.value.selectedRecord?.let{
                            repository.insertNewRecord(
                                record = it.copy(
                                    recordStatus = RecordStatus.NEW
                                )
                            )
                        }
                        delay(300)
                        _state.update {
                            it.copy(
                                selectedRecord = null
                            )
                        }
                    }
                }
            }

            BloodPressureEvent.DeleteArchived -> {
                viewModelScope.launch {
                    repository.archivedRecords.first().forEach {
                        Log.d(TAG, "${it.id}")
                        repository.deleteRecord(it)
                    }
                }
            }

            BloodPressureEvent.SetNew -> {
                _selectedState.update {
                    it.copy(
                        hasNewBeenClicked = true,
                        isNewSelected = true
                    )
                }
            }
        }
    }
}

