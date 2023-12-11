package com.codeartificers.schedulingapp.model;

//import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Document("Meeting")
public class Meeting {
    @Id
    private String meeting_id;
    private String user_id;
    private String calendar_id;
    private LocalDate date;
    private LocalDateTime start;
    private LocalDateTime end;

    private String location;
    private String meeting_Description;
    private Boolean isMeeting;



    public Meeting(){
        this.isMeeting = true;
    }
    public Meeting(String meeting_id, String user_id, String calendar_id, LocalDate date,LocalDateTime start,LocalDateTime end,
                   String location, String meeting_Descriptions){
        this.meeting_id=meeting_id;
        this.user_id = user_id;
        this.calendar_id = calendar_id;
        this.start =start;
        this.end = end;
        this.date = date;
        this.location = location;
        this.meeting_Description=meeting_Descriptions;
    }

    public String getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(String meeting_id) {
        this.meeting_id = meeting_id;
    }
    public String getUser_id() { return user_id;}
    public void setUser_id(String user_id){ this.user_id = user_id;}
    public String getCalendar_id() { return calendar_id;}
    public void setCalendar_id(String calendar_id){ this.calendar_id = calendar_id;}

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

    public String getLocation(){return location;}
    public void setLocation(String location){ this.location = location;}

    public String getMeeting_Description() {
        return meeting_Description;
    }

    public void setMeeting_Description(String meeting_Description) {
        this.meeting_Description = meeting_Description;
    }
    public boolean isMeeting(){return isMeeting;}
    public void setIsMeeting(boolean meeting){isMeeting = meeting;}
}
