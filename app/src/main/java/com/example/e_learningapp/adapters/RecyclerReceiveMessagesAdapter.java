package com.example.e_learningapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_learningapp.databinding.ReceiverMessageBinding;
import com.example.e_learningapp.databinding.SenderMessageBinding;
import com.example.e_learningapp.model.ChatModel;

import java.util.ArrayList;

public class RecyclerReceiveMessagesAdapter extends RecyclerView.Adapter<RecyclerReceiveMessagesAdapter.Holder> {
    private ArrayList<ChatModel> list = new ArrayList<>();

    public void setList(ArrayList<ChatModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReceiverMessageBinding binding = ReceiverMessageBinding
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
        private ReceiverMessageBinding binding;
        public Holder(ReceiverMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ChatModel chatModel) {
            binding.messageTv.setText(chatModel.getMessage());
            binding.usernameTv.setText(chatModel.getUsername());
        }
    }
}
