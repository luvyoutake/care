package com.takecare.takecare.Helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.takecare.takecare.Model.MedicalHistory;

import java.util.ArrayList;

public class FirebaseHelper {

    FirebaseAuth mAuth;

    String currentUserID;

    DatabaseReference db;
    Boolean saved=null;
    ArrayList<MedicalHistory> medicalhistories = new ArrayList<>();

    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    //WRITE IF NOT NULL
    public Boolean save(MedicalHistory medicalhistory)
    {
        if(medicalhistory==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child("MedicalHistory").push().setValue(medicalhistory);
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
        medicalhistories.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            MedicalHistory medicalhistory = ds.getValue(MedicalHistory.class);
            medicalhistories.add(medicalhistory);
        }

    }


    //READ BY HOOKING ONTO DATABASE OPERATION CALLBACKS
    public ArrayList<MedicalHistory> retrieve() {

        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("MedicalHistory")
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

        return medicalhistories;
    }
}
