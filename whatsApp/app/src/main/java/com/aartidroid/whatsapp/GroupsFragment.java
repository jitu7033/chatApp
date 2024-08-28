package com.aartidroid.whatsapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GroupsFragment extends Fragment {

    private View groupFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_Groups = new ArrayList<>();

    private DatabaseReference GroupRef;      // access the group from the dataBases



    public GroupsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView =  inflater.inflate(R.layout.fragment_groups, container, false);


        // reference of group in databases
        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        // reference to firebase database
        intializedField();

        RetrieveAndDisplay();  // display the group method

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // I is id  and i is position
                String currGroupName = adapterView.getItemAtPosition(position).toString();

                Intent groupChatIntent = new Intent(getContext(),GroupChatActivity.class);
                groupChatIntent.putExtra("groupName",currGroupName);  // put extra mean your are add extra
                startActivity(groupChatIntent);  // getContext Mean access the string,theme and other thing

            }
        });

        return groupFragmentView;
    }



    private void intializedField() {
        list_view = (ListView) groupFragmentView.findViewById(R.id.list_View);

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,list_of_Groups);

        list_view.setAdapter(arrayAdapter); // view the group
    }

    private void RetrieveAndDisplay() {

        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();

                Iterator iterator = dataSnapshot.getChildren().iterator();


                while(iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_Groups.clear();
                list_of_Groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}