package com.example.administrator.employeenote.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.Trace;
import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.common.TrackApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity {
    private TextView mename,mdate,mweekdate;
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    // 图片封装为一个数组
    private int[] icon = {R.mipmap.homeperson, R.mipmap.homeperson,
            R.mipmap.homemission, R.mipmap.homesetup, R.mipmap.homemission};
    private String[] iconName = {"我的上级", "我的下属", "我的行程", "设置", "日程"};
    private TrackApplication tapp;

    /**
     * 轨迹服务
     */
    public static Trace trace = null;
    /**
     * entity标识
     */
    public static String entityName = null;
    /**
     * 轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
     */
    private int traceType = 2;

    /**
     * 轨迹服务客户端
     */
    public static LBSTraceClient client = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initView();
    }

    private void initView() {
        tapp = (TrackApplication) getApplication();
        gview = (GridView) findViewById(R.id.menugrid);
        mename = (TextView) findViewById(R.id.ename);
        mdate = (TextView) findViewById(R.id.date);
        mweekdate = (TextView) findViewById(R.id.weekdate);
        mename.setText(tapp.getPerson().getEname());

        startMap();

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        goToAttract("getLeader", EmployeeListActivity.class);
                        break;
                    case 1:
                        goToAttract("getEmployee", EmployeeListActivity.class);
                        break;
                    case 2:
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
                        builder.setItems(new String[]{"新增行程", "查看行程"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        goToAttract("addmission", AddMissionActivity.class);
                                        break;
                                    case 1:
                                        goToAttract("mission", MissionActivity.class);
                                        break;
                                }
                            }
                        });
                        builder.show();
                        break;
                    case 3:
                        goToAttract("setup", SetUpActivity.class);
                        break;
                    case 4:
                        goToAttract("calendar", CalendarActivity.class);
                        break;
                }
            }
        });
        initItmList();
    }

    //跳转
    public void goToAttract(String type, Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("type", type);
        intent.putExtra("eid",tapp.getPerson().getEid());
        startActivity(intent);

    }

    //项目列表
    public void initItmList() {
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String[] from = {"menuitemimg", "menuitemtxt"};
        int[] to = {R.id.menuitemimg, R.id.menuitemtxt};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.grid_home, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);

    }

    public List<Map<String, Object>> getData() {
        //icon和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("menuitemimg", icon[i]);
            map.put("menuitemtxt", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }

    private void startMap() {
        //实例名称
        entityName = tapp.getPerson().getEid();
        //实例化轨迹服务客户端
        client = new LBSTraceClient(getApplicationContext());
        //实例化轨迹服务
        trace = new Trace(getApplicationContext(), tapp.getServiceId(), entityName, traceType);
        //位置采集周期
        int gatherInterval = 10;
        //打包周期
        int packInterval = 60;
        //设置位置采集和打包周期
        client.setInterval(gatherInterval, packInterval);

        //实例化开启轨迹服务回调接口
        OnStartTraceListener startTraceListener = new OnStartTraceListener() {
            //开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTraceCallback(int arg0, String arg1) {
                Toast.makeText(getApplicationContext(), arg0 + "    " + arg1, Toast.LENGTH_SHORT).show();
                if (arg0 == 0) {
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
                new android.app.AlertDialog.Builder(HomePageActivity.this)
                        .setMessage("停止成功")
                        .setPositiveButton("确定", null)
                        .show();
            }

            @Override
            public void onStopTraceFailed(int i, String s) {
//                Toast.makeText(getApplicationContext(),"停止失败", Toast.LENGTH_LONG).show();
                new android.app.AlertDialog.Builder(HomePageActivity.this)
                        .setMessage("停止失败")
                        .setPositiveButton("确定", null)
                        .show();
            }
        };
        client.stopTrace(trace, onStopTraceListener);
    }
}
