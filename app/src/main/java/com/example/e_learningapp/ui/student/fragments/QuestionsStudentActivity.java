package com.example.e_learningapp.ui.student.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.R;
import com.example.e_learningapp.databinding.ActivityQuestionsStudentBinding;
import com.example.e_learningapp.model.AnswerModel;
import com.example.e_learningapp.model.QuizModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionsStudentActivity extends AppCompatActivity {
    ActivityQuestionsStudentBinding binding;
    private String courseId;
    private String quizId;
    private String correctAnswer = Const.KEY_EMPTY_STRING;
    private int quizGrade = 0, position = 0;
    private ArrayList<QuizModel> list = new ArrayList<>();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionsStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDataId();
        initData();
        callBack();
    }

    private void getDataId() {
        courseId = getIntent().getStringExtra(Const.KEY_COURSE_ID);
        if (courseId == null) {
            Toast.makeText(this, "no course id", Toast.LENGTH_SHORT).show();
        }

        quizId = getIntent().getStringExtra(Const.KEY_QUIZ_ID);
        if (quizId == null) {
            Toast.makeText(this, "no quiz id", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child(Const.KEY_QUIZ)
                .child(quizId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            QuizModel quizModel = dataSnapshot.getValue(QuizModel.class);
                            list.add(quizModel);
                        }
                        initDataUi();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void callBack() {
        binding.nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (correctAnswer.equals(Const.KEY_EMPTY_STRING)) {
                    Toast.makeText(QuestionsStudentActivity.this, "please, choose answer", Toast.LENGTH_SHORT).show();
                } else {
                    initDataUi();
                }
            }
        });
        binding.answerA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctAnswer = "A";
                checkCorrectAnswer(binding.answerA, correctAnswer);
                setEnabled(binding.answerB, binding.answerC, binding.answerD);
            }
        });
        binding.answerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctAnswer = "B";
                checkCorrectAnswer(binding.answerB, correctAnswer);
                setEnabled(binding.answerC, binding.answerD, binding.answerA);
            }
        });
        binding.answerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctAnswer = "C";
                checkCorrectAnswer(binding.answerC, correctAnswer);
                setEnabled(binding.answerD, binding.answerA, binding.answerB);
            }
        });binding.answerD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctAnswer = "D";
                checkCorrectAnswer(binding.answerD, correctAnswer);
                setEnabled(binding.answerA, binding.answerB, binding.answerC);
            }
        });
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToRealDatabase();
                finish();
            }
        });
    }

    private void checkCorrectAnswer(TextView textView, String correctAnswer) {
        if (list.get(position).getCorrectAnswer().equals(correctAnswer)) {
            textView.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            quizGrade++;
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        }
        checkSubmit();
    }

    private void checkSubmit() {
        if (position == list.size() - 1) {
            binding.nextQuestionButton.setEnabled(false);
            binding.submitButton.setEnabled(true);
        } else {
            position++;
        }
    }

    private void setEnabled(TextView tv1, TextView tv2, TextView tv3) {
        tv1.setEnabled(false);
        tv2.setEnabled(false);
        tv3.setEnabled(false);
    }

    private void setAllEnabled() {
        binding.answerA.setEnabled(true);
        binding.answerB.setEnabled(true);
        binding.answerC.setEnabled(true);
        binding.answerD.setEnabled(true);
    }

    private void setBackground() {
        binding.answerA.setBackground(ContextCompat.getDrawable(this, R.drawable.answer));
        binding.answerB.setBackground(ContextCompat.getDrawable(this, R.drawable.answer));
        binding.answerC.setBackground(ContextCompat.getDrawable(this, R.drawable.answer));
        binding.answerD.setBackground(ContextCompat.getDrawable(this, R.drawable.answer));
    }

    private void setAllText() {
        binding.question.setText(list.get(position).getQuestion());
        binding.answerA.setText(list.get(position).getAnswerA());
        binding.answerB.setText(list.get(position).getAnswerB());
        binding.answerC.setText(list.get(position).getAnswerC());
        binding.answerD.setText(list.get(position).getAnswerD());
    }

    private void initDataUi() {
        setAllText();
        setAllEnabled();
        setBackground();
        correctAnswer = Const.KEY_EMPTY_STRING;
    }

    private void sendDataToRealDatabase() {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child(Const.KEY_QUIZ_GRADE_Student)
                .child(quizId)
                .child(FirebaseAuth.getInstance().getUid())
                .setValue(new AnswerModel(FirebaseAuth.getInstance().getUid(), quizGrade))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateQuizGrade();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuestionsStudentActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateQuizGrade() {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child(Const.KEY_MEMBERS)
                .child(FirebaseAuth.getInstance().getUid())
                .child("quizGrade")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int currentQuizGrade = snapshot.getValue(Integer.class);
                        incrementGrade(currentQuizGrade);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void incrementGrade(int currentQuizGrade) {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child(Const.KEY_MEMBERS)
                .child(FirebaseAuth.getInstance().getUid())
                .child("quizGrade")
                .setValue(currentQuizGrade + quizGrade)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuestionsStudentActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}