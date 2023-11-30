package com.codeartificers.schedulingapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Document("availability")
public class Availability {
    @Id
    private String availability_Id;
    private String user_id;
    private LocalDate date;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private boolean allDay;

    public Availability(){

    }
    public Availability(String availability_Id, String user_id, LocalDate date, LocalDateTime startTime, LocalDateTime endTime,
                        String title, boolean allDay) {
        this.availability_Id = availability_Id;
        this.user_id = user_id;
        this.date = date;
        this.title = title;
        this.allDay = allDay;
    }

    public String getAvailability_Id() {
        return availability_Id;
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

    public void setUser_id(String user_id){this.user_id = user_id;}
    public String getUser_id(){return user_id;}
    public void setTitle(String title){this.title = title;}
    public String getTitle(){return title;}
    public boolean isAllDay(){
        return allDay;
    }
    public void setAllDay(boolean allDay){
        this.allDay = allDay;
    }
}
