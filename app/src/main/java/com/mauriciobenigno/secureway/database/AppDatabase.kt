package com.mauriciobenigno.secureway.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mauriciobenigno.secureway.model.*

@Database(entities = [
    Local::class,
    District::class,
    Adjetivo::class,
    Report::class,
    Zona::class
], version = 9)

abstract class AppDatabase : RoomDatabase() {

    abstract fun Dao() : AppDao

    companion object {
        var INSTANCE : AppDatabase? = null

        fun getInstance(context : Context) : AppDatabase {
            return if(INSTANCE == null) {
                INSTANCE=  Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "database.db"
                ).fallbackToDestructiveMigration().build()

                INSTANCE as AppDatabase
            } else {
                INSTANCE as AppDatabase
            }
        }
    }
}