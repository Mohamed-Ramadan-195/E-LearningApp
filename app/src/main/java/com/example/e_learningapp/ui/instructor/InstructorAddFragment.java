package com.example.e_learningapp.ui.instructor;

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
import com.example.e_learningapp.databinding.FragmentInstructorAddBinding;
import com.example.e_learningapp.model.CourseModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InstructorAddFragment extends Fragment {
    private FragmentInstructorAddBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public InstructorAddFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInstructorAddBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callBack();
    }

    private void callBack() {
        binding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = binding.courseName.getText().toString().trim();
                String quizGrade = binding.quizGrade.getText().toString().trim();
                String projectGrade = binding.projectGrade.getText().toString().trim();
                String attendanceGrade = binding.attendanceGrade.getText().toString().trim();
                validate(courseName, quizGrade, projectGrade, attendanceGrade);
            }
        });
    }

    private void validate(String courseName, String quizGrade , String projectGrade, String attendanceGrade) {
        if (courseName.isEmpty()) {
            binding.courseName.setError(getString(R.string.required));
        } else if (quizGrade.isEmpty()) {
            binding.quizGrade.setError(getString(R.string.required));
        } else if (projectGrade.isEmpty()) {
            binding.projectGrade.setError(getString(R.string.required));
        } else if (attendanceGrade.isEmpty()) {
            binding.attendanceGrade.setError(getString(R.string.required));
        } else {
            sendDataToRealDatabase(courseName, quizGrade, projectGrade, attendanceGrade, reference.push().getKey(), firebaseAuth.getUid());
        }
    }

    private void sendDataToRealDatabase(String courseName, String quizGrade, String projectGrade, String attendanceGrade, String courseId, String instructorId) {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .setValue(new CourseModel(courseName, quizGrade, projectGrade, attendanceGrade, courseId, instructorId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Course Added", Toast.LENGTH_SHORT).show();
                        setTextEmpty();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setTextEmpty() {
        binding.courseName.setText(Const.KEY_EMPTY_STRING);
        binding.quizGrade.setText(Const.KEY_EMPTY_STRING);
        binding.projectGrade.setText(Const.KEY_EMPTY_STRING);
        binding.attendanceGrade.setText(Const.KEY_EMPTY_STRING);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}