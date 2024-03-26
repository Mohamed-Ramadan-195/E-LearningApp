package com.example.e_learningapp.ui.intro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.data.SharedPreference;
import com.example.e_learningapp.databinding.ActivitySplashBinding;
import com.example.e_learningapp.ui.authentication.SignInActivity;
import com.example.e_learningapp.ui.instructor.InstructorHomeActivity;
import com.example.e_learningapp.ui.student.StudentHomeActivity;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreference.init(getApplicationContext(), Const.KEY_AUTHENTICATION);
        SharedPreference.init(getApplicationContext(), Const.KEY_SLIDE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goTo();
            }
        }, 3000);
    }

    private void goTo() {
        if (SharedPreference.getData(Const.KEY_USER_EMAIL, "").equals(Const.KEY_EMPTY_STRING)) {
            if (SharedPreference.onBoardingRead()) {
                startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            } else {
                SharedPreference.firstOpen();
                startActivity(new Intent(SplashActivity.this, SlideActivity.class));
            }
        } else {
            if (SharedPreference.getData(Const.KEY_USER_TYPE, "").equals(Const.KEY_STUDENT)) {
                startActivity(new Intent(SplashActivity.this, StudentHomeActivity.class));
            } else if (SharedPreference.getData(Const.KEY_USER_TYPE, "").equals(Const.KEY_INSTRUCTOR)){
                startActivity(new Intent(SplashActivity.this, InstructorHomeActivity.class));
            } else {
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            }
        }
    }
}