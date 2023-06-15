package com.example.spotnow.profile;

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
import com.bumptech.glide.request.RequestOptions;
import com.example.spotnow.R;
import com.example.spotnow.main.reportPopupFragment;
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
    ImageView userImage;

    String targetUId = "";

    Button report_button;

    private boolean isFollowing = false;

    public user_ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser(); // Get the current logged-in user information
        final String uid = currentUser.getUid(); // Save the user's UID

        user_name = rootView.findViewById(R.id.profile_name);
        introduce = rootView.findViewById(R.id.profile_introduce);
        progressBar = rootView.findViewById(R.id.progressBar);
        like_sport = rootView.findViewById(R.id.profile_like_sport);
        region = rootView.findViewById(R.id.profile_region);
        following = rootView.findViewById(R.id.following_num);
        follower = rootView.findViewById(R.id.follow_num);
        report_button = rootView.findViewById(R.id.report_button);
        userImage = rootView.findViewById(R.id.profile_image);

        report_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("name", selected_name);
                reportPopupFragment reportPopupFragment = new reportPopupFragment();
                reportPopupFragment.setArguments(args);
                reportPopupFragment.show(getFragmentManager(),"custom_report_dialog");
            }
        });

        if (getArguments() != null) {
            selected_name = getArguments().getString("selected_name"); // Get the name from the bundle
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserInfo userinfo = snapshot.getValue(UserInfo.class); // Save the user object that matches the selected user information
                    if (userinfo.getName().equals(selected_name)) { // Check if the names match
                        user_name.setText(userinfo.getName());
                        introduce.setText(userinfo.getIntroduce_self());
                        like_sport.setText(userinfo.getSport());
                        region.setText(userinfo.getRegion());
                        progressBar.setProgress(100 - userinfo.getReport_cnt() * 5);
                        following.setText(Integer.toString(userinfo.getFollowing_num()));
                        follower.setText(Integer.toString(userinfo.getFollower_num()));
                        targetUId = snapshot.getKey();
                        Glide.with(user_ProfileFragment.this)
                                .load(userinfo.getProfileImage())
                                .apply(RequestOptions.circleCropTransform())
                                .into(userImage);
                        checkFollowStatus();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        follow_button = rootView.findViewById(R.id.follow_button);

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

        return rootView;
    }

    private void checkFollowStatus() {
        Query query = mDatabase.child(currentUserId).child("following").orderByValue().equalTo(targetUId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isFollowing = dataSnapshot.exists();
                updateButton();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CheckFollowStatus","Error while checking follow status");
                // Handle error
            }
        });
    }

    private void follow() {
        String targetUserId = targetUId;

        // Add myself to the follower list of the current user
        mDatabase.child(targetUserId).child("followers").push().setValue(currentUserId);

        // Add the current user to my following list
        mDatabase.child(currentUserId).child("following").push().setValue(targetUserId);
        isFollowing = true;

        // Increase following_num for currentUserId
        DatabaseReference currentuserRef = mDatabase.child(currentUserId);
        currentuserRef.child("following_num").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int followingNum = dataSnapshot.getValue(Integer.class);
                    followingNum++;
                    currentuserRef.child("following_num").setValue(followingNum);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Increase follower_num for targetUserId
        DatabaseReference targetuserRef = mDatabase.child(targetUserId);
        targetuserRef.child("follower_num").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int followerNum = dataSnapshot.getValue(Integer.class);
                    followerNum++;
                    targetuserRef.child("follower_num").setValue(followerNum);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        updateButton();
    }

    private void unfollow() {
        String targetUserId = targetUId;

        // Remove myself from the follower list of the current user and decrease the follower count
        Query followerQuery = mDatabase.child(targetUserId).child("followers").orderByValue().equalTo(currentUserId);
        DatabaseReference targetuserRef = mDatabase.child(targetUserId);
        followerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle exception
                Log.e("Unfollow","OnCancelled");
            }
        });
        targetuserRef.child("follower_num").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int followerNum = dataSnapshot.getValue(Integer.class);
                    followerNum--;
                    targetuserRef.child("follower_num").setValue(followerNum);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Remove the current user from my following list and decrease the following count
        Query followingQuery = mDatabase.child(currentUserId).child("following").orderByValue().equalTo(targetUserId);
        DatabaseReference currentuserRef = mDatabase.child(currentUserId);
        followingQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle exception
            }
        });
        currentuserRef.child("following_num").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int followingNum = dataSnapshot.getValue(Integer.class);
                    followingNum--;
                    currentuserRef.child("following_num").setValue(followingNum);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        mDatabase.child(currentUserId).child("following").child(targetUserId).removeValue();
        isFollowing = false;
        updateButton();
    }

    private void updateButton() {
        if (isFollowing) {
            follow_button.setText("Unfollow");
        } else {
            follow_button.setText("Follow");
        }
    }
}