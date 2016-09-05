package com.example.administrator.employeenote.entity.getHistory;

import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class PointsPOJO {
    private int loc_time;

    private List<Double> location ;

    private String create_time;

    private int floor;

    private double radius;

    private int speed;

    private int direction;

    public void setLoc_time(int loc_time){
        this.loc_time = loc_time;
    }
    public int getLoc_time(){
        return this.loc_time;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public void setCreate_time(String create_time){
        this.create_time = create_time;
    }
    public String getCreate_time(){
        return this.create_time;
    }
    public void setFloor(int floor){
        this.floor = floor;
    }
    public int getFloor(){
        return this.floor;
    }
    public void setRadius(double radius){
        this.radius = radius;
    }
    public double getRadius(){
        return this.radius;
    }
    public void setSpeed(int speed){
        this.speed = speed;
    }
    public int getSpeed(){
        return this.speed;
    }
    public void setDirection(int direction){
        this.direction = direction;
    }
    public int getDirection(){
        return this.direction;
    }

}
