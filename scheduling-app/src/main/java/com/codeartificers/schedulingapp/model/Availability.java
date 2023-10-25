package com.codeartificers.schedulingapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("availability")
public class Availability {
    @Id
    private String availability_Id;
    private String user_id;
    private String days;
    private String time;

    public Availability(){

    }
    public Availability(String availability_Id, String user_id, String days, String time) {
        this.availability_Id = availability_Id;
        this.user_id = user_id;
        this.days = days;
        this.time = time;
    }

    public String getAvailability_Id() {
        return availability_Id;
    }

    public void setAvailability_Id(String availability_Id) {
        this.availability_Id = availability_Id;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public void setUser_id(String user_id){this.user_id = user_id;}
    public String getUser_id(){return user_id;}
}
