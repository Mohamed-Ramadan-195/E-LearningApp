package com.example.e_learningapp.ui.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.example.e_learningapp.Const;
import com.example.e_learningapp.R;
import com.example.e_learningapp.data.SharedPreference;
import com.example.e_learningapp.databinding.ActivityCompleteProfileBinding;
import com.example.e_learningapp.model.UserModel;
import com.example.e_learningapp.ui.instructor.InstructorHomeActivity;
import com.example.e_learningapp.ui.student.StudentHomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CompleteProfileActivity extends AppCompatActivity {
    ActivityCompleteProfileBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    Uri profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompleteProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreference.init(getApplicationContext(), Const.KEY_AUTHENTICATION);
        setSpinner();
        callBack();
    }

    private void setSpinner() {
        String[] years = new String[] {"first year", "second year", "third year", "final year"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, years);
        binding.selectGradeSpinner.setAdapter(adapter);
    }

    private void callBack() {
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 19);
            }
        });
        binding.skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserType();
            }
        });
        binding.completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhone = binding.phoneNumber.getText().toString().trim();
                String userGrade = binding.selectGradeSpinner.getText().toString().trim();
                validate(userPhone, userGrade);
            }
        });
    }

    private void validate(String userPhoneNumber, String userGrade) {
        if (userPhoneNumber.isEmpty()) {
            binding.phoneNumber.setError(getString(R.string.required));
        } else if (userGrade.isEmpty()) {
            Toast.makeText(this, "please, Enter your grade", Toast.LENGTH_SHORT).show();
        } else if (!binding.male.isChecked() && !binding.female.isChecked()) {
            Toast.makeText(this, "please, select your sexual", Toast.LENGTH_SHORT).show();
        } else if (profileImage == null) {
            Toast.makeText(this, "please, select profile image", Toast.LENGTH_SHORT).show();
        } else {
            uploadImage(userPhoneNumber, userGrade);
        }
    }

    private String setRadioButton() {
        String sexual;
        if (binding.male.isChecked()) {
            sexual = binding.male.getText().toString().trim();
        } else if (binding.female.isChecked()) {
            sexual = binding.female.getText().toString().trim();
        } else {
            sexual = "not selected";
        }
        return sexual;
    }

    private void uploadImage(String userPhoneNumber, String userGrade) {
        if (profileImage != null) {
            StorageReference ref = storageReference
                    .child("images/")
                    .child(firebaseAuth.getUid() + System.currentTimeMillis());

            ref.putFile(profileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ref.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                sendDataToRealDatabase(uri.toString(), userPhoneNumber, userGrade, setRadioButton());
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(CompleteProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CompleteProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void sendDataToRealDatabase(String userImage, String userPhone, String userGrade, String userGender) {
        reference.child(Const.KEY_USERS)
                .child(FirebaseAuth.getInstance().getUid())
                .child(Const.KEY_ADDITIONAL_INFO)
                .setValue(new UserModel(userImage, userPhone, userGrade, userGender))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        setUserType();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(CompleteProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUserType() {
        String userType = SharedPreference.getData(Const.KEY_USER_TYPE, "Not Found");
        if (userType.equals(Const.KEY_STUDENT)) {
            startActivity(new Intent(CompleteProfileActivity.this, StudentHomeActivity.class));
        } else if (userType.equals(Const.KEY_INSTRUCTOR)){
            startActivity(new Intent(CompleteProfileActivity.this, InstructorHomeActivity.class));
        } else {
            Toast.makeText(this, "please, select user type", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 19 && data != null && data.getData() != null) {
            profileImage = data.getData();
            binding.profileImage.setImageURI(profileImage);
        }
    }
}