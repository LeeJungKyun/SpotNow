/*package com.example.spotnow;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.spotnow.main.LoginActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivityTest {

    @Mock
    private FirebaseAuth firebaseAuth;

    @Mock
    private Task<AuthResult> successfulTask;

    @Mock
    private Task<AuthResult> failedTask;

    private LoginActivity loginActivity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        loginActivity = new LoginActivity();
        loginActivity.firebaseAuth = firebaseAuth;
    }

    @Test
    public void testLoginUser_withValidCredentials() {
        Context context = ApplicationProvider.getApplicationContext();
        // Mocking the signInWithEmailAndPassword method to return a successful task
        when(firebaseAuth.signInWithEmailAndPassword(any(String.class), any(String.class)))
                .thenReturn(successfulTask);

        // Mocking the onComplete method of the successful task
        when(successfulTask.isSuccessful()).thenReturn(true);

        // Calling the loginUser method with valid email and password
        loginActivity.loginUser("test@example.com", "password");

        // Verifying that signInWithEmailAndPassword method is called with the correct parameters
        verify(firebaseAuth).signInWithEmailAndPassword("test@example.com", "password");
        // Verifying that addAuthStateListener method is called on a successful login
        verify(firebaseAuth).addAuthStateListener(loginActivity.firebaseAuthListener);
    }

    @Test
    public void testLoginUser_withInvalidCredentials() {
        Context context = ApplicationProvider.getApplicationContext();
        // Mocking the signInWithEmailAndPassword method to return a failed task
        when(firebaseAuth.signInWithEmailAndPassword(any(String.class), any(String.class)))
                .thenReturn(failedTask);

        // Mocking the onComplete method of the failed task
        when(failedTask.isSuccessful()).thenReturn(false);

        // Calling the loginUser method with invalid email and password
        loginActivity.loginUser("invalid@example.com", "invalidPassword");

        // Verifying that signInWithEmailAndPassword method is called with the correct parameters
        verify(firebaseAuth).signInWithEmailAndPassword("invalid@example.com", "invalidPassword");
        // Verifying that a toast message is shown for failed login
        // Note: This verification depends on the implementation of the Toast.makeText() method
        // and may require additional mocking or modifications to the LoginActivity code
        // to make it testable.
        // verify(toast).show();
    }
}*/