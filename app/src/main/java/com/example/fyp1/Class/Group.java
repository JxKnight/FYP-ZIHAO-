package com.example.fyp1.Class;

import java.util.ArrayList;

public class Group {
    public String ChatUID,CreadtedTime,Name,Doc;
    public ArrayList<String> Users;

    public Group(String chatUID, String creadtedTime, String name, ArrayList<String> users,String doc) {
        ChatUID = chatUID;
        CreadtedTime = creadtedTime;
        Name = name;
        Users = users;
        Doc = doc;
    }

    public Group(String chatUID, String creadtedTime, String name,String doc) {
        ChatUID = chatUID;
        CreadtedTime = creadtedTime;
        Name = name;
        Doc = doc;
    }

    public String getChatUID() {
        return ChatUID;
    }

    public void setChatUID(String chatUID) {
        ChatUID = chatUID;
    }

    public String getCreadtedTime() {
        return CreadtedTime;
    }

    public void setCreadtedTime(String creadtedTime) {
        CreadtedTime = creadtedTime;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<String> getUsers() {
        return Users;
    }

    public void setUsers(ArrayList<String> users) {
        Users = users;
    }

    public String getDoc() {
        return Doc;
    }

    public void setDoc(String doc) {
        Doc = doc;
    }
}
