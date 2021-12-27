package com.scet.saloonspot.models;


import java.util.ArrayList;

public class Request {

    String Amount, Appointment_Time, status, Pay_Status, Pay_Method, Saloon_Id, recommendtime;
    ArrayList<Services> service = new ArrayList<>();
    boolean isAccepted;
    String userID, key;
    public Saloon saloon;
    public User user;


    public Request() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getAppointment_Time() {
        return Appointment_Time;
    }

    public void setAppointment_Time(String appointment_Time) {
        Appointment_Time = appointment_Time;
    }

    public String getPay_Status() {
        return Pay_Status;
    }

    public void setPay_Status(String pay_Status) {
        Pay_Status = pay_Status;
    }

    public String getRecommendtime() {
        return recommendtime;
    }

    public void setRecommendtime(String recommendtime) {
        this.recommendtime = recommendtime;
    }

    public String getPay_Method() {
        return Pay_Method;
    }

    public void setPay_Method(String pay_Method) {
        Pay_Method = pay_Method;
    }

    public String getSaloon_Id() {
        return Saloon_Id;
    }

    public void setSaloon_Id(String saloon_Id) {
        Saloon_Id = saloon_Id;
    }

    public ArrayList<Services> getService() {
        return service;
    }

    public void setService(ArrayList<Services> service) {
        this.service = service;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Saloon getSaloon() {
        return saloon;
    }

    public void setSaloon(Saloon saloon) {
        this.saloon = saloon;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
