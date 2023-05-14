package com.example.spotnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity
{

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.sign_up_email);
        editTextPassword = (EditText) findViewById(R.id.sign_up_pw);
        editTextPassword_re = (EditText) findViewById(R.id.sign_up_pw_check);
        editTextName = (EditText) findViewById(R.id.sign_up_name);
        editTextSport = (EditText) findViewById(R.id.sign_up_sport);
        editTextRegion = (EditText) findViewById(R.id.sign_up_region);

        buttonJoin = (Button) findViewById(R.id.btn_join);

        buttonJoin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!editTextPassword_re.getText().toString().equals(editTextPassword.getText().toString())){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                }

                if (!editTextEmail.getText().toString().equals("") && !editTextPassword.getText().toString().equals(""))
                {
                    // 이메일과 비밀번호가 공백이 아닌 경우
                    createUser(editTextEmail.getText().toString(), editTextPassword.getText().toString(), editTextName.getText().toString(),
                            editTextSport.getText().toString(),editTextRegion.getText().toString());
                } else
                {
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(getApplicationContext(), "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createUser(String email, String password, String name, String sport, String region)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        UserInfo user = new UserInfo(name, email, sport, region,0,"",0,0);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // 회원가입 성공시
                            final String uid = task.getResult().getUser().getUid(); //회원가입 완료된 유저의 uid 가져오기

                            mDatabase.child("users").child(uid).setValue(user); // 회원별 uid를 부모로 유저 정보 DB에 저장

                            mDatabase.child("users").child(uid).child("following").push().setValue(user.getName());
                            mDatabase.child("users").child(uid).child("follower").push().setValue(user.getName());

                            Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            // 회원가입 실패한 경우
                            Toast.makeText(getApplicationContext(), "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}