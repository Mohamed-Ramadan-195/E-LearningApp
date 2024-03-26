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
import com.example.e_learningapp.databinding.FragmentAttendanceInstructorControlBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Random;

public class AttendanceInstructorControlFragment extends Fragment {
    private FragmentAttendanceInstructorControlBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private String courseId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAttendanceInstructorControlBinding.inflate(inflater, container, false);
        return binding.getRoot();
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
            Toast.makeText(getActivity(), courseId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
        }
    }

    private void callBack() {
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
                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}