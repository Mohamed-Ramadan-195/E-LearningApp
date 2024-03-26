package com.example.e_learningapp.ui.instructor.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.R;
import com.example.e_learningapp.databinding.FragmentQuizInstructorControlBinding;
import com.example.e_learningapp.model.QuizModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class QuizInstructorControlFragment extends Fragment {
    private FragmentQuizInstructorControlBinding binding;
    private ArrayList<QuizModel> list = new ArrayList<>();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    String courseId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuizInstructorControlBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCourseId();
        callBack();
    }

    private void getCourseId() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            courseId = getArguments().getString(Const.KEY_COURSE_ID);
        } else {
            Toast.makeText(getActivity(), "no course id", Toast.LENGTH_SHORT).show();
        }
    }

    private void callBack() {
        binding.nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = binding.question.getText().toString().trim();
                String answerA = binding.answerA.getText().toString().trim();
                String answerB = binding.answerB.getText().toString().trim();
                String answerC = binding.answerC.getText().toString().trim();
                String answerD = binding.answerD.getText().toString().trim();
                validate(question, answerA, answerB, answerC, answerD);
            }
        });
        binding.endQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnable();
            }
        });
        binding.uploadQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToRealDatabase(list, courseId);
            }
        });
    }

    private void setText() {
        binding.question.setText(Const.KEY_EMPTY_STRING);
        binding.answerA.setText(Const.KEY_EMPTY_STRING);
        binding.answerB.setText(Const.KEY_EMPTY_STRING);
        binding.answerC.setText(Const.KEY_EMPTY_STRING);
        binding.answerD.setText(Const.KEY_EMPTY_STRING);
        binding.choices.clearCheck();
    }

    private void setEnable() {
        binding.question.setEnabled(false);
        binding.answerA.setEnabled(false);
        binding.answerB.setEnabled(false);
        binding.answerC.setEnabled(false);
        binding.answerD.setEnabled(false);
        binding.choices.setEnabled(false);
        binding.nextQuestionButton.setEnabled(false);
        binding.uploadQuizButton.setEnabled(true);
    }

    private String getCorrectAnswer() {
        String correctAnswer = "no answer";
        if (binding.A.isChecked()) {
            correctAnswer = binding.A.getText().toString().trim();
        } else if (binding.B.isChecked()) {
            correctAnswer = binding.B.getText().toString().trim();
        } else if (binding.C.isChecked()) {
            correctAnswer = binding.C.getText().toString().trim();
        } else if (binding.D.isChecked()) {
            correctAnswer = binding.D.getText().toString().trim();
        }
        return correctAnswer;
    }

    private void validate(String question, String answerA, String answerB, String answerC, String answerD) {
        if (question.isEmpty()) {
            binding.question.setError(getString(R.string.required));
        } else if (answerA.isEmpty()) {
            binding.answerA.setError(getString(R.string.required));
        } else if (answerB.isEmpty()) {
            binding.answerB.setError(getString(R.string.required));
        } else if (answerC.isEmpty()) {
            binding.answerC.setError(getString(R.string.required));
        } else if (answerD.isEmpty()) {
            binding.answerD.setError(getString(R.string.required));
        } else if (!binding.A.isChecked() && !binding.B.isChecked() && !binding.C.isChecked() && !binding.D.isChecked()) {
            Toast.makeText(getActivity(), "select answer for this question", Toast.LENGTH_SHORT).show();
        } else {
            list.add(new QuizModel(question, answerA, answerB, answerC, answerD, getCorrectAnswer()));
            Toast.makeText(getActivity(), "question is added", Toast.LENGTH_SHORT).show();
            setText();
        }
    }

    private void sendDataToRealDatabase(ArrayList<QuizModel> quiz, String courseId) {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child(Const.KEY_QUIZ)
                .push()
                .setValue(quiz)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "quiz is uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}