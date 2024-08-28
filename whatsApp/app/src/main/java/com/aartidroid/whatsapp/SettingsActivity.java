package com.aartidroid.whatsapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionBarPolicy;

import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.view.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private Button updateAccountSetting;
    private EditText userName,userStatus;

    private CircleImageView userProfileImage;
    private ActivityResultLauncher<String>mGetContent;

    private StorageReference userProfileImgRef;

//    private static final int GalleryPic = 1;

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private ProgressDialog dialogBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

//        userProfileImgRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        rootRef = FirebaseDatabase.getInstance().getReference();

        initializedField();

        retriveUserInfo();



//        userProfileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mGetContent.launch("image/*");
//            }
//        });
//
//        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//            @Override
//            public void onActivityResult(Uri result) {
//                Intent intent = new Intent(SettingsActivity.this,CropperActivity.class);
//                intent.putExtra("DATA",result.toString());
//                startActivityForResult(intent,101);
//
//            }
//        });
        updateAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                updateSetting();
            }
        });
    }
    private void initializedField() {

        updateAccountSetting = (Button) findViewById(R.id.setting_button);
        userName = (EditText) findViewById(R.id.set_user_name);
        userStatus = (EditText) findViewById(R.id.set_user_status);
        userProfileImage = (CircleImageView) findViewById(R.id.set_profile_image);

        dialogBar = new ProgressDialog(this);
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        if(resultCode==-1 && requestCode==101){
//
//            dialogBar.setTitle("Loading");
//            dialogBar.setMessage("please wait while upload the image..");
//            dialogBar.setCanceledOnTouchOutside(false);
//            dialogBar.show();
//            String result = data.getStringExtra("RESULT");
//            Uri resultUri = null;
//            if(result!=null){
//                resultUri = Uri.parse(result);
//
//                StorageReference filePath = userProfileImgRef.child(currentUserId + ".jpg");
//                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                        if(task.isSuccessful()) {
//                            Toast.makeText(SettingsActivity.this, "Profile Image Upload SuccessFully", Toast.LENGTH_SHORT).show();
//
//                            final String downLoadUrl = task.getResult().getUploadSessionUri().toString();
//
//                            rootRef.child("Users").child(currentUserId).child("image")
//                                    .setValue(downLoadUrl)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//
//                                                Toast.makeText(SettingsActivity.this, "image upload SuccessFully on your database", Toast.LENGTH_SHORT).show();
//                                                dialogBar.dismiss();
//                                            } else {
//                                                Toast.makeText(SettingsActivity.this, "Error : " + task.getException().toString(), Toast.LENGTH_SHORT).show();
//                                                dialogBar.dismiss();
//                                            }
//                                        }
//                                    });
//                        }
//                        else{
//
//                            String message = task.getException().toString();
//                            Toast.makeText(SettingsActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
//                            dialogBar.dismiss();
//                        }
//
//                    }
//                });
//            }
//            userProfileImage.setImageURI(resultUri);
////
//        }
//
//    }
    private void retriveUserInfo() {

        rootRef.child("Users");
        rootRef.child(currentUserId);
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.exists()) && (snapshot.hasChild("name") && (snapshot.hasChild("image")))) {
                    String retrieveUserData = snapshot.child("name").getValue().toString();
                    String retrieveUserStatus = snapshot.child("status").getValue().toString();
                    String retrieveUserImage = snapshot.child("image").getValue().toString();

                    userName.setText(retrieveUserData);
                    userStatus.setText(retrieveUserStatus);
//                    Picasso.get().load(retrieveUserImage).into(userProfileImage);


                } else if ((snapshot.exists()) && (snapshot.hasChild("name"))) {

                    String retrieveUserData = snapshot.child("name").getValue().toString();
                    String retrieveUserStatus = snapshot.child("status").getValue().toString();

                    userName.setText(retrieveUserData);
                    userStatus.setText(retrieveUserStatus);


                } else {
                    Toast.makeText(SettingsActivity.this, "Please Update your Profile Status ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    private void updateSetting() {
        String setUserName = userName.getText().toString();
        String setUserStatus = userStatus.getText().toString();
        if(TextUtils.isEmpty(setUserName)){
            Toast.makeText(this, "Please enter your user name first...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(setUserStatus)){
            Toast.makeText(this, "Please set your status  first...", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String,String>profileMap = new HashMap<>();
            profileMap.put("uid",currentUserId);
            profileMap.put("name",setUserName);
            profileMap.put("status",setUserStatus);
            rootRef.child("Users").child(currentUserId).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserToMainActivity();
                        Toast.makeText(SettingsActivity.this, "Profile Updated SuccessFully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String message = task.getException().toString();
                        Toast.makeText(SettingsActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void sendUserToMainActivity() {
        Intent settingIntent = new Intent(SettingsActivity.this,MainActivity.class);
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingIntent);
        finish();
    }
}