package com.example.e_learningapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_learningapp.databinding.CourseItemBinding;
import com.example.e_learningapp.model.ChatModel;
import java.util.ArrayList;

public class RecyclerChatsAdapter extends RecyclerView.Adapter<RecyclerChatsAdapter.Holder> {
    private ArrayList<ChatModel> list = new ArrayList<>();
    private OnItemClick onItemClick;

    public void setList(ArrayList<ChatModel> list) {
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

    static class Holder extends RecyclerView.ViewHolder {
        private final CourseItemBinding binding;
        public Holder(CourseItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ChatModel chatModel) {
            binding.courseName.setText(chatModel.getUsername());
        }
    }
    public interface OnItemClick {
        void onClick (ChatModel chatModel);
    }
}
