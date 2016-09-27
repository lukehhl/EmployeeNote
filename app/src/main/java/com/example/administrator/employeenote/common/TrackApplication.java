package com.example.administrator.employeenote.common;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.trace.Trace;

/**
 * Created by Administrator on 2016/8/13.
 */
public class TrackApplication extends Application {
    private static Context context;
    private static Boolean exit;
    private static Trace trace = null;
    private static long serviceId = 122424;
    private String eid;



    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        context = getApplicationContext();
        exit = false;
        eid = "3";
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
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

    public static void setTrace(Trace trace) {
        TrackApplication.trace = trace;
    }

    public static Trace getTrace() {
        return trace;
    }

    public static long getServiceId() {
        return serviceId;
    }

    public static void showMessage(String message) {
        Looper.prepare();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        Looper.loop();
    }
}
