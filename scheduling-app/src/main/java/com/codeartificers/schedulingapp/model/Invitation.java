package com.codeartificers.schedulingapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Invitation")
public class Invitation {
    @Id
    private String invitation_id;
    private String calendar_id;
    private String user_id;
    private List<InvitedUser> invitedUsers;

    public Invitation(){}

    public Invitation(String invitation_id, String calendar_id, String user_id, List<InvitedUser> invitedUsers) {
        this.invitation_id = invitation_id;
        this.calendar_id = calendar_id;
        this.user_id = user_id;
        this.invitedUsers = invitedUsers;
    }

    public String getInvitation_id() {
        return invitation_id;
    }
    public void setInvitation_id(String invitation_id) {
        this.invitation_id = invitation_id;
    }
    public String getCalendar_id() {
        return calendar_id;
    }
    public void setCalendar_id(String calendar_id) {
        this.calendar_id = calendar_id;
    }
    public String getUser_id(){
        return user_id;
    }
    public void setUser_id(String user_id){
        this.user_id = user_id;
    }
    public List<InvitedUser> getInvitedUsers() {
        return invitedUsers;
    }
    public void setInvitedUsers(List<InvitedUser> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }
}
