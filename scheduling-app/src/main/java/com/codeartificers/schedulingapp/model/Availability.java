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
    //@JsonFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private ZonedDateTime startTime;
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
   // @JsonFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private ZonedDateTime endTime;
    private String title;
    private boolean allDay;

    public Availability(){

    }
    public Availability(String availability_Id, String user_id, LocalDate date, ZonedDateTime startTime, ZonedDateTime endTime,
                        String title, boolean allDay) {
        this.availability_Id = availability_Id;
        this.user_id = user_id;
        this.date = date;
        this.title = title;
        this.allDay = allDay;

        if(!allDay) {
            this.startTime = startTime;
            this.endTime = endTime;
        }else{
            this.startTime = null;
            this.endTime = null;
        }
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
