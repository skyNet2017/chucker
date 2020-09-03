package com.chuckerteam.chucker.internal.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chuckerteam.chucker.internal.data.entity.RecordedThrowable

@Database(entities = [RecordedThrowable::class], version = 5, exportSchema = false)
internal abstract class ChuckerDatabase : RoomDatabase() {

    abstract fun throwableDao(): RecordedThrowableDao

    companion object {
        private const val DB_NAME = "chucker.db"

        fun create(context: Context): ChuckerDatabase {
            // We eventually delete the old DB if a previous version of Chuck/Chucker was used.
            return Room.databaseBuilder(context, ChuckerDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
