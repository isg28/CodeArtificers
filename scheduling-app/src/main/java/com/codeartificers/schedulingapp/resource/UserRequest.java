package com.codeartificers.schedulingapp.resource;

import lombok.Getter;

@Getter
public class UserRequest {
    private String user_id;
    private String name;
    private String email;
    private String dob;
    private String username;

    public UserRequest() {
    }
    //Constructor to access id, name, email, dob, and username for User Profile
    public UserRequest(String user_id, String name, String email, String dob, String username) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.username = username;
    }
    //Getter and Setter methods for instance variables
    public String getUser_Id() {
        return user_id;
    }
    public void setUser_Id(String user_id) {
        this.user_id = user_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

}
