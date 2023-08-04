package com.example.bloodpressure.domain

import com.example.bloodpressure.data.csv.CSVGenerationStatus
import com.example.bloodpressure.data.Record
import com.example.bloodpressure.data.RecordUpdateStatus

data class BloodPressureState(
    val records: List<Record> = emptyList(),
    val archivedRecords: List<Record> = emptyList(),
    val exportedRecords: List<Record> = emptyList(),
    val newRecords: List<Record> = emptyList(),
    val selectedRecord: Record? = null,
    val isAddRecordSheetOpen: Boolean = false,
    val isSelectedRecordSheetOpen: Boolean = false,
    val systolicError: String? = null,
    val diastolicError: String? = null,
    val pulseError: String? = null,
    val generatingCsv: CSVGenerationStatus = CSVGenerationStatus.NOT,
    val hasPermission: Boolean = false,
    val isNewSelected: Boolean = false,
    val isExportedSelected: Boolean = false,
    val isArchivedSelected: Boolean = false,
    val hasNewBeenClicked: Boolean = false,
    val recordUpdateStatus: RecordUpdateStatus = RecordUpdateStatus.DONE
)