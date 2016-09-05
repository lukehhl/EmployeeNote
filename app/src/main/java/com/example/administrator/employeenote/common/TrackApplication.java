package com.example.administrator.employeenote.common;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Administrator on 2016/8/13.
 */
public class TrackApplication extends Application {
    private static Context context;
    private static Boolean exit;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        context = getApplicationContext();
        exit = false;
    }

    public static Context getContext() {
        return context;
    }

    public Boolean getExit() {
        return exit;
    }

    public void setExit(Boolean exit) {
        this.exit = exit;
    }

    public static void showMessage(String message) {
        Looper.prepare();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        Looper.loop();
    }
}
