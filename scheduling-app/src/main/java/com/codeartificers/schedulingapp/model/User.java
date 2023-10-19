package com.codeartificers.schedulingapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("user")
public class User{
    @Id
    private String user_id;
    private String name;
    private String email;
    private String dob;
    private String username;
    private String availability_Id;
    private String days;
    private String time;

    public User(){

    }

    public User(String user_id, String name, String email, String dob, String username){
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.username = username;
    }

    //Getter and Setter methods for each instance variable
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
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
