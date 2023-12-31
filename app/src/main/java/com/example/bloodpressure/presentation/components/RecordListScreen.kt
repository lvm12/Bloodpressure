package com.example.bloodpressure.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.data.Record
import com.example.bloodpressure.data.RecordStatus
import com.example.bloodpressure.domain.BloodPressureState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RecordsListScreen(
    state: BloodPressureState,
    onEvent: (BloodPressureEvent) -> Unit
) {
    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    onEvent(BloodPressureEvent.OnActionsClicked)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = "Actions"
                )
            }
        },
        topBar = {
            SelectorCheckBox(
                state = state,
                onEvent = onEvent
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(BloodPressureEvent.OnAddNewRecordClicked)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add person",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(50.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 4.dp),
                    ) {
                        item {
                            Text(
                                text = "Records (${
                                    listChecker(state).size
                                }):",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(listChecker(state)) { record ->
                            RecordItem(
                                record = record,
                                modifier = Modifier
                                    .clickable {
                                        onEvent(BloodPressureEvent.SelectRecord(record))
                                    }
                            )
                        }
                    }
                }
            }
    }
    if(state.isSelectedRecordSheetOpen){
        SelectedRecordSheet(
            record = state.selectedRecord,
            onEvent = onEvent)
    }
}

private fun listChecker(state: BloodPressureState): List<Record>{
    return if(state.selectedRecordStatus.containsAll(listOf( RecordStatus.NEW,RecordStatus.ARCHIVED,RecordStatus.EXPORTED))){
        state.allRecords
    }else{
        state.records
    }
}