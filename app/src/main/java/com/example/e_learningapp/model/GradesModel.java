package com.example.e_learningapp.model;

public class GradesModel {
   private int quizGrade;
   private int attendanceGrade;

    public GradesModel() {}

    public GradesModel(int quizGrade, int attendanceGrade) {
        this.quizGrade = quizGrade;
        this.attendanceGrade = attendanceGrade;
    }

    public int getQuizGrade() {
        return quizGrade;
    }

    public int getAttendanceGrade() {
        return attendanceGrade;
    }
}
