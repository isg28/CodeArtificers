package com.codeartificers.schedulingapp.resource;
import lombok.Getter;

@Getter
public class AvailabilityRequest {
    //Getter and Setter methods for instance variables
    private String availability_Id;
    private String days;
    private String time;

    public AvailabilityRequest(){

    }
    //Constructor to access availability_Id, days, and time for Availability entry
    public AvailabilityRequest(String availability_Id, String days, String time) {
        this.availability_Id = availability_Id;
        this.days = days;
        this.time = time;
    }

    public void setAvailability_Id(String availability_Id) {
        this.availability_Id = availability_Id;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
