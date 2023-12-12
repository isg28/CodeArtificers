package com.codeartificers.schedulingapp.model;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Document("user")
public class User {
    @Id
    private String user_id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String username;
    private String password;
    @Field("newPassword")
    private String newPassword;
    private String token;
    @ElementCollection
    private List<String> homepageCalendars = new ArrayList<>();



    public User(){

    }

    public User(String user_id, String firstName, String lastName, String email,
                LocalDate dob, String username, String password, String newPassword, String token){
        this.user_id = user_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
        this.token = token;
    }

    //Getter and Setter methods for each instance variable
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
    public String getLastName() { return lastName;}
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

    public String getUsername() {return username;}
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword(){ return password;}
    public void setPassword(String password){ this.password = password;}
    public String getNewPassword(){ return password;}
    public void setNewPassword(String newPassword){ this.password = password; }
    public String getToken(){return token;}
    public void setToken(String token){this.token = token;}
    public List<String> getHomepageCalendars() {
        return homepageCalendars;
    }

    public void setHomepageCalendars(List<String> homepageCalendars) {
        this.homepageCalendars = homepageCalendars;
    }


}
