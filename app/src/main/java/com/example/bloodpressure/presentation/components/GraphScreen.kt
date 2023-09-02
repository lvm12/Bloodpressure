package com.example.bloodpressure.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.data.Record
import com.example.bloodpressure.presentation.components.graph.Graph
import com.example.bloodpressure.presentation.components.graph.toDateNoYears

@Composable
fun GraphScreen(
    records: List<Record>,
    onEvent: (BloodPressureEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.Center
        ) {
            if(records.isNotEmpty()) {
                Graph(
                    records = records,
                    modifier = Modifier.fillMaxSize()
                )
            }else{
                Text(
                    text = "No data to display",
                    fontSize = 24.sp
                )
            }
        }
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ){
            Spacer(modifier = Modifier
                .height(30.dp)
            )
            Row {
                Spacer(modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .weight(1f)
                )
                KeyBox(
                    string = "Systolic pressure",
                    color = Color.Green,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            Row {
                Spacer(modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .weight(1f)
                )
                KeyBox(
                    string = "Diastolic pressure",
                    color = Color.Red,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            Row {
                Spacer(modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .weight(1f)
                )
                KeyBox(
                    string = "Pulse",
                    color = Color.Blue,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            Spacer(modifier = Modifier
                .height(20.dp)
            )
            if(records.isNotEmpty()) {
                Row {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .weight(1f)
                    )
                    Text(
                        text = "Results from ${records[0].createdAt.toDateNoYears()} until ${records.last().createdAt.toDateNoYears()}"
                    )
                }
            }
        }
        Box (
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ){
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "close",
                modifier = Modifier.clickable {
                    onEvent(BloodPressureEvent.DismissRecord)
                }
            )
        }
    }
}

@Composable
private fun RowScope.KeyBox(
    string: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row{
        Box(
            modifier = Modifier
                .height(16.dp)
                .width(16.dp)
                .background(
                    color = color
                )
        )
        Spacer(modifier = Modifier
            .width(8.dp)
        )
        Text(
            text = string,
            fontSize = 16.sp
        )
    }
}