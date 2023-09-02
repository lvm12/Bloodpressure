package com.example.bloodpressure.data.csv

import android.content.Context
import android.os.Environment
import androidx.annotation.RequiresApi
import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.data.sql.records.RecordsRepository
import com.example.bloodpressure.domain.SelectedState
import com.example.bloodpressure.presentation.components.toDate
import kotlinx.coroutines.flow.first
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
    private var csvData: String = ""

    suspend fun generateData(){
        csvData += "DAT, SYS, DIA, PUL\n"
        val records = repository.getRequiredRecords(state.selectedRecords).first()
        for(i in records){
            csvData += "${i.createdAt.toDate()},"
            csvData += "${i.systolicPressure},"
            csvData += "${i.diastolicPressure},"
            csvData += "${i.pulse}\n"
        }
    }

    @RequiresApi(30)
    fun saveData(
        context: Context
    ) {
            try {
                val path = Path(
                    path = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/BloodPressureResults/${
                        Clock.System.now().toEpochMilliseconds().fileDate()
                    }.csv"

                )
                path.createDirectories()
                path.deleteExisting()



                val root: File = path.createFile().toFile()

                try {
                    val fout = root.bufferedWriter()

                    fout.use {
                        it.write(csvData)
                    }

                    onEvent(BloodPressureEvent.ONCSVGenerated(CSVGenerationStatus.SUCCESSFUL))
                } catch (e: FileNotFoundException) {
                    var bool = false
                    try {
                        path.createFile()
                        bool = true
                    } catch (ex: IOException) {
                    }

                    if (bool) {
                        saveData(context)
                    } else {
                        onEvent(BloodPressureEvent.ONCSVGenerated(CSVGenerationStatus.FAILED))
                    }
                }
            }catch (exc: java.nio.file.FileAlreadyExistsException){
                onEvent(BloodPressureEvent.ONCSVGenerated(CSVGenerationStatus.RESTART_DEVICE))
            }
        }
}

private fun Long.fileDate(): String{
    val date = Date(this)
    val format = SimpleDateFormat("yyyy_MM_dd_HH_mm", Locale.ENGLISH)
    return format.format(date)
}