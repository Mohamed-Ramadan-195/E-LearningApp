package com.example.e_learningapp.ui.student.fragments;

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
import com.example.e_learningapp.adapters.RecyclerQuizAdapter;
import com.example.e_learningapp.databinding.FragmentQuizStudentControlBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class QuizStudentControlFragment extends Fragment {
    private FragmentQuizStudentControlBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    ArrayList<String> list = new ArrayList<>();
    RecyclerQuizAdapter adapter = new RecyclerQuizAdapter();
    private String courseId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuizStudentControlBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        setRecyclerView();
        getCourseId();
        callBack();
        initData();
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
        adapter.setOnItemClick(new RecyclerQuizAdapter.OnItemClick() {
            @Override
            public void onClick(String object) {
                String id = object;
                checkAnswered(id);
            }
        });
    }

    private void initData() {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child(Const.KEY_QUIZ)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            list.add(dataSnapshot.getKey());
                        }
                        adapter.setList(list);
                        binding.quizRecyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkAnswered(String quizId) {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child(Const.KEY_QUIZ_GRADE_Student)
                .child(quizId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(FirebaseAuth.getInstance().getUid())) {
                            Toast.makeText(getActivity(), "You are already answered", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getActivity(), QuestionsStudentActivity.class);
                            intent.putExtra(Const.KEY_QUIZ_ID, quizId);
                            intent.putExtra(Const.KEY_COURSE_ID, courseId);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

//    private void setRecyclerView() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        binding.quizRecyclerView.setLayoutManager(linearLayoutManager);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}