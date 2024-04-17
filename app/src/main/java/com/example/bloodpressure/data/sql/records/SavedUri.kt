package com.example.bloodpressure.data.sql.records

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Uri")
data class SavedUri(
    @PrimaryKey val uri: String
)
