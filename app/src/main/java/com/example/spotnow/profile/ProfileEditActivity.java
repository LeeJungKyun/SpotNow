package com.example.spotnow.profile;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spotnow.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ProfileEditActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Button submit_button;

    private EditText editTextPassword;
    private EditText editTextPassword_check;
    private EditText editTextdescription;
    private EditText editTextsports;
    private EditText editTextregion;

    private ImageView profileImage;
    private Uri profileImageUri;

    String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);

        profileImageUri = null;

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Get the current user's ID
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUid = currentUser.getUid();
        }

        // Initialize views
        editTextPassword = findViewById(R.id.profile_edit_pw);
        editTextPassword_check = findViewById(R.id.profile_edit_pw_check);
        editTextdescription = findViewById(R.id.profile_edit_description);
        editTextsports = findViewById(R.id.profile_edit_sport);
        editTextregion = findViewById(R.id.profile_edit_region);
        profileImage = findViewById(R.id.profile_image);

        // Load the profile image from Firebase Database
        mDatabase.child(currentUid).child("profileImage").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    ImageLoadTask imageLoadTask = new ImageLoadTask();
                    imageLoadTask.execute((String) task.getResult().getValue());
                }
            }
        });

        submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPwd = editTextPassword.getText().toString();
                // Check if the entered passwords match
                if (!editTextPassword.getText().toString().equals(editTextPassword_check.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                } else {
                    String newDescription = editTextdescription.getText().toString();
                    String newSports = editTextsports.getText().toString();
                    String newRegion = editTextregion.getText().toString();

                    Map<String, Object> userUpdates = new HashMap<>();
                    // Update the changed information
                    userUpdates.put("pw", newPwd);
                    userUpdates.put("introduce_self", newDescription);
                    userUpdates.put("sport", newSports);
                    userUpdates.put("region", newRegion);

                    mDatabase.child(currentUid).updateChildren(userUpdates);

                    if (profileImageUri != null)
                        uploadFile(profileImageUri);

                    finish();
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile(Uri imageUri) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("images/profiles/");
        if (imageUri != null) {
            StorageReference fileReference = FirebaseStorage.getInstance().getReference("images/profiles/")
                    .child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
                        downloadUrlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String uploadId = databaseReference.push().getKey();

                                    // Update the profile image URL in Firebase Database
                                    FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(currentUid).child("profileImage").setValue(downloadUri.toString());

                                } else {
                                    // Handle unsuccessful upload
                                }
                            }
                        });
                    })
                    .addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        // Progress listener can be added here if needed
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Get the selected image
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);
            profileImageUri = imageUri;
        }
    }

    private class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            String imageUrl = strings[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                profileImage.setImageBitmap(bitmap);
            }
        }
    }
}