package com.chuckerteam.chucker.internal.support

import android.util.Log
import com.chuckerteam.chucker.api.ExceptionCollector
import com.chuckerteam.chucker.internal.data.entity.ThrowableType

internal class ChuckerCrashHandler() : Thread.UncaughtExceptionHandler {

    private val defaultHandler: Thread.UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            ExceptionCollector.logThrowable(ThrowableType.TAG_CRASH, throwable)
        } catch (var7: Exception) {
            Log.e("TheCrashHandler", "An error occurred in the uncaught exception handler", var7)
        } finally {
            Log.d("TheCrashHandler", "Crashlytics completed exception processing. Invoking default exception handler.")
            if (defaultHandler != null) {
                this.defaultHandler.uncaughtException(thread, throwable)
            } else {
                try {
                    throwable.printStackTrace()
                    Thread.sleep(5000)
                } catch (e: Exception) {
                    e.printStackTrace()
                    //Log.e(TAG, "error : ", e);
                }
                //android.os.Process.killProcess(android.os.Process.myPid());
                //System.exit(1);
            }
        }
    }
}
