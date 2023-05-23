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

        //파이어베이스에서 정보 가져오기
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUid = firebaseAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(currentUid);

        editTextPassword = (EditText) findViewById(R.id.profile_edit_pw);
        editTextPassword_check = (EditText) findViewById(R.id.profile_edit_pw_check);
        String newPwd = editTextPassword_check.getText().toString();
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
            //수정버튼 눌렀을때
            @Override
            public void onClick(View v) {
                //비밀번호가 일치하지 않을때
                if (!editTextPassword.getText().toString().equals(editTextPassword_check.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                } else {

                    Map<String, Object> userUpdates = new HashMap<>();

                    userUpdates.put("pw", newPwd);
                    userUpdates.put("introduce_self", newDescription);
                    userUpdates.put("sport", newSports);
                    userUpdates.put("region", newRegion);

                    mDatabase.updateChildren(userUpdates);
                    finish();


                }
            }
        });
    }
}