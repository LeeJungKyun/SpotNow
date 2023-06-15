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
    private Spinner endMonthSpinner;
    private Spinner endDaySpinner;
    private Spinner endHourSpinner;
    private Spinner endMinuteSpinner;

    private EditText participantCountEditText;
    private EditText contentEditText;
    private EditText title;

    private TextView textView;

    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri; // Remember Selected Image's URI
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
        endMonthSpinner = findViewById(R.id.end_month_spinner);
        endDaySpinner = findViewById(R.id.end_day_spinner);
        endHourSpinner = findViewById(R.id.end_hour_spinner);
        endMinuteSpinner = findViewById(R.id.end_minute_spinner);
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
                openImagePicker(); // function to open album
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
        FirebaseUser currentUser = mAuth.getCurrentUser(); // Return current user's information

        final String uid = currentUser.getUid();

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

        // Check if all the required values are entered
        if (TiTle.isEmpty() || sport.equals("*선택*") || startTime.isEmpty() || endTime.isEmpty() || participantCount.isEmpty() || content.isEmpty()) {
            return;
        }

                /* Check date format
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                sdf.setLenient(false);
                try {
                    sdf.parse(startTime);
                    sdf.parse(endTime);
                } catch (ParseException e) {
                    return;
                } catch (java.text.ParseException e) {
                    throw new RuntimeException(e);
                }
                */

        // Get spotID from HomeFragment
        Intent getIntent = getIntent();
        long spotID = getIntent.getLongExtra("spotID", 0);
        String spotAddress = getIntent.getStringExtra("spotAddress");

        // Upload image and save the activity
        uploadImageAndCreateActivity(TiTle, sport, startTime, endTime, participantCount, content, spotID,activityOwner);

        finish();
    }

    private void sendComment() {
        String comment = Comment.getText().toString();
        // Firebase
        // Find activity id
        // Save user id and comment in Firebase (id, comment)

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
            textView.setVisibility(View.GONE); // Hide TextView when an image is selected
        } else {
            textView.setVisibility(View.VISIBLE); // Show TextView when no image is selected
        }
    }

    private void uploadImageAndCreateActivity(String title, String sport, String startTime, String endTime, String peopleCnt, String content, long spotID,String activityOwner) {
        if (selectedImageUri != null) {
            StorageReference imageRef = mStorage.child("images/" + UUID.randomUUID().toString());

            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image upload successful
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Get the download URL of the image
                                    String imageUrl = uri.toString();

                                    // Save activity information with the image URL
                                    createActivity(title, sport, startTime, endTime, peopleCnt, content, spotID, imageUrl,activityOwner);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to get the download URL of the image

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Image upload failed

                        }
                    });
        } else {
            // Save activity even if no image is selected
            createActivity(title, sport, startTime, endTime, peopleCnt, content, spotID, null,activityOwner);
        }
    }

    private void createActivity(String title, String sport, String startTime, String endTime, String peopleCnt, String content, long spotID, String imageUrl,String activityOwner) {
        mDatabase = FirebaseDatabase.getInstance().getReference();


        ActivityInfo activity = new ActivityInfo(title, sport, content, startTime, endTime, peopleCnt, spotID, imageUrl, activityOwner);
        mDatabase.child("activities").push().setValue(activity);
    }


}
