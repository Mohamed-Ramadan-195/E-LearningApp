package com.example.e_learningapp.ui.student;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.R;
import com.example.e_learningapp.adapters.RecyclerCoursesStudentAdapter;
import com.example.e_learningapp.data.SharedPreference;
import com.example.e_learningapp.databinding.FragmentStudentCourseBinding;
import com.example.e_learningapp.model.MembersModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
public class StudentCourseFragment extends Fragment {

    private FragmentStudentCourseBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ArrayList<MembersModel> list = new ArrayList<>();
    RecyclerCoursesStudentAdapter adapter = new RecyclerCoursesStudentAdapter();
    private String username;
    private String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStudentCourseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDataFromSharedPreference();
//        setRecyclerView();
        initData();
        callBack();
    }

    private void getDataFromSharedPreference() {
        SharedPreference.init(getActivity(), Const.KEY_AUTHENTICATION);
        username = SharedPreference.getData(Const.KEY_USER_NAME, "not found");
        email = SharedPreference.getData(Const.KEY_USER_EMAIL, "not found");
    }

    private void callBack() {
        binding.enrollCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseId = binding.courseIdEt.getText().toString().trim();
                validate(courseId);
            }
        });
        adapter.setOnItemClick(new RecyclerCoursesStudentAdapter.OnItemClick() {
            @Override
            public void onClick(MembersModel membersModel) {
                Intent intent = new Intent(getActivity(), StudentControlActivity.class);
                intent.putExtra(Const.KEY_COURSE_ID, membersModel.getCourseId());
                intent.putExtra(Const.KEY_COURSE_NAME, membersModel.getCourseName());
                startActivity(intent);
            }
        });
    }

    private void validate(String courseId) {
        if (courseId.isEmpty()) {
            binding.courseIdEt.setError(getString(R.string.required));
        } else {
            correctCode(courseId);
        }
    }

    private void correctCode(String courseId) {
        reference.child(Const.KEY_COURSES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(courseId)) {
                            getCourseName(courseId);
                        } else {
                            Toast.makeText(getActivity(), "invalid id", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getCourseName(String courseId) {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child("courseName")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String courseName = snapshot.getValue().toString();
                        sendDataToRealDatabase(courseId, courseName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void sendDataToRealDatabase(String courseId, String courseName) {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child(Const.KEY_MEMBERS)
                .child(FirebaseAuth.getInstance().getUid())
                .setValue(new MembersModel(firebaseAuth.getUid(), username, email, courseId, courseName, 0, 0))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.courseIdEt.setText(Const.KEY_EMPTY_STRING);
                        Toast.makeText(getActivity(), "enrolled", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        reference.child(Const.KEY_MEMBERS)
                .child(FirebaseAuth.getInstance().getUid())
                .child(courseId)
                .setValue(new MembersModel(firebaseAuth.getUid(), username, email, courseId, courseName, 0, 0))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void initData() {
        reference.child(Const.KEY_MEMBERS)
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MembersModel membersModel = dataSnapshot.getValue(MembersModel.class);
                            list.add(membersModel);
                        }
                        adapter.setList(list);
                        binding.studentCoursesRecyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

//    private void setRecyclerView() {
//        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());
//        binding.studentCoursesRecyclerView.setLayoutManager(linearLayoutManager);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}