package com.example.administrator.employeenote.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GE11522 on 2016-10-17.
 */

public class AgendaData {
    private String year, month, date, week;
    private List<EventData> eventDatas;

    public AgendaData(String year, String month, String date, List<EventData> eventDatas) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.eventDatas = eventDatas;
    }




    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<EventData> getEventDatas() {
        return eventDatas;
    }

    public void setEventDatas(List<EventData> eventDatas) {
        this.eventDatas.clear();
        this.eventDatas.addAll(eventDatas);
    }

    @Override
    public String toString() {
        return year + "-" + month + "-" + date;
    }
}
