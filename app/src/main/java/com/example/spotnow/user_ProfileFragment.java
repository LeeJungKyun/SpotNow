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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class user_ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    UserInfo userInfo;

    public user_ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile, container, false);

        TextView name = rootView.findViewById(R.id.profile_name);
        TextView introduce = rootView.findViewById(R.id.profile_introduce);
        ProgressBar progressBar = rootView.findViewById(R.id.progressBar);
        TextView like_sport = rootView.findViewById(R.id.profile_like_sport);
        TextView region = rootView.findViewById(R.id.profile_region);
        Button follow_button = rootView.findViewById(R.id.follow_button);

        follow_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(true){
                    follow_button.setText("언팔로우");
                    follow_button.setBackgroundColor(Color.WHITE);
                    follow_button.setTextColor(Color.BLACK);
                }
                else{
                    follow_button.setText("팔로우");
                    follow_button.setBackgroundColor(Color.BLUE);
                    follow_button.setTextColor(Color.WHITE);
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
}
