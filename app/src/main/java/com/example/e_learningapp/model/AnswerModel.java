package com.example.e_learningapp.model;

public class AnswerModel {
    private String studentId;
    private int quizGrade;

    public AnswerModel() {
    }

    public AnswerModel(String studentId, int quizGrade) {
        this.studentId = studentId;
        this.quizGrade = quizGrade;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getQuizGrade() {
        return quizGrade;
    }
}

