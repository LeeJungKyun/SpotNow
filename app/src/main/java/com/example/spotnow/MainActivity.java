package com.example.spotnow;

//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.Signature;
//import android.util.Base64;
//import android.util.Log;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login);
//    }
//
//}
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spotnow.common.FirebaseManager;
import com.example.spotnow.main.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityOwners = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser(); // 현재 로그인 한 유저 정보 반환

        final String uid = currentUser.getUid(); // 유저의 uid 저장

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabaseA = FirebaseDatabase.getInstance().getReference().child("activities");
        mDatabaseU = FirebaseDatabase.getInstance().getReference().child("users");

        mDatabaseU.child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String n = dataSnapshot.getValue(String.class);
                    name = n;
                    Log.d("NNNNN", "name = " + name);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebase", "Error getting data");
            }
        });
        navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.my_activity:
                        mDatabaseA.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String activityOwner = snapshot.child("activityOwner").getValue(String.class);
                                    activityOwners.add(activityOwner);
                                }
                                boolean isValueFound = activityOwners.contains(name);

                                if (isValueFound) {
                                    // 찾는 값이 ArrayList 안에 있을 때
                                    selectedFragment = new ownerFragment();
                                } else {
                                    // 찾는 값이 ArrayList 안에 없을 때
                                    selectedFragment = new participantFragment();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("firebase", "Error retrieving activity owners");
                            }
                        });
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

        // 앱 실행 시 초기 화면 설정
        selectedFragment = new HomeFragment(); // 초기 화면을 Fragment1로 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();

    }
}