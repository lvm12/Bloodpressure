package com.example.bloodpressure.presentation.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.domain.BloodPressureState


@Composable
fun PermissionRequestCard(
    state: BloodPressureState,
    onEvent:(BloodPressureEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onEvent(BloodPressureEvent.PermissionProvided(true))
        } else {
            onEvent(BloodPressureEvent.PermissionProvided(false))
        }
    }

    Card(
        modifier = modifier
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier
                .height(60.dp)
            )
            Text(
                text = "Please allow permission to export data as CSV",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier
                .height(40.dp)
            )
            Button(
                onClick = {
                    launcher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            ) {
                Text(
                    text = "Grant permission"
                )
            }
        }
    }
}