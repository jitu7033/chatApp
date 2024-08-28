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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class registerActivity extends AppCompatActivity {

    private Button createAccountButton;
    private EditText userRegisterPassword,userRegisterEmail;
    private TextView userAlreadyHaveAccount;

    private FirebaseAuth mAuth; // to create a new account save data in firebase

    private ProgressDialog loadingBar;          // this is for loading  Bar

    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //method call
        initializedField();

        mAuth = FirebaseAuth.getInstance();     // to initialized the firebase auth to create an account

        rootRef = FirebaseDatabase.getInstance().getReference();


        userAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {  //send user to login activity if user have account
            @Override
            public void onClick(View view) {  // send register to login page
                sendUserToLoginActivity();
            }
        });
        // set on clicker method to create a account
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });
    }
    private void CreateNewAccount() {
        String email = userRegisterEmail.getText().toString();
        String password = userRegisterPassword.getText().toString();

        if(TextUtils.isEmpty(email)){ // check if user is filled the email or not
                    Toast.makeText(this,"Please Enter The email....",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){ // check if user is fill password or not
                    Toast.makeText(this,"Please Enter The password....",Toast.LENGTH_SHORT).show();
        }
        else{           // if email and password are filled then create account of user

            loadingBar.setTitle("Creating New Account");  //  flicking screen to showing loading bar
            loadingBar.setMessage("Please wait While we are Creating new Account");
            loadingBar.setCanceledOnTouchOutside(true);  // if process are are running then the user is touch the screen this loading bar is not disappered until the account is created
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {            // to create account to use email and password
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){            // account create successfully then print the message to user
                                String CurrentUser = mAuth.getCurrentUser().getUid();
                                rootRef.child("Users").child(CurrentUser).setValue("");

                                sendUserToMainActivity();    // send user to login activity

                                Toast.makeText(registerActivity.this,"Account created successfully",Toast.LENGTH_SHORT).show();

                                // if account is created dismissed the loading bar
                        loadingBar.dismiss();

                    }
                    else{               // any error to show exception which error are concluded

                                String message = task.getException().toString();
                                Toast.makeText(registerActivity.this,"Error :"+ message,Toast.LENGTH_SHORT).show();

                                // and also if any error occurred dismissed the loading bar
                                loadingBar.dismiss();
                    }

                }
            });
        }
    }

    private void sendUserToMainActivity() {
        Intent RegisterIntent = new Intent(registerActivity.this,MainActivity.class);
        RegisterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(RegisterIntent);
        finish();
    }

    private void initializedField() {
        // accessing by id
        createAccountButton = (Button) findViewById(R.id.register_button);
        userRegisterPassword = (EditText) findViewById(R.id.register_password);
        userRegisterEmail = (EditText) findViewById(R.id.register_email);
        userAlreadyHaveAccount = (TextView) findViewById(R.id.already_have_account);

        loadingBar = new ProgressDialog(this);  // initialized the loading Bar
    }

    private void sendUserToLoginActivity(){     // send user to login activity

        // intent --->  jump from one application to another as a part of the whole process

        Intent loginIntent = new Intent(registerActivity.this,loginActivity.class);
        startActivity(loginIntent);
    }
}