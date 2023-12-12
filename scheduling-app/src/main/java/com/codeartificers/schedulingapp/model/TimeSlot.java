package com.codeartificers.schedulingapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimeSlot {
    private String user_id;
    private String username;
    private LocalDate date;
    private LocalDateTime start;
    private LocalDateTime end;

    public TimeSlot(){

    }
    public TimeSlot(String user_id, String username, LocalDate date, LocalDateTime start, LocalDateTime end){
        this.user_id = user_id;
        this.username = username;
        this.date = date;
        this.start = start;
        this.end = end;

    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getUsername() {return username;}
    public void setUsername(String username){ this.username = username;}

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
