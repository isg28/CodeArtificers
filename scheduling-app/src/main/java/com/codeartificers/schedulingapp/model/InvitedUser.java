package com.codeartificers.schedulingapp.model;

public class InvitedUser {
    private String user_id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;

    public InvitedUser(){}
    public InvitedUser(String email){
        this.email = email;
    }
    public InvitedUser(String user_id, String firstName, String lastName, String username, String email) {
        this.user_id = user_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() { return email;}
    public void setEmail(String email){this.email = email;}
}
