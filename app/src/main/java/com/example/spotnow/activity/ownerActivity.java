package com.example.spotnow.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.UUID;

public class ownerActivity extends AppCompatActivity {

    private EditText Comment;
    private Button modifyActivityButton;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activtiy_main);

        textView = findViewById(R.id.text_view);
        modifyActivityButton = findViewById(R.id.modifyActivity);
        placeHolderImage = findViewById(R.id.place_holder_image);
        activityTypeSpinner = findViewById(R.id.sport_spinner);
        monthSpinner = findViewById(R.id.month_spinner);
        daySpinner = findViewById(R.id.day_spinner);
        hourSpinner = findViewById(R.id.hour_spinner);
        minuteSpinner = findViewById(R.id.minute_spinner);
        endMonthSpinner = findViewById(R.id.end_month_spinner); // 추가된 부분
        endDaySpinner = findViewById(R.id.end_day_spinner); // 추가된 부분
        endHourSpinner = findViewById(R.id.end_hour_spinner); // 추가된 부분
        endMinuteSpinner = findViewById(R.id.end_minute_spinner); // 추가된 부분
        participantCountEditText = findViewById(R.id.participant_count_edit_text);
        contentEditText = findViewById(R.id.content_edit_text);
        title = findViewById(R.id.title);

        mStorage = FirebaseStorage.getInstance().getReference();

//        readParticipantListButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callParticipantList();
//            }
//        });

//        writeCommentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendComment();
//            }
//        });

        placeHolderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker(); // 앨벤여는거 함수 밑에 있음
            }
        });

        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(this, R.array.activity_types, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityTypeSpinner.setAdapter(activityAdapter);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(hourAdapter);

        ArrayAdapter<CharSequence> minuteAdapter = ArrayAdapter.createFromResource(this, R.array.minutes, android.R.layout.simple_spinner_item);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minuteSpinner.setAdapter(minuteAdapter);

        // Initialize end time spinners
        ArrayAdapter<CharSequence> endMonthAdapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        endMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endMonthSpinner.setAdapter(endMonthAdapter);

        ArrayAdapter<CharSequence> endDayAdapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        endDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endDaySpinner.setAdapter(endDayAdapter);

        ArrayAdapter<CharSequence> endHourAdapter = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        endHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endHourSpinner.setAdapter(endHourAdapter);

        ArrayAdapter<CharSequence> endMinuteAdapter = ArrayAdapter.createFromResource(this, R.array.minutes, android.R.layout.simple_spinner_item);
        endMinuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endMinuteSpinner.setAdapter(endMinuteAdapter);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser(); // 현재 로그인 한 유저 정보 반환

        final String uid = currentUser.getUid(); // 유저의 uid 저장

        ArrayList<String> path = new ArrayList<>();
        path.add(uid);
        path.add("name");
        FirebaseManager.GetReferencePath("users", path).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                Log.d("firebase", String.valueOf(task.getResult().getValue()));
                activityOwner=String.valueOf(task.getResult().getValue());
            }
        }
    });


        modifyActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {uploadActivity();}
        });

    }

    public void uploadActivity(){
        String TiTle = title.getText().toString();
        String sport = activityTypeSpinner.getSelectedItem().toString();

        String month = monthSpinner.getSelectedItem().toString();
        String day = daySpinner.getSelectedItem().toString();
        String hour = hourSpinner.getSelectedItem().toString();
        String minute = minuteSpinner.getSelectedItem().toString();
        String startTime = month + "-" + day + "-" + hour + ":" + minute;

        String endMonth = endMonthSpinner.getSelectedItem().toString(); // 추가된 부분
        String endDay = endDaySpinner.getSelectedItem().toString(); // 추가된 부분
        String endHour = endHourSpinner.getSelectedItem().toString(); // 추가된 부분
        String endMinute = endMinuteSpinner.getSelectedItem().toString(); // 추가된 부분
        String endTime = endMonth + "-" + endDay + "-" + endHour + ":" + endMinute; // 추가된 부분

        String participantCount = participantCountEditText.getText().toString();
        String content = contentEditText.getText().toString();

        // 전체값 팔수 입력
        if (TiTle.isEmpty() || sport.equals("*선택*") || startTime.isEmpty() || endTime.isEmpty() || participantCount.isEmpty() || content.isEmpty()) {
            Toast.makeText(ownerActivity.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

                /* 날짜 형식 확인
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                sdf.setLenient(false);
                try {
                    sdf.parse(startTime);
                    sdf.parse(endTime);
                } catch (ParseException e) {
                    Toast.makeText(ownerActivity.this, "날짜 형식이 잘못되었습니다. 올바른 형식은 'YYYY-MM-DD-HH:MM'입니다.", Toast.LENGTH_SHORT).show();
                    return;
                } catch (java.text.ParseException e) {
                    throw new RuntimeException(e);
                }
                */

        // HomeFragment로부터 spotID를 받아옴
        Intent getIntent = getIntent();
        long spotID = getIntent.getLongExtra("spotID", 0);
        String spotAddress = getIntent.getStringExtra("spotAddress");

        // 이미지 업로드 후 액티비티 저장
        uploadImageAndCreateActivity(TiTle, sport, startTime, endTime, participantCount, content, spotID,activityOwner);

        finish();
    }

    private void sendComment() {
        String comment = Comment.getText().toString();
        //firebase
        // activity id를 찾아서
        // user id 와 comment를 (id, comment) firebase에 저장
        Toast.makeText(getApplicationContext(), comment, Toast.LENGTH_SHORT).show();

        Comment.setText("");
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            placeHolderImage.setImageURI(selectedImageUri);
            textView.setVisibility(View.GONE); // 이미지가 선택되면 TextView를 숨김
        } else {
            textView.setVisibility(View.VISIBLE); // 이미지가 선택되지 않으면 TextView를 보이게 함
        }
    }

    private void uploadImageAndCreateActivity(String title, String sport, String startTime, String endTime, String peopleCnt, String content, long spotID,String activityOwner) {
        if (selectedImageUri != null) {
            StorageReference imageRef = mStorage.child("images/" + UUID.randomUUID().toString());

            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // 이미지 업로드 성공
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // 이미지의 다운로드 URL을 가져옴
                                    String imageUrl = uri.toString();

                                    // 액티비티 정보와 이미지 URL을 함께 저장
                                    createActivity(title, sport, startTime, endTime, peopleCnt, content, spotID, imageUrl,activityOwner);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // 이미지의 다운로드 URL을 가져오는데 실패
                                    Toast.makeText(ownerActivity.this, "이미지 업로드에 실패하였습니다.(DownloadERROR)", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // 이미지 업로드 실패
                            Toast.makeText(ownerActivity.this, "이미지 업로드에 실패하였습니다.(UPLOADERROR)", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // 이미지가 선택되지 않았을 경우에도 액티비티 저장
            createActivity(title, sport, startTime, endTime, peopleCnt, content, spotID, null,activityOwner);
        }
    }

    private void createActivity(String title, String sport, String startTime, String endTime, String peopleCnt, String content, long spotID, String imageUrl,String activityOwner) {
        mDatabase = FirebaseDatabase.getInstance().getReference();


        ActivityInfo activity = new ActivityInfo(title, sport, content, startTime, endTime, peopleCnt, spotID, imageUrl, activityOwner);
        Toast.makeText(getApplicationContext(), activity.spotID + activity.title, Toast.LENGTH_SHORT).show();
        mDatabase.child("activities").push().setValue(activity);
    }


}
