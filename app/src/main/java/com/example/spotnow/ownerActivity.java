package com.example.spotnow;

import android.app.Dialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotnow.main.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class ownerActivity extends AppCompatActivity {

    private Dialog deleteCheckDialog;
    private EditText Comment;
    private Button readParticipantListButton;
    private Button deleteActivityButton;
    private Button modifyActivityButton;
    private Button writeCommentButton;
    private ImageView placeHolderImage;
    private Spinner activityTypeSpinner;

    private EditText startTimeEditText;
    private EditText endTimeEditText;
    private EditText participantCountEditText;
    private EditText contentEditText;
    private EditText title;

    private DatabaseReference mDatabase;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activtiy_main);

        readParticipantListButton = findViewById(R.id.readParticipantList);
        deleteActivityButton = findViewById(R.id.deleteActivity);
        modifyActivityButton = findViewById(R.id.modifyActivity);
        writeCommentButton = findViewById(R.id.writeComment);
        Comment = findViewById(R.id.comment);
        placeHolderImage = findViewById(R.id.place_holder_image);
        activityTypeSpinner = findViewById(R.id.sport_spinner);
        startTimeEditText = findViewById(R.id.start_time_edit_text);
        endTimeEditText = findViewById(R.id.end_time_edit_text);
        participantCountEditText = findViewById(R.id.participant_count_edit_text);
        contentEditText = findViewById(R.id.content_edit_text);
        title = findViewById(R.id.title);

        readParticipantListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callParticipantList();
            }
        });

        writeCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });

        placeHolderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker(); // 앨밤여는거 함수 밑에 있음
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.activity_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityTypeSpinner.setAdapter(adapter);

        activityTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedActivityType = parent.getItemAtPosition(position).toString();
                Toast.makeText(ownerActivity.this, "Selected Activity Type: " + selectedActivityType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*중요요요요요요요*/
        modifyActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String TiTle = title.getText().toString();
                String sport = activityTypeSpinner.getSelectedItem().toString();
                String startTime = startTimeEditText.getText().toString();
                String endTime = endTimeEditText.getText().toString();
                String participantCount = participantCountEditText.getText().toString();
                String content = contentEditText.getText().toString();

                // 전체값 팔수 입력
                if (TiTle.isEmpty() || sport == "*선택*" || startTime.isEmpty() || endTime.isEmpty() || participantCount.isEmpty() || content.isEmpty()) {
                    Toast.makeText(ownerActivity.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 날짜 형식 확인해준다잉
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                sdf.setLenient(false);
                try {
                    sdf.parse(startTime);
                    sdf.parse(endTime);
                } catch (ParseException e) {
                    Toast.makeText(ownerActivity.this, "날짜 형식이 잘못되었습니다. 올바른 형식은 'YYYYMMDDHHMM'입니다.", Toast.LENGTH_SHORT).show();
                    return;
                } catch (java.text.ParseException e) {
                    throw new RuntimeException(e);
                }

                //HomeFragment로부터 spotID를 받아옴
                Intent getIntent = getIntent();
                long spotID = getIntent.getLongExtra("spotID", 0);


                createActivity(TiTle, sport, startTime, endTime, participantCount,spotID, content);

                finish();
            }
        });

    }


    private void callParticipantList() {
        // 대기 목록 호출
        // 이부분은 아마 등록하는 부분에서는 지워도 될듯 혹시 몰라 남겨놓음
    }

    private void sendComment() {
        String comment = Comment.getText().toString();
        // 이부분은 아마 등록하는 부분에서는 지워도 될듯 혹시 몰라 남겨놓음
        // 여기서 comment를 가져와서 DB에 저장하고 댓글창에 보여주기
        Toast.makeText(getApplicationContext(), comment, Toast.LENGTH_SHORT).show();

        Comment.setText("");
    }

    private void openImagePicker() { // 여기 저 이미지 뷰 클릭하면 핸드폰에 저장되어있는 앨범 열기
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 이미지 클릭하면 이미지 뷰에 넣기
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            placeHolderImage.setImageURI(imageUri);
        }
    }

    //액티비티를 파이어베이스에 저장
    private void createActivity(String title, String sport, String startTime, String endTime, String peopleCnt, long spotID, String content) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ActivityInfo activity = new ActivityInfo(title, sport, content, startTime, endTime, peopleCnt, spotID, "unknown");
        Toast.makeText(getApplicationContext(), activity.spotID+activity.title, Toast.LENGTH_SHORT).show();
        mDatabase.child("activities").push().setValue(activity);

    }
}
