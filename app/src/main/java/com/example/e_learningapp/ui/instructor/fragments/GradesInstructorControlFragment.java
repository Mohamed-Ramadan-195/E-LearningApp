package com.example.e_learningapp.ui.instructor.fragments;

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
import com.example.e_learningapp.adapters.RecyclerGradesAdapter;
import com.example.e_learningapp.data.SharedPreference;
import com.example.e_learningapp.databinding.FragmentGradesInstructorControlBinding;
import com.example.e_learningapp.model.GradesModel;
import com.example.e_learningapp.model.MembersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GradesInstructorControlFragment extends Fragment {
    private FragmentGradesInstructorControlBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    ArrayList<MembersModel> list = new ArrayList<>();
    RecyclerGradesAdapter adapter = new RecyclerGradesAdapter();
    private String courseId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGradesInstructorControlBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCourseId();
        setRecyclerView();
        initData();
        callBack();
    }

    private void getCourseId() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            courseId = getArguments().getString(Const.KEY_COURSE_ID);
        }
    }

    private void callBack() {
        adapter.setOnItemClick(new RecyclerGradesAdapter.OnItemClick() {
            @Override
            public void onClick(MembersModel membersModel) {

            }
        });
    }

    private void initData() {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child(Const.KEY_MEMBERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MembersModel membersModel = dataSnapshot.getValue(MembersModel.class);
                            list.add(membersModel);
                        }
                        adapter.setList(list);
                        binding.gradesRecyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void setRecyclerView() {
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());
        binding.gradesRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}