package com.codeartificers.schedulingapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Meeting")
public class Meeting {
    @Id
    private String meeting_id;
    private String time;
    private String day;
    private String participants;
    private String location;

    private String meeting_Description;


    public Meeting(){

    }
    public Meeting(String meeting_id,String time,String day,String participants, String location, String meeting_Descriptions){
        this.meeting_id=meeting_id;
        this.time=time;
        this.day=day;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
