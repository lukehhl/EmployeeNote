package com.example.administrator.employeenote.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.activity.DrawMapActivity;
import com.example.administrator.employeenote.activity.MissionActivity;
import com.example.administrator.employeenote.activity.MissionInfoActivity;
import com.example.administrator.employeenote.entity.MissionData;
import com.example.administrator.employeenote.utils.PlayerSingleton;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

import static com.example.administrator.employeenote.common.TrackApplication.SERVERURL;

/**
 * Created by GE11522 on 2016/9/28.
 */

public class MissionAdapter extends BaseAdapter {

    private static final String TAG = "missionadapter";
    private String delSign = null;
    private String finSign = null;
    private AlertDialog loaddialog;

    public List<MissionData> data;
    private LayoutInflater mInflater;
    private Context context;

    public MissionAdapter(Context context, List<MissionData> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (convertView == null) {
            hold = new MissionAdapter.ViewHold();
            convertView = mInflater.inflate(R.layout.list_mission, null);
            hold.yearView = (TextView) convertView.findViewById(R.id.year);
            hold.dateView = (TextView) convertView.findViewById(R.id.date);
            hold.customerView = (TextView) convertView.findViewById(R.id.customer);
            hold.findView = (Button) convertView.findViewById(R.id.btn_find);
            hold.delView = (Button) convertView.findViewById(R.id.btn_delete);
            hold.finishView = (Button) convertView.findViewById(R.id.btn_finish);
            hold.traceView = (Button) convertView.findViewById(R.id.btn_review);
            hold.stateView = (TextView) convertView.findViewById(R.id.state);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        hold.yearView.setText(data.get(position).getVtime().toString().substring(0, 4) + "年");
        hold.dateView.setText(data.get(position).getVtime().toString().substring(5, 16).replace('-', '月').replace(' ', '日'));
        hold.findView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MissionInfoActivity.class);
                it.putExtra("missiondata", new Gson().toJson(data.get(position)));
                context.startActivity(it);
            }
        });
        hold.customerView.setText(data.get(position).getVcustomer());
        if (data.get(position).getVsign() == 0) { //进行中的任务

            hold.delView.setEnabled(true);
            hold.delView.setVisibility(View.VISIBLE);
            hold.finishView.setEnabled(true);
            hold.finishView.setVisibility(View.VISIBLE);
            hold.traceView.setEnabled(false);
            hold.traceView.setVisibility(View.INVISIBLE);

            hold.delView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initDelMission(String.valueOf(data.get(position).getVid()), position);
                    //TODO 删除后处理

                }
            });

            hold.finishView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initUpdMistate(String.valueOf(data.get(position).getVid()), position, 1);
                }
            });
            hold.stateView.setText("进行中");
            hold.stateView.setTextColor(convertView.getResources().getColor(R.color.red));
        } else if (data.get(position).getVsign() == 1) { //已完成的任务
            hold.delView.setEnabled(false);
            hold.delView.setVisibility(View.INVISIBLE);
            hold.finishView.setEnabled(false);
            hold.finishView.setVisibility(View.INVISIBLE);
            hold.traceView.setEnabled(true);
            hold.traceView.setVisibility(View.VISIBLE);
            hold.traceView.setText("查看路线");


            hold.traceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String startTime = null, endTime = null;
                    try {
//                    startTime = String.valueOf(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.get(position).getVtime().toString()).getTime());
//                    endTime = String.valueOf(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.get(position).getEtime().toString()).getTime());
                        startTime = String.valueOf(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2016/08/22 08:40:00").getTime());
                        endTime = String.valueOf(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2016/08/22 08:55:00").getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Intent it = new Intent(context, DrawMapActivity.class);
                    it.putExtra("startTime", startTime);
                    it.putExtra("endTime", endTime);
                    context.startActivity(it);
                }
            });
            hold.stateView.setText("已完成");
            hold.stateView.setTextColor(convertView.getResources().getColor(R.color.green));

        } else if (data.get(position).getVsign() == 2) { //计划中的任务
            hold.delView.setEnabled(false);
            hold.delView.setVisibility(View.INVISIBLE);
            hold.finishView.setEnabled(false);
            hold.finishView.setVisibility(View.INVISIBLE);
            hold.traceView.setEnabled(true);
            hold.traceView.setVisibility(View.VISIBLE);

            hold.traceView.setText("开始任务");

            hold.traceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initUpdMistate(String.valueOf(data.get(position).getVid()), position, 0);
                }
            });
            hold.stateView.setText("计划中");
            hold.stateView.setTextColor(convertView.getResources().getColor(R.color.blue_selected));
        }

        return convertView;
    }

    private interface delVoiceIF {
        @GET("delMission.do")
        Call<ResponseBody> delMission(@Query("vid") String vid);
    }

    private interface updMistateIF {
        @GET("updMissionState.do")
        Call<ResponseBody> updMistate(@Query("vid") String vid,
                                      @Query("state") String state);
    }

    private void initDelMission(String vid, final int position) {
        final AlertDialog loaddialog = new SpotsDialog(context);
        loaddialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVERURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        delVoiceIF delVoiceIF = retrofit.create(delVoiceIF.class);
        Call<ResponseBody> call = delVoiceIF.delMission(vid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        delSign = response.body().string();
                        Toast.makeText(context, delSign, Toast.LENGTH_SHORT).show();
                        data.remove(position);
                        notifyDataSetChanged();
                        loaddialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "delete succeed");
                } else {
                    try {
                        Log.d(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, t.toString());
            }


        });
    }

    private void initUpdMistate(String vid, final int position, final int state) {
        final AlertDialog loaddialog = new SpotsDialog(context);
        loaddialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVERURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        updMistateIF finmissionIF = retrofit.create(updMistateIF.class);
        Call<ResponseBody> call = finmissionIF.updMistate(vid, String.valueOf(state));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        finSign = response.body().string();
                        data.get(position).setVsign(state);
                        notifyDataSetChanged();
                        loaddialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "upd succeed");
                } else {
                    try {
                        Log.d(TAG, response.errorBody().string());
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, t.toString());
            }


        });
    }



    private void playVoice(String filename) {
        try {
            MediaPlayer mPlayer = PlayerSingleton.getInstance(context, Uri.parse(context.getCacheDir() + File.separator + filename));
            mPlayer.reset();
            mPlayer.setDataSource(context.getCacheDir() + File.separator + filename);
            mPlayer.prepare();
            mPlayer.start();
            Log.d(TAG, "开始播放");
        } catch (IOException e) {
            Log.d(TAG, "播放失败");
        }
    }

    class ViewHold {
        //public ImageView image;
        public TextView yearView, dateView, stateView, customerView;
        public Button findView, delView, traceView, finishView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
