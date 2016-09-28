package com.example.administrator.employeenote.entity;

/**
 * Created by GE11522 on 2016-9-5.
 */
public class EmployeeData {
    private String eid,ename,ejob,edepart,etele,ephone,eemail;

    public EmployeeData(String eid, String ename, String ejob, String edepart, String etele, String ephone, String eemail) {
        this.eid = eid;
        this.ename = ename;
        this.ejob = ejob;
        this.edepart = edepart;
        this.etele = etele;
        this.ephone = ephone;
        this.eemail = eemail;
    }

    public String getEtele() {
        return etele;
    }

    public void setEtele(String etele) {
        this.etele = etele;
    }

    public String getEphone() {
        return ephone;
    }

    public void setEphone(String ephone) {
        this.ephone = ephone;
    }

    public String getEemail() {
        return eemail;
    }

    public void setEemail(String eemail) {
        this.eemail = eemail;
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
