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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.common.TrackApplication;
import com.example.administrator.employeenote.entity.MissionData;
import com.example.administrator.employeenote.utils.LoadDialog;
import com.example.administrator.employeenote.utils.PlayerSingleton;
import com.google.gson.Gson;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

import static com.example.administrator.employeenote.common.TrackApplication.SERVERURL;
import static com.example.administrator.employeenote.utils.DateUtils.unixToString;


public class MissionInfoActivity extends AppCompatActivity {

    private ImageView back;
    private TextView submit, mtime;
    private Button svoice, pvoice;
    private EditText mdes, mcustomer;
    private TrackApplication tapp;

    private MediaRecorder mRecorder;
    private String mFileName = null;
    private String voicename;
    private String date;
    private Handler mhandler;

    private MissionData mdata;
    private String eid;

    private final String TAG = "MissionInfoActivity";
    private AlertDialog loaddialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_info);
        initView();
    }

    private void initView() {
        tapp = (TrackApplication) getApplication();
        mhandler = new Handler(Looper.getMainLooper());
        loaddialog = new SpotsDialog(this);
        Intent it = getIntent();
        mdata = new Gson().fromJson(it.getStringExtra("missiondata"), MissionData.class);
        eid = it.getStringExtra("eid");

        back = (ImageView) findViewById(R.id.back);
        submit = (TextView) findViewById(R.id.submit);
        svoice = (Button) findViewById(R.id.startvoice);
        pvoice = (Button) findViewById(R.id.playvoice);
        mdes = (EditText) findViewById(R.id.descrip_ed);
        mtime = (TextView) findViewById(R.id.time_tx);
        mcustomer = (EditText) findViewById(R.id.customer_ed);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mcustomer.setText(mdata.getVcustomer());
        mdes.setText(mdata.getVdes());
        mtime.setText(mdata.getVtime().toString().substring(0, 16));
        date = mtime.getText().toString();
        voicename = mdata.getVsrc().replace("voice/", "");
        mFileName = this.getCacheDir().toString() + File.separator + voicename;

        if (tapp.getPerson().getEid().equalsIgnoreCase(eid)) {
            if (mdata.getVsign() == 2) {
                mtime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog tp = new TimePickerDialog.Builder()
                                .setCallBack(new OnDateSetListener() {
                                    @Override
                                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                        mtime.setText(unixToString(millseconds));
                                    }
                                })
                                .setCancelStringId("取消")
                                .setSureStringId("确认")
                                .setTitleStringId("选择时间")
                                .setCyclic(true)
                                .setMinMillseconds(946656000)
                                .setCurrentMillseconds(System.currentTimeMillis())
                                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                                .setType(Type.ALL)
                                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                                .setWheelItemTextSize(12)
                                .build();
                        tp.show(getSupportFragmentManager(), "all");
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
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(MissionInfoActivity.this)
                                .setMessage("确认开始任务？")
                                .setPositiveButton("开始", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        initRetrofit();
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
            } else{
                svoice.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                mcustomer.setFocusable(false);
                mdes.setFocusable(false);
            }
        } else {
            svoice.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            mcustomer.setFocusable(false);
            mdes.setFocusable(false);
        }

        pvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = mdata.getVsrc().replaceAll("voice/", "");
                if (!fileIsExists(fname))
                    initGetVoice(mdata.getVsrc(),
                            fname);
                else playVoice(fname);

            }
        });


    }

    private void stopVoice() {//停止录音

        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Toast.makeText(getApplicationContext(), "保存录音" + mFileName, Toast.LENGTH_SHORT).show();
        svoice.setText("重新录音");
        mdata.setVsrc("voice/" + voicename);
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
        mFileName = this.getCacheDir().toString() + File.separator + voicename;
        File directory = new File(mFileName).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            Log.i(TAG, "Path to file could not be created");
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
            Log.e(TAG, "startVoice() failed");
        }
        mRecorder.start();
    }

    public interface FileUploadService { //retrofit上传文件接口
        @Multipart
        @POST("updMission.do")
        Call<ResponseBody> upload(@Part("vid") int vid,
                                  @Part("eid") int eid,
                                  @Part("vdes") String vdes,
                                  @Part("vtime") String vtime,
                                  @Part("vsrc") RequestBody src,
                                  @Part MultipartBody.Part file,
                                  @Part("vsign") int vsign,
                                  @Part("vcustomer") String vcustomer);
    }

    public void initRetrofit() {
        LoadDialog.showDialog(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(tapp.SERVERURL)
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
        RequestBody src = RequestBody.create(
                MediaType.parse("multipart/form-data"), descriptionString);
        mdata.setVdes(mdes.getText().toString() + "");
        mdata.setVcustomer(mcustomer.getText().toString() + "");
        mdata.setVtime(Timestamp.valueOf(mtime.getText().toString() + ":00"));
        if (!mdata.getVsrc().equals("voice/" + voicename))
            mdata.setVsrc("voice/" + voicename);
        Call<ResponseBody> call = service.upload(mdata.getVid(), mdata.getEid(), mdata.getVdes(), mtime.getText().toString() + ":00", src, body, mdata.getVsign(), mdata.getVcustomer());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if (response.isSuccessful()) { //上传成功
                    LoadDialog.cancelDialog();
                    Toast.makeText(MissionInfoActivity.this, "success", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(MissionInfoActivity.this)
                            .setMessage("修改成功")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();
                } else
                    try {
                        Log.d("retrofit", response.errorBody().string());
                        Toast.makeText(MissionInfoActivity.this, "出现错误" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
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

    public boolean fileIsExists(String filename) {
        try {
            File f = new File(this.getCacheDir() + File.separator + filename);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    private interface getVoiceIF {
        @GET
        Call<ResponseBody> getVoice(@Url String fileUrl);
    }

    private void initGetVoice(String url, final String filename) {
        LoadDialog.showDialog(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVERURL)
                .build();

        getVoiceIF downloadService = retrofit.create(getVoiceIF.class);

        Call<ResponseBody> call = downloadService.getVoice(tapp.SERVERURL + url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), filename);
                    Log.d(TAG, "file download was a success? " + writtenToDisk);
                } else {
                    Log.d(TAG, "server contact failed");
                    loaddialog.dismiss();
                    Toast.makeText(MissionInfoActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String filename) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(this.getCacheDir() + File.separator + filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                LoadDialog.cancelDialog();
                playVoice(filename);
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void playVoice(String filename) {
        try {
            MediaPlayer mPlayer = PlayerSingleton.getInstance(this, Uri.parse(this.getCacheDir().toString() + File.separator + filename));
            mPlayer.reset();
            mPlayer.setDataSource(this.getCacheDir().toString() + File.separator + filename);
            mPlayer.prepare();
            mPlayer.start();
            Log.d(TAG, "开始播放");
        } catch (IOException e) {
            Log.d(TAG, "播放失败");
        }
    }

}
