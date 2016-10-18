package com.example.administrator.employeenote.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.dsw.calendar.entity.CalendarInfo;
import com.dsw.calendar.views.CircleCalendarView;
import com.example.administrator.employeenote.R;

import java.util.ArrayList;
import java.util.List;


public class CalendarActivity extends AppCompatActivity {

    private CircleCalendarView circleCalendarView;
    private CalendarView calendar;
    private TextView text;
    private ListView agendals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
    }

    private void initView() {
//        calendar = (CalendarView) findViewById(R.id.calendar_view);
        List<CalendarInfo> list = new ArrayList<CalendarInfo>();
        text = (TextView) findViewById(R.id.text);
        agendals = (ListView) findViewById(R.id.agenda_ls);
        circleCalendarView = (CircleCalendarView) findViewById(R.id.calendar_view);
        list.add(new CalendarInfo(2016,10,18,"1",2));
        circleCalendarView.setCalendarInfos(list);
    }
}