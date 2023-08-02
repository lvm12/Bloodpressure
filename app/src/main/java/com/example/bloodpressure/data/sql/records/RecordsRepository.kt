package com.example.bloodpressure.data.sql.records

import androidx.annotation.WorkerThread
import com.example.bloodpressure.data.Record
import com.example.bloodpressure.data.RecordStatus
import kotlinx.coroutines.flow.Flow

class RecordsRepository(
    private val recordsDAO: RecordsDAO
) {
    val allRecords: Flow<List<Record>> = recordsDAO
        .getAllRecords()

    val exportedRecords: Flow<List<Record>> = recordsDAO
        .getExportedRecords()

    val archivedRecords: Flow<List<Record>> = recordsDAO
        .getArchivedRecords()

    val newRecords: Flow<List<Record>> = recordsDAO
        .getNewRecords()

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
}