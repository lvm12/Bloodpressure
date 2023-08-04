package com.example.bloodpressure.data.sql.records

import com.example.bloodpressure.data.BloodPressureEvent
import com.example.bloodpressure.data.Record
import com.example.bloodpressure.data.RecordStatus
import com.example.bloodpressure.data.RecordUpdateStatus
import com.example.bloodpressure.domain.BloodPressureState
import kotlinx.coroutines.flow.first

class RecordsUpdater(
    private val repository: RecordsRepository,
    private val state: BloodPressureState,
    private val onEvent: (BloodPressureEvent) -> Unit
) {
    suspend fun archiveExported(){
        val records = repository.exportedRecords.first()
        records.forEach {
            repository.insertNewRecord(
                Record(
                    id = it.id,
                    systolicPressure = it.systolicPressure,
                    diastolicPressure = it.diastolicPressure,
                    pulse = it.pulse,
                    createdAt = it.createdAt,
                    recordStatus = RecordStatus.ARCHIVED
                )
            )
        }
        onEvent(BloodPressureEvent.RecordUpdateStatus(RecordUpdateStatus.DONE))
    }
    suspend fun deleteAllArchived(){
        val records = repository.archivedRecords.first()
        records.forEach {
            repository.deleteRecord(it)
        }
        onEvent(BloodPressureEvent.RecordUpdateStatus(RecordUpdateStatus.DONE))
    }

    suspend fun archiveRecord(){
        val record = state.selectedRecord
        record?.let {
            repository.insertNewRecord(
                Record(
                    id = it.id,
                    systolicPressure = it.systolicPressure,
                    diastolicPressure = it.diastolicPressure,
                    pulse = it.pulse,
                    createdAt = it.createdAt,
                    recordStatus = RecordStatus.ARCHIVED
                )
            )
        }
    }

    suspend fun unArchiveRecord(){
        val record = state.selectedRecord
        record?.let {
            repository.insertNewRecord(
                Record(
                    id = it.id,
                    systolicPressure = it.systolicPressure,
                    diastolicPressure = it.diastolicPressure,
                    pulse = it.pulse,
                    createdAt = it.createdAt,
                    recordStatus = RecordStatus.ARCHIVED
                )
            )
        }
    }
}