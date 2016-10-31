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
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.adapter.EmployeeAdapter;
import com.example.administrator.employeenote.common.TrackApplication;
import com.example.administrator.employeenote.entity.EmployeeData;
import com.example.administrator.employeenote.entity.RealLocationData;
import com.example.administrator.employeenote.utils.LoadDialog;
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
    private EmployeeAdapter adapter;
    private List<EmployeeData> elist = null;
    private RealLocationData realData = null;
    private ImageView btn_back, btn_refresh;

    private TrackApplication tapp;
    public static Handler handler;
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

        initRetrofit();

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
                initRetrofit();
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
        LoadDialog.showDialog(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(tapp.SERVERURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        employeeGetIF employeeGetIF = retrofit.create(employeeGetIF.class);
        Call<List<EmployeeData>> call = employeeGetIF.getEmployee(tapp.getPerson().getEid(),
                method);

        call.enqueue(new Callback<List<EmployeeData>>() {
            @Override
            public void onResponse(Call<List<EmployeeData>> call, Response<List<EmployeeData>> response) {
                if (response.isSuccessful()) {
                    elist = response.body();
                    initRetrofitreal();

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

    public void initRetrofitreal() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.map.baidu.com/trace/v2/entity/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        realLocGetIF realLocGetIF = retrofit.create(realLocGetIF.class);
        String entityNames = "";
        for (int i = 0; i < elist.size(); i++) {
            if (i == elist.size() - 1)
                entityNames += elist.get(i).getEid();
            else entityNames += elist.get(i).getEid() + ",";
        }
        Call<RealLocationData> call = realLocGetIF.getrealLoc("kAq1Mi2NFQT3WUFs7tUQWhjGCEmG38rF", tapp.serviceId, entityNames,
                1, 0, 1, 1000, "82:C7:11:19:17:AB:4E:B3:AA:D7:2F:FA:46:F8:0F:CB:DF:83:AD:46;com.example.administrator.employeenote");

        call.enqueue(new Callback<RealLocationData>() {
            @Override
            public void onResponse(Call<RealLocationData> call, Response<RealLocationData> response) {
                if (response.isSuccessful()) {
                    realData = response.body();
                    setLoc();
                    adapter = new EmployeeAdapter(EmployeeListActivity.this, elist);
                    listview.setAdapter(adapter);
                    LoadDialog.cancelDialog();
                    Log.d(TAG, new Gson().toJson(realData));
                } else {
                    try {
                        Toast.makeText(EmployeeListActivity.this, response.errorBody().string(), Toast.LENGTH_LONG).show();
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

    private void setLoc() {
        for (int i = 0; i < elist.size(); i++) {
            if (realData != null && realData.getEntities() != null) {
                for (int j = 0; j < realData.getEntities().size(); j++) {
                    if (elist.get(i).getEid().equals(realData.getEntities().get(j).getEntity_name())) {
                        LatLng point = new LatLng(realData.getEntities().get(j).getRealtime_point().getLocation().get(0)
                                , realData.getEntities().get(j).getRealtime_point().getLocation().get(1));
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
}
