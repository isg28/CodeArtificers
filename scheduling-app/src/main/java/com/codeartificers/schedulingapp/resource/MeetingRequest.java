package com.codeartificers.schedulingapp.resource;

//import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class MeetingRequest {
    private String meeting_id;
   // @JsonFormat(pattern = "HH:mm")
    private LocalDateTime start;
   // @JsonFormat(pattern = "HH:mm")
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
