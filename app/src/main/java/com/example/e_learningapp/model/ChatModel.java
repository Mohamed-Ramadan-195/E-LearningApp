package com.example.e_learningapp.model;

public class ChatModel {
    private String message;
    private String username;
    private String userId;

    public ChatModel() {}

    public ChatModel(String message, String username, String userId) {
        this.message = message;
        this.username = username;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }
}
