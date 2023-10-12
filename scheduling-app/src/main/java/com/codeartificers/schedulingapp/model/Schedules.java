package com.codeartificers.schedulingapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("schedules")
public class Schedules{
    @Id
    private String user;
    private String availability;
    private String meetings;
    private String timeslots;

    public Schedules(){

    }

    public Schedules(String user, String availability, String meetings, String timeslots){
        this.user = user;
        this.availability = availability;
        this.meetings = meetings;
        this.timeslots = timeslots;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getMeetings() {
        return meetings;
    }

    public void setMeetings(String meetings) {
        this.meetings = meetings;
    }

    public String getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(String timeslots) {
        this.timeslots = timeslots;
    }
}
