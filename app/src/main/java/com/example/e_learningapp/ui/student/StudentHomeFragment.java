package com.example.e_learningapp.ui.student;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.data.SharedPreference;
import com.example.e_learningapp.databinding.FragmentStudentHomeBinding;
import com.example.e_learningapp.ui.intro.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;

public class StudentHomeFragment extends Fragment {
    private FragmentStudentHomeBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStudentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreference.init(getActivity(), Const.KEY_AUTHENTICATION);
        callBack();
    }

    private void callBack() {
        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreference.setData(Const.KEY_USER_NAME, Const.KEY_EMPTY_STRING);
                SharedPreference.setData(Const.KEY_USER_EMAIL, Const.KEY_EMPTY_STRING);
                SharedPreference.setData(Const.KEY_USER_TYPE, Const.KEY_EMPTY_STRING);
                SharedPreference.setData(Const.KEY_USER_ID, Const.KEY_EMPTY_STRING);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), SplashActivity.class));
            }
        });
    }
}