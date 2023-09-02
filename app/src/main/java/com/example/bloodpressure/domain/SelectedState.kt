package com.example.bloodpressure.domain

import com.example.bloodpressure.data.RecordStatus

data class SelectedState (
    val isNewSelected: Boolean = true,
    val isExportedSelected: Boolean = true,
    val isArchivedSelected: Boolean = true,
    val hasNewBeenClicked: Boolean = true,
    val selectedRecords: List<RecordStatus> = listOf(RecordStatus.EXPORTED,RecordStatus.NEW,RecordStatus.ARCHIVED)
)