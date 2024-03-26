package com.example.e_learningapp.ui.instructor;

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
import com.example.e_learningapp.databinding.FragmentInstructorCourseBinding;
import com.example.e_learningapp.model.CourseModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class InstructorCourseFragment extends Fragment {

    private FragmentInstructorCourseBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    RecyclerCoursesInstructorAdapter adapter = new RecyclerCoursesInstructorAdapter();
    private ArrayList<CourseModel> list = new ArrayList<>();
    public InstructorCourseFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInstructorCourseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRecyclerView();
        initData();
        callBack();
    }

    private void callBack() {
        adapter.setOnItemClick(new RecyclerCoursesInstructorAdapter.OnItemClick() {
            @Override
            public void onClick(CourseModel courseModel) {
                String id = courseModel.getCourseId();
                Intent intent = new Intent(getActivity(), InstructorControlActivity.class);
                intent.putExtra(Const.KEY_COURSE_ID, id);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        reference.child(Const.KEY_COURSES)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                CourseModel courseModel = dataSnapshot.getValue(CourseModel.class);
                                if (courseModel.getInstructorId().equals(FirebaseAuth.getInstance().getUid())) {
                                    list.add(courseModel);
                                }
                                adapter.setList(list);
                                binding.courseRecyclerView.setAdapter(adapter);
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

    private void setRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.courseRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}