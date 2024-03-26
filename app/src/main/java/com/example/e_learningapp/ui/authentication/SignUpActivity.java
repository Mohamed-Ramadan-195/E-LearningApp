package com.example.e_learningapp.ui.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.R;
import com.example.e_learningapp.data.SharedPreference;
import com.example.e_learningapp.databinding.ActivitySignUpBinding;
import com.example.e_learningapp.model.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreference.init(getApplicationContext(), Const.KEY_AUTHENTICATION);
        setSpinner();
        callBack();
    }

    private void setSpinner() {
        String[] continueAs = new String[] {Const.KEY_STUDENT, Const.KEY_INSTRUCTOR};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, continueAs);
        binding.continuaAsSpinner.setAdapter(adapter);
    }

    private void callBack() {
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = binding.usernameSignUp.getText().toString().trim();
                String userEmail = binding.emailSignUp.getText().toString().trim();
                String userPassword = binding.passwordSignUp.getText().toString();
                String userType = binding.continuaAsSpinner.getText().toString().trim();
                validate(userName, userEmail, userPassword, userType);
            }
        });
    }

    private void validate(String userName, String userEmail, String userPassword, String userType) {
        if (userName.isEmpty()) {
            binding.usernameSignUp.setError(getString(R.string.required));
        } else if (userEmail.isEmpty()) {
            binding.emailSignUp.setError(getString(R.string.required));
        } else if (userPassword.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please, Enter password", Toast.LENGTH_SHORT).show();
        } else if (userPassword.length() < 8) {
            Toast.makeText(SignUpActivity.this, "Please, Enter password contain 8 characters", Toast.LENGTH_SHORT).show();
        } else if (userType.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please, Enter user type", Toast.LENGTH_SHORT).show();
        } else if (!binding.termsAndCondition.isChecked()) {
            Toast.makeText(SignUpActivity.this, "Please, Checked Terms & Conditions", Toast.LENGTH_SHORT).show();
        } else {
            startAuthentication(userName, userEmail, userPassword, userType);
        }
    }

    private void startAuthentication(String userName, String userEmail, String userPassword, String userType) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        sendDataToRealDatabase(userName, userEmail, userPassword, userType, firebaseAuth.getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendDataToRealDatabase(String userName, String userEmail, String userPassword, String userType, String userId) {
        reference.child(Const.KEY_USERS)
                .child(userId)
                .setValue(new UserModel(userName, userEmail, userPassword, userType, userId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        sendDataToSharedPreference(userName, userEmail, userType, userId);
                        setIntent(userPassword, userType);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendDataToSharedPreference(String userName, String userEmail, String userType, String userId) {
        SharedPreference.setData(Const.KEY_USER_NAME, userName);
        SharedPreference.setData(Const.KEY_USER_EMAIL, userEmail);
        SharedPreference.setData(Const.KEY_USER_TYPE, userType);
        SharedPreference.setData(Const.KEY_USER_ID, userId);
    }

    private void setIntent(String userPassword, String userType) {
        Intent intent = new Intent(SignUpActivity.this, CompleteProfileActivity.class);
        intent.putExtra(Const.KEY_USER_TYPE, userType);
        intent.putExtra(Const.KEY_USER_PASSWORD, userPassword);
        startActivity(intent);
    }
}