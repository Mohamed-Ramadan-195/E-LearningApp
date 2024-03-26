package com.example.e_learningapp.ui.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.e_learningapp.R;
import com.example.e_learningapp.adapters.FragmentViewPagerAdapter;
import com.example.e_learningapp.databinding.ActivityStudentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class StudentHomeActivity extends AppCompatActivity {
    ActivityStudentHomeBinding binding;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    FragmentViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();
        adapter = new FragmentViewPagerAdapter(this, fragmentArrayList);

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.bottomNavigationView.setSelectedItemId(R.id.home);
                        break;
                    case 1:
                        binding.bottomNavigationView.setSelectedItemId(R.id.course);
                        break;
                    case 2:
                        binding.bottomNavigationView.setSelectedItemId(R.id.chat);
                        break;
                    case 3:
                        binding.bottomNavigationView.setSelectedItemId(R.id.profile);
                        break;
                }
                super.onPageSelected(position);
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    binding.viewPager.setCurrentItem(0);
                } else if (item.getItemId() == R.id.course) {
                    binding.viewPager.setCurrentItem(1);
                }  else if (item.getItemId() == R.id.chat) {
                    binding.viewPager.setCurrentItem(2);
                } else if (item.getItemId() == R.id.profile) {
                    binding.viewPager.setCurrentItem(3);
                }
                return true;
            }
        });
    }
    private void initData() {
        fragmentArrayList.add(new StudentHomeFragment());
        fragmentArrayList.add(new StudentCourseFragment());
        fragmentArrayList.add(new StudentChatFragment());
        fragmentArrayList.add(new StudentProfileFragment());
    }
}