package com.example.spotnow;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.spotnow.ActivityInfo;
import com.example.spotnow.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class participantFragment extends AppCompatActivity {

    private Dialog participantDialog;
    private EditText comment;
    private Button writeParticipateInfoButton;
    private Button writeReportButton;
    private Button writeCommentButton;
    private ImageView activityImageView;
    private TextView titleTextView;
    private TextView contentTextView;

    private DatabaseReference mDatabase;

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

        // 인텐트에서 데이터 받아오기
        Intent intent = getIntent();
        if (intent != null) {
            String activityTitle = intent.getStringExtra("activityTitle");
            String activityContent = intent.getStringExtra("activityContent");

            titleTextView.setText(activityTitle);
            contentTextView.setText(activityContent);

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
        }

        writeParticipateInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParticipantDialog();
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
                sendComment();
            }
        });
    }

    private void showParticipantDialog() {
        participantDialog = new Dialog(this);
        participantDialog.setContentView(R.layout.participant_dialog);

        participantDialog.show();
    }

    private void reporting() {
        participantDialog = new Dialog(this);
        participantDialog.setContentView(R.layout.report);

        participantDialog.show();
    }

    private void sendComment() {
        String c = comment.getText().toString();

        // 여기서 c를 가져와서 DB에 저장한 뒤 댓글 창에 보여주기
        Toast.makeText(this, c, Toast.LENGTH_SHORT).show();

        comment.setText("");
    }
}
