package com.example.myandroidpro;

public class UserModel {
    private String _id;
    private String user_name;
    private Integer user_image;
    private String email;
    private String password;

    public UserModel(String user_name, String user_email, String password) {
        this.user_name = user_name;
        this.email = user_email;
        this.password = password;
    }

    public String get_id() { return _id; }

    public void set_id(String _id) { this._id = _id; }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Integer getUser_image() { return user_image; }

    public String getUser_email() {
        return email;
    }

    public void setUser_email(String user_email) {
        this.email = user_email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
