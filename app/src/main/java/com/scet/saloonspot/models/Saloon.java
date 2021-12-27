package com.scet.saloonspot.models;

import javax.sql.StatementEvent;

public class Saloon {

    String name;
    String address;
    String mobileNo;
    String workingHr;
    String area;
    String email;
    String password;
    String image;
    String ext;
    String id;
    String avgRating;

    public Saloon(){}

    public Saloon(String name, String address, String mobileNo, String workingHr, String area, String email, String password, String image,String ext){
        this.name = name;
        this.address = address;
        this.area = area;
        this.mobileNo = mobileNo;
        this.workingHr = workingHr;
        this.email = email;
        this.password = password;
        this.image = image;
        this.ext = ext;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getArea() {
        return area;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public String getExt() {
        return ext;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public String getWorkingHr() {
        return workingHr;
    }

    public void setWorkingHr(String workingHr) {
        this.workingHr = workingHr;
    }
}

