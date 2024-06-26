package com.example.bloodpressure.data.csv

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.data.sql.records.RecordsRepository
import com.example.bloodpressure.domain.SelectedState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.use
import kotlin.io.path.createFile
import kotlin.io.path.deleteExisting


class CSV (
    private val state: SelectedState,
    private val repository: RecordsRepository,
    val onEvent: (BloodPressureEvent) -> Unit
){
    private val TAG = "CSV"
    private var csvData: String = ""

    suspend fun generateData(){
        csvData += "IND,DAT,SYS,DIA,PUL,COM\n"
        val records = repository.getRequiredRecords(state.selectedRecords).first()
        records.forEachIndexed{index, i ->
            csvData += "${index.toString()},"
            csvData += "${i.createdAt.toCSVDate()},"
            csvData += "${i.systolicPressure},"
            csvData += "${i.diastolicPressure},"
            csvData += "${i.pulse},"
            csvData += "${
                if (i.comment.isNotEmpty()) i.comment
                else "N/A"
            }\n"
        }
        Log.d(TAG, "Data is $csvData")
    }

    @RequiresApi(30)
    fun saveData(
        context: Context
    ) {
        Log.d(TAG, "Entered function")
        CoroutineScope(Dispatchers.Default).launch {
            Log.d(TAG, "Entered Coroutine")
            try {
                Log.d(TAG, repository.getUri().toString())
                val uri = repository.getUri()[0].uri.toUri()
                val root = DocumentFile.fromTreeUri(context, uri) ?: throw IOException()
                val file = root.createFile(
                    "text/csv",
                    Clock.System.now().toEpochMilliseconds().fileDate()
                ) ?: throw IOException()
                Log.d(TAG, "Created file")
                try {
                    val output = context.contentResolver.openOutputStream(file.uri) ?: throw IOException()
                    output.use {
                        it.write(csvData.toByteArray())
                    }
                    onEvent(BloodPressureEvent.ONCSVGenerated(CSVGenerationStatus.SUCCESSFUL))
                } catch (e: FileNotFoundException) {
                    var bool = false
                    try {
                        bool = root.createFile(
                            "text/csv",
                            "${Clock.System.now().toEpochMilliseconds().fileDate()}.csv"
                        ) != null
                    } catch (ex: IOException) {
                        Log.d(TAG, "Failed")
                        ex.printStackTrace()
                    }

                    if (bool) {
                        saveData(context)
                    } else {
                        onEvent(BloodPressureEvent.ONCSVGenerated(CSVGenerationStatus.FAILED))
                    }
                }
            } catch (exc: java.nio.file.FileAlreadyExistsException) {
                onEvent(BloodPressureEvent.ONCSVGenerated(CSVGenerationStatus.RESTART_DEVICE))
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}

private fun Long.fileDate(): String{
    val date = Date(this)
    val format = SimpleDateFormat("yyyy_MM_dd_HH_mm", Locale.ENGLISH)
    return format.format(date)
}

private fun Long.toCSVDate(): String{
    val date = Date(this)
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    return format.format(date)
}