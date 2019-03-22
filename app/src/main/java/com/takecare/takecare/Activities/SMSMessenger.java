package com.takecare.takecare.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.takecare.takecare.Adapter.CustomAdapterSubProfile;
import com.takecare.takecare.Helper.FirebaseHelperSubProfile;
import com.takecare.takecare.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class SMSMessenger extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    DatabaseReference db, databaseReference;
    FirebaseHelperSubProfile helper;
    CustomAdapterSubProfile adapter;

  private EditText number,message1;
  private Button btnSendTXT;

    String currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();


        number = findViewById(R.id.inputNumber);
        message1 = findViewById(R.id.inputMessage);
        btnSendTXT = findViewById(R.id.sendButton);

        btnSendTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    // Construct data

                    String text = "&text=" + message1.getText().toString();
                    String source = "&source=" + "Take Care";
                    String destination = "&destination=" + number.getText().toString();

                    HttpURLConnection conn = (HttpURLConnection) new URL("https://api.wavecell.com/sms/v1/TAKECARE_0kKCF_hq/single").openConnection();

                    String data = destination + text + source;

                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", "Bearer rOdCYwD5JFEzmoXomzWM9BORZZJG2uqiq4kRw8l9cyk");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                    conn.getOutputStream().write(data.getBytes("UTF-8"));
                    final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    final StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        Toast.makeText(SMSMessenger.this,line.toString(), Toast.LENGTH_SHORT).show();
                    }
                    rd.close();

                } catch (Exception e) {
                    Toast.makeText(SMSMessenger.this,e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        StrictMode.ThreadPolicy st= new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(st);

    }



    private void updateNavHeader() {
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent navigationIntent = new Intent(SMSMessenger.this, NavigationActivity.class);
            startActivity(navigationIntent);
        } else if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(SMSMessenger.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (id == R.id.nav_history) {
            Intent historyIntent = new Intent(SMSMessenger.this, MedicalHistoryActivity.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_reminder) {

        } else if (id == R.id.nav_sub) {

        } else if (id == R.id.nav_about) {
            Intent aboutIntent = new Intent(SMSMessenger.this, AboutActivity.class);
            startActivity(aboutIntent);
        } else if (id == R.id.nav_log) {
            FirebaseAuth.getInstance().signOut(); //End user session
            startActivity(new Intent(SMSMessenger.this, LoginActivity.class)); //Go back to home page
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
