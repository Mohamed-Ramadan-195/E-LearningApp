package com.example.e_learningapp.ui.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.R;
import com.example.e_learningapp.data.SharedPreference;
import com.example.e_learningapp.databinding.ActivityStudentControlBinding;
import com.example.e_learningapp.ui.student.fragments.StudentShowControlActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentControlActivity extends AppCompatActivity {
    ActivityStudentControlBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private String courseId;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentControlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSharedPreferenceData();
        courseId = getIntent().getStringExtra(Const.KEY_COURSE_ID);
        binding.studentName.setText(username);
        setTexts();
        callBack();
    }

    private void getSharedPreferenceData() {
        SharedPreference.init(getApplicationContext(), Const.KEY_AUTHENTICATION);
        username = SharedPreference.getData(Const.KEY_USER_NAME, "not found");
    }

    private void setTexts() {
        String courseName = getIntent().getStringExtra(Const.KEY_COURSE_NAME);
        binding.courseName.setText(courseName);
    }

    private void callBack() {
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.quizCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntent(Const.KEY_QUIZ);
            }
        });
        binding.materialCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntent(Const.KEY_MATERIAL);
            }
        });
        binding.gradesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntent(Const.KEY_GRADES);
            }
        });
        binding.attendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String attendanceCode = binding.attendanceIdEt.getText().toString().trim();
                validate(attendanceCode);
            }
        });
    }

    private void validate(String attendanceCode) {
        if (attendanceCode.isEmpty()) {
            binding.attendanceIdEt.setError(getString(R.string.required));
        } else {
            correctCode(attendanceCode, courseId);
        }
    }

    private void correctCode(String attendanceCode, String courseId) {
        reference.child(Const.KEY_ATTENDANCE)
                .child(courseId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(attendanceCode)) {
                            if (snapshot.child(attendanceCode).hasChild(FirebaseAuth.getInstance().getUid())) {
                                Toast.makeText(StudentControlActivity.this, "you are already attend", Toast.LENGTH_SHORT).show();
                            } else {
                                sendDataToRealDatabase(attendanceCode, courseId);
                            }
                        } else {
                            Toast.makeText(StudentControlActivity.this, "invalid", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void sendDataToRealDatabase(String attendanceCode, String courseId) {
        reference.child(Const.KEY_ATTENDANCE)
                .child(courseId)
                .child(attendanceCode)
                .child(FirebaseAuth.getInstance().getUid())
                .setValue("attend")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(StudentControlActivity.this, "attend", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentControlActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setIntent(String fragmentKey) {
        Intent intent = new Intent(StudentControlActivity.this, StudentShowControlActivity.class);
        intent.putExtra(Const.KEY_FRAGMENT, fragmentKey);
        intent.putExtra(Const.KEY_COURSE_ID, courseId);
        startActivity(intent);
    }
}