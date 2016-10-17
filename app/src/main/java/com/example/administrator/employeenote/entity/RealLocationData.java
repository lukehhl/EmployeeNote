package com.example.administrator.employeenote.entity;


import java.util.List;

/**
 * Created by GE11522 on 2016/10/8.
 */

public class RealLocationData {
    private int status;

    private String message;

    private int size;

    private int total;

    private List<Entities> entities;

    public class Entities {
        private String entity_name;

        private String create_time;

        private String modify_time;

        private Realtime_point realtime_point;



        public void setEntity_name(String entity_name) {
            this.entity_name = entity_name;
        }

        public String getEntity_name() {
            return this.entity_name;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getCreate_time() {
            return this.create_time;
        }

        public void setModify_time(String modify_time) {
            this.modify_time = modify_time;
        }

        public String getModify_time() {
            return this.modify_time;
        }

        public void setRealtime_point(Realtime_point realtime_point) {
            this.realtime_point = realtime_point;
        }

        public Realtime_point getRealtime_point() {
            return this.realtime_point;
        }

    }

    public class Realtime_point {
        private int loc_time;

        private List<Double> location;

        private int floor;

        private double radius;

        private float speed;

        private int direction;

        public void setLoc_time(int loc_time) {
            this.loc_time = loc_time;
        }

        public int getLoc_time() {
            return this.loc_time;
        }

        public void setLocation(List<Double> location) {
            this.location = location;
        }

        public List<Double> getLocation() {
            return this.location;
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }

        public int getFloor() {
            return this.floor;
        }

        public void setRadius(double radius) {
            this.radius = radius;
        }

        public double getRadius() {
            return this.radius;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public float getSpeed() {
            return this.speed;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public int getDirection() {
            return this.direction;
        }

    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return this.total;
    }

    public void setEntities(List<Entities> entities) {
        this.entities = entities;
    }

    public List<Entities> getEntities() {
        return this.entities;
    }
}
