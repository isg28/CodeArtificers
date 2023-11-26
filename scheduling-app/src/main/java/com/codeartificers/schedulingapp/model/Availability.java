package com.codeartificers.schedulingapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Document("availability")
public class Availability {
    @Id
    private String availability_Id;
    private String user_id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "HH:mm")

    private LocalTime endTime;
    private String title;
    private boolean allDay;

    public Availability(){

    }
    public Availability(String availability_Id, String user_id, LocalDate date, LocalTime startTime, LocalTime endTime,
                        String title, boolean allDay) {
        this.availability_Id = availability_Id;
        this.user_id = user_id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
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
