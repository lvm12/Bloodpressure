package com.example.bloodpressure.presentation.components

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.data.csv.Permissions
import java.io.IOException


@Composable
fun PermissionRequestCard(
    onEvent:(BloodPressureEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){
        try {
            val uri = it.data?.data ?: throw IOException()
            val takeFlags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, takeFlags)
            onEvent(BloodPressureEvent.setUri(uri))
            onEvent(BloodPressureEvent.PermissionProvided(true))
        }catch (_:Exception){}
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
                    launcher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE))
                }
            ) {
                Text(
                    text = "Give folder access"
                )
            }
        }
    }
}