package com.example.bloodpressure.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bloodpressure.data.BloodPressureEvent

@Composable
fun ActionsScreen(
    modifier: Modifier = Modifier,
    onEvent: (BloodPressureEvent) -> Unit
) {
    var showSnackbar by remember{ mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = "Close",
            modifier = Modifier
                .clickable {
                    onEvent(BloodPressureEvent.DismissRecord)
                }
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier
                .height(100.dp)
            )
            Button(
                onClick = {
                    onEvent(BloodPressureEvent.OnAddNewRecordClicked)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier
                    .width(8.dp)
                )
                Text(
                    text = "Add record"
                )
            }
            Spacer(modifier = Modifier
                .height(8.dp)
            )
            FilledTonalButton(
                onClick = {
                    onEvent(BloodPressureEvent.OnExportAsCSVClicked)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Export as CSV"
                )
            }
            Spacer(modifier = Modifier
                .height(8.dp)
            )
            FilledTonalButton(
                onClick = {
                    onEvent(BloodPressureEvent.ArchiveRecord(null))
                    showSnackbar = true
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Mark exported records as archived")
            }
            Spacer(modifier = Modifier
                .height(8.dp)
            )
            FilledTonalButton(
                onClick = {
                    onEvent(BloodPressureEvent.DeleteArchived)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Delete archived records")
            }
            Spacer(modifier = Modifier
                .height(8.dp)
            )
            FilledTonalButton(
                onClick = {
                    onEvent(BloodPressureEvent.OnGraphScreenClicked)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Graphs")
            }
        }
        if(showSnackbar) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Snackbar(
                    modifier = Modifier.fillMaxWidth(),
                    dismissAction = {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            modifier = Modifier
                                .clickable { 
                                    showSnackbar = !showSnackbar
                                }
                        )
                    }
                ) {
                    Text(text = "Completing action")
                }
            }
        }
    }
}