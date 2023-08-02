package com.example.bloodpressure.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.datetime.Clock

@Entity(tableName = "BloodPressureRecords")
data class Record(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val systolicPressure: String,
    val diastolicPressure: String,
    val pulse: String,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    var recordStatus: RecordStatus = RecordStatus.NEW
)