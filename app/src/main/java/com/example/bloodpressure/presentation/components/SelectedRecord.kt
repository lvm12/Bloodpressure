package com.example.bloodpressure.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.NewLabel
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.data.Record
import com.example.bloodpressure.data.RecordStatus


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedRecordSheet(
    record: Record?,
    onEvent: (BloodPressureEvent) -> Unit,
    modifier: Modifier = Modifier
) {
   ModalBottomSheet(
       onDismissRequest = {
           onEvent(BloodPressureEvent.DismissRecord)
       },
       modifier = modifier
   ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ){
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close sheet",
                    modifier = Modifier.clickable {
                        onEvent(BloodPressureEvent.DismissRecord)
                    }
                )
                Text(
                    text = "Date: ${record?.createdAt?.toDate()}",
                    fontSize = 24.sp
                )
                Text(
                    text = "SYS: ${record?.systolicPressure}",
                    fontSize = 24.sp
                )
                Text(
                    text = "DIA: ${record?.diastolicPressure}",
                    fontSize = 24.sp
                )
                Text(
                    text = "PUL: ${record?.pulse}",
                    fontSize = 24.sp
                )
                Text(
                    text = "Status: ${
                        when(record?.recordStatus){
                            RecordStatus.NEW -> "NEW"
                            RecordStatus.EXPORTED -> "EXPORTED"
                            RecordStatus.ARCHIVED -> "ARCHIVED"
                            RecordStatus.ERROR -> "ERROR"
                            null -> "Bit odd"
                        }
                    }",
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic
                )
                Row {
                    Button(
                        onClick = {
                            record?.let {
                                onEvent(BloodPressureEvent.EditRecord(it))
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit record",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(
                            modifier = Modifier
                                .width(8.dp)
                        )
                        Text(
                            text = "Edit record"
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    FilledTonalButton(
                        onClick = {
                            record?.let{
                                onEvent(
                                    BloodPressureEvent.DeleteRecord
                                )
                            }
                        }
                    ){
                        Icon(
                            imageVector =
                                Icons.Rounded.DeleteForever,
                            contentDescription =
                                "Delete"
                        )
                        Spacer(
                            modifier = Modifier
                                .width(8.dp)
                        )
                        Text(
                            text =
                                "Delete record"
                        )
                    }
                }
                if(record?.recordStatus == RecordStatus.ARCHIVED) {
                    Spacer(
                        modifier = Modifier
                            .height(8.dp)
                    )
                    FilledTonalButton(
                        onClick = {
                            onEvent(BloodPressureEvent.ArchiveRecord(record))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.NewLabel,
                            contentDescription = "De-Archive record"
                        )
                        Spacer(modifier = Modifier
                            .width(8.dp)
                        )
                        Text(
                            text = "Set status to new"
                        )
                    }
                }
            }
        }
   }
}

