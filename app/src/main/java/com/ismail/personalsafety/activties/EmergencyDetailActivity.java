package com.ismail.personalsafety.activties;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ismail.personalsafety.ModelClasses.Request;
import com.ismail.personalsafety.R;
import com.ismail.personalsafety.databinding.ActivityEmergencyDetailBinding;

import java.util.concurrent.TimeUnit;

public class EmergencyDetailActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String emergencyType;
    private String phoneNumber;
    private int emergencyImageResId;
    ActivityEmergencyDetailBinding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding = DataBindingUtil.setContentView(this, R.layout.activity_emergency_detail);

        // Initialize Firebase references
        databaseReference = FirebaseDatabase.getInstance().getReference("requests");
        storageReference = FirebaseStorage.getInstance().getReference("images");

        // Initialize Firebase references
        mAuth = FirebaseAuth.getInstance();


        // Retrieve data from intent extras
        Intent intent = getIntent();
         emergencyType = intent.getStringExtra("emergencyText");
         emergencyImageResId = intent.getIntExtra("emergencyImage", 0);
         phoneNumber = intent.getStringExtra("emergencyNum");


        // Set data to TextViews and ImageView
        binding.typeOfEmergency.setText(emergencyType);
        binding.imageViewTypeOfCar.setImageResource(emergencyImageResId);
        binding.shortNum.setText(phoneNumber);

        String emergencyDetail = binding.edDesctiption.getText().toString();


        binding.btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestToAdmin();
            }
        });





    }

    private long lastRequestTime = 0; // Initialize the last request time

    private void sendRequestToAdmin() {
        // Check if the current time is less than an hour since the last request
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRequestTime < TimeUnit.HOURS.toMillis(1)) {
            Toast.makeText(EmergencyDetailActivity.this, "Please wait at least one hour before sending another request", Toast.LENGTH_SHORT).show();
            return; // Exit the method if the condition is met
        }

        // Start the progress bar
        binding.progressBar.setVisibility(View.VISIBLE);

        // Convert drawable to URI and upload image to Firebase Storage
        Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + emergencyImageResId);


        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + ".jpg");

        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();


                        // Fetch user data
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            String userId = currentUser.getUid();
                            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

                            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        String userName = dataSnapshot.child("userName").getValue(String.class);
                                        String userDistrict = dataSnapshot.child("userDistrict").getValue(String.class);
                                        String userPhone = dataSnapshot.child("userPhoneNum").getValue(String.class);


                                        // Create a request object
                                        Request request = new Request(
                                                emergencyType,
                                                userName, // Use the actual user name
                                                userDistrict, // Use the actual district
                                                userPhone,
                                                imageUrl
                                        );

                                        // Save the request to Firebase Realtime Database
                                        databaseReference.push().setValue(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                binding.progressBar.setVisibility(View.GONE); // Stop the progress bar
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(EmergencyDetailActivity.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                                                    lastRequestTime = System.currentTimeMillis(); // Update the last request time
                                                } else {
                                                    Toast.makeText(EmergencyDetailActivity.this, "Failed to send request", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        binding.progressBar.setVisibility(View.GONE); // Stop the progress bar
                                        Toast.makeText(EmergencyDetailActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    binding.progressBar.setVisibility(View.GONE); // Stop the progress bar
                                    Toast.makeText(EmergencyDetailActivity.this, "Failed to read user data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.progressBar.setVisibility(View.GONE); // Stop the progress bar

                Toast.makeText(EmergencyDetailActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(EmergencyDetailActivity.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
