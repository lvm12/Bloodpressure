package com.example.bloodpressure.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodpressure.data.Record
import kotlinx.datetime.Clock
import java.text.SimpleDateFormat
import java.util.Date

fun Long.toDate(): String{
    val date: Date = Date(this)
    val format = SimpleDateFormat("dd.MM.yyyy")
    return format.format(date)
}

@Composable
fun RecordItem(
    record: Record,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = record.createdAt.toDate(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
            )
            Spacer(
                modifier = Modifier
                    .width(20.dp)
            )
            Text(
                text = "SBP: ${record.systolicPressure}",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            )
            Spacer(
                modifier = Modifier
                    .width(20.dp)
            )
            Text(
                text = "DBP: ${record.diastolicPressure}",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            )
            Spacer(
                modifier = Modifier
                    .width(20.dp)
            )
            Text(
                text = "Pulse: ${record.pulse}",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            )
        }
        Spacer(
            modifier = modifier
                .height(4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            Spacer(
                modifier = Modifier
                    .width(16.dp)
            )
            Divider(
                modifier = Modifier
                    .weight(1f),
                color = MaterialTheme.colorScheme.onSurface,
                thickness = 1.dp
            )
            Spacer(
                modifier = Modifier
                    .width(16.dp)
            )
        }
        Spacer(modifier = Modifier
            .height(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecordItemPreview() {
    RecordItem(record  = Record(
            systolicPressure = "144",
    diastolicPressure = "80",
        pulse = "60",
    id = 0,
    createdAt = Clock.System.now().toEpochMilliseconds()
    ))
}