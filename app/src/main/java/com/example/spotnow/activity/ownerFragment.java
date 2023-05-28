package com.example.spotnow.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotnow.R;
import com.example.spotnow.common.FirebaseManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ownerFragment extends Fragment {

    private Dialog deleteCheckDialog;
    private EditText Comment;
    private Button readParticipantListButton;
    private Button deleteActivityButton;
    private Button modifyActivityButton;
    private Button writeCommentButton;
    private ImageView placeHolderImage;
    private Spinner activityTypeSpinner;
    private Spinner monthSpinner;
    private Spinner daySpinner;
    private Spinner hourSpinner;
    private Spinner minuteSpinner;
    private Spinner endMonthSpinner; // 추가된 부분
    private Spinner endDaySpinner; // 추가된 부분
    private Spinner endHourSpinner; // 추가된 부분
    private Spinner endMinuteSpinner; // 추가된 부분

    private EditText participantCountEditText;
    private EditText contentEditText;
    private EditText title;
    private TextView textView;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri; // 선택된 이미지의 Uri를 저장하는 변수
    private String activityOwner;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.owner_activtiy_main, container, false);

        textView = rootView.findViewById(R.id.text_view);
//        readParticipantListButton = rootView.findViewById(R.id.readParticipantList);
//        deleteActivityButton = rootView.findViewById(R.id.deleteActivity);
        modifyActivityButton = rootView.findViewById(R.id.modifyActivity);
//        writeCommentButton = rootView.findViewById(R.id.writeComment);
//        Comment = rootView.findViewById(R.id.comment);
        placeHolderImage = rootView.findViewById(R.id.place_holder_image);
        activityTypeSpinner = rootView.findViewById(R.id.sport_spinner);
        monthSpinner = rootView.findViewById(R.id.month_spinner);
        daySpinner = rootView.findViewById(R.id.day_spinner);
        hourSpinner = rootView.findViewById(R.id.hour_spinner);
        minuteSpinner = rootView.findViewById(R.id.minute_spinner);
        endMonthSpinner = rootView.findViewById(R.id.end_month_spinner); // 추가된 부분
        endDaySpinner = rootView.findViewById(R.id.end_day_spinner); // 추가된 부분
        endHourSpinner = rootView.findViewById(R.id.end_hour_spinner); // 추가된 부분
        endMinuteSpinner = rootView.findViewById(R.id.end_minute_spinner); // 추가된 부분

        participantCountEditText = rootView.findViewById(R.id.participant_count_edit_text);
        contentEditText = rootView.findViewById(R.id.content_edit_text);
        title = rootView.findViewById(R.id.title);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            activityOwner = currentUser.getUid();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        setupActivityTypeSpinner();
        setupDateTimeSpinners();

        placeHolderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        readParticipantListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 참가자 목록을 읽는 동작 수행
                readParticipantList();
            }
        });

        deleteActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 활동 삭제를 수행
                showDeleteCheckDialog();
            }
        });

        modifyActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 활동 수정을 수행
                modifyActivity();
            }
        });

        writeCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 댓글 작성을 수행
                sendComment();
            }
        });

        return rootView;
    }

    private void setupActivityTypeSpinner() {
        // 스피너에 표시될 아이템 목록 설정
        ArrayList<String> activityTypes = new ArrayList<>();
        activityTypes.add("Type1");
        activityTypes.add("Type2");
        activityTypes.add("Type3");

        // 어댑터를 생성하여 스피너에 연결
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, activityTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityTypeSpinner.setAdapter(adapter);
    }

    private void setupDateTimeSpinners() {
        // 월 스피너에 표시될 아이템 목록 설정
        ArrayList<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(String.valueOf(i));
        }

        // 일 스피너에 표시될 아이템 목록 설정
        ArrayList<String> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            days.add(String.valueOf(i));
        }

        // 시간 스피너에 표시될 아이템 목록 설정
        ArrayList<String> hours = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            hours.add(String.format("%02d", i));
        }

        // 분 스피너에 표시될 아이템 목록 설정
        ArrayList<String> minutes = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            minutes.add(String.format("%02d", i));
        }

        // 어댑터를 생성하여 스피너에 연결
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, months);
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, days);
        ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, hours);
        ArrayAdapter<String> minuteAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, minutes);

        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthSpinner.setAdapter(monthAdapter);
        daySpinner.setAdapter(dayAdapter);
        hourSpinner.setAdapter(hourAdapter);
        minuteSpinner.setAdapter(minuteAdapter);

        endMonthSpinner.setAdapter(monthAdapter); // 추가된 부분
        endDaySpinner.setAdapter(dayAdapter); // 추가된 부분
        endHourSpinner.setAdapter(hourAdapter); // 추가된 부분
        endMinuteSpinner.setAdapter(minuteAdapter); // 추가된 부분
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            placeHolderImage.setImageURI(selectedImageUri);
        }
    }

    private void readParticipantList() {
        // 참가자 목록을 읽는 동작 수행하는 메서드
        // 구현 내용 생략
    }

    private void showDeleteCheckDialog() {
        deleteCheckDialog = new Dialog(requireContext());
        deleteCheckDialog.setContentView(R.layout.deletecheck_dialog);

        Button deleteButton = deleteCheckDialog.findViewById(R.id.Yes);
        Button cancelButton = deleteCheckDialog.findViewById(R.id.No);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 활동 삭제를 수행하는 메서드 호출
                deleteActivity();
                deleteCheckDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCheckDialog.dismiss();
            }
        });

        deleteCheckDialog.show();
    }

    private void deleteActivity() {
        // 활동 삭제를 수행하는 메서드
        // 구현 내용 생략
    }

    private void modifyActivity() {
        // 활동 수정을 수행하는 메서드
        // 구현 내용 생략
    }

    private void sendComment() {
        String c = Comment.getText().toString();
        //firebase
        // activity id를 찾아서
        // user id 와 comment를 (id, comment) firebase에 저장
        // 여기서 c를 가져와서 DB에 저장한 뒤 댓글 창에 보여주기

//        Toast.makeText(getActivity().getApplicationContext(), c, Toast.LENGTH_SHORT).show();

        Comment.setText("");
    }
}