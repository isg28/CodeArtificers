package com.codeartificers.schedulingapp.resource;

import lombok.Getter;

@Getter
public class ScheduleRequest {
    private String user;
    private String availability;
    private String meetings;
    private String timeslots;

    public ScheduleRequest() {
    }

    public ScheduleRequest(String user, String availability, String meetings, String timeslots) {
        this.user = user;
        this.availability = availability;
        this.meetings = meetings;
        this.timeslots = timeslots;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setMeetings(String meetings) {
        this.meetings = meetings;
    }

    public void setTimeslots(String timeslots) {
        this.timeslots = timeslots;
    }
}
