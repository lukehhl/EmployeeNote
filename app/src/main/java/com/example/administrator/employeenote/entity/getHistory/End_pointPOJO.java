package com.example.administrator.employeenote.entity.getHistory;

/**
 * Created by Administrator on 2016/8/15.
 */
public class End_pointPOJO {
    private double longitude;

    private double latitude;

    private int coord_type;

    private int loc_time;

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
    public double getLongitude(){
        return this.longitude;
    }
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }
    public double getLatitude(){
        return this.latitude;
    }
    public void setCoord_type(int coord_type){
        this.coord_type = coord_type;
    }
    public int getCoord_type(){
        return this.coord_type;
    }
    public void setLoc_time(int loc_time){
        this.loc_time = loc_time;
    }
    public int getLoc_time(){
        return this.loc_time;
    }

}
