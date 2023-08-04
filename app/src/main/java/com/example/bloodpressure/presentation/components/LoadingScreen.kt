package com.example.bloodpressure.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        Spacer(modifier = Modifier
            .height(150.dp)
        )
        Text("Executing actions")
        Spacer(modifier = Modifier
            .height(20.dp)
        )
        DotsPulsing(dotSize = 20.dp, delayUnit = 300)
    }
}