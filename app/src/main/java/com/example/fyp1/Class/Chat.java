package com.example.fyp1.Class;

import java.util.List;

public class Chat {
    public String CreatedDate;
    List<String > ChatDetails;

    public Chat(String createdDate, List<String> chatDetails) {
        CreatedDate = createdDate;
        ChatDetails = chatDetails;
    }

    public Chat(List<String> chatDetails) {
        ChatDetails = chatDetails;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public List<String> getChatDetails() {
        return ChatDetails;
    }

    public void setChatDetails(List<String> chatDetails) {
        ChatDetails = chatDetails;
    }
}
