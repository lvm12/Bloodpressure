package com.example.bloodpressure.domain

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.bloodpressure.data.sql.records.RecordsRepository

class BloodPressureViewModelFactory(
    private val repository: RecordsRepository,
    private val navController: NavHostController,
    private val application: Application,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BloodPressureViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return BloodPressureViewModel(repository, navController, application) as T
        }
        throw(IllegalArgumentException("View Model does not exist"))
    }
}