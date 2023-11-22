package com.codeartificers.schedulingapp.service;

import com.codeartificers.schedulingapp.model.Invitation;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendInvitationNotification(Invitation invitation){
        //This could involve sending emails or using a message queue on a client's account - but for now I'll print it out on the console
        System.out.println("Sending invitations to participants...");
        System.out.println("Meeting ID: " + invitation.getMeetingId());
        System.out.println("Sender ID: " + invitation.getSenderId());
        System.out.print("InvitationId: " + invitation.getInvitationId());
        System.out.println("Participant Ids: " + invitation.getInvitedUsers());
    }

}
