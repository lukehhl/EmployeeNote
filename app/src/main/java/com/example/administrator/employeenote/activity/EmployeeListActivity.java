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

public class EmployeeListActivity extends AppCompatActivity {
    private ListView listview;
    private List<EmployeeData> elist = null;
    private RealLocationData realData = null;
    private ImageView btn_back, btn_refresh;

    private TrackApplication tapp;
    public static Handler handler;
    private String method;

    private final String TAG = "retrofit";
    private boolean realsign = false;


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
                Gson gson = new Gson();
                String info = gson.toJson(elist.get(position));
                Intent it = new Intent(getApplicationContext(), EmployeeActivity.class);
                it.putExtra("eminfo", info);
                it.putExtra("type", method);
                startActivity(it);
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

    private void findLocationAtTime(List<EmployeeData> ep) {

        //entity标识列表（多个entityName，以英文逗号"," 分割）
        String entityNames = "";
        for (int i = 0; i < ep.size(); i++) {
            if (i == ep.size() - 1)
                entityNames += ep.get(i).getEid();
            else entityNames += ep.get(i).getEid() + ",";
        }

        // 检索条件（格式为 : "key1=value1,key2=value2,....."）
        String columnKey = "";
        // 返回结果的类型（0 : 返回全部结果，1 : 只返回entityName的列表）
        int returnType = 0;
        // 活跃时间，UNIX时间戳（指定该字段时，返回从该时间点之后仍有位置变动的entity的实时点集合）
        int activeTime = 0;
        // 分页大小
        int pageSize = 1000;
        // 分页索引
        int pageIndex = 1;
        // Entity监听器
        OnEntityListener entityListener = new OnEntityListener() {
            // 查询失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                Log.i(TAG, "onRequestFailedCallback" + "arg0 = " + arg0);
                realsign = false;
            }

            // 查询entity回调接口，返回查询结果列表
            @Override
            public void onQueryEntityListCallback(String arg0) {
                realData = GsonService.parseJson(arg0,
                        RealLocationData.class);
                realsign = true;
                Log.i(TAG, "onQueryEntityListCallback" + " arg0 = " + arg0);
            }
        };
        // 查询实时轨迹
        HomePageActivity.client.queryEntityList(tapp.getServiceId(), entityNames, columnKey, returnType, activeTime, pageSize, pageIndex, entityListener);
    }

    private void point2geo(String realLoc) {
        realData = GsonService.parseJson(realLoc,
                RealLocationData.class);
        realsign = true;
    }

    public void getEInfo() {
        new Thread() {
            @Override
            public void run() {
                initRetrofit();
                while (true) {
                    if (elist != null) {
                        getRealLoc();
                        break;
                    }
                }
            }
        }.start();
    }

    private void getRealLoc() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                realsign = false;
                findLocationAtTime(elist);
                while (true) {
                    if (realsign) {
                        setLoc();
                        Log.i(TAG, "local");
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

    private void setLoc(){
        for (int i = 0; i < elist.size(); i++) {
            for (int j = 0; j < realData.getEntities().size(); j++) {
                if (elist.get(i).getEid().equals(realData.getEntities().get(j).getEntity_name())) {
                    LatLng point = new LatLng(realData.getEntities().get(j).getRealtime_point().getLocation().get(0), realData.getEntities().get(j).getRealtime_point().getLocation().get(1));
                    if (tapp.isInJM(point)) {
                        elist.get(i).setElocal("集美厂区");
                    } else if (tapp.isInTA(point)) {
                        elist.get(i).setElocal("同安厂区");
                    } else {
                        elist.get(i).setElocal("外出");
                    }
                }
            }
        }
    }
}
