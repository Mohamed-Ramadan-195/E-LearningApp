package com.example.e_learningapp.adapters.messages;

import com.example.e_learningapp.databinding.ReceiverMessageBinding;
import com.example.e_learningapp.model.ChatModel;

public class ReceiveMessageViewHolder extends MessageViewHolder {
    private ReceiverMessageBinding binding;
    public ReceiveMessageViewHolder(ReceiverMessageBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public void bind(ChatModel chatModel) {
        binding.messageTv.setText(chatModel.getMessage());
        binding.usernameTv.setText(chatModel.getUsername());
    }
}
