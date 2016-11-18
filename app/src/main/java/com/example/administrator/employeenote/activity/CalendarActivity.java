package com.example.administrator.employeenote.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dsw.calendar.component.MonthView;
import com.dsw.calendar.entity.CalendarInfo;
import com.dsw.calendar.views.CircleCalendarView;
import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.adapter.AgendaAdapter;
import com.example.administrator.employeenote.entity.AgendaData;
import com.example.administrator.employeenote.entity.EventData;
import com.example.administrator.employeenote.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;


public class CalendarActivity extends AppCompatActivity {

    private CircleCalendarView circleCalendarView;
    private CalendarView calendar;
    private TextView text;
    private ImageView back;
    private ListView agendals;

    private List<CalendarInfo> list;
    private AgendaAdapter adapter;
    private List<AgendaData> adatas;
    private List<EventData> edatas;

    //为了兼容不同版本的日历,2.2以后url发生改变
    private static String calanderURL = "";
    private static String calanderEventURL = "";
    private static String calanderRemiderURL = "";

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
        setContentView(R.layout.activity_calendar);
        initView();
    }

    private void initView() {
//        calendar = (CalendarView) findViewById(R.id.calendar_view);

        adatas = new ArrayList<>();
        edatas = new ArrayList<>();
        adapter = new AgendaAdapter(CalendarActivity.this, adatas);

        list = new ArrayList<>();
        text = (TextView) findViewById(R.id.text);
        back = (ImageView) findViewById(R.id.back);
        agendals = (ListView) findViewById(R.id.agenda_ls);
        agendals.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        circleCalendarView = (CircleCalendarView) findViewById(R.id.calendar_view);
        initDes();

        circleCalendarView.setDateClick(new MonthView.IDateClick() {
            @Override
            public void onClickOnDate(int year, int month, int day) {
                String time = year + "-" + month + "-" + day;
                int week = DateUtils.dayForWeek(time);
                adatas.clear();
                Log.i("calendar", month + String.valueOf(DateUtils.getMonthDays(year, month)));

                for (int i = -week; i <= 6 - week; i++) {
                    if (day + i > 0 && day + i <= DateUtils.getMonthDays(year, month)){
                        adatas.add(new AgendaData(String.valueOf(year), String.valueOf(month), String.valueOf(day + i), new ArrayList<EventData>()));}
                }
                opEvent();
                adapter.notifyDataSetChanged();

            }
        });


    }

    private void opEvent() {
        for (int i = 0; i < edatas.size(); i++) {
            List<EventData> a_edatas = new ArrayList<>();
            for (int j = 0; j < adatas.size(); j++) {
                if (edatas.get(i).getDtend() - edatas.get(i).getDtstart() <= 86400000) { //持续时间不超过一天的事务
                    long uxatime = DateUtils.date2unix(adatas.get(j).toString());
//                    Log.i("calendar", uxatime + " " + edatas.get(i).getDtstart() + " " + edatas.get(i).getDtend());
                    if (edatas.get(i).getDtstart() > uxatime && edatas.get(i).getDtend() < (uxatime + 86400000)) {
                        a_edatas.add(edatas.get(i));
                        adatas.get(j).setEventDatas(a_edatas);
                        break;
                    }
                } else { //持续时间超过一天的事务
                    long uxatime = DateUtils.date2unix(adatas.get(j).toString());
                    Log.i("calendar", uxatime + " " + edatas.get(i).getDtstart() + " " + edatas.get(i).getDtend() + "x");
                    if (edatas.get(i).getDtstart() - uxatime < 86400000) {
                        if (edatas.get(i).getDtstart() > uxatime && edatas.get(i).getDtend() > (uxatime + 86400000) && edatas.get(i).getDtstart() < (uxatime + 86400000)) {
                            EventData ed = new EventData(edatas.get(i).getTitle(), edatas.get(i).getDescription(), edatas.get(i).getDtstart(), uxatime + 86399999);
                            a_edatas.add(ed);
                            adatas.get(j).setEventDatas(a_edatas);
//                            Log.i("calendar", uxatime + " " + ed.getDtstart() + " " + ed.getDtend() + "x");
                            a_edatas.clear();
                        }
                        if (edatas.get(i).getDtstart() < uxatime && edatas.get(i).getDtend() > (uxatime + 86400000)) {
                            EventData ed = new EventData(edatas.get(i).getTitle(), edatas.get(i).getDescription(), uxatime, uxatime + 86399999);
                            a_edatas.add(ed);
                            adatas.get(j).setEventDatas(a_edatas);
                            a_edatas.clear();
                        }
                        if (edatas.get(i).getDtstart() < uxatime && edatas.get(i).getDtend() < (uxatime + 86400000) && edatas.get(i).getDtend() > uxatime) {
                            EventData ed = new EventData(edatas.get(i).getTitle(), edatas.get(i).getDescription(), uxatime, edatas.get(i).getDtend());
                            a_edatas.add(ed);
                            adatas.get(j).setEventDatas(a_edatas);
                            a_edatas.clear();
                        }
                    }
                }
            }
        }
    }

    private void initDes() {
        Cursor eventCursor = getContentResolver().query(Uri.parse(calanderEventURL), null,
                null, null, null);
        if (eventCursor.getCount() > 0) {
            while (eventCursor.moveToNext()) {
//                    eventCursor.moveToLast();
                String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
                String eventStime = eventCursor.getString(eventCursor.getColumnIndex("dtstart"));
                String eventEtime = eventCursor.getString(eventCursor.getColumnIndex("dtend"));
                String eventDiscr = eventCursor.getString(eventCursor.getColumnIndex("DESCRIPTION"));
                edatas.add(new EventData(eventTitle, eventDiscr, Long.valueOf(eventStime), Long.valueOf(eventEtime)));
                Log.i("calendar", eventStime + "    " + eventEtime);

//                        Toast.makeText(CalendarActivity.this, DateUtils.unixToString(Long.valueOf(eventStime)), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(TestActivity.this, eventTitle, Toast.LENGTH_LONG).show();
            }
        }
        for (int i = 0; i < edatas.size(); i++) {
            CalendarInfo cinfo;
            //TODO 在日历上标记事务
            if (edatas.get(i).getDtend() - edatas.get(i).getDtstart() <= 86400000) {//持续时间不超过一天的事务
                cinfo = new CalendarInfo(Integer.parseInt(DateUtils.unixToStringyear(edatas.get(i).getDtstart())),
                        Integer.parseInt(DateUtils.unixToStringmonth(edatas.get(i).getDtstart())),
                        Integer.parseInt(DateUtils.unixToStringdate(edatas.get(i).getDtstart())),
                        "*", 2);
                list.add(cinfo);
            } else if (edatas.get(i).getDtend() - edatas.get(i).getDtstart() > 86400000) {//持续时间超过一天的事务
                //TODO 持续时间超过一天的事务处理
                int dur = (Integer.parseInt(DateUtils.unixToStringdate(edatas.get(i).getDtend()))
                        - Integer.parseInt(DateUtils.unixToStringdate(edatas.get(i).getDtstart()))) + 1;

                for (int j = 0; j < dur; j++) {
                    cinfo = new CalendarInfo(Integer.parseInt(DateUtils.unixToStringyear(edatas.get(i).getDtstart())),
                            Integer.parseInt(DateUtils.unixToStringmonth(edatas.get(i).getDtstart())),
                            Integer.parseInt(DateUtils.unixToStringdate(edatas.get(i).getDtstart())) + j,
                            "*", 2);
                    Log.i("calendar", String.valueOf(cinfo.day));

                    list.add(cinfo);
                }
            }

        }
//                for (int i = 0; i < list.size()-1; i++) {
//                    if (list.get(i).year == list.get(i+1).year&&list.get(i).month == list.get(i+1).month&&list.get(i).day == list.get(i+1).day) {
//                        list.remove(i);
//                    }
//
//                }
        circleCalendarView.setCalendarInfos(list);
    }
}
