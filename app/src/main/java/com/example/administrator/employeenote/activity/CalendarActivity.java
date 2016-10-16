package com.example.administrator.employeenote.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.administrator.employeenote.R;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendar;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
    }

    private void initView() {
        calendar = (CalendarView) findViewById(R.id.calendar_view);
        text = (TextView) findViewById(R.id.text);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                view.setBackgroundColor(getResources().getColor(R.color.red));
                text.setText(String.valueOf(dayOfMonth));
            }
        });

    }
}
