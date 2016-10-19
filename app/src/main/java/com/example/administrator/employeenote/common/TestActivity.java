package com.example.administrator.employeenote.common;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.entity.AgendaData;

import java.util.Calendar;
import java.util.List;

public class TestActivity extends Activity implements View.OnClickListener {
    private Button b1, b2;
    private Button mReadUserButton;
    private Button mReadEventButton;
    private Button mWriteEventButton;
    private TextView tx;

    private TrackApplication tapp;

    private static String calanderURL = "";
    private static String calanderEventURL = "";
    private static String calanderRemiderURL = "";

    //为了兼容不同版本的日历,2.2以后url发生改变
    static {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            calanderURL = "content://com.android.calendar/calendars";
            calanderEventURL = "content://com.android.calendar/events";
            calanderRemiderURL = "content://com.android.calendar/reminders";

        } else {
            calanderURL = "content://calendar/calendars";
            calanderEventURL = "content://calendar/events";
            calanderRemiderURL = "content://calendar/reminders";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        tapp = (TrackApplication) getApplication();
        mReadUserButton = (Button) findViewById(R.id.readUserButton);
        mReadEventButton = (Button) findViewById(R.id.readEventButton);
        mWriteEventButton = (Button) findViewById(R.id.writeEventButton);
        tx = (TextView) findViewById(R.id.textView);
        mReadUserButton.setOnClickListener(this);
        mReadEventButton.setOnClickListener(this);
        mWriteEventButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mReadUserButton) {

            Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null,
                    null, null, null);
            if (userCursor.getCount() > 0) {
                userCursor.moveToFirst();
                String userName = userCursor.getString(userCursor.getColumnIndex("name"));
                Toast.makeText(TestActivity.this, userName, Toast.LENGTH_LONG).show();
            }
        } else if (v == mReadEventButton) {
            Cursor eventCursor = getContentResolver().query(Uri.parse(calanderEventURL), null,
                    null, null, null);
            if (eventCursor.getCount() > 0) {
                while (eventCursor.moveToNext()) {
//                    eventCursor.moveToLast();
                    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
                    tx.setText(tx.getText() + "   " + eventTitle);
//                    Toast.makeText(TestActivity.this, eventTitle, Toast.LENGTH_LONG).show();
                }
            }
        } else if (v == mWriteEventButton) {
            //获取要出入的gmail账户的id
            String calId = "";
            Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null,
                    null, null, null);
            if (userCursor.getCount() > 0) {
                userCursor.moveToFirst();
                calId = userCursor.getString(userCursor.getColumnIndex("_id"));
            }

            ContentValues event = new ContentValues();
            event.put("title", "与苍井空小姐动作交流");
            event.put("description", "Frankie受空姐邀请,今天晚上10点以后将在Sheraton动作交流.lol~");
            //插入hoohbood@gmail.com这个账户
            event.put("calendar_id", calId);

            Calendar mCalendar = Calendar.getInstance();
            mCalendar.set(Calendar.HOUR_OF_DAY, 10);
            long start = mCalendar.getTime().getTime();
            mCalendar.set(Calendar.HOUR_OF_DAY, 11);
            long end = mCalendar.getTime().getTime();

            event.put("dtstart", start);
            event.put("dtend", end);
            event.put("hasAlarm", 1);
            event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");

            Uri newEvent = getContentResolver().insert(Uri.parse(calanderEventURL), event);
            long id = 2;
            tx.setText(String.valueOf(id));
            ContentValues values = new ContentValues();
            values.put("event_id", id);
            //提前10分钟有提醒
            values.put("minutes", 30);
            getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
            Toast.makeText(TestActivity.this, "插入事件成功!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void insAgenda(List<AgendaData.EventData> eventDatas) {
        for (int i = 0; i < eventDatas.size(); i++) {
            Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null,
                    null, null, null);
            if (userCursor.getCount() > 0) {
                userCursor.moveToFirst();
                tapp.calendar_id = userCursor.getString(userCursor.getColumnIndex("_id"));
            }

            ContentValues event = new ContentValues();
            event.put("title", eventDatas.get(i).getTitle());
            event.put("description", eventDatas.get(i).getDescription());
            //插入hoohbood@gmail.com这个账户
            event.put("calendar_id", tapp.calendar_id);

            event.put("dtstart", eventDatas.get(i).getDtstart());
            event.put("dtend", eventDatas.get(i).getDtend());
            event.put("hasAlarm", eventDatas.get(i).getHasalarm());
            event.put(CalendarContract.Events.EVENT_TIMEZONE, tapp.TIMEZONE);

            Uri newEvent = getContentResolver().insert(Uri.parse(calanderEventURL), event);
            long id = Long.parseLong(newEvent.getLastPathSegment());
            ContentValues values = new ContentValues();
            values.put("event_id", eventDatas.get(i).getEvent_id());
            values.put("minutes", eventDatas.get(i).getMinutes());
            getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
            Log.i("insAgenda", "success");
        }

    }


}
