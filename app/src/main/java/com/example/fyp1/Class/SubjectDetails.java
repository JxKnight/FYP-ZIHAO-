package com.example.fyp1.Class;

import java.util.ArrayList;

public class SubjectDetails {
    public ArrayList<String> ExtrafFiles,LectureNote,TutorialNote;

    public SubjectDetails(ArrayList<String> extrafFiles, ArrayList<String> lectureNote, ArrayList<String> tutorialNote) {
        ExtrafFiles = extrafFiles;
        LectureNote = lectureNote;
        TutorialNote = tutorialNote;
    }

    public ArrayList<String> getExtrafFiles() {
        return ExtrafFiles;
    }

    public void setExtrafFiles(ArrayList<String> extrafFiles) {
        ExtrafFiles = extrafFiles;
    }

    public ArrayList<String> getLectureNote() {
        return LectureNote;
    }

    public void setLectureNote(ArrayList<String> lectureNote) {
        LectureNote = lectureNote;
    }

    public ArrayList<String> getTutorialNote() {
        return TutorialNote;
    }

    public void setTutorialNote(ArrayList<String> tutorialNote) {
        TutorialNote = tutorialNote;
    }
}
