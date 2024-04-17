package com.example.bloodpressure

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.bloodpressure.data.sql.records.RecordsDatabase
import com.example.bloodpressure.data.sql.records.RecordsRepository
import com.example.bloodpressure.domain.BloodPressureViewModel
import com.example.bloodpressure.domain.BloodPressureViewModelFactory
import com.example.bloodpressure.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val database by lazy{ RecordsDatabase.getDatabase(this)}
            val repository by lazy{ RecordsRepository(database.dao()) }
            val factory = BloodPressureViewModelFactory(
                repository = repository,
                navController = navController,
                application = application,
            )
            val viewModel  = ViewModelProvider(this, factory)[BloodPressureViewModel::class.java]
            LaunchedEffect(key1 = database) {
                viewModel.setPermissionValue(repository.getUri().isNotEmpty())
            }

            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

