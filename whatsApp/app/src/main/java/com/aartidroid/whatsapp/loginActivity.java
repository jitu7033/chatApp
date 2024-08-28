package com.aartidroid.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

    // define login button to use login activity
    private Button loginButton ,phoneLoginButton;
    // define all the process in login button

    private EditText userEmail,userPassword;
    private TextView needNewAccountLink,forgetPasswordLink;

    private FirebaseAuth mAuth;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();




        //method call
        InitializedField();


        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendUserToRegisterActivity();

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogin();
            }
        });

        phoneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToPhoneLoginActivity();
            }
        });


    }

    private void AllowUserToLogin() {

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (TextUtils.isEmpty(email)) { // check if user is filled the email or not
            Toast.makeText(this, "Please Enter The email....", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) { // check if user is fill password or not


            Toast.makeText(this, "Please Enter The password....", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("Please Wait....");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        sendUserToMainActivity();
                        Toast.makeText(loginActivity.this,"Logged in  successfully",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else{
                        String message = task.getException().toString();
                        Toast.makeText(loginActivity.this,"Error :"+message,Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });



        }
    }

    private void InitializedField() {

        // accessing here to using id

        loginButton = (Button) findViewById(R.id.login_button);
        phoneLoginButton = (Button) findViewById(R.id.phone_login_button);
        userEmail = (EditText) findViewById(R.id.login_email);
        userPassword = (EditText) findViewById(R.id.login_password);
        needNewAccountLink = (TextView) findViewById(R.id.need_new_account);
        // forgetPassWord
        forgetPasswordLink = (TextView) findViewById(R.id.forget_password);
        loadingBar = new ProgressDialog(this);
    }
    private void sendUserToMainActivity() {
        Intent RegisterIntent = new Intent(loginActivity.this,MainActivity.class);
        RegisterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(RegisterIntent);
        finish();
    }
    private void sendUserToRegisterActivity(){     // user to register activity
        Intent registerIntent = new Intent(loginActivity.this,registerActivity.class);
        startActivity(registerIntent);
    }
    private void sendUserToPhoneLoginActivity() {
        Intent phoneIntent = new Intent(loginActivity.this,phoneLoginActivity.class);
        startActivity(phoneIntent);
    }
}