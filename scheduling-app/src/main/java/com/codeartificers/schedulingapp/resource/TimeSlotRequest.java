package com.codeartificers.schedulingapp.resource;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class TimeSlotRequest {
    private String user_id;
    private LocalDate date;
    //@JsonFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalDateTime startTime;
    //@JsonFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalDateTime endTime;

    public TimeSlotRequest(){

    }
    public TimeSlotRequest(String user_id, LocalDate date, LocalDateTime startTime, LocalDateTime endTime){
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
