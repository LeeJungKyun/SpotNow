package com.example.spotnow.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spotnow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class participantFragment extends AppCompatActivity {

    private Dialog participantDialog;
    private Dialog reportingDialog;
    private EditText comment;
    private Button writeParticipateInfoButton;
    private Button writeReportButton;
    private Button writeCommentButton;
    private ImageView activityImageView;
    private TextView ownerName;
    private TextView titleTextView;
    private TextView contentTextView;
    private RecyclerView commentShow;
    private DatabaseReference mDatabase;
    private String ActivityId;
    private FirebaseAuth mAuth;
    private String UID;
    private String UserName;
    private long timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_activity_main);

        writeParticipateInfoButton = findViewById(R.id.writeParticipateInfo);
        writeReportButton = findViewById(R.id.writeReport);
        writeCommentButton = findViewById(R.id.writeComment);
        comment = findViewById(R.id.comment);
        activityImageView = findViewById(R.id.place_holder_image);
        titleTextView = findViewById(R.id.title);
        contentTextView = findViewById(R.id.bottom_text_view);
        ownerName = findViewById(R.id.ownerName);
        commentShow = findViewById(R.id.commentShow);
        commentShow.setLayoutManager(new LinearLayoutManager(this));

        timestamp = new Date().getTime();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            UID = currentUser.getUid();
        }
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    UserName = name;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 데이터 읽기를 취소한 경우 처리할 내용을 작성합니다.
            }
        });

        CommentAdapter adapter = new CommentAdapter();
        commentShow.setAdapter(adapter);


        // 인텐트에서 데이터 받아오기
        Intent intent = getIntent();
        if (intent != null) {
            String activityTitle = intent.getStringExtra("activityTitle");
            String activityContent = intent.getStringExtra("activityContent");
            String activityOwner = intent.getStringExtra("activityOwner");
            String activitySport = intent.getStringExtra("activitySport");
            String activityPeopleCnt = intent.getStringExtra("activityPeopleCnt");
            String activityStartTime = intent.getStringExtra("activityStartTime");
            String activityEndTime = intent.getStringExtra("activityEndTime");

            ownerName.setText(activityOwner);
            titleTextView.setText(activityTitle);
            contentTextView.setText("종목: "+activitySport+"\n\n"+"내용: "+activityContent+"\n\n"+"시작시간: " +"\n" +activityStartTime+"\n\n"+"종료시간: " +"\n"+activityEndTime+"\n\n"+"인원: "+activityPeopleCnt);
            contentTextView.setMovementMethod(new ScrollingMovementMethod());
            // Firebase에서 데이터 가져오기
            mDatabase = FirebaseDatabase.getInstance().getReference("activities");
            mDatabase.orderByChild("title").equalTo(activityTitle).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ActivityInfo activity = snapshot.getValue(ActivityInfo.class);
                        if (activity != null && activity.getContent().equals(activityContent)) {
                            // 원하는 데이터를 찾았을 경우
                            String activityId = snapshot.getKey();
                            ActivityId = activityId;
                            Toast.makeText(participantFragment.this, ActivityId,Toast.LENGTH_SHORT).show();
                            String imageUrl = activity.getImageUrl();

                            Glide.with(participantFragment.this).load(imageUrl).into(activityImageView);

                            Toast.makeText(participantFragment.this, "Activity ID: " + activityId, Toast.LENGTH_SHORT).show();
                            Toast.makeText(participantFragment.this, "Activity Title: " + activity.getTitle(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(participantFragment.this, "Activity Content: " + activity.getContent(), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // 실패했으
                    Toast.makeText(participantFragment.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
                }
            });

            DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("activities");
            commentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<CommentInfo> comments = new ArrayList<>();
                    for (DataSnapshot commentSnapshot : snapshot.child(ActivityId).child("comment").getChildren()) {
                        String commentText = commentSnapshot.child("comment").getValue(String.class);
                        String userName = commentSnapshot.child("userName").getValue(String.class);
                        long TimeStamp = new Date().getTime();

                        CommentInfo commentInfo = new CommentInfo(userName, commentText, TimeStamp);
                        comments.add(commentInfo);

                        adapter.setCommentList(comments);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // 데이터 읽기를 취소한 경우 처리할 내용을 작성합니다.
                }
            });
            commentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<CommentInfo> comments = new ArrayList<>();
                    for(DataSnapshot commentSnapshot : snapshot.child(ActivityId).child("comment").getChildren()){
                        String commentText = commentSnapshot.child("comment").getValue(String.class);
                        String userName = commentSnapshot.child("userName").getValue(String.class);
                        long TimeStamp = new Date().getTime();

                        CommentInfo commentInfo = new CommentInfo(userName, commentText, TimeStamp);
                        comments.add(commentInfo);

                        adapter.setCommentList(comments);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        writeParticipateInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParticipantDialog(UserName, timestamp);
            }
        });

        writeReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reporting();
            }
        });

        writeCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = comment.getText().toString();
                sendComment(UserName, c, timestamp);
                comment.setText("");

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });
    }

    private void showParticipantDialog(String userName, long TimeStamp) {
        participantDialog = new Dialog(this);
        participantDialog.setContentView(R.layout.participant_dialog);

        EditText dialogEditText = participantDialog.findViewById(R.id.DiaText);
        Button dialogButton = participantDialog.findViewById(R.id.DiaSubmitButton);

        dialogButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Dialog에서 작성한 문자열 가져오기
                String dialogText = dialogEditText.getText().toString();
                String CommentText = "[참여]" + dialogText;
                // 가져온 문자열 사용하기
                sendComment(userName, CommentText, TimeStamp);

                // Dialog 닫기
                participantDialog.dismiss();
            }
        });

        participantDialog.show();
    }

    private void reporting() {
        reportingDialog = new Dialog(this);
        reportingDialog.setContentView(R.layout.report);

        reportingDialog.show();
    }

    private void sendComment(String UID, String c, long timestamp) {
        if (c.trim().isEmpty()) {
            // 댓글이 비어있는 경우 처리
            Toast.makeText(this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        mDatabase = FirebaseDatabase.getInstance().getReference().child("activities").child(ActivityId).child("comment");
        CommentInfo commentInfo = new CommentInfo(UID, c, timestamp);

        Toast.makeText(this, ActivityId, Toast.LENGTH_SHORT).show();

        String commentId = mDatabase.push().getKey();
        mDatabase.child(commentId).setValue(commentInfo);
        Toast.makeText(this, c, Toast.LENGTH_SHORT).show();
    }
}
