package com.example.e_learningapp.ui.instructor.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.R;

public class InstructorShowControlActivity extends AppCompatActivity {
    QuizInstructorControlFragment quizInstructorControlFragment = new QuizInstructorControlFragment();
    MaterialInstructorControlFragment materialInstructorControlFragment = new MaterialInstructorControlFragment();
    GradesInstructorControlFragment gradesInstructorControlFragment = new GradesInstructorControlFragment();
    String courseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_show_control);

        courseId = getIntent().getStringExtra(Const.KEY_COURSE_ID);
        setFragment();
    }

    private void setFragment() {
        String fragmentKey = getIntent().getStringExtra(Const.KEY_FRAGMENT);
        switch (fragmentKey) {
            case Const.KEY_GRADES:
                handleFragment(gradesInstructorControlFragment);
                break;
            case Const.KEY_QUIZ:
                handleFragment(quizInstructorControlFragment);
                break;
            case Const.KEY_MATERIAL:
                handleFragment(materialInstructorControlFragment);
                break;
        }
    }

    public void handleFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.KEY_COURSE_ID, courseId);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}