package com.example.administrator.employeenote.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.employeenote.R;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
    }
}
