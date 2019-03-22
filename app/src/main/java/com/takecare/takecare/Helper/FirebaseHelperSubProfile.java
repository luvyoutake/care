package com.takecare.takecare.Helper;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.takecare.takecare.Model.SubProfile;

import java.util.ArrayList;

public class FirebaseHelperSubProfile {

    FirebaseAuth mAuth;

    String currentUserID;

    DatabaseReference db;
    Boolean saved=null;
    ArrayList<SubProfile> subprofiles = new ArrayList<>();

    public FirebaseHelperSubProfile(DatabaseReference db) {
        this.db = db;
    }

    //WRITE IF NOT NULL
    public Boolean save(SubProfile subprofile)
    {
        if(subprofile==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child("SubProfile").push().setValue(subprofile);
                saved=true;

            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }
        return saved;
    }

    //IMPLEMENT FETCH DATA AND FILL ARRAY LIST
    private void fetchData(DataSnapshot dataSnapshot)
    {
        subprofiles.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            SubProfile subprofile = ds.getValue(SubProfile.class);
            subprofiles.add(subprofile);
        }

    }


    //READ BY HOOKING ONTO DATABASE OPERATION CALLBACKS
    public ArrayList<SubProfile> retrieve() {

        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("SubProfile")
                .orderByChild("currentuserid")
                .equalTo(currentUserID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return subprofiles;
    }
    public ArrayList<SubProfile> retrieveSpin() {

        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("SubProfile")
                .orderByChild("currentuserid")
                .equalTo(currentUserID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return subprofiles;
    }
}

