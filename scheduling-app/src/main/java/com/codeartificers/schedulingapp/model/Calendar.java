package com.codeartificers.schedulingapp.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Calendar {
    @Id
    String calendar_id;
    String user_id;
    String calendarTitle;
    @ElementCollection
    private List<String> sharedWith;

    @OneToMany
    private List<User> users;

    public Calendar(){

    }

    public Calendar(String calendar_id, String user_id, String calendarTitle) {
        this.calendar_id = calendar_id;
        this.user_id = user_id;
        this.calendarTitle = calendarTitle;
    }

    public String getCalendar_id() {
        return calendar_id;
    }

    public void setCalendar_id(String calendar_id) {
        this.calendar_id = calendar_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCalendarTitle() {
        return calendarTitle;
    }

    public void setCalendarTitle(String calendarTitle) {
        this.calendarTitle = calendarTitle;
    }

    public List<String> getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(List<String> sharedWith) {
        this.sharedWith = sharedWith;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
