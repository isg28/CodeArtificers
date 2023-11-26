package com.codeartificers.schedulingapp.resource;

public class LoginRequest {
    private String email;
    private String password;
    private String user_id;

    public LoginRequest(String email, String password, String user_id) {
        this.email = email;
        this.password = password;
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getUser_id(){
        return user_id;
    }
    public void setUser_id(String user_id){
        this.user_id = user_id;
    }
}
