package com.codeartificers.schedulingapp.resource;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class AvailabilityRequest {
    private String availability_id;
    private String user_id;
    private LocalDate date;
   private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private boolean allDay;

    public AvailabilityRequest(){

    }
    //Constructor to access availability_Id, days, and time for Availability entry
    public AvailabilityRequest(String availability_id, String user_id, LocalDate date, LocalDateTime start,
                               LocalDateTime end, String title, boolean allDay) {
        this.availability_id = availability_id;
        this.user_id = user_id;
        this.date = date;
        this.title = title;
        this.start = start;
        this.end = end;
        this.allDay = allDay;
    }

    public void setAvailability_id(String availability_id) {
        this.availability_id = availability_id;
    }

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

    public String getUser_id(){return user_id;}
    public void setUserId(String user_id){this.user_id = user_id;}
    public String getTitle(){return title;}
    public void setTitle(String title){this.title = title;}
    public boolean isAllDay(){
        return allDay;
    }
    public void setAllDay(boolean allDay){
        this.allDay = allDay;
    }

}
