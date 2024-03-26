package com.example.e_learningapp.ui.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.example.e_learningapp.R;
import com.example.e_learningapp.adapters.SlideViewPagerAdapter;

public class SlideActivity extends AppCompatActivity {
    public static ViewPager viewPager;
    SlideViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new SlideViewPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
    }
}