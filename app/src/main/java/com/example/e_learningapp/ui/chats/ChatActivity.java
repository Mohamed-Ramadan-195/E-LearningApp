package com.example.e_learningapp.ui.chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.adapters.RecyclerReceiveMessagesAdapter;
import com.example.e_learningapp.adapters.RecyclerSendMessagesAdapter;
import com.example.e_learningapp.adapters.messages.RecyclerMessagesAdapter;
import com.example.e_learningapp.data.SharedPreference;
import com.example.e_learningapp.databinding.ActivityChatBinding;
import com.example.e_learningapp.model.ChatModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    RecyclerMessagesAdapter adapter;
    private ArrayList<ChatModel> list =new ArrayList<>();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private String courseId;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new RecyclerMessagesAdapter(this);
        getData();
        courseId = getIntent().getStringExtra(Const.KEY_COURSE_ID);
        setRecyclerView();
        callBack();
        initData();
    }

    private void getData() {
        SharedPreference.init(getApplicationContext(), Const.KEY_AUTHENTICATION);
        username = SharedPreference.getData(Const.KEY_USER_NAME, "not found");
    }

    private void callBack() {
        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.textMessage.getText().toString().trim();
                validate(message);
            }
        });
    }

    private void validate(String message) {
        if (message.isEmpty()) {
            Toast.makeText(this, "please, write message before send", Toast.LENGTH_SHORT).show();
        } else {
            binding.textMessage.setText("");
            sendMessageToRealDatabase(message);
        }
    }

    private void sendMessageToRealDatabase(String message) {
        reference.child(Const.KEY_MESSAGES)
                .child(courseId)
                .push()
                .setValue(new ChatModel(message, username, FirebaseAuth.getInstance().getUid()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ChatActivity.this, "message sent", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initData() {
        reference.child(Const.KEY_MESSAGES)
                .child(courseId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                            list.add(chatModel);
                        }
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        binding.messagesRecyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.messagesRecyclerView.setLayoutManager(linearLayoutManager);
    }
}