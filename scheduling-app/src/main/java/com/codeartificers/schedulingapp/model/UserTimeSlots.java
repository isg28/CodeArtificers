package com.codeartificers.schedulingapp.model;

import java.util.List;

public class UserTimeSlots {
    private String userId;
    private String username;
    private List<TimeSlot> timeslots;

    public UserTimeSlots(String userId, List<TimeSlot> timeslots) {
        this.userId = userId;
        this.timeslots = timeslots;
    }

    public UserTimeSlots(String userId, String username, List<TimeSlot> timeslots) {
        this.userId = userId;
        this.username = username;
        this.timeslots = timeslots;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUsername(){ return username;}
    public void setUsername(String username){ this.username = username;}

    public List<TimeSlot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<TimeSlot> timeslots) {
        this.timeslots = timeslots;
    }
}
