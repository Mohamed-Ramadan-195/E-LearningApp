package com.example.e_learningapp.adapters.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.data.SharedPreference;
import com.example.e_learningapp.databinding.ReceiverMessageBinding;
import com.example.e_learningapp.databinding.SenderMessageBinding;
import com.example.e_learningapp.model.ChatModel;
import java.util.ArrayList;

public class RecyclerMessagesAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private String userId;
    private Context context;
    private ArrayList<ChatModel> list = new ArrayList<>();

    public void setList(ArrayList<ChatModel> list) {
        this.list = list;
    }

    public RecyclerMessagesAdapter(Context context) {
        this.context = context;
        SharedPreference.init(context, Const.KEY_AUTHENTICATION);
        userId = SharedPreference.getData(Const.KEY_USER_ID, "not found");
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
    @Override
    public int getItemViewType(int position) {
        ChatModel chatModel = (ChatModel) list.get(position);

        if (chatModel.getUserId().equals(userId)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatModel chatModel = (ChatModel) list.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SendMessageViewHolder) holder).bind(chatModel);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceiveMessageViewHolder) holder).bind(chatModel);
        }
    }
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            SenderMessageBinding binding = SenderMessageBinding.inflate(inflater, parent, false);
            return new SendMessageViewHolder(binding);
        } else {
            ReceiverMessageBinding binding = ReceiverMessageBinding.inflate(inflater, parent, false);
            return new ReceiveMessageViewHolder(binding);
        }
    }
}
