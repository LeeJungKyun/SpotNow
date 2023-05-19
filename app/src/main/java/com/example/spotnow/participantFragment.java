package com.example.spotnow;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class participantFragment extends Fragment {

    private Dialog participantDialog;
    private EditText comment;
    private Button writeParticipateInfoButton;
    private Button writeReportButton;
    private Button writeCommentButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.participant_activity_main, container, false);

        writeParticipateInfoButton = rootView.findViewById(R.id.writeParticipateInfo);
        writeReportButton = rootView.findViewById(R.id.writeReport);
        writeCommentButton = rootView.findViewById(R.id.writeComment);
        comment = rootView.findViewById(R.id.comment);

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

        return rootView;
    }

    private void showParticipantDialog() {
        participantDialog = new Dialog(requireContext());
        participantDialog.setContentView(R.layout.participant_dialog);

        participantDialog.show();
    }

    private void reporting() {
        participantDialog = new Dialog(requireContext());
        participantDialog.setContentView(R.layout.report);

        participantDialog.show();
    }

    private void sendComment() {
        String c = comment.getText().toString();

        // 여기서 c를 가져와서 DB에 저장한 뒤 댓글 창에 보여주기
        Toast.makeText(requireContext(), c, Toast.LENGTH_SHORT).show();

        comment.setText("");
    }
}