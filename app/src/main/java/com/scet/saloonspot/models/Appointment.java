package com.scet.saloonspot.models;

public class Appointment {
    String User_id, id;
    Request request;

    public Appointment(String user_id, String id, Request request) {
        User_id = user_id;
        this.id = id;
        this.request = request;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
