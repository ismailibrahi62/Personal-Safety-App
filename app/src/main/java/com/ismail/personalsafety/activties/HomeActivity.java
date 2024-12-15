package com.ismail.personalsafety.activties;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.ismail.personalsafety.R;
import com.ismail.personalsafety.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);


        binding.police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity("Police","  9 1 1",R.drawable.police_car);
            }
        });
        binding.ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity("Ambulance","  1 1 1",R.drawable.ambulance_car);
            }
        });
        binding.animalSafety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity("Animal Safety","  2 2 2",R.drawable.animal_safe_car);
            }
        });
        binding.fireTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity("Fire Truck","  3 3 3",R.drawable.fire_truck);
            }
        });
        binding.lgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(HomeActivity.this);
            }
        });


    }
    private void startNextActivity(String text, String emergencyNum, int imageResId) {
        // Start the next activity and pass the selected text and image resource ID
        Intent intent = new Intent(HomeActivity.this, EmergencyDetailActivity.class);
        intent.putExtra("emergencyText", text);
        intent.putExtra("emergencyImage", imageResId);
        intent.putExtra("emergencyNum",emergencyNum);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    public static void show(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Sign out from Firebase Authentication
                FirebaseAuth.getInstance().signOut();

                // Navigate to LoginActivity
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }}