package com.example.bloodpressure

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bloodpressure.domain.BloodPressureViewModel
import com.example.bloodpressure.presentation.components.ActionsScreen
import com.example.bloodpressure.presentation.components.AddRecord
import com.example.bloodpressure.presentation.components.ExportAsCsvScreen
import com.example.bloodpressure.presentation.components.GraphScreen
import com.example.bloodpressure.presentation.components.LoadingScreen
import com.example.bloodpressure.presentation.components.RecordsListScreen

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun App(
    viewModel: BloodPressureViewModel
) {

    val state by viewModel.state.collectAsState()

    NavHost(
        navController = viewModel.navController,
        startDestination = "start-screen",
    ){
        composable(
            route = "start-screen"
        ){
            RecordsListScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = "add-record-screen"
        ){
            AddRecord(
                state = state,
                newRecord = viewModel.newRecord,
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = "export-as-csv"
        ){
            ExportAsCsvScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = "actions-page"
        ){
            ActionsScreen(
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = "loading-screen"
        ){
            LoadingScreen()
        }
        composable(
            route = "graph-screen"
        ){
            GraphScreen(
                records = state.records,
                onEvent = viewModel::onEvent
            )
        }
    }
}