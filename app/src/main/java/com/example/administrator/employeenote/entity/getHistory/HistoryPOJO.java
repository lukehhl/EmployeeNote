package com.example.administrator.employeenote.entity.getHistory;

import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class HistoryPOJO {
    private int status;

    private int size;

    private int total;

    private String entity_name;

    private int distance;

    private Start_pointPOJO start_point;

    private End_pointPOJO end_point;

    private List<PointsPOJO> points ;

    private String message;

    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setSize(int size){
        this.size = size;
    }
    public int getSize(){
        return this.size;
    }
    public void setTotal(int total){
        this.total = total;
    }
    public int getTotal(){
        return this.total;
    }
    public void setEntity_name(String entity_name){
        this.entity_name = entity_name;
    }
    public String getEntity_name(){
        return this.entity_name;
    }
    public void setDistance(int distance){
        this.distance = distance;
    }
    public int getDistance(){
        return this.distance;
    }
    public void setStart_point(Start_pointPOJO start_point){
        this.start_point = start_point;
    }
    public Start_pointPOJO getStart_point(){
        return this.start_point;
    }
    public void setEnd_point(End_pointPOJO end_point){
        this.end_point = end_point;
    }
    public End_pointPOJO getEnd_point(){
        return this.end_point;
    }
    public void setPoints(List<PointsPOJO> points){
        this.points = points;
    }
    public List<PointsPOJO> getPoints(){
        return this.points;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }

}
