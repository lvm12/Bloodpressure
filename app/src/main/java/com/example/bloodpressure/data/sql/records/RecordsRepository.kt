package com.example.bloodpressure.data.sql.records

import androidx.annotation.WorkerThread
import com.example.bloodpressure.data.Record
import com.example.bloodpressure.data.RecordStatus
import kotlinx.coroutines.flow.Flow

class RecordsRepository(
    private val recordsDAO: RecordsDAO
) {
    @WorkerThread
    suspend fun getUri(): List<SavedUri>{
        return recordsDAO.getUri()
    }
    @WorkerThread
    suspend fun deleteUri(){
        recordsDAO.deleteUri()
    }
    @WorkerThread
    suspend fun setUri(uri: SavedUri){
        recordsDAO.setUri(uri)
    }
    val allRecords: Flow<List<Record>> = recordsDAO
        .getAllRecords()

    @WorkerThread
    fun getRequiredRecords(list: List<RecordStatus>): Flow<List<Record>>{
        return recordsDAO.getSelectedRecords(list)
    }
    @WorkerThread
    suspend fun insertNewRecord(record: Record){
        recordsDAO.insertRecord(record)
    }

    @WorkerThread
    suspend fun deleteRecord(record: Record){
        recordsDAO.deleteRecord(record)
    }

    @WorkerThread
    suspend fun updateRecordsStatus(newStatus: RecordStatus, oldStatus: RecordStatus){
        recordsDAO.updateRecordsStatus(newStatus, oldStatus)
    }

    @WorkerThread
    suspend fun updateRecord(newStatus: RecordStatus, id: Long){
        recordsDAO.updateRecord(newStatus, id)
    }

    @WorkerThread
    suspend fun deleteRecordsByStatus(status: RecordStatus){
        recordsDAO.deleteRecordsByStatus(status)
    }
}