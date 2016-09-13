package com.example.administrator.employeenote.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.entity.Employee.EmployeeData;

import java.util.List;

public class EmployeeListActivity extends AppCompatActivity {
    private ListView listview = null;
    private List<EmployeeData> elist = null;
    private ImageView btn_back, btn_refresh;


    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeelist);
        initView();
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.eList);
        btn_back = (ImageView) findViewById(R.id.back);
        btn_refresh = (ImageView) findViewById(R.id.refresh);

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
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
