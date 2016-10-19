package com.example.administrator.employeenote.common;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.example.administrator.employeenote.entity.EmployeeData;

/**
 * Created by Administrator on 2016/8/13.
 */
public class TrackApplication extends Application {
    private static Context context;
    public static final String SERVERURL = "http://10.30.100.22:8080/gesac/";
    public static final String TIMEZONE = "Asia/Shanghai";
    private static Boolean exit;
    public static long serviceId = 122424;
    public static String calendar_id = "";
    private String eid;
    private EmployeeData person;
    public static final LatLng TCENTER = new LatLng(118.144916,24.703409);
    public static final int TRADIUS = 10;
    public static final LatLng JCENTER = new LatLng(118.108858,24.604165);
    public static final int JRADIUS = 10;

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
        if (SpatialRelationUtil.isCircleContainsPoint(JCENTER, JRADIUS,point))
            return true;
        else return false;
    }
    public static boolean isInTA(LatLng point){
        if (SpatialRelationUtil.isCircleContainsPoint(TCENTER, TRADIUS,point))
            return true;
        else return false;
    }
}
