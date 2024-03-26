package com.example.e_learningapp.ui.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.R;
import com.example.e_learningapp.data.SharedPreference;
import com.example.e_learningapp.databinding.ActivitySignInBinding;
import com.example.e_learningapp.model.UserModel;
import com.example.e_learningapp.ui.instructor.InstructorHomeActivity;
import com.example.e_learningapp.ui.student.StudentHomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreference.init(getApplicationContext(), Const.KEY_AUTHENTICATION);
        callBack();
    }

    private void callBack() {
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = binding.emailSignIn.getText().toString().trim();
                String userPassword = binding.passwordSignIn.getText().toString();
                validate(userEmail, userPassword);
            }
        });
        binding.tvGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validate(String userEmail, String userPassword) {
        if (userEmail.isEmpty()) {
            binding.emailSignIn.setError(getString(R.string.required));
        } else if (userPassword.isEmpty()) {
            Toast.makeText(SignInActivity.this, "Please, Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
            startAuthentication(userEmail, userPassword);
        }
    }

    private void startAuthentication(String userEmail, String userPassword) {
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        setUserType();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignInActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUserType() {
        reference.child(Const.KEY_USERS)
                .child(Objects.requireNonNull(firebaseAuth.getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            if (userModel != null) {
                                sendDataToSharedPreference(userModel.getUserName(), userModel.getUserEmail(), userModel.getUserType(), userModel.getUserId());
                                if (userModel.getUserType().equals(Const.KEY_STUDENT)) {
                                    startActivity(new Intent(SignInActivity.this, StudentHomeActivity.class));
                                } else {
                                    startActivity(new Intent(SignInActivity.this, InstructorHomeActivity.class));
                                }
                            }
                        } else {
                            Toast.makeText(SignInActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SignInActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendDataToSharedPreference(String userName, String userEmail, String userType, String userId) {
        SharedPreference.setData(Const.KEY_USER_NAME, userName);
        SharedPreference.setData(Const.KEY_USER_EMAIL, userEmail);
        SharedPreference.setData(Const.KEY_USER_TYPE, userType);
        SharedPreference.setData(Const.KEY_USER_ID, userId);
    }
}