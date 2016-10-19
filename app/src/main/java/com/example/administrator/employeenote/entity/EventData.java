package com.example.administrator.employeenote.entity;

/**
 * Created by GE11522 on 2016/10/19.
 */

public class EventData {
    private String title,description;
    private long dtstart,dtend,event_id;
    private int hasalarm,minutes;

    public EventData(String title, String description, long dtstart, long dtend, int event_id) {
        this.title = title;
        this.description = description;
        this.dtstart = dtstart;
        this.dtend = dtend;
        this.event_id = event_id;
    }

    public EventData(String title, String description, long dtstart, long dtend, int event_id, int hasalarm, int minutes) {
        this.title = title;
        this.description = description;
        this.dtstart = dtstart;
        this.dtend = dtend;
        this.event_id = event_id;
        this.hasalarm = hasalarm;
        this.minutes = minutes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDtstart() {
        return dtstart;
    }

    public void setDtstart(long dtstart) {
        this.dtstart = dtstart;
    }

    public long getDtend() {
        return dtend;
    }

    public void setDtend(long dtend) {
        this.dtend = dtend;
    }

    public long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(long event_id) {
        this.event_id = event_id;
    }

    public int getHasalarm() {
        return hasalarm;
    }

    public void setHasalarm(int hasalarm) {
        this.hasalarm = hasalarm;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
