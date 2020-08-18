package com.chuckerteam.chucker.internal.support

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.LongSparseArray
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.chuckerteam.chucker.R
import com.chuckerteam.chucker.api.Chucker
import com.chuckerteam.chucker.internal.data.entity.RecordedThrowable
import com.chuckerteam.chucker.internal.ui.BaseChuckerActivity
import java.util.HashSet

internal class NotificationHelper(val context: Context) {

    companion object {
        private const val TRANSACTIONS_CHANNEL_ID = "chucker_transactions"
        private const val ERRORS_CHANNEL_ID = "chucker_errors"

        private const val TRANSACTION_NOTIFICATION_ID = 1138
        private const val ERROR_NOTIFICATION_ID = 3546

        private const val BUFFER_SIZE = 10
        private const val INTENT_REQUEST_CODE = 11
        private val transactionIdsSet = HashSet<Long>()

        fun clearBuffer() {

        }
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val transactionsScreenIntent by lazy {
        PendingIntent.getActivity(
            context,
            TRANSACTION_NOTIFICATION_ID,
            Chucker.getLaunchIntent(context, Chucker.SCREEN_HTTP),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private val errorsScreenIntent by lazy {
        PendingIntent.getActivity(
            context,
            ERROR_NOTIFICATION_ID,
            Chucker.getLaunchIntent(context, Chucker.SCREEN_ERROR),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val transactionsChannel = NotificationChannel(
                TRANSACTIONS_CHANNEL_ID,
                context.getString(R.string.chucker_network_notification_category),
                NotificationManager.IMPORTANCE_LOW
            )
            val errorsChannel = NotificationChannel(
                ERRORS_CHANNEL_ID,
                context.getString(R.string.chucker_throwable_notification_category),
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannels(listOf(transactionsChannel, errorsChannel))
        }
    }





    fun show(throwable: RecordedThrowable) {
        if (!BaseChuckerActivity.isInForeground) {
            val builder =
                NotificationCompat.Builder(context, ERRORS_CHANNEL_ID)
                    .setContentIntent(errorsScreenIntent)
                    .setLocalOnly(true)
                    .setSmallIcon(R.drawable.chucker_ic_error_notifications)
                    .setColor(ContextCompat.getColor(context, R.color.chucker_status_error))
                    .setContentTitle(throwable.clazz)
                    .setAutoCancel(true)
                    .setContentText(throwable.message)
                    .addAction(createClearAction(ClearDatabaseService.ClearAction.Error))
            notificationManager.notify(ERROR_NOTIFICATION_ID, builder.build())
        }
    }

    private fun createClearAction(clearAction: ClearDatabaseService.ClearAction):
        NotificationCompat.Action {
            val clearTitle = context.getString(R.string.chucker_clear)
            val deleteIntent = Intent(context, ClearDatabaseService::class.java).apply {
                putExtra(ClearDatabaseService.EXTRA_ITEM_TO_CLEAR, clearAction)
            }
            val intent = PendingIntent.getService(
                context, INTENT_REQUEST_CODE,
                deleteIntent, PendingIntent.FLAG_ONE_SHOT
            )
            return NotificationCompat.Action(R.drawable.chucker_ic_delete_white, clearTitle, intent)
        }

    fun dismissTransactionsNotification() {
        notificationManager.cancel(TRANSACTION_NOTIFICATION_ID)
    }

    fun dismissErrorsNotification() {
        notificationManager.cancel(ERROR_NOTIFICATION_ID)
    }
}
