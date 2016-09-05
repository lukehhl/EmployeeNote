package com.example.administrator.employeenote.entity.searchEntity;



/**
 * Created by Administrator on 2016/8/13.
 */
public class Realtime_point {
    private int loc_time;

    public Double[] getLocation() {
        return location;
    }

    public void setLocation(Double[] location) {
        this.location = location;
    }

    private Double[] location ;

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
