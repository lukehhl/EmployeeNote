package com.example.administrator.employeenote.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
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
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public class AddMissionActivity extends AppCompatActivity {
    private ImageView back;
    private TextView submit;
    private Button svoice, pvoice, smisson;
    private TrackApplication tapp;

    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String mFileName = null;
    private String date;

    private Handler mhandler;
    private Boolean retroSign = false;
    private Boolean mapSign = false;

    private final String LOG_TAG = "AddMissionActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mission);
        initView();
    }

    private void initView() {

        tapp = (TrackApplication) getApplication();
        mhandler = new Handler(Looper.getMainLooper());



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
                                startMission();
                                dialog.cancel();
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

        SimpleDateFormat sDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sDateFormat2 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        date = sDateFormat1.format(new java.util.Date());
        String filename = sDateFormat2.format(new Date());
        mFileName = AddMissionActivity.this.getCacheDir().toString() + "/" + tapp.getEid() + "-" + filename + ".amr";

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



    public interface FileUploadService { //retrofit上传文件接口
        @Multipart
        @POST("addMission.do")
        Call<ResponseBody> upload(@Part("eid") String eid,
                                  @Part("vtime") String vtime,
                                  @Part("vsrc") RequestBody description,
                                  @Part MultipartBody.Part file,
                                  @Part("vsign") String vsign);
    }

    public void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(tapp.serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //先创建 service
        FileUploadService service = retrofit.create(FileUploadService.class);
        //构建要上传的文件
        File file = new File(mFileName);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("voiceFile", file.getName(), requestFile);
        String descriptionString = file.getName();
        RequestBody description = RequestBody.create(
                MediaType.parse("multipart/form-data"), descriptionString);
        Call<ResponseBody> call = service.upload(tapp.getEid(), date, description, body, "0");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if (response.isSuccessful()) { //上传成功
                    Toast.makeText(AddMissionActivity.this, "success", Toast.LENGTH_SHORT).show();
                    retroSign = true;
                } else
                    try {
                        Log.d("retrofit",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void startMission() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                initRetrofit();
                while (true) {
                    //TODO 加载动画
                    if (retroSign) {
                        mhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(AddMissionActivity.this)
                                        .setMessage("提交成功")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent it = new Intent(AddMissionActivity.this, MissionActivity.class);
                                                startActivity(it);
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        });
                        break;
                    }
                }

            }
        }.start();
    }


}
