package com.example.administrator.employeenote.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.entity.Employee.EmployeeData;

import java.util.List;

public class EmployeeActivity extends AppCompatActivity {
    private TextView mename, mejob, medepart, melocal, metele, mephone, meemail;
    private Button btn_trace;
    private ImageView btn_back,btn_refresh;
    private List<EmployeeData> employeeDataList;
    private String eid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        initView();
    }

    private void initView() {

        mename = (TextView) findViewById(R.id.ename);
        mejob = (TextView) findViewById(R.id.ejob);
        medepart = (TextView) findViewById(R.id.edepart);
        melocal = (TextView) findViewById(R.id.elocal);
        metele = (TextView) findViewById(R.id.etele);
        mephone = (TextView) findViewById(R.id.ephone);
        meemail = (TextView) findViewById(R.id.eemail);
        btn_trace = (Button) findViewById(R.id.trace);
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
        btn_trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(EmployeeActivity.this, DrawMapActivity.class);
                it.putExtra("eid", eid);
                startActivity(it);
            }
        });
    }


}
