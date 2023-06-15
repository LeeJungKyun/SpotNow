package com.example.spotnow.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.spotnow.R;
import com.example.spotnow.profile.my_ProfileFragment;
import com.example.spotnow.profile.user_ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectedFragment = null;
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener;
    private DatabaseReference mDatabaseA;
    private DatabaseReference mDatabaseU;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private String name;
    private ArrayList<String> activityOwners;

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request GPS permission
        checkLocationPermission();

        activityOwners = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser(); // Get current logged-in user information

        final String uid = currentUser.getUid(); // Store the user's UID

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabaseA = FirebaseDatabase.getInstance().getReference().child("activities");
        mDatabaseU = FirebaseDatabase.getInstance().getReference().child("users");

        mDatabaseU.child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String n = dataSnapshot.getValue(String.class);
                    name = n;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log.e("firebase", "Error getting data");
            }
        });
        navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.profile:
                        selectedFragment = new my_ProfileFragment();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
                }
                return true;
            }
        };

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        // Set the initial screen when the app is launched
        selectedFragment = new HomeFragment(); // Set the initial screen to HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1004) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = new Bundle();
                bundle.putString("selected_name",data.getStringExtra("selected_user_name")); // Store the selected user's information in the bundle

                selectedFragment = new user_ProfileFragment();
                selectedFragment.setArguments(bundle); // Pass the bundle to the fragment transition

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit(); // Switch to the user profile fragment

            } else {
                Toast.makeText(MainActivity.this, "result cancel!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkLocationPermission() {
        // Check if location permission is already granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, perform desired operation
            // e.g., Use GPS functionality
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // User granted the permission, perform desired operation
                // e.g., Use GPS functionality
            } else {
                // User denied the permission, display a notification or take other action
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}