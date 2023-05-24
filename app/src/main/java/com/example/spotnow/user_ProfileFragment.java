package com.example.spotnow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class user_ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    UserInfo userInfo;
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    Button follow_button;
    String selected_name;
    TextView user_name;
    TextView introduce;
    ProgressBar progressBar;
    TextView like_sport;
    TextView region;
    TextView following;
    TextView follower;

    String targetUId;

    Button report_button;

    private boolean isFollowing = false;

    public user_ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser(); // 현재 로그인 한 유저 정보 반환
        final String uid = currentUser.getUid(); // 유저의 uid 저장

        user_name = rootView.findViewById(R.id.profile_name);
        introduce = rootView.findViewById(R.id.profile_introduce);
        progressBar = rootView.findViewById(R.id.progressBar);
        like_sport = rootView.findViewById(R.id.profile_like_sport);
        region = rootView.findViewById(R.id.profile_region);
        following = rootView.findViewById(R.id.following_num);
        follower = rootView.findViewById(R.id.follow_num);
        report_button = rootView.findViewById(R.id.report_button);

        report_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle args = new Bundle();
                args.putString("name", selected_name);
                reportPopupFragment reportPopupFragment = new reportPopupFragment();
                reportPopupFragment.setArguments(args);
                reportPopupFragment.show(getFragmentManager(),"custom_report_dialog");
            }
        });

        if (getArguments() != null)
        {
            selected_name = getArguments().getString("selected_name"); // 번들에서 이름 가져오기
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    UserInfo userinfo = snapshot.getValue(UserInfo.class); // 선택된 유저 정보와 일치하는 유저 객체 저장
                    if(userinfo.getName().equals(selected_name)) // 똑같은 이름인지 돌면서 확인
                    {
                        user_name.setText(userinfo.getName());
                        introduce.setText(userinfo.getIntroduce_self());
                        like_sport.setText(userinfo.getSport());
                        region.setText(userinfo.getRegion());
                        progressBar.setProgress(100-userinfo.getReport_cnt()*5);
                        following.setText(Integer.toString(userinfo.getFollowing_num()));
                        follower.setText(Integer.toString(userinfo.getFollower_num()));
                        targetUId = snapshot.getKey();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        String targetUserId = targetUId;

        //현재 들어와있는 User의 팔로워목록에 나를 추가
        mDatabase.child(targetUserId).child("followers").push().setValue(currentUserId);
        //내 팔로잉 목록에 현재 들어와있는 유저 추가
        mDatabase.child(currentUserId).child("following").push().setValue(targetUserId);
        isFollowing = true;

        updateButton();
    }

    //언팔로우 메소드
    private void unfollow() {
        //언팔로우할 대상
        String targetUserId = targetUId;

        //들어와있는 User의 팔로워 목록에서 나를 삭제
        Query followerQuery = mDatabase.child(targetUserId).child("followers").orderByValue().equalTo(currentUserId);
        followerQuery.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        snapshot.getRef().removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                //예외 처리
            }
        });

        //내 팔로잉 목록에서 들어와있는 User 삭제
        Query followingQuery = mDatabase.child(currentUserId).child("following").orderByValue().equalTo(targetUserId);
        followingQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //예외처리
            }
        });
        mDatabase.child(currentUserId).child("following").child(targetUserId).removeValue();

        updateButton();
    }

    private void updateButton() {
        if (isFollowing) {
            follow_button.setText("언팔로우");
        } else {
            follow_button.setText("팔로우");
        }
    }
}