package com.takecare.takecare.Activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.takecare.takecare.Interface.ItemClickListener;
import com.takecare.takecare.Model.Item;
import com.takecare.takecare.R;
import com.takecare.takecare.ViewHolder.ItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MedicalTermsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    List<Item> items = new ArrayList();
    FirebaseRecyclerAdapter<Item, ItemViewHolder> adapter;

    SparseBooleanArray expandState = new SparseBooleanArray();

    private RecyclerView recyclerView;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_terms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Items");

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

        //init view
        recyclerView = (RecyclerView)findViewById(R.id.lst_item);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(LM);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);

        //init view
        recyclerView = (RecyclerView)findViewById(R.id.lst_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //retrieve data
        retrieveData();

        //set data
        setData();

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
            Intent navigationIntent = new Intent(MedicalTermsActivity.this,NavigationActivity.class);
            startActivity(navigationIntent);
        } else if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(MedicalTermsActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (id == R.id.nav_history) {
            Intent historyIntent = new Intent(MedicalTermsActivity.this, MedicalHistoryActivity.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_reminder) {

        } else if (id == R.id.nav_sub) {
            Intent subprofileIntent = new Intent(MedicalTermsActivity.this,SubProfileActivity.class);
            startActivity(subprofileIntent);
        } else if (id == R.id.nav_about) {
            Intent aboutIntent = new Intent(MedicalTermsActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
        } else if (id == R.id.nav_log) {
            FirebaseAuth.getInstance().signOut(); //End user session
            startActivity(new Intent(MedicalTermsActivity.this, LoginActivity.class)); //Go back to home page
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

        //user image using glide
        //import the library

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
    }

    private void setData() {
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Items")
                .orderByChild("category")
                .equalTo("medical terminology");

        FirebaseRecyclerOptions<Item> options = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(query,Item.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Item , ItemViewHolder>(options) {

            @Override
            public int getItemViewType(int position) {
                if(items.get(position).isExpandable())
                    return 1;
                else
                    return 0;
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, final int position, @NonNull final Item model) {

                switch (holder.getItemViewType())
                {
                    case 0: //without item
                    { ItemViewHolder viewHolder = (ItemViewHolder)holder;
                        viewHolder.setIsRecyclable(false);
                        viewHolder.txt_item_text.setText(model.getText());
                        // event
                        viewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean b) {
                                Toast.makeText(MedicalTermsActivity.this, "" + items.get(position).getText(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                    case 1: //with item
                    {
                        final ItemViewHolder viewHolder = (ItemViewHolder)holder;
                        viewHolder.setIsRecyclable(false);
                        viewHolder.txt_item_text.setText(model.getText());
                        //because
                        viewHolder.expandableLinearLayout.setInRecyclerView(true);
                        viewHolder.expandableLinearLayout.setExpanded(expandState.get(position));
                        viewHolder.expandableLinearLayout.setListener(new ExpandableLayoutListenerAdapter() {
                            @Override
                            public void onPreOpen() {
                                changeRotate(viewHolder.button,0f,180f).start();
                                expandState.put(position,true);
                            }

                            @Override
                            public void onPreClose() {
                                changeRotate(viewHolder.button,180f,0f).start();
                                expandState.put(position,false);
                            }

                        });
                        viewHolder.button.setRotation(expandState.get(position)?180f:0);
                        viewHolder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                viewHolder.expandableLinearLayout.toggle();

                            }
                        });

                        viewHolder.txt_child_item_text.setText(model.getSubText());

                        //set event
                        viewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean b) {
                                changeRotate(viewHolder.button,0f,180f).start();
                                expandState.put(position,true);
                                viewHolder.expandableLinearLayout.toggle();
                            }
                        });
                    }
                    break;
                    default:
                        break;
                }

            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                if (viewType == 0)  //without item
                {
                    View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_without_child, viewGroup, false);
                    return new ItemViewHolder(itemView, viewType == 1); //false

                } else //with items
                {
                    View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_with_child_medterm,viewGroup,false);
                    return new ItemViewHolder(itemView, viewType == 1 ); //true
                }
            }
        };

        expandState.clear();
        for (int i=0;1<items.size();i++)
            expandState.append(i,false);

        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    private ObjectAnimator changeRotate(RelativeLayout button, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "rotation",from,to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    private void retrieveData() {
        items.clear();

        DatabaseReference db = FirebaseDatabase.getInstance()
                .getReference()
                .child("Items");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapShot:dataSnapshot.getChildren())
                {
                    Item item = itemSnapShot.getValue(Item.class);

                    items.add(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR",""+ databaseError.getMessage());
            }
        });

    }

    @Override
    protected void  onStart(){
        if (adapter != null)
            adapter.startListening();
        super.onStart();

    }
    @Override
    protected void onStop(){
        if(adapter !=null)
            adapter.stopListening();
        super.onStop();
    }
}
