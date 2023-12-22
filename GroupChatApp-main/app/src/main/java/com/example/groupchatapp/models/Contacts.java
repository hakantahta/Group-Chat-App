package com.example.groupchatapp.models;

public class Contacts {

    private String UserName;
    private String UserPhoneNumber;
    private String UserPhoto;

    public Contacts() {
    }

    public Contacts(String UserName, String UserPhoneNumber, String UserPhoto) {
        this.UserName = UserName;
        this.UserPhoneNumber = UserPhoneNumber;
        this.UserPhoto = UserPhoto;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserPhoneNumber() {
        return UserPhoneNumber;
    }

    public void setUserPhoneNumber(String UserPhoneNumber) {
        this.UserPhoneNumber = UserPhoneNumber;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String UserPhoto) {
        this.UserPhoto = UserPhoto;
    }
}
