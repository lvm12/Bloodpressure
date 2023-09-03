package com.example.bloodpressure.domain

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.data.csv.CSVGenerationStatus
import com.example.bloodpressure.data.Record
import com.example.bloodpressure.data.RecordStatus
import com.example.bloodpressure.data.RecordUpdateStatus
import com.example.bloodpressure.data.csv.CSV
import com.example.bloodpressure.data.sql.records.RecordsRepository
import com.example.bloodpressure.data.sql.records.RecordsUpdater
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BloodPressureViewModel(
    private val repository: RecordsRepository,
    val navController: NavHostController,
    private val application: Application,
): ViewModel() {

    private val _state = MutableStateFlow(BloodPressureState())
    private val _selectedState = MutableStateFlow(SelectedState())

    @RequiresApi(Build.VERSION_CODES.R)
    private val recordUpdater = RecordsUpdater(
        repository = repository,
        state = _state.value,
        onEvent = this::onEvent
    )

    val state = combine(
        _state,
        _selectedState,
    ){state, selectedState ->
        state.copy(
            records = repository.getRequiredRecords(selectedState.selectedRecords).first(),
            allRecords = repository.allRecords.first(),
            isNewSelected = selectedState.isNewSelected,
            isExportedSelected = selectedState.isExportedSelected,
            isArchivedSelected = selectedState.isArchivedSelected,
            hasNewBeenClicked = selectedState.hasNewBeenClicked,
            selectedRecordStatus = selectedState.selectedRecords
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), BloodPressureState())

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
                    comment = ""
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
                    state = _selectedState.value,
                    repository = repository,
                    onEvent = this::onEvent
                )
                viewModelScope.launch {
                    csv.generateData()
                    csv.saveData(context = application.applicationContext)
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
                _state.update {
                    it.copy(
                        recordUpdateStatus = RecordUpdateStatus.EXECUTING
                    )
                }
                navController.navigate("loading-screen")
                _selectedState.update {
                    it.copy(
                        hasNewBeenClicked = false,
                        isNewSelected = false
                    )
                }
                if(_state.value.generatingCsv != CSVGenerationStatus.FAILED) {
                    viewModelScope.launch {
                        recordUpdater.exportNew()
                    }
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
                            else{state.selectedRecords - RecordStatus.ARCHIVED},
                        hasNewBeenClicked = true
                    )
                }
            }
            is BloodPressureEvent.ExportedSelected -> {
                _selectedState.update { state->
                    state.copy(
                        isExportedSelected = event.value,
                        selectedRecords =
                            if(event.value) {state.selectedRecords + RecordStatus.EXPORTED}
                            else {state.selectedRecords - RecordStatus.EXPORTED},
                        hasNewBeenClicked = true
                    )
                }
            }
            is BloodPressureEvent.NewSelected -> {
                _selectedState.update { state->
                    state.copy(
                        isNewSelected = event.value,
                        selectedRecords =
                            if(event.value){state.selectedRecords + RecordStatus.NEW}
                            else{state.selectedRecords - RecordStatus.NEW},
                        hasNewBeenClicked = true
                    )
                }
            }

            is BloodPressureEvent.ArchiveRecord -> {
                if(event.record == null) {
                    _state.update {
                        it.copy(
                            recordUpdateStatus = RecordUpdateStatus.EXECUTING
                        )
                    }
                    navController.navigate("loading-screen")
                    viewModelScope.launch {
                        recordUpdater.archiveExported()
                    }
                }else if(event.record.recordStatus != RecordStatus.ARCHIVED){
                    _state.update {
                        it.copy(
                            isSelectedRecordSheetOpen = false,
                        )
                    }
                    viewModelScope.launch {
                        recordUpdater.archiveRecord()
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
                        recordUpdater.unArchiveRecord()
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
                        recordUpdater.deleteAllArchived()
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

            is BloodPressureEvent.RecordUpdaterStatus -> {
                navController.navigate("start-screen")
                _state.update {
                    it.copy(
                        recordUpdateStatus = RecordUpdateStatus.DONE
                    )
                }
                _selectedState.update {
                    it.copy(
                        selectedRecords = listOf(RecordStatus.ARCHIVED,RecordStatus.NEW,RecordStatus.EXPORTED),
                        isArchivedSelected = true,
                        isExportedSelected = true,
                        isNewSelected = true
                    )
                }
            }

            BloodPressureEvent.OnGraphScreenClicked -> {
                navController.navigate("graph-screen")
                _state.update {
                    it.copy(
                        selectedRecord = null,
                        isSelectedRecordSheetOpen = false,
                    )
                }
            }

            is BloodPressureEvent.OnCommentChanged -> {
                newRecord = newRecord?.copy(
                    comment = event.value
                )
            }
        }
    }
}

