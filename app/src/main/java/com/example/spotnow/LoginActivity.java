package com.example.spotnow;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kotlin.Function;

public class LoginActivity extends AppCompatActivity
{

    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseAuth firebaseAuth;
    Button sign_up_button;
    Button sign_in_button;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        FirebaseManager.init();


        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById(R.id.login_email);
        editTextPassword = (EditText) findViewById(R.id.login_pw);


        sign_in_button = (Button) findViewById(R.id.login_sign_in_button);
        sign_in_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!editTextEmail.getText().toString().equals("") && !editTextPassword.getText().toString().equals(""))
                {
                    loginUser(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }

                firebaseAuthListener = new FirebaseAuth.AuthStateListener()
                {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
                    {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null)
                        {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                        }
                    }
                };
            }
        });

        sign_up_button = findViewById(R.id.login_signup_button);
        sign_up_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

    }


    public void loginUser(String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    // 로그인 성공
                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                    firebaseAuth.addAuthStateListener(firebaseAuthListener);
                } else
                {
                    // 로그인 실패
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}