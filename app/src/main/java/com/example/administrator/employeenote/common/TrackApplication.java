package com.example.administrator.employeenote.common;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.baidu.trace.Trace;
import com.example.administrator.employeenote.entity.EmployeeData;

/**
 * Created by Administrator on 2016/8/13.
 */
public class TrackApplication extends Application {
    private static Context context;
    public static final String serverUrl = "http://60.205.178.163:8080/gesac/";
    private static Boolean exit;
    public static long serviceId = 122424;
    private String eid;
    private EmployeeData person;
    public static final LatLng tcenter = new LatLng(118.144916,24.703409);
    public static final int tradius = 10;
    public static final LatLng jcenter = new LatLng(118.108858,24.604165);
    public static final int jradius = 10;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        context = getApplicationContext();
        exit = false;
        eid = "8";
    }

    public EmployeeData getPerson() {
        return person;
    }

    public void setPerson(EmployeeData person) {
        this.person = person;
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

    public long getServiceId() {
        return serviceId;
    }

    public static void showMessage(String message) {
        Looper.prepare();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        Looper.loop();
    }

    public static boolean isInJM(LatLng point){
        if (SpatialRelationUtil.isCircleContainsPoint(jcenter,jradius,point))
            return true;
        else return false;
    }
    public static boolean isInTA(LatLng point){
        if (SpatialRelationUtil.isCircleContainsPoint(tcenter,tradius,point))
            return true;
        else return false;
    }
}
