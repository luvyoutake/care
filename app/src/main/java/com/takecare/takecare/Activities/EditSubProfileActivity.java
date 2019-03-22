package com.takecare.takecare.Activities;

import android.content.Intent;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.takecare.takecare.R;

public class EditSubProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    EditText fnameTxt, lnameTxt, phoneTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sub_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        fnameTxt = (EditText)findViewById(R.id.firstnameEditText);
        lnameTxt = (EditText)findViewById(R.id.lastnameEditText);
        phoneTxt = (EditText)findViewById(R.id.phoneEditText);

        //GET INTENT
        Intent i=this.getIntent();

        //RECEIVE DATA
        String firstname=i.getExtras().getString("FIRST_KEY");
        String lastname=i.getExtras().getString("LAST_KEY");
        String phone=i.getExtras().getString("PHONE_KEY");

        //BIND DATA
        fnameTxt.setText(firstname);
        lnameTxt.setText(lastname);
        phoneTxt.setText(phone);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            Intent navigationIntent = new Intent(EditSubProfileActivity.this,NavigationActivity.class);
            startActivity(navigationIntent);
        } else if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(EditSubProfileActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (id == R.id.nav_history) {
            Intent historyIntent = new Intent(EditSubProfileActivity.this, MedicalHistoryActivity.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_reminder) {

        } else if (id == R.id.nav_sub) {
            Intent subprofileIntent = new Intent(EditSubProfileActivity.this,SubProfileActivity.class);
            startActivity(subprofileIntent);
        } else if (id == R.id.nav_about) {
            Intent aboutIntent = new Intent(EditSubProfileActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
        } else if (id == R.id.nav_log) {
            FirebaseAuth.getInstance().signOut(); //End user session
            startActivity(new Intent(EditSubProfileActivity.this, LoginActivity.class)); //Go back to home page
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
