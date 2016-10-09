package com.example.administrator.employeenote.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnTrackListener;
import com.baidu.trace.Trace;
import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.common.TrackApplication;
import com.example.administrator.employeenote.entity.getHistory.HistoryTrackData;
import com.example.administrator.employeenote.utils.GsonService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DrawTraceActivity extends Activity implements View.OnClickListener {

    BaiduMap mBaiduMap = null;
    MapView mMapView = null;
    private Button btnReplay = null;
    private Button btnPlay = null;
    private Button btnPause = null;

    private int startTime = 0;
    private int endTime = 0;


    /**
     * Track监听器
     */
    protected static OnTrackListener trackListener = null;


    private TextView tvDatetime = null;


    private Polyline mVirtureRoad;
    private Marker mMoveMarker;
    private Handler mHandler;
    private Thread thread;
    private TrackApplication tapp;
    private boolean suspended = false;

    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 130;
    private static final double DISTANCE = 0.0001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        mMapView.showZoomControls(false);
        mHandler = new Handler(Looper.getMainLooper());

        Intent it = getIntent();
        startTime = Integer.parseInt(it.getStringExtra("startTime").substring(0,10));
        endTime = Integer.parseInt(it.getStringExtra("endTime").substring(0,10));

        initView();
//         初始化OnTrackListener
        initOnTrackListener();
    }

    /**
     * 初始化
     */
    private void initView() {

        tapp = (TrackApplication) getApplication();


        btnReplay = (Button) findViewById(R.id.btn_replay);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnPlay = (Button) findViewById(R.id.btn_play);


        btnReplay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnPlay.setOnClickListener(this);

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
        String entityName = "mycar";
        // 是否返回精简的结果（0 : 否，1 : 是）On tracking
        int simpleReturn = 0;
        // 是否返回纠偏后轨迹（0 : 否，1 : 是）
        int isProcessed = processed;

        // 分页大小
        int pageSize = 1000;
        // 分页索引
        int pageIndex = 1;

        HomePageActivity.client.queryHistoryTrack(tapp.getServiceId(), entityName, simpleReturn,
                isProcessed, processOption,
                startTime, endTime,
                pageSize,
                pageIndex,
                trackListener);
    }

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
            mBaiduMap.clear();
            Collections.reverse(latLngList);
            initRoadData(latLngList);
            tapp.setExit(true);
            moveLooper();

        }

    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_play:
                // 查询轨迹
                queryTrack();
                btnReplay.setEnabled(true);
                break;

            case R.id.btn_pause:
                if (suspended) {
                    suspended = false;
                    synchronized (thread) {
                        thread.notify();
                    }
                } else suspended = true;
                break;

            case R.id.btn_replay:
                tapp.setExit(true);
                moveLooper();
                btnPlay.setEnabled(false);
                btnPause.setEnabled(true);
                break;

            default:
                break;
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


    //绘制平滑轨迹
    private void initRoadData(final List<LatLng> polylines) {

        OverlayOptions polylineOptions;

        polylineOptions = new PolylineOptions().points(polylines).width(10).color(Color.RED);

        mVirtureRoad = (Polyline) mBaiduMap.addOverlay(polylineOptions);

        OverlayOptions markerOptions;
        markerOptions = new MarkerOptions()
                .flat(true)
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.icon_gcoding1))
                .position(polylines.get(0));
        mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
        System.out.println("aaaaaaaaaaaaaaaaaaaa");
    }

    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {

        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;

    }

    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }

    /**
     * 循环进行移动逻辑
     */
    public void moveLooper() {
        tapp.setExit(false);

        thread = new Thread() {

            public void run() {

                for (int i = 0; !tapp.getExit() && i < mVirtureRoad.getPoints().size() - 1; i++) {
                    final LatLng startPoint = mVirtureRoad.getPoints().get(i);
                    final LatLng endPoint = mVirtureRoad.getPoints().get(i + 1);
                    MapStatus mMapStatus = new MapStatus.Builder()
                            .target(centrepoint(startPoint, endPoint))
                            .zoom(16)
                            .build();
                    final MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);


                    mMoveMarker.setPosition(startPoint);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // refresh marker's rotate
                            if (tapp.getExit() || mMapView == null) {
                                return;
                            }
//                                mMoveMarker.setRotate((float) getAngle(startPoint,
//                                        endPoint));

                            mBaiduMap.setMapStatus(mMapStatusUpdate);
                        }
                    });
                    double slope = getSlope(startPoint, endPoint);

                    //是不是正向的标示（向上设为正向）
                    boolean isReverse = (startPoint.latitude > endPoint.latitude);

                    double intercept = getInterception(slope, startPoint);

                    double xMoveDistance = isReverse ? getXMoveDistance(slope)
                            : -1 * getXMoveDistance(slope);


                    for (double j = startPoint.latitude; !((j > endPoint.latitude) ^ isReverse); j = j - xMoveDistance) {
                        LatLng latLng;
                        if (slope != Double.MAX_VALUE) {
                            latLng = new LatLng(j, (j - intercept) / slope);
                        } else {
                            latLng = new LatLng(j, startPoint.longitude);
                        }
                        final LatLng finalLatLng = latLng;

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mMapView == null) {
                                    return;
                                }
                                // refresh marker's position
//                                else if (!tapp.getExit())
                                mMoveMarker.setPosition(finalLatLng);
                            }
                        });

                        try {
                            Thread.sleep(TIME_INTERVAL);
                            synchronized (thread) {
                                if (suspended) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            btnPlay.setEnabled(false);
                                            btnPause.setEnabled(true);
                                        }
                                    });
                                    thread.wait();
                                } else {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            btnPlay.setEnabled(true);
                                            btnPause.setEnabled(false);
                                        }
                                    });
                                }

                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

        };

        thread.start();
    }

    public LatLng centrepoint(LatLng s, LatLng e) {
        LatLng c = new LatLng((s.latitude + e.latitude) / 2, (s.longitude + e.longitude) / 2);
        return c;
    }


    /**
     * 方法必须重写
     */


    @Override
    protected void onResume() {
        super.onResume();
        tapp.setExit(false);
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        tapp.setExit(true);
        mMapView.onPause();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tapp.setExit(true);
        mMapView.onDestroy();
        mBaiduMap.clear();
    }

}
