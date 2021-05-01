package com.example.fyp1.Class;

public class StudyMaterial {
    public String CreatedDate,Name,Doc;

    public StudyMaterial(String createdDate, String name,String doc) {
        CreatedDate = createdDate;
        Name = name;
        Doc = doc;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDoc() {
        return Doc;
    }

    public void setDoc(String doc) {
        Doc = doc;
    }
}
