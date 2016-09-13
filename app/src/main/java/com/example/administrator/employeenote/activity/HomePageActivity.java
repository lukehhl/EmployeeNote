package com.example.administrator.employeenote.activity;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.common.TrackApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity {
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    // 图片封装为一个数组
    private int[] icon = {R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher};
    private String[] iconName = {"我的上级", "我的下属", "其他"};
    private TrackApplication tapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initView();
    }

    private void initView() {
        tapp = (TrackApplication) getApplication();
        gview = (GridView) findViewById(R.id.menugrid);
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        goToAttract("up");
                        break;
                    case 1:
                        goToAttract("down");
                        break;
                    case 2:
                        goToAttract("other");
                        break;
                }
            }
        });
        initItmList();
    }

    //跳转
    public void goToAttract(String type) {
        Intent intent = new Intent(this, EmployeeListActivity.class);
        intent.putExtra("eid", tapp.getEid());
        intent.putExtra("type", type);
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
}
