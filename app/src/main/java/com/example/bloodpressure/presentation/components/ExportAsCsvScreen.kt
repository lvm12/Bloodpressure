package com.example.bloodpressure.presentation.components

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrokenImage
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.FileDownloadDone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.data.CSVGenerationStatus
import com.example.bloodpressure.domain.BloodPressureState

@Composable
fun ExportAsCsvScreen(
    state: BloodPressureState,
    onEvent: (BloodPressureEvent) -> Unit
) {
    if(!state.hasPermission){
        PermissionRequestCard(state = state, onEvent = onEvent, modifier = Modifier.fillMaxWidth())
    }

    if(state.hasPermission) {
        Spacer(
            modifier = Modifier
                .height(60.dp)
        )
        if(state.generatingCsv == CSVGenerationStatus.RESTART_DEVICE){
            Card {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "URI error, will be fixed by restarting your device",
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier
                        .height(20.dp)
                    )
                    Text(
                        text = "If you are trying to create a new version of this file on the same day, delete the file if it exists. Then restart your device and try again.",
                        textAlign = TextAlign.Justify,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier
                        .height(20.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            Icon(
                imageVector =
                if (state.generatingCsv == CSVGenerationStatus.GENERATING) Icons.Rounded.FileDownload
                else if (state.generatingCsv == CSVGenerationStatus.SUCCESSFUL) Icons.Rounded.FileDownloadDone
                else if (state.generatingCsv == CSVGenerationStatus.FAILED) Icons.Rounded.BrokenImage
                else Icons.Rounded.BrokenImage,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
            )
            Spacer(
                modifier = Modifier.height(20.dp)
            )
            Text(
                text =
                if (state.generatingCsv == CSVGenerationStatus.GENERATING) "Generating CSV"
                else if (state.generatingCsv == CSVGenerationStatus.SUCCESSFUL) "CSV generated"
                else if (state.generatingCsv == CSVGenerationStatus.FAILED) "CSV failed"
                else if (state.generatingCsv == CSVGenerationStatus.RESTART_DEVICE) "ERROR: Restart device"
                else "This shouldn't happen",
                fontSize = 24.sp
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
            if (state.generatingCsv == CSVGenerationStatus.GENERATING) {
                DotsPulsing(
                    dotSize = 20.dp,
                    delayUnit = 300
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .height(52.dp)
                )
                Button(
                    onClick = {
                        onEvent(BloodPressureEvent.OnDoneClickedCSV)
                    }
                ) {
                    Text(text = "Done")
                }
            }
        }
    }
}