package com.example.administrator.employeenote.entity.searchEntity;

/**
 * Created by Administrator on 2016/8/13.
 */
public class Entities {
    private String entity_name;

    private String create_time;

    private String modify_time;

    private Realtime_point realtime_point;

    public void setEntity_name(String entity_name){
        this.entity_name = entity_name;
    }
    public String getEntity_name(){
        return this.entity_name;
    }
    public void setCreate_time(String create_time){
        this.create_time = create_time;
    }
    public String getCreate_time(){
        return this.create_time;
    }
    public void setModify_time(String modify_time){
        this.modify_time = modify_time;
    }
    public String getModify_time(){
        return this.modify_time;
    }
    public void setRealtime_point(Realtime_point realtime_point){
        this.realtime_point = realtime_point;
    }
    public Realtime_point getRealtime_point(){
        return this.realtime_point;
    }
}
