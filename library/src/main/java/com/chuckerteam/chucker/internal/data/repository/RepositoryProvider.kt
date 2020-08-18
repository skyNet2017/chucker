package com.chuckerteam.chucker.internal.data.repository

import android.content.Context
import com.chuckerteam.chucker.internal.data.repository.RepositoryProvider.initialize
import com.chuckerteam.chucker.internal.data.room.ChuckerDatabase

/**
 * A singleton to hold the [HttpTransactionRepository] and [RecordedThrowableRepository] instances.
 * Make sure you call [initialize] before accessing the stored instance.
 */
internal object RepositoryProvider {

    private var throwableRepository: RecordedThrowableRepository? = null



    fun throwable(): RecordedThrowableRepository {
        return checkNotNull(throwableRepository) {
            "You can't access the throwable repository if you don't initialize it!"
        }
    }

    /**
     * Idempotent method. Must be called before accessing the repositories.
     */
    fun initialize(context: Context) {
        if (throwableRepository == null) {
            val db = ChuckerDatabase.create(context)
            throwableRepository = RecordedThrowableDatabaseRepository(db)
        }
    }
}
