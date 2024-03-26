package com.example.e_learningapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_learningapp.databinding.CourseItemBinding;
import com.example.e_learningapp.model.CourseModel;

import java.util.ArrayList;

public class RecyclerCoursesInstructorAdapter extends RecyclerView.Adapter<RecyclerCoursesInstructorAdapter.Holder> {

    ArrayList<CourseModel> list;
    private OnItemClick onItemClick;

    public void setList(ArrayList<CourseModel> list) {
        this.list = list;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CourseItemBinding binding = CourseItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private CourseItemBinding binding;
        public Holder(CourseItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null)
                        onItemClick.onClick(list.get(getLayoutPosition()));
                }
            });
        }
        public void bind(CourseModel courseModel) {
            binding.courseName.setText(courseModel.getCourseName());
        }
    }

    public interface OnItemClick {
        void onClick (CourseModel courseModel);
    }
}
