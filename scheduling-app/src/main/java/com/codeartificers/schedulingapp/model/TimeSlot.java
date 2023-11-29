package com.codeartificers.schedulingapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class TimeSlot {
    private String user_id;
    private LocalDate date;
    //@JsonFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private ZonedDateTime startTime;
    //@JsonFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private ZonedDateTime endTime;

    public TimeSlot(){

    }
    public TimeSlot(String user_id, LocalDate date, ZonedDateTime startTime, ZonedDateTime endTime){
        this.user_id = user_id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;

    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }
}
