package com.example.spotnow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    UserInfo userInfo;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        TextView name = rootView.findViewById(R.id.profile_name);
        TextView introduce = rootView.findViewById(R.id.profile_introduce);
        ProgressBar progressBar = rootView.findViewById(R.id.progressBar);
        TextView like_sport = rootView.findViewById(R.id.profile_like_sport);
        TextView region = rootView.findViewById(R.id.profile_region);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser(); // 현재 로그인 한 유저 정보 반환

        final String uid = currentUser.getUid(); // 유저의 uid 저장

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    userInfo = task.getResult().getValue(UserInfo.class); // 유저 정보 한번 불러오기

                    // 데이터 반영
                    name.setText(userInfo.getName());
                    introduce.setText(userInfo.getIntroduce_self());
                    like_sport.setText(userInfo.getSport());
                    region.setText(userInfo.getRegion());
                    progressBar.setProgress(100-userInfo.getReport_cnt());

                }
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }
}
