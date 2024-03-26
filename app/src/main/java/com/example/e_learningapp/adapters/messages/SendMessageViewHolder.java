package com.example.e_learningapp.adapters.messages;

import com.example.e_learningapp.databinding.SenderMessageBinding;
import com.example.e_learningapp.model.ChatModel;

public class SendMessageViewHolder extends MessageViewHolder{
    private SenderMessageBinding binding;
    public SendMessageViewHolder(SenderMessageBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public void bind(ChatModel chatModel) {
        binding.messageTv.setText(chatModel.getMessage());
    }
}
