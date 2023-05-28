package com.example.spotnow.main;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.spotnow.R;
import com.example.spotnow.activity.ownerFragment;
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

        // GPS 퍼미션 요청
        checkLocationPermission();

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
//                    case R.id.my_activity:
//                        mDatabaseA.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                    String activityOwner = snapshot.child("activityOwner").getValue(String.class);
//                                    activityOwners.add(activityOwner);
//                                }
//                                boolean isValueFound = activityOwners.contains(name);
//
//                                if (isValueFound) {
//                                    // 찾는 값이 ArrayList 안에 있을 때
//                                    selectedFragment = new ownerFragment();
//                                } else {
//                                    // 찾는 값이 ArrayList 안에 없을 때
//                                    //selectedFragment = new participantFragment();
//                                }
//                            }
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                                Log.e("firebase", "Error retrieving activity owners");
//                            }
//                        });
//                        break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1004) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = new Bundle();
                bundle.putString("selected_name",data.getStringExtra("selected_user_name")); // 선택된 유저 정보 번들에 담기

                selectedFragment = new user_ProfileFragment();
                selectedFragment.setArguments(bundle); // 프래그먼트 이동간 번들 담기

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit(); // 유저 정보 프래그먼트로 갈아끼우기

            } else {
                Toast.makeText(MainActivity.this, "result cancle!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkLocationPermission() {
        // 위치 권한이 이미 허용되어 있는지 확인합니다.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 허용되지 않은 경우, 사용자에게 권한을 요청합니다.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            // 권한이 이미 허용된 경우, 원하는 작업을 수행합니다.
            // 예: GPS 기능 사용
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 사용자가 권한을 허용한 경우, 원하는 작업을 수행합니다.
                // 예: GPS 기능 사용
            } else {
                // 사용자가 권한을 거부한 경우, 알림을 표시하거나 다른 조치를 취합니다.
                Toast.makeText(this, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}