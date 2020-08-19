package com.chuckerteam.chucker.internal.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.chuckerteam.chucker.internal.data.entity.RecordedThrowable
import com.chuckerteam.chucker.internal.data.entity.RecordedThrowableTuple

@Dao
internal interface RecordedThrowableDao {

    @Query("SELECT id,tag,activity,date,clazz,message FROM throwables WHERE tag = :tag ORDER BY date DESC")
    fun getTuples(tag: String): LiveData<List<RecordedThrowableTuple>>

    @Insert
    suspend fun insert(throwable: RecordedThrowable): Long?

    @Query("DELETE FROM throwables WHERE tag = :tag")
    suspend fun deleteAll(tag: String)

    @Query("SELECT * FROM throwables WHERE id = :id")
    fun getById(id: Long): LiveData<RecordedThrowable>

    @Query("DELETE FROM throwables WHERE date <= :threshold")
    suspend fun deleteBefore(threshold: Long)
}
