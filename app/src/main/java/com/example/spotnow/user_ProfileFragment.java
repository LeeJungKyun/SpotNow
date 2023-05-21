package com.example.spotnow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class user_ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    UserInfo userInfo;
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    Button follow_button;
    private boolean isFollowing = false;

    public user_ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile, container, false);
        follow_button = rootView.findViewById(R.id.follow_button);

        checkFollowStatus();

        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowing) {
                    unfollow();
                } else {
                    follow();
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void checkFollowStatus() {
        String targetUserId = "targetUID";

        mDatabase.child(currentUserId).child("following").child(targetUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isFollowing = dataSnapshot.exists();
                updateButton();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //에러처리
            }
        });
    }

    //팔로우 메소드
    private void follow() {
        String targetUserId = "targetUId";

        //현재 들어와있는 User의 팔로워목록에 나를 추가
        mDatabase.child(targetUserId).child("followers").child(currentUserId).setValue(true);

        //내 팔로잉 목록에 현재 들어와있는 유저 추가
        mDatabase.child(currentUserId).child("following").child(targetUserId).setValue(true);

        isFollowing = true;

        updateButton();
    }

    //언팔로우메소드
    private void unfollow() {
        //언팔로우할 대상
        String targetUserId="targetUId";
        //들어와있는 User의 팔로워 목록에서 나를 삭제
        mDatabase.child(targetUserId).child("followers").child(currentUserId).removeValue();
        //내 팔로잉 목록에서 들어와있는 User 삭제
        mDatabase.child(currentUserId).child("following").child(targetUserId).removeValue();
    }

    private void updateButton() {
        if (isFollowing) {
            follow_button.setText("언팔로우");
        } else {
            follow_button.setText("팔로우");
        }
    }
}
