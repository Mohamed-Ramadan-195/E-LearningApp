package com.example.e_learningapp.ui.student.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.R;

public class StudentShowControlActivity extends AppCompatActivity {
    QuizStudentControlFragment quizStudentControlFragment = new QuizStudentControlFragment();
    MaterialStudentControlFragment materialStudentControlFragment = new MaterialStudentControlFragment();
    GradesStudentControlFragment gradesStudentControlFragment = new GradesStudentControlFragment();
    private String courseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_show_control);

        courseId = getIntent().getStringExtra(Const.KEY_COURSE_ID);
        setFragment();
    }

    private void setFragment() {
        String fragmentKey = getIntent().getStringExtra(Const.KEY_FRAGMENT);
        switch (fragmentKey) {
            case Const.KEY_QUIZ:
                handleFragment(quizStudentControlFragment);
                break;
            case Const.KEY_MATERIAL:
                handleFragment(materialStudentControlFragment);
                break;
            case Const.KEY_GRADES:
                handleFragment(gradesStudentControlFragment);
                break;
        }
    }

    private void handleFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.KEY_COURSE_ID, courseId);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}