package com.example.e_learningapp.model;

public class MembersModel {
    private String studentId;
    private String studentUserName;
    private String studentUserEmail;
    private String courseId;
    private String courseName;
    private int quizGrade;
    private int attendanceGrade;

    public MembersModel() {}

    public MembersModel(String studentId, String studentUserName, String studentUserEmail, String courseId, String courseName, int quizGrade, int attendanceGrade) {
        this.studentId = studentId;
        this.studentUserName = studentUserName;
        this.studentUserEmail = studentUserEmail;
        this.courseId = courseId;
        this.courseName = courseName;
        this.quizGrade = quizGrade;
        this.attendanceGrade = attendanceGrade;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentUserName() {
        return studentUserName;
    }

    public void setStudentUserName(String studentUserName) {
        this.studentUserName = studentUserName;
    }

    public String getStudentUserEmail() {
        return studentUserEmail;
    }

    public void setStudentUserEmail(String studentUserEmail) {
        this.studentUserEmail = studentUserEmail;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {return courseName;}

    public int getQuizGrade() {
        return quizGrade;
    }

    public int getAttendanceGrade() {
        return attendanceGrade;
    }
}
