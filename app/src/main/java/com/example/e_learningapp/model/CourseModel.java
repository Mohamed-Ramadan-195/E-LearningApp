package com.example.e_learningapp.model;

public class CourseModel {
    private String courseName;
    private String quizGrade;
    private String projectGrade;
    private String attendanceGrade;
    private String courseId;
    private String instructorId;

    public CourseModel() {}

    public CourseModel(String courseName, String quizGrade, String projectGrade, String attendanceGrade, String courseId, String instructorId) {
        this.courseName = courseName;
        this.quizGrade = quizGrade;
        this.projectGrade = projectGrade;
        this.attendanceGrade = attendanceGrade;
        this.courseId = courseId;
        this.instructorId = instructorId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getQuizGrade() {
        return quizGrade;
    }

    public String getProjectGrade() {
        return projectGrade;
    }

    public String getAttendanceGrade() {
        return attendanceGrade;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getInstructorId() {
        return instructorId;
    }
}
