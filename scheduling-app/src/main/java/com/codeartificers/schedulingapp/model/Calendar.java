package com.codeartificers.schedulingapp.model;

import org.springframework.data.annotation.Id;

public class Calendar {
    @Id
    String calendar_id;
    String user_id;
    String calendarTitle;

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
}
