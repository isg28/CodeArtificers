package com.codeartificers.schedulingapp.resource;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Getter
public class AvailabilityRequest {
    private String availability_Id;
    private String user_id;
    @JsonFormat(pattern = "yyyy-MM-dd") // Adjust the pattern
    private LocalDate date;
   //@JsonFormat(pattern = "HH:mm")
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
   //@JsonFormat(pattern = "HH:mm") // Adjust the pattern
   @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
   private ZonedDateTime startTime;
    //@JsonFormat(pattern = "HH:mm")
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    //@JsonFormat(pattern = "HH:mm") // Adjust the pattern
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private ZonedDateTime endTime;
    private String title;
    private boolean allDay;

    public AvailabilityRequest(){

    }
    //Constructor to access availability_Id, days, and time for Availability entry
    public AvailabilityRequest(String availability_Id, String user_id, LocalDate date, ZonedDateTime startTime,
                               ZonedDateTime endTime, String title, boolean allDay) {
        this.availability_Id = availability_Id;
        this.user_id = user_id;
        this.date = date;
        this.title = title;
        this.allDay = allDay;
        if (!allDay) {
            this.startTime = startTime;
            this.endTime = endTime;
        } else {
            this.startTime = null;
            this.endTime = null;
        }
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
