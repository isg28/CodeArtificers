package com.codeartificers.schedulingapp.resource;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserRequest {
    private String user_id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String username;

    public UserRequest() {
    }
    //Constructor to access id, name, email, dob, and username for User Profile
    public UserRequest(String user_id, String firstName, String lastName, String email, LocalDate dob, String username) {
        this.user_id = user_id;
        this.firstName = firstName;
        this.lastName = lastName;
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
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName(){ return lastName;}
    public void setLastName(String lastName){ this.lastName = lastName;}
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

}
