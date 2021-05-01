package com.example.fyp1.Class;

public class Attendance {
    public String DateTime,SubjectCode,doc;

    public Attendance(String dateTime, String subjectCode, String doc) {
        DateTime = dateTime;
        SubjectCode = subjectCode;
        this.doc = doc;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getSubjectCode() {
        return SubjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        SubjectCode = subjectCode;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }
}
