package com.example.bloodpressure.data.sql.records

import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.data.RecordStatus
import com.example.bloodpressure.data.RecordUpdateStatus
import com.example.bloodpressure.domain.BloodPressureState

class RecordsUpdater(
    private val repository: RecordsRepository,
    private val state: BloodPressureState,
    private val onEvent: (BloodPressureEvent) -> Unit
) {
    suspend fun archiveExported(){
        repository.updateRecordsStatus(
            newStatus = RecordStatus.ARCHIVED,
            oldStatus = RecordStatus.EXPORTED
        )
        onEvent(BloodPressureEvent.RecordUpdaterStatus(RecordUpdateStatus.DONE))
    }
    suspend fun deleteAllArchived(){
        repository.deleteRecordsByStatus(RecordStatus.ARCHIVED)
        onEvent(BloodPressureEvent.RecordUpdaterStatus(RecordUpdateStatus.DONE))
    }

    suspend fun archiveRecord(){
        state.selectedRecord?.id?.let {
            repository.updateRecord(
                newStatus = RecordStatus.ARCHIVED,
                id = it
            )
        }
    }

    suspend fun unArchiveRecord(){
        state.selectedRecord?.let {
            repository.updateRecord(
                newStatus = RecordStatus.NEW,
                id = it.id
            )
        }
    }

    suspend fun exportNew(){
        repository.updateRecordsStatus(
            newStatus = RecordStatus.EXPORTED,
            oldStatus = RecordStatus.NEW
        )
        onEvent(BloodPressureEvent.RecordUpdaterStatus(RecordUpdateStatus.DONE))
    }
}