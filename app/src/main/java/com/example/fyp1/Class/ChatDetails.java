package com.example.fyp1.Class;

public class ChatDetails {
    public String chatDetails,chatTime,receiver,sender;

    public ChatDetails(String chatDetails, String chatTime, String receiver, String sender) {
        this.chatDetails = chatDetails;
        this.chatTime = chatTime;
        this.receiver = receiver;
        this.sender = sender;
    }

    public String getChatDetails() {
        return chatDetails;
    }

    public void setChatDetails(String chatDetails) {
        this.chatDetails = chatDetails;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
