package com.ismail.personalsafety.activties;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismail.personalsafety.R;
import com.ismail.personalsafety.ModelClasses.User;
import com.ismail.personalsafety.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;
    private ActivityRegistrationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Pre-fill the EditText with the required prefix
        binding.edPhoneNum.setText("090 ");
        binding.edPhoneNum.setSelection(binding.edPhoneNum.getText().length());

        binding.loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        binding.btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = binding.edPersonName.getText().toString();
                String userEmail = binding.edEmail.getText().toString();
                String userDistrict = binding.edDistrict.getText().toString();
                String userPhoneNum = binding.edPhoneNum.getText().toString();
                String userPassword = binding.edPasswordLogin.getText().toString();
                String userConfirmPassword = binding.edConfirm.getText().toString();

                if(userName.isEmpty()){
                    binding.edPersonName.setError("Your name is required");
                    binding.edPersonName.requestFocus();
                }
                else if(userName.contains(" ")){
                    binding.edPersonName.setError("Name cannot contain whitespace");
                    binding.edPersonName.requestFocus();
                }
                else if(userName.length() > 10){
                    binding.edPersonName.setError("Your name cannot more than ten characters");
                    binding.edPersonName.requestFocus();
                }
                else if(userEmail.isEmpty()){
                    binding.edEmail.setError("Your email is required");
                    binding.edEmail.requestFocus();
                }
                else if(userEmail.contains(" ")){
                    binding.edEmail.setError("Email cannot contain whitespace");
                    binding.edEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    binding.edEmail.setError("Please enter a valid email address");
                    binding.edEmail.requestFocus();
                }
                else if(userDistrict.isEmpty()){
                    binding.edDistrict.setError("Your district is required");
                    binding.edDistrict.requestFocus();
                }
                else if(userPhoneNum.isEmpty()){
                    binding.edDistrict.setError("Your phone is required");
                    binding.edDistrict.requestFocus();
                }
                else if(userPassword.isEmpty()){
                    binding.edPasswordLogin.setError("Your password is required");
                    binding.edPasswordLogin.requestFocus();
                }
                else if(userPassword.length() < 6){
                    binding.edPasswordLogin.setError("Your password must be greater than 5 characters");
                    binding.edPasswordLogin.requestFocus();
                }
                else if(userPassword.length() > 10){
                    binding.edPasswordLogin.setError("Your password must be less than 10 characters");
                    binding.edPasswordLogin.requestFocus();
                }
                else if(userConfirmPassword.isEmpty()){
                    binding.edConfirm.setError("Your Confirm password is required");
                    binding.edConfirm.requestFocus();
                }
                else if(userConfirmPassword.length() < 6){
                    binding.edConfirm.setError("Your Confirm password must be greater than 5 characters");
                    binding.edConfirm.requestFocus();
                }
                else if(userConfirmPassword.length() > 10){
                    binding.edConfirm.setError("Your Confirm password must be less than 10 characters");
                    binding.edConfirm.requestFocus();
                }
                else if (!userPassword.equals(userConfirmPassword)) {
                    binding.edConfirm.setError("Your Confirm password does not match the password");
                    binding.edConfirm.requestFocus();
                    binding.edConfirm.setText("");
                    binding.edPasswordLogin.setText("");
                    binding.edPasswordLogin.setHint("Re-enter password");
                    binding.edConfirm.setHint("Re-enter confirm password");
                }
                else {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        storeUserInfoAndRegister(userName,userDistrict,userPhoneNum,userEmail,userPassword);
                }



            }
        });
    }

    private void storeUserInfoAndRegister(String userName, String userDistrict, String userPhoneNum, String userEmail, String userPassword) {
        binding.progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        binding.progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            // Registration successful
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Create user object
                                User newUser = new User(userName, userDistrict, userPhoneNum, userEmail);

                                // Save user information to Realtime Database
                                mDatabase.child("users").child(user.getUid()).setValue(newUser)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegistrationActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(RegistrationActivity.this, HomeActivity.class);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                    finish();
                                                } else {
                                                    Toast.makeText(RegistrationActivity.this, "Failed to store user information.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Registration failed
                            Toast.makeText(RegistrationActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}