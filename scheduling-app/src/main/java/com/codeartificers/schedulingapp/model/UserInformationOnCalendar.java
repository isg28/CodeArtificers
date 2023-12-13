package com.codeartificers.schedulingapp.model;
import java.time.LocalDate;

public class UserInformationOnCalendar {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String username;

    public UserInformationOnCalendar(String firstName, String lastName, String email, LocalDate dob, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.username = username;
    }

    public UserInformationOnCalendar() {

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
