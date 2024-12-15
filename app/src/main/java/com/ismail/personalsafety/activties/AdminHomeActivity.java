package com.ismail.personalsafety.activties;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismail.personalsafety.ModelClasses.Request;
import com.ismail.personalsafety.R;
import com.ismail.personalsafety.adapters.RequestAdapter;
import com.ismail.personalsafety.databinding.ActivityAdminHomeBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {
    ActivityAdminHomeBinding binding;
    private RequestAdapter adapter;
    private List<Request> requestList;
    private AlertDialog logoutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_home);
        binding.reyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestList = new ArrayList<>();
        adapter = new RequestAdapter(requestList);
        binding.reyclerView.setAdapter(adapter);

        binding.lgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        // Fetch data from Firebase Realtime Database
        FirebaseDatabase.getInstance().getReference("requests")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        requestList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Request request = snapshot.getValue(Request.class);
                            requestList.add(request);
                        }
                        Collections.reverse(requestList); // Reverse the order of the list
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                    }
                });
    }
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Sign out the user and navigate to the login activity
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        logoutDialog = builder.create();
        logoutDialog.show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
        finish();
    }


}
