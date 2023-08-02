package com.example.bloodpressure.domain

import com.example.bloodpressure.data.RecordStatus

data class SelectedState (
    val isNewSelected: Boolean = false,
    val isExportedSelected: Boolean = false,
    val isArchivedSelected: Boolean = false,
    val hasNewBeenClicked: Boolean = false,
    val selectedRecords: List<RecordStatus> = emptyList<RecordStatus>()
)