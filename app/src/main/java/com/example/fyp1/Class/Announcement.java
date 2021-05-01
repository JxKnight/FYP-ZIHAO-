package com.example.fyp1.Class;

public class Announcement {
    private String Title, CreatedTime, Faculty, Details,Creator,SubjectName;

    public Announcement(String title, String createdTime, String faculty, String details, String creator, String subjectName) {
        Title = title;
        CreatedTime = createdTime;
        Faculty = faculty;
        Details = details;
        Creator = creator;
        SubjectName = subjectName;
    }

    public Announcement(String title, String details) {
        Title = title;
        Details = details;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }
}
