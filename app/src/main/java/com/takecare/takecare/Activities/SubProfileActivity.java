package com.takecare.takecare.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.takecare.takecare.Adapter.CustomAdapter;
import com.takecare.takecare.Adapter.CustomAdapterSubProfile;
import com.takecare.takecare.Helper.FirebaseHelper;
import com.takecare.takecare.Helper.FirebaseHelperSubProfile;
import com.takecare.takecare.Model.Item;
import com.takecare.takecare.Model.MedicalHistory;
import com.takecare.takecare.Model.SubProfile;
import com.takecare.takecare.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SubProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    List<SubProfile> subprofile = new ArrayList();

    DatabaseReference db, databaseReference;
    FirebaseHelperSubProfile helper;
    CustomAdapterSubProfile adapter;
    ListView lv;
    FirebaseDatabase firebaseDatabase;

    EditText fnameTxt,lnameTxt,phoneTxt,date, time,medicine,number;
    Button send;

    String currentUserID;

    Calendar mCurrentDate;
    int day,month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = mAuth.getCurrentUser().getUid();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInputDialogSms();
            }
        });
        FloatingActionButton fab_follow = (FloatingActionButton) findViewById(R.id.fab_follow);

        fab_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInputDialogSub();
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

        lv = (ListView) findViewById(R.id.lv);
        lv.setDivider(null);

        //INITIALIZE FIRE BASE DB
        databaseReference = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelperSubProfile(databaseReference);

        //ADAPTER
        adapter = new CustomAdapterSubProfile(this,helper.retrieve());
        lv.setAdapter(adapter);
    }

    private void displayInputDialogSub() {
        Dialog d=new Dialog(this);
        d.setContentView(R.layout.layout_dialog_subprofile);

        fnameTxt = (EditText) d.findViewById(R.id.firstnameEditText);
        lnameTxt= (EditText) d.findViewById(R.id.lastnameEditText);
        phoneTxt= (EditText) d.findViewById(R.id.phoneEditText);


        Button saveBtn= (Button) d.findViewById(R.id.saveBtn);

        //SAVE
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //GET DATA
                String fname = fnameTxt.getText().toString();
                String lname = lnameTxt.getText().toString();
                String phone = phoneTxt.getText().toString();

                //SET DATA
                SubProfile s = new SubProfile();
                s.setFirstname(fname);
                s.setLastname(lname);
                s.setPhone(phone);
                s.setCurrentuserid(currentUserID);

                //SIMPLE VALIDATION
                if (TextUtils.isEmpty(fname)) {
                    Toast.makeText(SubProfileActivity.this, "Enter date", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(lname)) {
                    Toast.makeText(SubProfileActivity.this, "Enter diagnosis", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(SubProfileActivity.this, "Enter hospital or clinic", Toast.LENGTH_SHORT).show();
                }
                else {
                    //THEN SAVE
                    if (helper.save(s)) {
                        //IF SAVED CLEAR EDITXT
                        fnameTxt.setText("");
                        lnameTxt.setText("");
                        phoneTxt.setText("");

                        Toast.makeText(SubProfileActivity.this, "New sub profile added.", Toast.LENGTH_SHORT).show();

                        adapter = new CustomAdapterSubProfile(SubProfileActivity.this, helper.retrieve());
                        lv.setAdapter(adapter);
                    }
                }
            }
        });

        d.show();
    }



    //DISPLAY INPUT DIALOG
    private void displayInputDialogSms()
    {
        Dialog d=new Dialog(this);
        d.setContentView(R.layout.sms_layout);

        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month+1;


        time = (EditText)d.findViewById(R.id.inputTime);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SubProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText(String.format("%02d", selectedHour)+":"+ String.format("%02d", selectedMinute));
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time\n");
                mTimePicker.show();
            }
        });

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);
        month = month+1;

        date = (EditText)d.findViewById(R.id.inputDate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SubProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        date.setText(month+"/"+dayOfMonth+"/"+year);
                    }
                }, year,month,day );
                datePickerDialog.show();
            }
        });

        time = (EditText)d.findViewById(R.id.inputTime);
        date = (EditText)d.findViewById(R.id.inputDate);
        medicine = (EditText)d.findViewById(R.id.inputMessage);
        number = (EditText)d.findViewById(R.id.inputNumber);
        send = (Button)d.findViewById(R.id.sendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Construct data

                    String text = "&text=" + medicine.getText().toString();
                    String source = "&source=" + "Take Care";
                    String destination = "&destination=" + number.getText().toString();
                    //String schedule = "&schedule=" +  number.getText().toString();

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
                        Toast.makeText(SubProfileActivity.this,line.toString(), Toast.LENGTH_SHORT).show();
                    }
                    rd.close();

                } catch (Exception e) {
                    Toast.makeText(SubProfileActivity.this,e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        StrictMode.ThreadPolicy st= new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(st);



        Spinner sp = (Spinner) findViewById(R.id.spinner);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelperSubProfile(databaseReference);

        //ADAPTER
        adapter = new CustomAdapterSubProfile(this,helper.retrieve());
        //sp.setAdapter(adapter);

        /*Query query = FirebaseDatabase.getInstance().getReference()
                .child("SubProfile")
                .orderByChild("currentuserid")
                .equalTo(currentUserID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("firstname").getValue(String.class);
                    areas.add(areaName);
                }
                Spinner areaSpinner = (Spinner) findViewById(R.id.spinner);

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(SubProfileActivity.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(areasAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        d.show();
    }


    private void retrieveData() {
        subprofile.clear();

        DatabaseReference db = FirebaseDatabase.getInstance()
                .getReference()
                .child("SubProfile");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapShot:dataSnapshot.getChildren())
                {
                    SubProfile subProfile = itemSnapShot.getValue(SubProfile.class);

                    subprofile.add(subProfile);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR",""+ databaseError.getMessage());
            }
        });
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
            Intent navigationIntent = new Intent(SubProfileActivity.this,NavigationActivity.class);
            startActivity(navigationIntent);
        } else if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(SubProfileActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (id == R.id.nav_history) {
            Intent historyIntent = new Intent(SubProfileActivity.this, MedicalHistoryActivity.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_reminder) {

        } else if (id == R.id.nav_sub) {
            Intent subprofileIntent = new Intent(SubProfileActivity.this,SubProfileActivity.class);
            startActivity(subprofileIntent);
        } else if (id == R.id.nav_about) {
            Intent aboutIntent = new Intent(SubProfileActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
        } else if (id == R.id.nav_log) {
            FirebaseAuth.getInstance().signOut(); //End user session
            startActivity(new Intent(SubProfileActivity.this, LoginActivity.class)); //Go back to home page
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
