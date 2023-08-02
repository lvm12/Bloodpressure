package com.example.bloodpressure.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.data.Record
import com.example.bloodpressure.domain.BloodPressureState

@Composable
fun AddRecord(
    state: BloodPressureState,
    newRecord: Record?,
    onEvent: (BloodPressureEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
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
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .height(100.dp)
            )
            Text(
                text = "Enter details:",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(20.dp))
            RecordTextField(
                value = newRecord?.systolicPressure ?: "",
                placeholder = "Systolic pressure",
                error = state.systolicError,
                onValueChanged = {
                    onEvent(BloodPressureEvent.OnSystolicPressureChanged(it))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))
            RecordTextField(
                value = newRecord?.diastolicPressure ?: "",
                placeholder = "Diastolic pressure",
                error = state.diastolicError,
                onValueChanged = {
                    onEvent(
                        BloodPressureEvent
                            .OnDiastolicPressureChanged(it)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))
            RecordTextField(
                value = newRecord?.pulse ?: "",
                placeholder = "Pulse",
                error = state.pulseError,
                onValueChanged = {
                    onEvent(
                        BloodPressureEvent
                            .OnPulseChanged(it)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    onEvent(
                        BloodPressureEvent.SaveRecord
                    )
                }
            ){
                Text(
                    text = "Save record"
                )
            }
        }
    }
}

@Composable
private fun RecordTextField (
    value: String,
    placeholder: String,
    error: String?,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
    ){
        OutlinedTextField(
            value = value,
            onValueChange = onValueChanged,
            placeholder = {
                Text(
                    text = placeholder
                )
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        if(error != null){
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}