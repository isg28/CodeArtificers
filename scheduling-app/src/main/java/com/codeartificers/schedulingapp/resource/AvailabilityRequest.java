package com.codeartificers.schedulingapp.resource;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class AvailabilityRequest {
    private String availability_Id;
    private String user_id;
    private LocalDate date;
   private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private boolean allDay;

    public AvailabilityRequest(){

    }
    //Constructor to access availability_Id, days, and time for Availability entry
    public AvailabilityRequest(String availability_Id, String user_id, LocalDate date, LocalDateTime startTime,
                               LocalDateTime endTime, String title, boolean allDay) {
        this.availability_Id = availability_Id;
        this.user_id = user_id;
        this.date = date;
        this.title = title;
        this.allDay = allDay;
        /*
        if (!allDay) {
            this.start = startTime;
            this.end = endTime;
        } else {
            this.start = null;
            this.end = null;
        }
         */
    }

    public void setAvailability_Id(String availability_Id) {
        this.availability_Id = availability_Id;
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
