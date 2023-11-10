package com.codeartificers.schedulingapp.model;

import java.util.List;

public class UserTimeSlots {
    private String userId;
    private List<TimeSlot> timeslots;

    public UserTimeSlots(String userId, List<TimeSlot> timeslots) {
        this.userId = userId;
        this.timeslots = timeslots;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<TimeSlot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<TimeSlot> timeslots) {
        this.timeslots = timeslots;
    }
}
