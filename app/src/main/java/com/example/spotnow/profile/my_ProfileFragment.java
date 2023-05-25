package com.example.spotnow.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.spotnow.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class my_ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    UserInfo userInfo;

    public my_ProfileFragment() {
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
        Button profile_edit_button = rootView.findViewById(R.id.profile_edit_button);
        TextView following = rootView.findViewById(R.id.following_num);
        TextView follower = rootView.findViewById(R.id.follower_num);
        ImageView profile_image = rootView.findViewById(R.id.profile_image);

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
                    progressBar.setProgress(100-userInfo.getReport_cnt()*5);
                    following.setText(Integer.toString(userInfo.getFollowing_num()));
                    follower.setText(Integer.toString(userInfo.getFollower_num()));
                    Glide.with(my_ProfileFragment.this).load(userInfo.getProfileImage()).into(profile_image);
                }
            }
        });


        profile_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // profile_edit_button 클릭 시 실행될 코드
                Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
}
