package com.example.fyp1.Class;

import java.util.ArrayList;

public class User {
    private String Name, ID, Contact, Faculty, Email,FirstEntry,role,doc;
    private ArrayList<String> group;

    public User() {
        //empty constructor needed
    }

    public User(String name, String ID, String doc, ArrayList<String> group) {
        Name = name;
        this.ID = ID;
        this.doc = doc;
        this.group = group;
    }

    public User(String name, String email, String firstEntry) {
        Name = name;
        Email = email;
        FirstEntry = firstEntry;
    }

    public User(String ID, String doc) {
        this.ID = ID;
        this.doc = doc;
    }

    public User(String name, String ID, String contact, String doc) {
        Name = name;
        this.ID = ID;
        Contact = contact;
        this.doc = doc;
    }

    public User(String name, String id, String contact, String faculty, String email, String firstEntry, String role,String doc) {
        Name = name;
        ID = id;
        Contact = contact;
        Faculty = faculty;
        Email = email;
        this.FirstEntry = firstEntry;
        this.role =role;
        this.doc = doc;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstEntry() {
        return FirstEntry;
    }

    public void setFirstEntry(String firstEntry) {
        this.FirstEntry = firstEntry;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<String> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<String> group) {
        this.group = group;
    }
}
