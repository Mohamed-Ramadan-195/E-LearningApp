package com.example.e_learningapp.model;

public class UserModel {
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userType;
    private String userId;
    private String userImage;
    private String userPhone;
    private String userGrade;
    private String userGender;

    public UserModel() {
    }

    public UserModel(String userName, String userEmail, String userPassword, String userType, String userId) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userType = userType;
        this.userId = userId;
    }

    public UserModel(String userImage, String userPhone, String userGrade, String userGender) {
        this.userImage = userImage;
        this.userPhone = userPhone;
        this.userGrade = userGrade;
        this.userGender = userGender;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserGrade() {
        return userGrade;
    }

    public String getUserGender() {
        return userGender;
    }
}
