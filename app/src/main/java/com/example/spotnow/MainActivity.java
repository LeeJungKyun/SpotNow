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
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spotnow.main.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectedFragment = null;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.my_activity:
                        selectedFragment = new ActivityFragment();
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