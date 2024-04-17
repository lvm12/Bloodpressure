package com.example.bloodpressure.data.sql.records

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.bloodpressure.data.Record
import com.example.bloodpressure.data.RecordStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordsDAO {
    @Query("SELECT * FROM Uri LIMIT 1")
    suspend fun getUri(): List<SavedUri>

    @Query("DELETE FROM Uri")
    suspend fun deleteUri()

    @Upsert
    suspend fun setUri(uri: SavedUri)
    @Query("SELECT * FROM BloodPressureRecords ORDER BY createdAt DESC")
    fun getAllRecords(): Flow<List<Record>>

    @Upsert(entity = Record::class)
    suspend fun insertRecord(record: Record)

    @Delete(entity = Record::class)
    suspend fun deleteRecord(record: Record)

    @Query("SELECT * FROM BloodPressureRecords WHERE id == :id;")
    suspend fun getRecordByID(id: Long): Record

    @Query("SELECT * FROM BloodPressureRecords WHERE recordStatus == 'NEW' ORDER BY createdAt DESC")
    fun getNewRecords(): Flow<List<Record>>

    @Query("SELECT * FROM BloodPressureRecords WHERE recordStatus == 'EXPORTED' ORDER BY createdAt DESC")
    fun getExportedRecords(): Flow<List<Record>>

    @Query("SELECT * FROM BloodPressureRecords WHERE recordStatus == 'ARCHIVED' ORDER BY createdAt DESC")
    fun getArchivedRecords(): Flow<List<Record>>

    @Query("SELECT * FROM BloodPressureRecords WHERE recordStatus IN (:list) ORDER BY createdAt DESC")
    fun getSelectedRecords(list: List<RecordStatus>): Flow<List<Record>>

    @Query("UPDATE BloodPressureRecords SET recordStatus = :newStatus WHERE recordStatus == :oldStatus")
    suspend fun updateRecordsStatus(newStatus: RecordStatus, oldStatus: RecordStatus)

    @Query("DELETE FROM BloodPressureRecords WHERE recordStatus == :status")
    suspend fun deleteRecordsByStatus(status: RecordStatus)

    @Query("UPDATE BloodPressureRecords SET recordStatus = :newStatus WHERE id == :id")
    suspend fun updateRecord(newStatus: RecordStatus, id: Long)
}