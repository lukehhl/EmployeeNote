package com.example.administrator.employeenote.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.example.administrator.employeenote.R;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AgendaInfoActivity extends AppCompatActivity {
    private TextView mstartime, mendtime, mtype;
    private EditText mremark, mtitle;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_info);
        initView();
    }

    private void initView() {
        mtitle = (EditText) findViewById(R.id.atitle);
        mstartime = (TextView) findViewById(R.id.starttime);
        mendtime = (TextView) findViewById(R.id.endtime);
        mtype = (TextView) findViewById(R.id.atype);
        mremark = (EditText) findViewById(R.id.remark);
        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mstartime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tp = new TimePickerDialog.Builder()
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                mstartime.setText(getDateToString(millseconds));
                            }
                        })
                        .setCancelStringId("取消")
                        .setSureStringId("确认")
                        .setTitleStringId("选择时间")
                        .setCyclic(true)
                        .setMinMillseconds(946656000)
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                        .setType(Type.ALL)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                        .setWheelItemTextSize(12)
                        .build();
                tp.show(getSupportFragmentManager(), "all");
            }
        });
        mendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tp = new TimePickerDialog.Builder()
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                mendtime.setText(getDateToString(millseconds));
                            }
                        })
                        .setCancelStringId("取消")
                        .setSureStringId("确认")
                        .setTitleStringId("选择时间")
                        .setCyclic(true)
                        .setMinMillseconds(946656000)
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                        .setType(Type.ALL)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                        .setWheelItemTextSize(12)
                        .build();
                tp.show(getSupportFragmentManager(), "all");
            }
        });

        mtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AgendaInfoActivity.this)
                        .setItems(new String[]{"工作安排", "公司放假", "请假", "出差"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        mtype.setText("工作安排");
                                        break;
                                    case 1:
                                        mtype.setText("公司放假");
                                        break;
                                    case 2:
                                        mtype.setText("请假");
                                        break;
                                    case 3:
                                        mtype.setText("出差");
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });

    }

    public String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = new Date(time);
        return sf.format(d);
    }
}
