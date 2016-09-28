package com.example.administrator.employeenote.entity;

import java.sql.Timestamp;

public class VoiceData {
	private int vid,eid;
	private Timestamp vtime;
	private String vsrc;
	
	public VoiceData(int vid, int eid, Timestamp vtime, String vsrc) {
		super();
		this.vid = vid;
		this.eid = eid;
		this.vtime = vtime;
		this.vsrc = vsrc;
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
	
	

}
