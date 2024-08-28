package com.aartidroid.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;

    //fire base
    private FirebaseUser currentUser;

    private FirebaseAuth mAuth;

    private TabAccessorAdapter myTabAccessorAdapter;
    // reference database
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("WhatsApp");


        myViewPager  =(ViewPager) findViewById(R.id.main_tabs_pager);
        myTabAccessorAdapter = new TabAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabAccessorAdapter);

        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);

        // unique id given by database
        mAuth = FirebaseAuth.getInstance();
        // get Current user
        currentUser = mAuth.getCurrentUser();
        // ref of user
        rootRef = FirebaseDatabase.getInstance().getReference();

        Toast.makeText(MainActivity.this, ""+rootRef, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser==null){
            sendUserToLoginActivity();
        }
        else{  // if user are already login the verify the user

            VerifyUserExistance();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.main_logOut_option){

            mAuth.signOut();
            sendUserToLoginActivity();

        }

        if(item.getItemId()==R.id.main_setting_option){
            sendUserToSettingActivity();

        }
        if(item.getItemId()==R.id.main_create_group_option){
            RequestNewGroup();

        }
        if(item.getItemId()==R.id.main_find_friends_option){

        }
        return true;

    }
    private void RequestNewGroup() {
        // At first alert the dialog box and to request the join group
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter Group Name : ");

        final EditText groupNameField = new EditText(MainActivity.this);

        // hint to the user
        groupNameField.setHint("e.g family ");

        // view
        builder.setView(groupNameField);

        // create button
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String groupName = groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(MainActivity.this,"Please Write Group Name",Toast.LENGTH_SHORT).show();

                }
                else{
                    CreateNewGroup(groupName);
                }

            }
        });

        // cancel the group
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        builder.show();
    }
    private void CreateNewGroup(String groupName) {

        DatabaseReference groupsRef = rootRef.child("Groups").push();

        try {
            groupsRef.push().setValue(groupName);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Some Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void VerifyUserExistance() {

        String currentUserId = mAuth.getCurrentUser().getUid();
        rootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("name").exists())){
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
                else{
                    sendUserToSettingActivity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this,loginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToSettingActivity() {
        Intent SettingIntent = new Intent(MainActivity.this,SettingsActivity.class);
        SettingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SettingIntent);
        finish();
    }
}