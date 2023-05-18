package com.example.spotnow;

import static com.example.spotnow.common.FirebaseManager.database;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spotnow.common.FirebaseManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileEditActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    Button submit_button;

    private EditText editTextPassword;
    private EditText editTextPassword_check;
    private EditText editTextdescription;
    private EditText editTextsports;
    private EditText editTextregion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);

        editTextPassword = (EditText) findViewById(R.id.profile_edit_pw);
        editTextPassword_check = (EditText) findViewById(R.id.profile_edit_pw_check);
        String newPwd;
        editTextdescription = (EditText) findViewById(R.id.profile_edit_description);
        String newDescription = editTextdescription.getText().toString();
        editTextsports = (EditText) findViewById(R.id.profile_edit_sport);
        String newSports = editTextsports.getText().toString();
        editTextregion = (EditText) findViewById(R.id.profile_edit_region);
        String newRegion = editTextregion.getText().toString();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        submit_button = (Button) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            //수정버튼 눌렀을때ㅣ
            @Override
            public void onClick(View v) {
                //비밀번호가 일치하지 않을때
                /*if (!editTextPassword.getText().toString().equals("") && !editTextPassword_check.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                } else {*/
                    firebaseAuth = FirebaseAuth.getInstance();
                    String currentUid = mAuth.getCurrentUser().getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference("users").child(currentUid);

                    Map<String, Object> userUpdates = new HashMap<>();

                    userUpdates.put("pw",editTextPassword.getText().toString());
                    userUpdates.put("introduce_self",editTextdescription.getText().toString());
                    userUpdates.put("sport",editTextsports.getText().toString());
                    userUpdates.put("region",editTextregion.getText().toString());

                    mDatabase.updateChildren(userUpdates);
                    finish();


            }
        });
    }
}