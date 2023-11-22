package com.codeartificers.schedulingapp.resource;
import com.codeartificers.schedulingapp.model.InvitedUser;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class InvitationRequest {
    private String meetingId;
    private String senderId;
    private List<String> invitedUsers;

    public InvitationRequest(String meetingId, String senderId, List<String> invitedUsers) {
        this.meetingId = meetingId;
        this.senderId = senderId;
        this.invitedUsers = invitedUsers;
    }

    public String getMeetingId() {
        return meetingId;
    }
    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }
    public String getSenderId() {
        return senderId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public List<String> getInvitedUsers() {
        return invitedUsers;
    }
    public void setInvitedUsers(List<String> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }
}

