package com.takecare.takecare.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.takecare.takecare.Adapter.CustomAdapter;
import com.takecare.takecare.Helper.FirebaseHelper;
import com.takecare.takecare.Model.MedicalHistory;
import com.takecare.takecare.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MedicalHistoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    DatabaseReference db, databaseReference;
    FirebaseHelper helper;
    CustomAdapter adapter;
    ListView lv;
    FirebaseDatabase firebaseDatabase;
    EditText dateTxt,diagnosisTxt,hospitalTxt,medicineTxt,adviceTxt,doctorTxt,followupTxt;

    String currentUserID;

    Calendar mCurrentDate;
    int day,month,year;


    ArrayList<MedicalHistory> medicalhistories = new ArrayList<MedicalHistory>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        db = firebaseDatabase.getReference().child("MedicalHistory");

        lv = (ListView) findViewById(R.id.lv);
        lv.setDivider(null);
        ArrayAdapter<MedicalHistory> arrayAdapter = new ArrayAdapter<MedicalHistory>(this,R.layout.listviewmedhistory);
        lv.setAdapter(arrayAdapter);

        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = mAuth.getCurrentUser().getUid();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //nav drawer
        updateNavHeader();

        lv = (ListView) findViewById(R.id.lv);

        //INITIALIZE FIRE BASE DB
        databaseReference = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(databaseReference);

        //ADAPTER
        adapter = new CustomAdapter(this,helper.retrieve());
        lv.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_Add_Medical_History);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInputDialog();
            }
        });

    }

    //DISPLAY INPUT DIALOG
    private void displayInputDialog()
    {
        Dialog d=new Dialog(this);
        d.setContentView(R.layout.layout_dialog_medical_history);

        dateTxt = (EditText) d.findViewById(R.id.dateEditText);
        diagnosisTxt= (EditText) d.findViewById(R.id.diagnosisEditText);
        hospitalTxt= (EditText) d.findViewById(R.id.hosEditText);
        medicineTxt = (EditText) d.findViewById(R.id.prescriptedEditText);
        adviceTxt = (EditText) d.findViewById(R.id.adviceEditText);
        doctorTxt = (EditText) d.findViewById(R.id.doctorEditText);
        followupTxt = (EditText) d.findViewById(R.id.followupEditText);

        Button saveBtn= (Button) d.findViewById(R.id.saveBtn);

        //date picker
        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month+1;

        dateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MedicalHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        dateTxt.setText(month+"/"+dayOfMonth+"/"+year);
                    }
                }, year,month,day );
                datePickerDialog.show();
            }
        });

        followupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MedicalHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        followupTxt.setText(month+"/"+dayOfMonth+"/"+year);
                    }
                }, year,month,day );
                datePickerDialog.show();
            }
        });

        //SAVE
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //GET DATA
                //String id = db.push().getKey();
                String date = dateTxt.getText().toString();
                String diagnosis = diagnosisTxt.getText().toString();
                String hospital = hospitalTxt.getText().toString();
                String medicine = medicineTxt.getText().toString();
                String advice = adviceTxt.getText().toString();
                String doctor = doctorTxt.getText().toString();
                String followup = followupTxt.getText().toString();

                //SET DATA
                MedicalHistory s = new MedicalHistory();
                s.setDate(date);
                s.setDiagnosis(diagnosis);
                s.setHospital(hospital);
                s.setPrescriptedmedicine(medicine);
                s.setOtheradvice(advice);
                s.setDoctor(doctor);
                s.setFollowup(followup);
                s.setCurrentuserid(currentUserID);
                //s.setId(id);

                //SIMPLE VALIDATION
                if (TextUtils.isEmpty(date)) {
                    Toast.makeText(MedicalHistoryActivity.this, "Enter date", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(diagnosis)) {
                    Toast.makeText(MedicalHistoryActivity.this, "Enter diagnosis", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(hospital)) {
                    Toast.makeText(MedicalHistoryActivity.this, "Enter hospital or clinic", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(medicine)) {
                    Toast.makeText(MedicalHistoryActivity.this, "Enter prescripted medicine", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(doctor)) {
                    Toast.makeText(MedicalHistoryActivity.this, "Enter your doctor", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(followup)) {
                    Toast.makeText(MedicalHistoryActivity.this, "Enter follow-up check-up", Toast.LENGTH_SHORT).show();
                }
                else {
                    //THEN SAVE
                    if (helper.save(s)) {
                        //IF SAVED CLEAR EDITXT
                        dateTxt.setText("");
                        diagnosisTxt.setText("");
                        hospitalTxt.setText("");
                        medicineTxt.setText("");
                        adviceTxt.setText("");
                        doctorTxt.setText("");
                        followupTxt.setText("");

                        Toast.makeText(MedicalHistoryActivity.this, "New medical history added.", Toast.LENGTH_SHORT).show();

                        adapter = new CustomAdapter(MedicalHistoryActivity.this, helper.retrieve());
                        lv.setAdapter(adapter);
                    }
                }
            }
        });

        d.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent navigationIntent = new Intent(MedicalHistoryActivity.this,NavigationActivity.class);
            startActivity(navigationIntent);
        } else if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(MedicalHistoryActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (id == R.id.nav_history) {
            Intent historyIntent = new Intent(MedicalHistoryActivity.this, MedicalHistoryActivity.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_reminder) {

        } else if (id == R.id.nav_sub) {
            Intent subprofileIntent = new Intent(MedicalHistoryActivity.this,SubProfileActivity.class);
            startActivity(subprofileIntent);
        } else if (id == R.id.nav_about) {
            Intent aboutIntent = new Intent(MedicalHistoryActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
        } else if (id == R.id.nav_log) {
            FirebaseAuth.getInstance().signOut(); //End user session
            startActivity(new Intent(MedicalHistoryActivity.this, LoginActivity.class)); //Go back to home page
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);

        navUserMail.setText(currentUser.getEmail());
        navUserName.setText(currentUser.getDisplayName());

        //now we will use Glide to load user image
        //import the library

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
    }
}
