package com.example.e_learningapp.ui.student.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.databinding.FragmentGradesStudentControlBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GradesStudentControlFragment extends Fragment {
    private FragmentGradesStudentControlBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private String courseId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGradesStudentControlBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCourseId();
        getStudentGrade();
        getQuizGrade();
        getAttendanceGrade();
    }

    private void getCourseId() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            courseId = getArguments().getString(Const.KEY_COURSE_ID);
        } else {
            Toast.makeText(getActivity(), "no course id", Toast.LENGTH_SHORT).show();
        }
    }

    private void getStudentGrade() {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child(Const.KEY_MEMBERS)
                .child(FirebaseAuth.getInstance().getUid())
                .child("quizGrade")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String studentGrade = snapshot.getValue().toString();
                        binding.studentGrade.setText(studentGrade);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getQuizGrade() {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child("quizGrade")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String quizGrade = snapshot.getValue().toString();
                        binding.quizGrade.setText(quizGrade);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAttendanceGrade() {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child("attendanceGrade")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String attendanceGrade = snapshot.getValue().toString();
                        binding.attendanceGrade.setText(attendanceGrade);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}