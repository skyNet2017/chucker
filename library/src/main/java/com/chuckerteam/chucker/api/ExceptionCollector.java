package com.chuckerteam.chucker.api;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chuckerteam.chucker.internal.data.entity.ThrowableType;

import java.lang.ref.WeakReference;

public class ExceptionCollector {

    public static WeakReference<Activity> top;
    static ChuckerCollector collector;
    static Application app;
    public static IReportToServer reportToServer;

    public static void init(Application application){
        app = application;
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                top = new WeakReference<>(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                top = new WeakReference<>(activity);
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    public static void logThrowable(String tag,Throwable throwable){
        if(collector == null){
            collector = new ChuckerCollector(app,true);
        }
        collector.onError(tag,throwable);
        if(reportToServer != null){
            reportToServer.report(tag, throwable);
        }
    }

    public static void logThrowable(Throwable throwable){
        logThrowable(ThrowableType.TAG_normal,throwable);
    }
}
