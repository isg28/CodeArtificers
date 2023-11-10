package com.codeartificers.schedulingapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
@Document("Meeting")
public class Meeting {
    @Id
    private String meeting_id;

    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private String participants;
    private String location;

    private String meeting_Description;


    public Meeting(){

    }
    public Meeting(String meeting_id,LocalDate date,LocalTime startTime,LocalTime endTime,String participants, String location, String meeting_Descriptions){
        this.meeting_id=meeting_id;
        this.startTime=startTime;
        this.endTime = endTime;
        this.date = date;
        this.participants=participants;
        this.location = location;
        this.meeting_Description=meeting_Descriptions;
    }

    public String getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(String meeting_id) {
        this.meeting_id = meeting_id;
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

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }
    public String getLocation(){return location;}
    public void setLocation(String location){ this.location = location;}

    public String getMeeting_Description() {
        return meeting_Description;
    }

    public void setMeeting_Description(String meeting_Description) {
        this.meeting_Description = meeting_Description;
    }
}
