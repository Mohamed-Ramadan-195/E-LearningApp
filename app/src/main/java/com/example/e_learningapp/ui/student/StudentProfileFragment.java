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
import com.example.e_learningapp.databinding.FragmentStudentProfileBinding;
import com.example.e_learningapp.ui.intro.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;

public class StudentProfileFragment extends Fragment {
    private FragmentStudentProfileBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStudentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreference.init(getActivity(), Const.KEY_AUTHENTICATION);
        getDataFromSharedPreference();
        callBack();
    }

    private void getDataFromSharedPreference() {
        String username = SharedPreference.getData(Const.KEY_USER_NAME, "not found");
        binding.usernameProfile.setText(username);
    }

    private void callBack() {
        binding.yourProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.settingsProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.privacyPolicyProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.helpCenterProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.logOutProfileButton.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}