package com.example.administrator.employeenote.entity;

import java.sql.Timestamp;

public class VoiceData {
	private int vid,eid,vsign;
	private Timestamp vtime;
	private Timestamp etime;
	private String vsrc;
	
	public VoiceData(int vid, int eid, Timestamp vtime, Timestamp etime, String vsrc, int vsign) {
		super();
		this.vid = vid;
		this.eid = eid;
		this.vtime = vtime;
		this.etime = etime;
		this.vsrc = vsrc;
		this.vsign = vsign;
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
	

}
