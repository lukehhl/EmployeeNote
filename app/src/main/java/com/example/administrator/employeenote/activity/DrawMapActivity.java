package com.example.administrator.employeenote.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.trace.OnTrackListener;
import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.common.TrackApplication;
import com.example.administrator.employeenote.entity.getHistory.HistoryTrackData;
import com.example.administrator.employeenote.utils.GsonService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrawMapActivity extends Activity {
    BaiduMap mBaiduMap = null;
    MapView mMapView = null;

    private int startTime = 0;
    private int endTime = 0;


    // 起点图标
    private static BitmapDescriptor bmStart;
    // 终点图标
    private static BitmapDescriptor bmEnd;

    // 起点图标覆盖物
    private static MarkerOptions startMarker = null;
    // 终点图标覆盖物
    private static MarkerOptions endMarker = null;
    // 路线覆盖物
    public static PolylineOptions polyline = null;

    private static MarkerOptions markerOptions = null;

    /**
     * Track监听器
     */
    protected static OnTrackListener trackListener = null;

    private MapStatusUpdate msUpdate = null;

    private TextView tvDatetime = null;


    private TrackApplication tapp;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_trace);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        mMapView.showZoomControls(false);


        Intent it = getIntent();
        startTime = Integer.parseInt(it.getStringExtra("startTime").substring(0, 10));
        endTime = Integer.parseInt(it.getStringExtra("endTime").substring(0, 10));
        init();

        // 初始化OnTrackListener
        initOnTrackListener();
        queryTrack();


    }

    /**
     * 初始化
     */
    private void init() {
        tapp = (TrackApplication) getApplication();

        tvDatetime = (TextView) findViewById(R.id.tv_datetime);
//        tvDatetime.setText(" 任务时间 : \n" + String.valueOf(startTime) + " — "
//                + String.valueOf(endTime));
        tvDatetime.setText(" 任务时间 : \n" + Unix2Date(String.valueOf(startTime), "yyyy-MM-dd HH:mm") + " 至 "
                + Unix2Date(String.valueOf(endTime), "yyyy-MM-dd HH:mm"));

    }

    public String Unix2Date(String timestampString, String formats) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));
        return date;
    }

    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack(int processed, String processOption) {

        // entity标识
        String entityName = tapp.getPerson().getEname();
        // 是否返回精简的结果（0 : 否，1 : 是）
        int simpleReturn = 0;
        // 是否返回纠偏后轨迹（0 : 否，1 : 是）
        int isProcessed = processed;

        // 分页大小
        int pageSize = 1000;
        // 分页索引
        int pageIndex = 1;

        HomePageActivity.client.queryHistoryTrack(tapp.serviceId, "mycar", simpleReturn,
                isProcessed, processOption,
                startTime, endTime,
                pageSize,
                pageIndex,
                trackListener);
    }

    // 查询里程

    /**
     * 轨迹查询(先选择日期，再根据是否纠偏，发送请求)
     */
    private void queryTrack() {
        queryHistoryTrack(1, "need_denoise=1,need_vacuate=1,need_mapmatch=1");
    }

    /**
     * 显示历史轨迹
     *
     * @param historyTrack
     */
    private void showHistoryTrack(String historyTrack) {

        HistoryTrackData historyTrackData = GsonService.parseJson(historyTrack,
                HistoryTrackData.class);

        List<LatLng> latLngList = new ArrayList<LatLng>();
        if (historyTrackData != null && historyTrackData.getStatus() == 0) {
            if (historyTrackData.getListPoints() != null) {
                latLngList.addAll(historyTrackData.getListPoints());
            }

            // 绘制历史轨迹
            drawHistoryTrack(latLngList, historyTrackData.distance);

        }

    }


    /**
     * 初始化OnTrackListener
     */
    private void initOnTrackListener() {

        trackListener = new OnTrackListener() {

            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                // TODO Auto-generated method stub
                TrackApplication.showMessage("track请求失败回调接口消息 : " + arg0);
            }

            // 查询历史轨迹回调接口
            @Override
            public void onQueryHistoryTrackCallback(String arg0) {
                // TODO Auto-generated method stub
                super.onQueryHistoryTrackCallback(arg0);
                showHistoryTrack(arg0);
            }

            @Override
            public void onQueryDistanceCallback(String arg0) {
                // TODO Auto-generated method stub
                try {
                    JSONObject dataJson = new JSONObject(arg0);
                    if (null != dataJson && dataJson.has("status") && dataJson.getInt("status") == 0) {
                        double distance = dataJson.getDouble("distance");
                        DecimalFormat df = new DecimalFormat("#.0");
                        TrackApplication.showMessage("里程 : " + df.format(distance) + "米");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    TrackApplication.showMessage("queryDistance回调消息 : " + arg0);
                }
            }

            @Override
            public Map<String, String> onTrackAttrCallback() {
                // TODO Auto-generated method stub
                System.out.println("onTrackAttrCallback");
                return null;
            }

        };
    }

    /**
     * 绘制历史轨迹
     *
     * @param points
     */
    private void drawHistoryTrack(final List<LatLng> points, final double distance) {
        // 绘制新覆盖物前，清空之前的覆盖物
        mBaiduMap.clear();

        if (points.size() == 1) {
            points.add(points.get(0));
        }

        if (points == null || points.size() == 0) {
            TrackApplication.showMessage("当前查询无轨迹点");
            resetMarker();
        } else if (points.size() > 1) {

            LatLng llC = points.get(0);
            LatLng llD = points.get(points.size() - 1);
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(llC).include(llD).build();

            msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);

            bmStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_start);
            bmEnd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_end);

            // 添加起点图标
            startMarker = new MarkerOptions()
                    .position(points.get(points.size() - 1)).icon(bmStart)
                    .zIndex(9).draggable(true);

            // 添加终点图标
            endMarker = new MarkerOptions().position(points.get(0))
                    .icon(bmEnd).zIndex(9).draggable(true);

            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10)
                    .color(Color.RED).points(points);

            markerOptions = new MarkerOptions();
            markerOptions.flat(true);
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_gcoding));
            markerOptions.position(points.get(points.size() - 1));

            addMarker();

            TrackApplication.showMessage("当前轨迹里程为 : " + (int) distance + "米");

        }

    }

    /**
     * 添加覆盖物
     */
    protected void addMarker() {

        if (null != msUpdate) {
            mBaiduMap.animateMapStatus(msUpdate, 2000);
        }

        if (null != startMarker) {
            mBaiduMap.addOverlay(startMarker);
        }

        if (null != endMarker) {
            mBaiduMap.addOverlay(endMarker);
        }

        if (null != polyline) {
            mBaiduMap.addOverlay(polyline);
        }

    }

    /**
     * 重置覆盖物
     */
    private void resetMarker() {
        startMarker = null;
        endMarker = null;
        polyline = null;
    }

}
