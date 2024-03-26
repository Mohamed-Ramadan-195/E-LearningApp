package com.example.e_learningapp.ui.student;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.e_learningapp.adapters.FragmentViewPagerAdapter;
import com.example.e_learningapp.databinding.FragmentStudentChatBinding;
import com.example.e_learningapp.ui.chats.GroupChatFragment;
import com.example.e_learningapp.ui.chats.PrivateChatFragment;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;

public class StudentChatFragment extends Fragment {
    FragmentStudentChatBinding binding;
    ArrayList<Fragment> nestedFragment = new ArrayList<>();
    FragmentViewPagerAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStudentChatBinding.inflate(inflater, container, false);

        nestedFragment.add(new PrivateChatFragment());
        nestedFragment.add(new GroupChatFragment());

        adapter = new FragmentViewPagerAdapter(requireActivity(), nestedFragment);
        binding.nestedViewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.nestedTabLayout, binding.nestedViewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Private");
            } else {
                tab.setText("Group");
            }
        }).attach();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}