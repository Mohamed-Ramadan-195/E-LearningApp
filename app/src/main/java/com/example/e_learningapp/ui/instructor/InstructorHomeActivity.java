package com.example.e_learningapp.ui.instructor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.e_learningapp.R;
import com.example.e_learningapp.adapters.FragmentViewPagerAdapter;
import com.example.e_learningapp.databinding.ActivityInstructorHomeBinding;
import com.google.android.material.navigation.NavigationBarView;
import java.util.ArrayList;

public class InstructorHomeActivity extends AppCompatActivity {
    ActivityInstructorHomeBinding binding;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    FragmentViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstructorHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();
        adapter = new FragmentViewPagerAdapter(this, fragmentArrayList);
        binding.viewPager.setAdapter(adapter);
        callBack();
    }

    private void callBack() {
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
                        binding.bottomNavigationView.setSelectedItemId(R.id.add);
                        break;
                    case 3:
                        binding.bottomNavigationView.setSelectedItemId(R.id.chat);
                        break;
                    case 4:
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
                } else if (item.getItemId() == R.id.add) {
                    binding.viewPager.setCurrentItem(2);
                } else if (item.getItemId() == R.id.chat) {
                    binding.viewPager.setCurrentItem(3);
                } else if (item.getItemId() == R.id.profile) {
                    binding.viewPager.setCurrentItem(4);
                }
                return true;
            }
        });
    }

    private void initData() {
        fragmentArrayList.add(new InstructorHomeFragment());
        fragmentArrayList.add(new InstructorCourseFragment());
        fragmentArrayList.add(new InstructorAddFragment());
        fragmentArrayList.add(new InstructorChatFragment());
        fragmentArrayList.add(new InstructorProfileFragment());
    }
}