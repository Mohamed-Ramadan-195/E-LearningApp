package com.example.e_learningapp.ui.student.fragments;

import android.content.Intent;
import android.net.Uri;
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
import com.example.e_learningapp.adapters.RecyclerMaterialsAdapter;
import com.example.e_learningapp.databinding.FragmentMaterialStudentControlBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MaterialStudentControlFragment extends Fragment {
    private FragmentMaterialStudentControlBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    ArrayList<String> list = new ArrayList<>();
    RecyclerMaterialsAdapter adapter = new RecyclerMaterialsAdapter();
    private String courseId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMaterialStudentControlBinding.inflate(inflater, container, false);
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

    private void callBack() {
        adapter.setOnItemClick(new RecyclerMaterialsAdapter.OnItemClick() {
            @Override
            public void onClick(String object) {
                Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCourseId() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            courseId = getArguments().getString(Const.KEY_COURSE_ID);
        } else {
            Toast.makeText(getActivity(), "no course id", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        reference.child(Const.KEY_COURSES)
                .child(courseId)
                .child(Const.KEY_MATERIAL)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            list.add(dataSnapshot.getValue(String.class));
                        }
                        adapter.setList(list);
                        binding.materialsRecyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

//    private void setRecyclerView() {
//        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());
//        binding.materialsRecyclerView.setLayoutManager(linearLayoutManager);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}