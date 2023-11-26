package com.codeartificers.schedulingapp.resource;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;


import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class AvailabilityRequest {
    private String availability_Id;
    private String user_id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
   //@JsonFormat(pattern = "HH:mm")
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat (pattern = "HH:mm")
   private LocalTime startTime;
    //@JsonFormat(pattern = "HH:mm")
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private String title;
    private boolean allDay;

    public AvailabilityRequest(){

    }
    //Constructor to access availability_Id, days, and time for Availability entry
    public AvailabilityRequest(String availability_Id, String user_id, LocalDate date, LocalTime startTime,
                               LocalTime endTime, String title, boolean allDay) {
        this.availability_Id = availability_Id;
        this.user_id = user_id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.allDay = allDay;
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
