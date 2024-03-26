package com.example.e_learningapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.e_learningapp.R;
import com.example.e_learningapp.ui.authentication.SignInActivity;
import com.example.e_learningapp.ui.intro.SlideActivity;

public class SlideViewPagerAdapter extends PagerAdapter {
    private Context context;

    public SlideViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_screen, container, false);

        ImageView vectorImage = view.findViewById(R.id.vector_image);
        ImageView indicator1 = view.findViewById(R.id.indicator1);
        ImageView indicator2 = view.findViewById(R.id.indicator2);
        ImageView indicator3 = view.findViewById(R.id.indicator3);
        ImageView indicator4 = view.findViewById(R.id.indicator4);
        ImageView backButton = view.findViewById(R.id.back);
        ImageView nextButton = view.findViewById(R.id.next);
        TextView title = view.findViewById(R.id.title);
        TextView body = view.findViewById(R.id.body);
        Button btnGetStarted = view.findViewById(R.id.get_started_button);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SignInActivity.class);
                context.startActivity(intent);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlideActivity.viewPager.setCurrentItem(position + 1);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlideActivity.viewPager.setCurrentItem(position - 1);
            }
        });

        switch (position) {
            case 0:
                vectorImage.setImageResource(R.drawable.user_type_vector);
                setIndicator(indicator1);
                title.setText(R.string.user_type);
                body.setText(R.string.user_type_body);
                backButton.setVisibility(View.GONE);
                break;
            case 1:
                vectorImage.setImageResource(R.drawable.course_vector);
                setIndicator(indicator2);
                title.setText(R.string.course);
                body.setText(R.string.course_body);
                break;
            case 2:
                vectorImage.setImageResource(R.drawable.chat_vector);
                setIndicator(indicator3);
                title.setText(R.string.chat);
                body.setText(R.string.chat_body);
                break;
            case 3:
                vectorImage.setImageResource(R.drawable.quiz_vector);
                setIndicator(indicator4);
                title.setText(R.string.quiz);
                body.setText(R.string.quiz_body);
                nextButton.setVisibility(View.GONE);
                btnGetStarted.setVisibility(View.VISIBLE);
                break;
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void setIndicator(ImageView imageView) {
        imageView.setImageResource(R.drawable.selected_circle);
    }
}
