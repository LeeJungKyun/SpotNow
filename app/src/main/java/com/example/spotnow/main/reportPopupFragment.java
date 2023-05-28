package com.example.spotnow.main;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.spotnow.R;
import com.example.spotnow.profile.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class reportPopupFragment extends DialogFragment
{
    private DatabaseReference mDatabase;

    Button yes_button;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_popup, container, false);

        Bundle mArgs = getArguments();
        String userName = mArgs.getString("name");

        yes_button = view.findViewById(R.id.yesButton);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        yes_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserInfo userinfo;
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            UserInfo userinfo = snapshot.getValue(UserInfo.class); // 선택된 유저 정보와 일치하는 유저 객체 저장
                            if(userinfo.getName().equals(userName)) // 똑같은 이름인지 돌면서 확인
                            {
                                String userUid = snapshot.getKey();
                                mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userUid);

                                Map<String, Object> userUpdates = new HashMap<>();

                                userUpdates.put("report_cnt", userinfo.getReport_cnt() + 1);
                                mDatabase.updateChildren(userUpdates);
                                dismiss();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        // 다이얼로그 UI 요소에 대한 작업 수행

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 다이얼로그 UI 요소 초기화 및 이벤트 처리
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        return dialog;
    }
}
