package com.chuckerteam.chucker.sample;

import android.app.Application;
import android.content.Context;
//import androidx.multidex.MultiDex;
import com.hss01248.network.body.meta.interceptor.BodyUtil2;

/**
 * @Despciption todo
 * @Author hss
 * @Date 05/07/2022 15:02
 * @Version 1.0
 */
public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        BodyUtil2.attachBaseContext(base);
    }
}
