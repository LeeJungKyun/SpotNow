package com.example.spotnow;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.util.FusedLocationSource;

public class ActivityFragment extends Fragment{
    private Button creatorButton;
    private Button participantButton;


    public ActivityFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.temp, container, false);

        creatorButton = rootView.findViewById(R.id.creator_button);
        participantButton = rootView.findViewById(R.id.participant_button);

        creatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ownerActivity.class);
                startActivity(intent);
            }
        });
        participantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), participantActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

}


/*
public class ActivityFragment extends Fragment {
    Button creatorButton = findViewById(R.id.creator_button);
    Button participantButton = findViewById(R.id.participant_button);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp); // 여기서 activity_temp는 TempActivity의 레이아웃 파일입니다.



        creatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityFragment.this, ownerActivity.class);
                startActivity(intent);
            }
        });

        participantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityFragment.this, participantActivity.class);
                startActivity(intent);
            }
        });
    }
}
*/
