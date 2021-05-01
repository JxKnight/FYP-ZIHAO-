package com.example.fyp1.Class;

public class SubjectLectureClass {
    String lecture, lectureClass, time, day;

    public SubjectLectureClass(String lecture, String lectureClass, String time, String day) {
        this.lecture = lecture;
        this.lectureClass = lectureClass;
        this.time = time;
        this.day = day;
    }

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public String getLectureClass() {
        return lectureClass;
    }

    public void setLectureClass(String lectureClass) {
        this.lectureClass = lectureClass;
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
