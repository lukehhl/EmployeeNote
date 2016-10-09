package com.example.administrator.employeenote.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.OnEntityListener;
import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.adapter.EmployeeAdapter;
import com.example.administrator.employeenote.common.TrackApplication;
import com.example.administrator.employeenote.entity.EmployeeData;
import com.example.administrator.employeenote.entity.RealLocationData;
import com.example.administrator.employeenote.utils.GsonService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class EmployeeActivity extends AppCompatActivity {
    private TextView mename, mejob, medepart, melocal, metele, mephone, meemail;
    private Button btn_mission;
    private ImageView btn_back;
    private EmployeeData eminfo = null;
    private RealLocationData realData = null;

    private TrackApplication tapp;
    private Handler handler;
    private final String TAG = "EmployeeActivityE";
    private LatLng point = null;
    private String place = "定位失败";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        initView();
    }

    private void initView() {

        Intent it = getIntent();
        eminfo = GsonService.parseJson(it.getStringExtra("eminfo"), EmployeeData.class);
        String method = it.getStringExtra("type");

        tapp = (TrackApplication) getApplication();
        handler = new Handler(Looper.getMainLooper());

        mename = (TextView) findViewById(R.id.ename);
        mejob = (TextView) findViewById(R.id.ejob);
        medepart = (TextView) findViewById(R.id.edepart);
        melocal = (TextView) findViewById(R.id.elocall);
        metele = (TextView) findViewById(R.id.etele);
        mephone = (TextView) findViewById(R.id.ephone);
        meemail = (TextView) findViewById(R.id.eemail);
        btn_mission = (Button) findViewById(R.id.mission);
        btn_back = (ImageView) findViewById(R.id.back);

       initRetrofit();

        mename.setText(eminfo.getEname());
        mejob.setText(eminfo.getEjob());
        medepart.setText(eminfo.getEdepart());
        metele.setText(eminfo.getEtele());
        mephone.setText(eminfo.getEphone());
        meemail.setText(eminfo.getEemail());

        btn_back.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                        }
                                    }

        );


        if ("getLeader".equalsIgnoreCase(method)) {
            btn_mission.setVisibility(View.GONE);
        } else if ("getEmployee".equalsIgnoreCase(method)) {
            btn_mission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(EmployeeActivity.this, MissionActivity.class);
                    it.putExtra("eid", eminfo.getEid());
                    startActivity(it);
                }
            });
        }

    }

    public void point2geo(RealLocationData real) {
        point = new LatLng(real.getEntities().get(0).getRealtime_point().getLocation().get(0), real.getEntities().get(0).getRealtime_point().getLocation().get(1));
        if (tapp.isInJM(point)) {
            place = "集美厂区";
        } else if (tapp.isInTA(point)) {
            place = "同安厂区";
        } else {
            place = "外出";
        }
        melocal.setText(place);
    }

    interface realLocGetIF { //retrofit接口
        @GET("list")
        Call<RealLocationData> getrealLoc(@Query("ak") String ak,
                                             @Query("service_id") long service_id,
                                            @Query("entity_names") String entity_names,
                                            @Query("active_time") int active_time,
                                            @Query("return_type") int return_type,
                                            @Query("page_index") int page_index,
                                            @Query("page_size") int page_size,
                                             @Query("mcode") String mcode);
    }

    public void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.map.baidu.com/trace/v2/entity/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        realLocGetIF realLocGetIF = retrofit.create(realLocGetIF.class);
        Call<RealLocationData> call = realLocGetIF.getrealLoc("kAq1Mi2NFQT3WUFs7tUQWhjGCEmG38rF",tapp.serviceId,eminfo.getEid(),
                1,0,1,1000,"C0:99:6C:50:5D:24:E7:CB:5E:72:37:84:5D:DB:50:BB:35:7F:7F:3D;com.example.administrator.employeenote");

        call.enqueue(new Callback<RealLocationData>() {
            @Override
            public void onResponse(Call<RealLocationData> call, Response<RealLocationData> response) {
                if (response.isSuccessful()) {
                    realData = response.body();
                    point2geo(realData);
                    Log.d(TAG, new Gson().toJson(realData));
                } else {
                    try {
                        Log.d(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<RealLocationData> call, Throwable t) {
                Log.d(TAG, t.toString());
            }


        });
    }
}
