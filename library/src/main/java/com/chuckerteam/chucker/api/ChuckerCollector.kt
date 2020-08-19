package com.chuckerteam.chucker.api

import android.content.Context

import com.chuckerteam.chucker.internal.data.entity.RecordedThrowable
import com.chuckerteam.chucker.internal.data.entity.ThrowableType
import com.chuckerteam.chucker.internal.data.repository.RepositoryProvider
import com.chuckerteam.chucker.internal.support.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The collector responsible of collecting data from a [ChuckerInterceptor] and
 * storing it/displaying push notification. You need to instantiate one of those and
 * provide it to
 *
 * @param context An Android Context
 * @param showNotification Control whether a notification is shown while HTTP activity
 * is recorded.
 * @param retentionPeriod Set the retention period for HTTP transaction data captured
 * by this collector. The default is one week.
 */
class ChuckerCollector @JvmOverloads constructor(
        context: Context,
        var showNotification: Boolean = true,
        retentionPeriod: RetentionManager.Period = RetentionManager.Period.FOREVER
) {
    private val retentionManager: RetentionManager = RetentionManager(context, retentionPeriod)
    private val notificationHelper: NotificationHelper = NotificationHelper(context)

    init {
        RepositoryProvider.initialize(context)
    }



    /**
     * Call this method when a throwable is triggered and you want to record it.
     * @param tag A tag you choose 取值: ThrowableType.tag_xxx. 可以自定义,但不要加太多,多少个tag,就多少个tab
     * @param throwable The triggered [Throwable]
     */
    fun onError(tag: String, throwable: Throwable) {
        val recordedThrowable = RecordedThrowable(tag, throwable)
        ThrowableType.addType(tag)
        recordedThrowable.top_activity = ExceptionCollector.top?.get()?.javaClass?.simpleName
        CoroutineScope(Dispatchers.IO).launch {
            RepositoryProvider.throwable().saveThrowable(recordedThrowable)
        }
        if (showNotification) {
            notificationHelper.show(recordedThrowable)
        }
        retentionManager.doMaintenance()
    }


}
