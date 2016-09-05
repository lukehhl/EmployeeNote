package com.example.administrator.employeenote.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.Trace;
import com.example.administrator.employeenote.R;

public class MainActivity extends Activity {

    /**
     * 轨迹服务
     */
    protected static Trace trace = null;

    /**
     * entity标识
     */
    protected static String entityName = "mycar";

    /**
     * 鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
     */
    protected static long serviceId = 122424;

    /**
     * 轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
     */
    private int traceType = 2;

    /**
     * 轨迹服务客户端
     */
    protected static LBSTraceClient client = null;

    /**
     * Entity监听器
     */
    protected static OnEntityListener entityListener = null;



    protected static MapView bmapView = null;
    protected static BaiduMap mBaiduMap = null;



    protected static Context mContext = null;

    private Button mstartser, mstopser, mbdmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init(){

        //实例化轨迹服务客户端
        client = new LBSTraceClient(getApplicationContext());


        //实例化轨迹服务
        trace = new Trace(getApplicationContext(), serviceId, entityName, traceType);

        mstartser = (Button) findViewById(R.id.startser);
        mstopser = (Button) findViewById(R.id.stopser);
        mbdmap = (Button) findViewById(R.id.bdmap);
        mstartser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMap();
            }
        });

        mstopser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMap();
            }
        });
        mbdmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this,DrawMapActivity.class);
                startActivity(it);
            }
        });
        //位置采集周期
        int gatherInterval = 5;
        //打包周期
        int packInterval = 60;
        //设置位置采集和打包周期
        client.setInterval(gatherInterval, packInterval);
    }

    private void startMap(){

        //实例化开启轨迹服务回调接口
        OnStartTraceListener startTraceListener = new OnStartTraceListener() {
            //开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTraceCallback(int arg0, String arg1) {
//                Toast.makeText(getApplicationContext(),arg0 + "    " + arg1, Toast.LENGTH_LONG).show();
                new  AlertDialog.Builder(MainActivity.this)
                        .setMessage(arg0 + "    " + arg1)
                        .setPositiveButton("确定",null)
                        .show();

            }
            //轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTracePushCallback(byte arg0, String arg1) {
            }
        };
        //开启轨迹服务
        client.startTrace(trace, startTraceListener);
    }

    private void stopMap(){
//        Toast.makeText(getApplicationContext(),"停止", Toast.LENGTH_LONG).show();


        OnStopTraceListener onStopTraceListener = new OnStopTraceListener() {
            @Override
            public void onStopTraceSuccess() {
//                Toast.makeText(getApplicationContext(),"停止成功", Toast.LENGTH_LONG).show();
                new  AlertDialog.Builder(MainActivity.this)
                        .setMessage("停止成功")
                        .setPositiveButton("确定",null)
                        .show();
            }

            @Override
            public void onStopTraceFailed(int i, String s) {
//                Toast.makeText(getApplicationContext(),"停止失败", Toast.LENGTH_LONG).show();
                new  AlertDialog.Builder(MainActivity.this)
                        .setMessage("停止失败")
                        .setPositiveButton("确定",null)
                        .show();

            }
        };
        client.stopTrace(trace,onStopTraceListener);
    }
}
