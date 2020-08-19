package com.chuckerteam.chucker.internal.data.repository

import androidx.lifecycle.LiveData
import com.chuckerteam.chucker.internal.data.entity.RecordedThrowable
import com.chuckerteam.chucker.internal.data.entity.RecordedThrowableTuple
import com.chuckerteam.chucker.internal.data.room.ChuckerDatabase
import com.chuckerteam.chucker.internal.support.distinctUntilChanged

internal class RecordedThrowableDatabaseRepository(
    private val database: ChuckerDatabase
) : RecordedThrowableRepository {

    override fun getRecordedThrowable(id: Long): LiveData<RecordedThrowable> {
        return database.throwableDao().getById(id).distinctUntilChanged()
    }

    override suspend fun deleteAllThrowables(tag:String) {
        database.throwableDao().deleteAll(tag)
    }

    override fun getSortedThrowablesTuples(tag:String): LiveData<List<RecordedThrowableTuple>> {
        return database.throwableDao().getTuples(tag)
    }

    override suspend fun saveThrowable(throwable: RecordedThrowable) {
        database.throwableDao().insert(throwable)
    }

    override suspend fun deleteOldThrowables(threshold: Long) {
        database.throwableDao().deleteBefore(threshold)
    }
}
