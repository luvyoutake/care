package com.takecare.takecare.Activities;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.takecare.takecare.Model.ItemDoctor;
import com.takecare.takecare.R;

public class WayDocActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<ItemDoctor, WayDocActivity.NewsViewHolder> mPeopleRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_way_doc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        updateNavHeader();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Doctors");

        //"ItemDoctor" here will reflect what you have called your database in Firebase.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors");
        mDatabase.keepSynced(true);

        mPeopleRV = (RecyclerView) findViewById(R.id.myRecycleView);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Doctors");
        Query personsQuery = personsRef.orderByKey();


        /*Query personsQuery = FirebaseDatabase.getInstance().getReference()
                .child("Doctors")
                .equalTo("place")
                .orderByKey();*/

        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<ItemDoctor>().setQuery(personsQuery, ItemDoctor.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<ItemDoctor, WayDocActivity.NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(WayDocActivity.NewsViewHolder holder, final int position, final ItemDoctor model) {
                holder.setdName(model.getdName());
                holder.setspecialization(model.getSpecialization());
                holder.setImage(getBaseContext(), model.getImage());
                holder.setspecialization(model.getSpecialization());
                holder.setSched(model.getSched());
                holder.setClinic(model.getClinic());
                holder.setfee(model.getFee());
                holder.setPlace(model.getPlace());


                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String url = model.getUrl();
                        Intent intent = new Intent(getApplicationContext(), PractoActivity.class);
                        intent.putExtra("id", url);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public WayDocActivity.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_way_doc, parent, false);

                return new WayDocActivity.NewsViewHolder(view);
            }
        };

        mPeopleRV.setAdapter(mPeopleRVAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();


    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public NewsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setdName(String dName){
            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
            post_title.setText(dName);
        }
        public void setspecialization (String specialization){
            TextView post_desc = (TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(specialization);
        }
        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
        public void setSched(String sched){
            TextView post_sched = (TextView)mView.findViewById(R.id.post_sched);
            post_sched.setText(sched);
        }
        public void setClinic(String clinic){
            TextView post_clinic = (TextView)mView.findViewById(R.id.post_clinic);
            post_clinic.setText(clinic);
        }
        public void setfee(String fee){
            TextView post_fee = (TextView)mView.findViewById(R.id.post_fee);
            post_fee.setText(fee);
        }
        public void setPlace(String place){
            TextView post_place = (TextView)mView.findViewById(R.id.post_place);
            post_place.setText(place);
        }

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
            Intent navigationIntent = new Intent(WayDocActivity.this,NavigationActivity.class);
            startActivity(navigationIntent);
        } else if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(WayDocActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (id == R.id.nav_history) {
            Intent historyIntent = new Intent(WayDocActivity.this, MedicalHistoryActivity.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_reminder) {

        } else if (id == R.id.nav_sub) {

        } else if (id == R.id.nav_about) {
            Intent aboutIntent = new Intent(WayDocActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
        } else if (id == R.id.nav_log) {
            FirebaseAuth.getInstance().signOut(); //End user session
            startActivity(new Intent(WayDocActivity.this, LoginActivity.class)); //Go back to home page
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
