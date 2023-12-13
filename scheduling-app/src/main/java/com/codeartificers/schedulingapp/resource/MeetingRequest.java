package com.codeartificers.schedulingapp.resource;


import java.time.LocalDate;
import java.time.LocalDateTime;

public class MeetingRequest {
    private String meeting_id;
    private String user_id;
    private String calendar_id;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDate date;
    private String location;
    private String meeting_Description;

    public MeetingRequest() {

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

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getLocation(){ return location;}
    public void setLocation(String location){ this.location = location;}

    public String getMeeting_Description() {
        return meeting_Description;
    }

    public void setMeeting_Description(String meeting_Description) {
        this.meeting_Description = meeting_Description;
    }
}
