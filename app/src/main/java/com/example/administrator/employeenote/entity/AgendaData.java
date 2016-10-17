package com.example.administrator.employeenote.entity;

import java.util.List;

/**
 * Created by GE11522 on 2016-10-17.
 */

public class AgendaData {
    private String year, month, date, week;
    private int count;
    private List<AgendaInfoData> agendaInfoData;

    public AgendaData(String year, String month, String date, String week, int count, List<AgendaInfoData> agendaInfoData) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.week = week;
        this.count = count;
        this.agendaInfoData = agendaInfoData;
    }

    public class AgendaInfoData{
        private int id;
        private String time;
        private String title;

        public AgendaInfoData(int id, String time, String title) {
            this.id = id;
            this.time = time;
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<AgendaInfoData> getAgendaInfoData() {
        return agendaInfoData;
    }

    public void setAgendaInfoData(List<AgendaInfoData> agendaInfoData) {
        this.agendaInfoData = agendaInfoData;
    }
}
