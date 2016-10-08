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
import com.example.administrator.employeenote.common.TrackApplication;
import com.example.administrator.employeenote.entity.EmployeeData;
import com.example.administrator.employeenote.entity.RealLocationData;
import com.example.administrator.employeenote.utils.GsonService;


public class EmployeeActivity extends AppCompatActivity {
    private TextView mename, mejob, medepart, melocal, metele, mephone, meemail;
    private Button btn_mission;
    private ImageView btn_back;

    private TrackApplication tapp;
    private Handler handler;
    private final String TAG = "EmployeeActivityE";
    private LatLng point = null;
    private boolean realsign = false;
    private String realError = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        initView();
    }

    private void initView() {

        Intent it = getIntent();
        final EmployeeData eminfo = GsonService.parseJson(it.getStringExtra("eminfo"), EmployeeData.class);
        String method = it.getStringExtra("type");

        tapp = (TrackApplication) getApplication();
        handler = new Handler(Looper.getMainLooper());

        mename = (TextView) findViewById(R.id.ename);
        mejob = (TextView) findViewById(R.id.ejob);
        medepart = (TextView) findViewById(R.id.edepart);
        melocal = (TextView) findViewById(R.id.elocal);
        metele = (TextView) findViewById(R.id.etele);
        mephone = (TextView) findViewById(R.id.ephone);
        meemail = (TextView) findViewById(R.id.eemail);
        btn_mission = (Button) findViewById(R.id.mission);
        btn_back = (ImageView) findViewById(R.id.back);

        realLoc();

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
                    Intent it = new Intent(EmployeeActivity.this, DrawMapActivity.class);
                    it.putExtra("eid", eminfo.getEid());
                    startActivity(it);
                }
            });
        }

    }

    private void findLocationAtTime() {

        //entity标识列表（多个entityName，以英文逗号"," 分割）
        String entityNames = tapp.getEid();
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
                realError = arg0;
                realsign = false;
            }

            // 查询entity回调接口，返回查询结果列表
            @Override
            public void onQueryEntityListCallback(String arg0) {
                point2geo(arg0);
                Log.i(TAG, "onQueryEntityListCallback" + " arg0 = " + arg0);
            }
        };
        // 查询实时轨迹
        HomePageActivity.client.queryEntityList(tapp.getServiceId(), entityNames, columnKey, returnType, activeTime, pageSize, pageIndex, entityListener);
    }

    private void point2geo(String realLoc) {
        RealLocationData realData = GsonService.parseJson(realLoc,
                RealLocationData.class);
        point = new LatLng(realData.getEntities().get(0).getRealtime_point().getLocation().get(0), realData.getEntities().get(0).getRealtime_point().getLocation().get(1));
        realsign = true;
    }

    private void realLoc() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                realsign = false;
                findLocationAtTime();
                while (true) {
                    Log.i("loop", "111111111111111111");
                    if (realsign&& point!= null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (tapp.isInJM(point)) {
                                    melocal.setText("集美厂区");
                                    Log.i("ssss", "222222");

                                } else if (tapp.isInTA(point)) {
                                    melocal.setText("同安厂区");
                                    Log.i("ssss", "222222");
                                } else {
                                    melocal.setText("外出");
                                    Log.i("ssss", "222222");
                                }
                            }
                        });
                        break;
                    }else if (realError != null){
                        Toast.makeText(EmployeeActivity.this, realError, Toast.LENGTH_SHORT).show();
                        Log.i("ssss", "3333333");

                        break;
                    }
                }
            }
        }.start();
    }
}
