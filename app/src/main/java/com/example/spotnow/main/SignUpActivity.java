package com.example.spotnow.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spotnow.R;
import com.example.spotnow.profile.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPassword_re;
    private EditText editTextName;
    private EditText editTextSport;
    private EditText editTextRegion;
    private Button buttonJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        // Assigning EditText fields to variables
        editTextEmail = (EditText) findViewById(R.id.sign_up_email);
        editTextPassword = (EditText) findViewById(R.id.sign_up_pw);
        editTextPassword_re = (EditText) findViewById(R.id.sign_up_pw_check);
        editTextName = (EditText) findViewById(R.id.sign_up_name);
        editTextSport = (EditText) findViewById(R.id.sign_up_sport);
        editTextRegion = (EditText) findViewById(R.id.sign_up_region);

        buttonJoin = (Button) findViewById(R.id.btn_join);

        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if passwords match
                if (!editTextPassword_re.getText().toString().equals(editTextPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_LONG).show();
                }

                if (!editTextEmail.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")) {
                    // If email and password are not empty
                    createUser(
                            editTextEmail.getText().toString(),
                            editTextPassword.getText().toString(),
                            editTextName.getText().toString(),
                            editTextSport.getText().toString(),
                            editTextRegion.getText().toString(),
                            editTextPassword.getText().toString()
                    );
                } else {
                    // If email or password is empty
                    Toast.makeText(getApplicationContext(), "Please enter an email and password.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createUser(String email, String password, String name, String sport, String region, String pw) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        UserInfo user = new UserInfo(name, email, sport, region, 0, "", 0, 0, pw, "");

        // Create user using Firebase Auth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful
                            final String uid = task.getResult().getUser().getUid(); // Get the UID of the registered user
                            mDatabase.child("users").child(uid).setValue(user); // Store user information in the database with UID as the parent

                            Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // Registration failed
                            Toast.makeText(getApplicationContext(), "An account with this email already exists.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
