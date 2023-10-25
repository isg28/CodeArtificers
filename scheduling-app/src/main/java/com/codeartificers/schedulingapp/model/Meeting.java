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
    private String meeting_Description;

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

    public String getMeeting_Description() {
        return meeting_Description;
    }

    public void setMeeting_Description(String meeting_Description) {
        this.meeting_Description = meeting_Description;
    }
}
