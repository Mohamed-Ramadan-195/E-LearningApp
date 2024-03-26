package com.example.e_learningapp.adapters.messages;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_learningapp.model.ChatModel;

public abstract class MessageViewHolder extends RecyclerView.ViewHolder {
    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bind(ChatModel chatModel);
}
