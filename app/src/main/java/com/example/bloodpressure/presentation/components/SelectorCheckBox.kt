package com.example.bloodpressure.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.domain.BloodPressureState

@Composable
fun SelectorCheckBox(
    state: BloodPressureState,
    onEvent: (BloodPressureEvent) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Show:",
            fontSize = 10.sp
        )
    }
    Row {
        ShowCheckBox(
            state = state.isNewSelected,
            onCheckedChange = {onEvent(BloodPressureEvent.NewSelected(it))},
            text = ":New"
        )
        ShowCheckBox(
            state = state.isExportedSelected,
            onCheckedChange = {onEvent(BloodPressureEvent.ExportedSelected(it))},
            text = ":Exported"
        )
        ShowCheckBox(
            state = state.isArchivedSelected,
            onCheckedChange = {onEvent(BloodPressureEvent.ArchivedSelected(it))},
            text = ":Archived"
        )
    }
}

@Composable
fun ShowCheckBox(
    state: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    text: String
) {
    Row (verticalAlignment = Alignment.CenterVertically){
        Checkbox(
            checked = state,
            onCheckedChange = {
                onCheckedChange(it)
            },
            modifier = modifier
        )
        Text(
            text = text,
            fontSize = 10.sp
        )
    }
}