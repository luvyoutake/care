package com.takecare.takecare.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.takecare.takecare.R;

public class MedHistoryDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    TextView dateTxt,diagnosisTxt,hosTxt,prescriptedTxt,adviceTxt,doctorTxt,followupTxt;

    String currentUserID, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_history_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        dateTxt = (TextView) findViewById(R.id.tv_Date);
        diagnosisTxt= (TextView) findViewById(R.id.tv_Diagnosis);
        hosTxt = (TextView) findViewById(R.id.tv_PlaceOfConsultation);
        prescriptedTxt= (TextView) findViewById(R.id.tv_PrescriptedMedicine);
        adviceTxt= (TextView) findViewById(R.id.tv_OtherAdvice);
        doctorTxt = (TextView) findViewById(R.id.tv_DoctorName);
        followupTxt = (TextView) findViewById(R.id.tv_FollowUp);

        //GET INTENT
        Intent i=this.getIntent();

        //RECEIVE DATA
        String date=i.getExtras().getString("DATE_KEY");
        String diagnosis=i.getExtras().getString("DIAGNOSIS_KEY");
        String hospital=i.getExtras().getString("HOSPITAL_KEY");
        String medicine=i.getExtras().getString("PRESCRIPTED_KEY");
        String advice=i.getExtras().getString("ADVICE_KEY");
        String doctor=i.getExtras().getString("DOCTOR_KEY");
        String followup=i.getExtras().getString("FOLLOWUP_KEY");

        //BIND DATA
        dateTxt.setText(date);
        diagnosisTxt.setText(diagnosis);
        hosTxt.setText(hospital);
        prescriptedTxt.setText(medicine);
        adviceTxt.setText(advice);
        doctorTxt.setText(doctor);
        followupTxt.setText(followup);

        //nav drawer
        updateNavHeader();
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
            Intent navigationIntent = new Intent(MedHistoryDetailActivity.this,NavigationActivity.class);
            startActivity(navigationIntent);
        } else if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(MedHistoryDetailActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (id == R.id.nav_history) {
            Intent historyIntent = new Intent(MedHistoryDetailActivity.this, MedicalHistoryActivity.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_reminder) {

        } else if (id == R.id.nav_sub) {
            Intent subprofileIntent = new Intent(MedHistoryDetailActivity.this,SubProfileActivity.class);
            startActivity(subprofileIntent);
        } else if (id == R.id.nav_about) {
            Intent aboutIntent = new Intent(MedHistoryDetailActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
        } else if (id == R.id.nav_log) {
            FirebaseAuth.getInstance().signOut(); //End user session
            startActivity(new Intent(MedHistoryDetailActivity.this, LoginActivity.class)); //Go back to home page
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