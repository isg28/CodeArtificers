package com.codeartificers.schedulingapp.resource;

import org.springframework.data.annotation.Id;

public class CalenderRequest {
    @Id
    String calender_id;
    String user_id;
    String calenderTitle;

    public CalenderRequest(String calender_id, String user_id, String calenderTitle) {
        this.calender_id = calender_id;
        this.user_id = user_id;
        this.calenderTitle = calenderTitle;
    }

    public String getCalender_id() {
        return calender_id;
    }

    public void setCalender_id(String calender_id) {
        this.calender_id = calender_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCalenderTitle() {
        return calenderTitle;
    }

    public void setCalenderTitle(String calenderTitle) {
        this.calenderTitle = calenderTitle;
    }
}
