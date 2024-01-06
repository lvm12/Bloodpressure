package com.example.bloodpressure.data.sql.records

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bloodpressure.data.Record

@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class RecordsDatabase: RoomDatabase() {
    abstract fun dao(): RecordsDAO

    companion object {
        @Volatile
        private var INSTANCE: RecordsDatabase? = null

        fun getDatabase(context: Context): RecordsDatabase {
            //IF INSTANCE IS NOT NULL RETURN IT
            //ELSE CREATE DATABASE
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = RecordsDatabase::class.java,
                    name = "records-database"
                ).build()

                INSTANCE = instance
                //Return instance
                instance
            }
        }
    }
}