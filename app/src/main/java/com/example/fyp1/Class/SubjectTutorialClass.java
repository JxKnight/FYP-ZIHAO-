package com.example.fyp1.Class;

public class SubjectTutorialClass {
    String tutorial, tutorialClass, time, day;

    public SubjectTutorialClass(String tutorial, String tutorialClass, String time, String day) {
        this.tutorial = tutorial;
        this.tutorialClass = tutorialClass;
        this.time = time;
        this.day = day;
    }

    public String getTutorial() {
        return tutorial;
    }

    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }

    public String getTutorialClass() {
        return tutorialClass;
    }

    public void setTutorialClass(String tutorialClass) {
        this.tutorialClass = tutorialClass;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
