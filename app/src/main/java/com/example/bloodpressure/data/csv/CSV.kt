package com.example.bloodpressure.data.csv

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.data.CSVGenerationStatus
import com.example.bloodpressure.data.Record
import com.example.bloodpressure.domain.BloodPressureState
import com.example.bloodpressure.presentation.components.toDate
import kotlinx.datetime.Clock
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.use
import kotlin.io.path.createFile
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists


class CSV (
    private val state: BloodPressureState,
    val onEvent: (BloodPressureEvent) -> Unit
){
    private var csvData: String = ""

    fun generateData(){
        csvData += "DAT, SYS, DIA, PUL\n"
        for(i in state.records){
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
                        Clock.System.now().toEpochMilliseconds().toDate()
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
                    e.printStackTrace()
                    var bool: Boolean = false
                    try {
                        path.createFile()
                        bool = true
                    } catch (ex: IOException) {
                        ex.printStackTrace()
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