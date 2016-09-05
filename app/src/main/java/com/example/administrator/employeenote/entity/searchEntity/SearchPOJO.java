package com.example.administrator.employeenote.entity.searchEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public class SearchPOJO {
    private int status;

    private String message;

    private int size;

    private int total;

    private List<Entities> entities ;

    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
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
    public void setEntities(List<Entities> entities){
        this.entities = entities;
    }
    public List<Entities> getEntities(){
        return this.entities;
    }

}

