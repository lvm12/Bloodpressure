package com.example.bloodpressure.domain

import com.example.bloodpressure.data.csv.CSVGenerationStatus
import com.example.bloodpressure.data.Record
import com.example.bloodpressure.data.RecordStatus
import com.example.bloodpressure.data.RecordUpdateStatus

data class BloodPressureState(
    val records: List<Record> = emptyList(),
    val allRecords: List<Record> = emptyList(),
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
    val isNewSelected: Boolean = SelectedState().isNewSelected,
    val isExportedSelected: Boolean = SelectedState().isExportedSelected,
    val isArchivedSelected: Boolean = SelectedState().isArchivedSelected,
    val hasNewBeenClicked: Boolean = false,
    val selectedRecordStatus: List<RecordStatus> = emptyList(),
    val recordUpdateStatus: RecordUpdateStatus = RecordUpdateStatus.DONE
)