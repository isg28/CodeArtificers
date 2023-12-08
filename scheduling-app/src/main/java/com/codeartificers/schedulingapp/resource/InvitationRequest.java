package com.codeartificers.schedulingapp.resource;
import com.codeartificers.schedulingapp.model.InvitedUser;
import org.springframework.data.annotation.Id;

import java.util.List;

public class InvitationRequest {
    @Id
    private String invitation_id;
    private String user_id;
    private String calendar_id;
    private List<InvitedUser> invitedUsers;

    public InvitationRequest(String invitation_id, String user_id, String calendar_id, List<InvitedUser> invitedUsers) {
        this.invitation_id = invitation_id;
        this.user_id = user_id;
        this.calendar_id = calendar_id;
        this.invitedUsers = invitedUsers;
    }

    public String getInvitation_id() {
        return invitation_id;
    }
    public void setInvitation_id(String invitation_id) {
        this.invitation_id = invitation_id;
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getCalendar_id(){ return calendar_id;}
    public void setCalendar_id(String calendar_id){ this.calendar_id = calendar_id;}
    public List<InvitedUser> getInvitedUsers() {
        return invitedUsers;
    }
    public void setInvitedUsers(List<InvitedUser> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }
}

