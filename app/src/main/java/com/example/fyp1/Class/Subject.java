package com.example.fyp1.Class;

public class Subject {
    String subjectCode, subjectName, subjectCreditHours,subjectClass,subjectTime;


    public Subject(String subjectCode, String subjectName, String subjectCreditHours, String subjectClass, String subjectTime) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectCreditHours = subjectCreditHours;
        this.subjectClass = subjectClass;
        this.subjectTime = subjectTime;
    }

    public Subject(String subjectCode, String subjectName, String subjectCreditHours) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectCreditHours = subjectCreditHours;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCreditHours() {
        return subjectCreditHours;
    }

    public void setSubjectCreditHours(String subjectCreditHours) {
        this.subjectCreditHours = subjectCreditHours;
    }

    public String getSubjectClass() {
        return subjectClass;
    }

    public void setSubjectClass(String subjectClass) {
        this.subjectClass = subjectClass;
    }

    public String getSubjectTime() {
        return subjectTime;
    }

    public void setSubjectTime(String subjectTime) {
        this.subjectTime = subjectTime;
    }
}
