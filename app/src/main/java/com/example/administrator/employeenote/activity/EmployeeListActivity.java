package com.example.administrator.employeenote.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.adapter.EmployeeAdapter;
import com.example.administrator.employeenote.common.TrackApplication;
import com.example.administrator.employeenote.entity.EmployeeData;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class EmployeeListActivity extends AppCompatActivity {
    private ListView listview;
    private List<EmployeeData> elist = null;
    private ImageView btn_back, btn_refresh;

    private TrackApplication tapp;
    private Handler handler;
    private String method;

    private final String TAG = "retrofit";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeelist);


        initView();
    }

    private void initView() {

        tapp = (TrackApplication) getApplication();
        Intent it = getIntent();
        method = it.getStringExtra("type");
        handler = new Handler(Looper.getMainLooper());

        listview = (ListView) findViewById(R.id.eList);
        btn_back = (ImageView) findViewById(R.id.back);
        btn_refresh = (ImageView) findViewById(R.id.refresh);

        getEInfo();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 刷新人员列表操作
//                listview.removeAllViews();
                getEInfo();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    interface employeeGetIF { //retrofit接口
        @GET("getEmployee.do")
        Call<List<EmployeeData>> getEmployee(@Query("id") String id,
                                             @Query("method") String method);
    }

    public List<EmployeeData> initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(tapp.serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        employeeGetIF employeeGetIF = retrofit.create(employeeGetIF.class);
        Call<List<EmployeeData>> call = employeeGetIF.getEmployee(tapp.getEid(),
                method);

        call.enqueue(new Callback<List<EmployeeData>>() {
            @Override
            public void onResponse(Call<List<EmployeeData>> call, Response<List<EmployeeData>> response) {
                if (response.isSuccessful()) {
                    elist = response.body();
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
            public void onFailure(Call<List<EmployeeData>> call, Throwable t) {
                Log.d(TAG, t.toString());
            }


        });

        return elist;
    }

    public void getEInfo() {
        new Thread() {
            @Override
            public void run() {
                initRetrofit();
                while (true) {
                    if (elist != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                EmployeeAdapter adapter = new EmployeeAdapter(EmployeeListActivity.this, elist);
                                listview.setAdapter(adapter);
                            }
                        });
                        break;
                    }
                }
            }
        }.start();
    }
}
