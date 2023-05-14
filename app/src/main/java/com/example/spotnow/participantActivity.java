package com.example.spotnow;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class participantActivity extends AppCompatActivity {

    private Dialog participantDialog;
    private EditText Comment;
    private Button writeParticipateInfoButton;
    private Button writeReportButton;
    private Button writeCommentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_activity_main);

        writeParticipateInfoButton = findViewById(R.id.writeParticipateInfo);
        writeReportButton = findViewById(R.id.writeReport);
        writeCommentButton = findViewById(R.id.writeComment);
        Comment = findViewById(R.id.comment);
        writeParticipateInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showParticipantDialog(); }
        });
        writeReportButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){reporting();}
        });
        writeCommentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){sendComment();}
        });
    }
    private void showParticipantDialog() {
        participantDialog = new Dialog(participantActivity.this);
        participantDialog.setContentView(R.layout.participant_dialog);

        participantDialog.show();
    }
    private void reporting(){
        participantDialog = new Dialog(participantActivity.this);
        participantDialog.setContentView(R.layout.report);

        participantDialog.show();
    }
    private void sendComment(){
        String C = Comment.getText().toString();

        //여기서 C가져다가 db에 저장했다가 댓글창에 보여주기
        Toast.makeText(getApplicationContext(), C, Toast.LENGTH_SHORT).show();

        Comment.setText("");
    }
}