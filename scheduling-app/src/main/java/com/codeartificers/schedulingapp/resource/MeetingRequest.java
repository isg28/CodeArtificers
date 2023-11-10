package com.codeartificers.schedulingapp.resource;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class MeetingRequest {
    private String meeting_id;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private LocalDate date;
    private String participants;
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
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
