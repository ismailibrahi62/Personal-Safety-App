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
import com.google.firebase.database.FirebaseDatabase;
import com.ismail.personalsafety.R;
import com.ismail.personalsafety.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
        private FirebaseAuth mAuth;
        ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        binding.registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = binding.edEmailLogin.getText().toString();
                String userPassword = binding.edPasswordLogin.getText().toString();

                if(userEmail.isEmpty()){
                    binding.edEmailLogin.setError("Your email is required");
                    binding.edEmailLogin.requestFocus();
                }
                else if (userEmail.contains(" ")) {
                    binding.edEmailLogin.setError("Email cannot contain whitespace");
                    binding.edEmailLogin.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    binding.edEmailLogin.setError("Please enter a valid email address");
                    binding.edEmailLogin.requestFocus();
                }
                else if(userPassword.isEmpty()){
                    binding.edPasswordLogin.setError("Your password is required");
                    binding.edPasswordLogin.requestFocus();
                }
                else if (userPassword.length() > 10) {
                    binding.edPasswordLogin.setError("Password cannot be more than 10 characters");
                    binding.edPasswordLogin.requestFocus();
                }
                else if (userPassword.contains(" ")) {
                    binding.edPasswordLogin.setError("Password cannot contain whitespace");
                    binding.edPasswordLogin.requestFocus();
                }
                else if (userPassword.length() < 6) {
                    binding.edPasswordLogin.setError("Password cannot be less than 6 characters");
                    binding.edPasswordLogin.requestFocus();
                }
                else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    loginUser(userEmail,userPassword);
                }
            }
        });


    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            // Sign in success, check the user's email domain
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userEmail = user.getEmail();
                                if (userEmail != null && userEmail.endsWith("@admin.ps.com")) {
                                    // Navigate to admin side
                                    Intent i = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                } else {
                                    // Navigate to user side
                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if the user is already signed in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();

        }
    }
}