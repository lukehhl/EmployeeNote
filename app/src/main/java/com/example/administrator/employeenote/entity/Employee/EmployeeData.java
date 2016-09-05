package com.example.administrator.employeenote.entity.Employee;

/**
 * Created by GE11522 on 2016-9-5.
 */
public class EmployeeData {
    private String eid,ename,ejob,edepart;

    public EmployeeData(String eid, String ename, String ejob, String edepart) {
        this.eid = eid;
        this.ename = ename;
        this.ejob = ejob;
        this.edepart = edepart;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEjob() {
        return ejob;
    }

    public void setEjob(String ejob) {
        this.ejob = ejob;
    }

    public String getEdepart() {
        return edepart;
    }

    public void setEdepart(String edepart) {
        this.edepart = edepart;
    }
}
