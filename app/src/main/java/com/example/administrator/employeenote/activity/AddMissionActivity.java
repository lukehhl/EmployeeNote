package com.example.administrator.employeenote.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.Trace;
import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.common.TrackApplication;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class AddMissionActivity extends AppCompatActivity {
    private ImageView back;
    private TextView submit;
    private Button svoice, pvoice, smisson;
    private TrackApplication tapp;

    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String mFileName = null;

    private Handler mhandler;

    private final String LOG_TAG = "AddMissionActivity";

    /**
     * 轨迹服务
     */
    protected static Trace trace = null;
    /**
     * entity标识
     */
    protected static String entityName = null;
    /**
     * 轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
     */
    private int traceType = 2;

    /**
     * 轨迹服务客户端
     */
    protected static LBSTraceClient client = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mission);
        initView();
    }

    private void initView() {

        tapp = (TrackApplication) getApplication();
        mhandler = new Handler(Looper.getMainLooper());

        //实例名称
        entityName = tapp.getEid();
        //实例化轨迹服务客户端
        client = new LBSTraceClient(getApplicationContext());
        //实例化轨迹服务
        trace = new Trace(getApplicationContext(), tapp.getServiceId(), entityName, traceType);
        //位置采集周期
        int gatherInterval = 5;
        //打包周期
        int packInterval = 60;
        //设置位置采集和打包周期
        client.setInterval(gatherInterval, packInterval);

        back = (ImageView) findViewById(R.id.back);
        submit = (TextView) findViewById(R.id.submit);
        svoice = (Button) findViewById(R.id.startvoice);
        pvoice = (Button) findViewById(R.id.playvoice);
        smisson = (Button) findViewById(R.id.startmission);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        svoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startVoice();
                        break;
                    case MotionEvent.ACTION_UP:
                        stopVoice();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        pvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            mPlayer = MediaPlayer.create(AddMissionActivity.this, Uri.parse(mFileName));
                            mPlayer.reset();
                            mPlayer.setDataSource(mFileName);
                            mPlayer.prepare();
                            mPlayer.start();
                            Log.i(LOG_TAG, "开始播放");
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "播放失败");
                        }
                    }
                }.start();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddMissionActivity.this)
                        .setMessage("确认开始任务？")
                        .setPositiveButton("开始", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startMap();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });

    }

    private void stopVoice() {//停止录音

        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Toast.makeText(getApplicationContext(), "保存录音" + mFileName, Toast.LENGTH_SHORT).show();
        mhandler.post(new Runnable() {
            @Override
            public void run() {
                svoice.setText("重新录音");
            }
        });
        svoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        File file = new File(mFileName);
                        file.delete();
                        startVoice();
                        break;
                    case MotionEvent.ACTION_UP:
                        stopVoice();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void startVoice() {//开始录音

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        mFileName = AddMissionActivity.this.getCacheDir().toString() + "/" + tapp.getEid() + "_" + date + ".amr";

        File directory = new File(mFileName).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            Log.i(LOG_TAG, "Path to file could not be created");
        }
        Toast.makeText(getApplicationContext(), "按住开始录音,松开结束录音", Toast.LENGTH_SHORT).show();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setOutputFile(mFileName);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();

    }

    private void startMap() {

        //实例化开启轨迹服务回调接口
        OnStartTraceListener startTraceListener = new OnStartTraceListener() {
            //开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTraceCallback(int arg0, String arg1) {
                new AlertDialog.Builder(AddMissionActivity.this)
                        .setMessage(arg0 + "    " + arg1)
                        .setPositiveButton("确定", null)
                        .show();
                if (arg0 == 0){
                    Intent it = new Intent(AddMissionActivity.this,MissionActivity.class);
                    startActivity(it);
                }
            }

            //轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTracePushCallback(byte arg0, String arg1) {
            }
        };
        //开启轨迹服务
        client.startTrace(trace, startTraceListener);
    }

    private void stopMap() {
//        Toast.makeText(getApplicationContext(),"停止", Toast.LENGTH_LONG).show();


        OnStopTraceListener onStopTraceListener = new OnStopTraceListener() {
            @Override
            public void onStopTraceSuccess() {
//                Toast.makeText(getApplicationContext(),"停止成功", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(AddMissionActivity.this)
                        .setMessage("停止成功")
                        .setPositiveButton("确定", null)
                        .show();
            }

            @Override
            public void onStopTraceFailed(int i, String s) {
//                Toast.makeText(getApplicationContext(),"停止失败", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(AddMissionActivity.this)
                        .setMessage("停止失败")
                        .setPositiveButton("确定", null)
                        .show();

            }
        };
        client.stopTrace(trace, onStopTraceListener);
    }
}
