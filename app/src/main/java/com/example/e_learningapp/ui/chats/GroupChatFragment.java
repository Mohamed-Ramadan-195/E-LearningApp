package com.example.e_learningapp.ui.chats;

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
import com.example.e_learningapp.adapters.RecyclerCoursesInstructorAdapter;
import com.example.e_learningapp.adapters.RecyclerCoursesStudentAdapter;
import com.example.e_learningapp.data.SharedPreference;
import com.example.e_learningapp.databinding.FragmentGroupChatBinding;
import com.example.e_learningapp.model.CourseModel;
import com.example.e_learningapp.model.MembersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class GroupChatFragment extends Fragment {
    FragmentGroupChatBinding binding;
    RecyclerCoursesStudentAdapter recyclerCoursesStudentAdapter = new RecyclerCoursesStudentAdapter();
    RecyclerCoursesInstructorAdapter recyclerCoursesAdapter = new RecyclerCoursesInstructorAdapter();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    ArrayList<MembersModel> membersList = new ArrayList<>();
    ArrayList<CourseModel> coursesList = new ArrayList<>();
    private String userType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGroupChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getSharedPreferenceData();
        setRecyclerView();
        initData();
        callBack();
    }

    private void getSharedPreferenceData() {
        SharedPreference.init(getActivity(), Const.KEY_AUTHENTICATION);
        userType = SharedPreference.getData(Const.KEY_USER_TYPE, "not found");
    }

    private void callBack() {
        recyclerCoursesAdapter.setOnItemClick(new RecyclerCoursesInstructorAdapter.OnItemClick() {
            @Override
            public void onClick(CourseModel courseModel) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(Const.KEY_COURSE_ID, courseModel.getCourseId());
                intent.putExtra(Const.KEY_COURSE_NAME, courseModel.getCourseName());
                startActivity(intent);
            }
        });
        recyclerCoursesStudentAdapter.setOnItemClick(new RecyclerCoursesStudentAdapter.OnItemClick() {
            @Override
            public void onClick(MembersModel membersModel) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(Const.KEY_COURSE_ID, membersModel.getCourseId());
                intent.putExtra(Const.KEY_COURSE_NAME, membersModel.getCourseName());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        if (userType.equals(Const.KEY_STUDENT)) {
            reference.child(Const.KEY_MEMBERS)
                    .child(FirebaseAuth.getInstance().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            membersList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                MembersModel membersModel = dataSnapshot.getValue(MembersModel.class);
                                membersList.add(membersModel);
                            }
                            recyclerCoursesStudentAdapter.setList(membersList);
                            binding.groupChatsRecyclerView.setAdapter(recyclerCoursesStudentAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        } else if (userType.equals(Const.KEY_INSTRUCTOR)){
            reference.child(Const.KEY_COURSES)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            coursesList.clear();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    CourseModel courseModel = dataSnapshot.getValue(CourseModel.class);
                                    if (courseModel.getInstructorId().equals(FirebaseAuth.getInstance().getUid())) {
                                        coursesList.add(courseModel);
                                    }
                                    recyclerCoursesAdapter.setList(coursesList);
                                    binding.groupChatsRecyclerView.setAdapter(recyclerCoursesAdapter);
                                }
                            } else {
                                Toast.makeText(getActivity(), "no courses", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void setRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.groupChatsRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}