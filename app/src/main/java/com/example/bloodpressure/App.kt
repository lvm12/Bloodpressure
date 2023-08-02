package com.example.bloodpressure

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bloodpressure.domain.BloodPressureViewModel
import com.example.bloodpressure.presentation.components.ActionsScreen
import com.example.bloodpressure.presentation.components.AddRecord
import com.example.bloodpressure.presentation.components.ExportAsCsvScreen
import com.example.bloodpressure.presentation.components.RecordsListScreen

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
    }
}