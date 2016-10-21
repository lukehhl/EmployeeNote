package com.example.administrator.employeenote.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.adapter.MissionAdapter;
import com.example.administrator.employeenote.common.TrackApplication;
import com.example.administrator.employeenote.entity.MissionData;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MissionActivity extends AppCompatActivity {
    private static final String TAG = "retrofit";
    private ImageView back, refresh;
    private ListView mlist;


    private TrackApplication tapp;
    public static Handler handler;
    private MissionAdapter adapter;
    private String eid;

    private List<MissionData> vlist;
    private Boolean retrofitSign = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        initView();

    }

    private void initView() {

        tapp = (TrackApplication) getApplication();
        handler = new Handler(Looper.getMainLooper());

        Intent it = getIntent();
        eid = it.getStringExtra("eid");

        back = (ImageView) findViewById(R.id.back);
        refresh = (ImageView) findViewById(R.id.refresh);
        mlist = (ListView) findViewById(R.id.mList);

        initMission();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vlist.clear();
                initMission();
            }
        });

    }

    public interface missionGetIF {
        @GET("getMission.do")
        Call<List<MissionData>> getMission(@Query("eid") String id);
    }

    public void initMission() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(tapp.SERVERURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        missionGetIF missionGetIF = retrofit.create(missionGetIF.class);
        Call<List<MissionData>> call = missionGetIF.getMission(eid);

        call.enqueue(new Callback<List<MissionData>>() {
            @Override
            public void onResponse(Call<List<MissionData>> call, Response<List<MissionData>> response) {
                if (response.isSuccessful()) {
                    vlist = response.body();
                    adapter = new MissionAdapter(MissionActivity.this, vlist);
                    mlist.setAdapter(adapter);
                    Toast.makeText(getApplicationContext(), "getlist success", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "succeed");
                } else {
                    try {
                        Log.d(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<MissionData>> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vlist != null) {
            vlist.clear();
            initMission();
        }
    }
}
