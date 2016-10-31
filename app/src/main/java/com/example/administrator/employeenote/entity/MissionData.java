package com.example.administrator.employeenote.entity;

import java.sql.Timestamp;

public class MissionData {
    private int vid, eid, vsign;
    private Timestamp vtime;
    private Timestamp etime;
    private String vsrc, vdes,vcustomer;

    public MissionData(int vid, int eid, int vsign, Timestamp vtime, Timestamp etime, String vsrc, String vdes, String vcustomer) {
        this.vid = vid;
        this.eid = eid;
        this.vsign = vsign;
        this.vtime = vtime;
        this.etime = etime;
        this.vsrc = vsrc;
        this.vdes = vdes;
        this.vcustomer = vcustomer;
    }

    public String getVcustomer() {
        return vcustomer;
    }

    public void setVcustomer(String vcustomer) {
        this.vcustomer = vcustomer;
    }

    public int getVsign() {
        return vsign;
    }

    public void setVsign(int vsign) {
        this.vsign = vsign;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public Timestamp getVtime() {
        return vtime;
    }

    public void setVtime(Timestamp vtime) {
        this.vtime = vtime;
    }

    public String getVsrc() {
        return vsrc;
    }

    public void setVsrc(String vsrc) {
        this.vsrc = vsrc;
    }

    public Timestamp getEtime() {
        return etime;
    }

    public void setEtime(Timestamp etime) {
        this.etime = etime;
    }

    public String getVdes() {
        return vdes;
    }

    public void setVdes(String vdes) {
        this.vdes = vdes;
    }
}
