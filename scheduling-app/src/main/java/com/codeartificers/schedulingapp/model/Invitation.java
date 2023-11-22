package com.codeartificers.schedulingapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Invitation")
public class Invitation {
    @Id
    private String invitationId;
    private String meetingId;
    private String senderId;
    private List<InvitedUser> invitedUsers;

    public Invitation(){}

    public Invitation(String invitationId, String meetingId, String senderId, List<InvitedUser> invitedUsers) {
        this.invitationId = invitationId;
        this.meetingId = meetingId;
        this.senderId = senderId;
        this.invitedUsers = invitedUsers;
    }

    public String getInvitationId() {
        return invitationId;
    }
    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }
    public String getMeetingId() {
        return meetingId;
    }
    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }
    public String getSenderId(){
        return senderId;
    }
    public void setSenderId(String senderId){
        this.senderId = senderId;
    }
    public List<InvitedUser> getInvitedUsers() {
        return invitedUsers;
    }
    public void setInvitedUsers(List<InvitedUser> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }
}
