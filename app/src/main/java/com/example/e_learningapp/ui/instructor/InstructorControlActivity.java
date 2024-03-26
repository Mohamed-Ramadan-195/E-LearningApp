package com.example.e_learningapp.ui.instructor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.databinding.ActivityInstructorControlBinding;
import com.example.e_learningapp.ui.instructor.fragments.InstructorShowControlActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class InstructorControlActivity extends AppCompatActivity {
    ActivityInstructorControlBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    String courseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstructorControlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        courseId = getIntent().getStringExtra(Const.KEY_COURSE_ID);
        binding.courseIdButton.setText(courseId);
        callBack();
    }

    private void callBack() {
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.gradesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntent(Const.KEY_GRADES);
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
        binding.courseIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyText();
                Toast.makeText(InstructorControlActivity.this, "copied text", Toast.LENGTH_SHORT).show();
            }
        });
        binding.generateCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.confirm.setEnabled(true);
                binding.attendanceCode.setText(generateRandomNumber() + "");
            }
        });
        binding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String attendanceCode = binding.attendanceCode.getText().toString().trim();
                sendDataToRealDatabase(attendanceCode);
                Toast.makeText(InstructorControlActivity.this, "success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int generateRandomNumber() {
        Random random = new Random();
        int upperBound = 9999 - 1000;
        return random.nextInt(upperBound) + 1000;
    }

    private void sendDataToRealDatabase(String attendanceCode) {
        reference.child(Const.KEY_ATTENDANCE)
                .child(courseId)
                .child(attendanceCode + "")
                .setValue("")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InstructorControlActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void copyText() {
        String buttonText = binding.courseIdButton.getText().toString().trim();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(Const.KEY_COPIED_TEXT, buttonText);
        clipboard.setPrimaryClip(clipData);
    }

    private void setIntent(String fragmentKey) {
        Intent intent = new Intent(InstructorControlActivity.this, InstructorShowControlActivity.class);
        String courseId = binding.courseIdButton.getText().toString().trim();
        intent.putExtra(Const.KEY_COURSE_ID, courseId);
        intent.putExtra(Const.KEY_FRAGMENT, fragmentKey);
        startActivity(intent);
    }
}