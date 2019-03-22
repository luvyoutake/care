package com.takecare.takecare.Activities;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class GenericFragment extends Fragment {

    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    RecyclerView recyclerView;

    List<Item> items = new ArrayList();
    FirebaseRecyclerAdapter<Item, ItemViewHolder> adapter;

    SparseBooleanArray expandState = new SparseBooleanArray();

    public GenericFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_generic, container, false);

        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //init view
        recyclerView = (RecyclerView)v.findViewById(R.id.lst_item);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(LM);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);

        //init view
        recyclerView = (RecyclerView)v.findViewById(R.id.lst_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //retrieve data
        retrieveData();

        //set data
        setData();

        return v;
    }

    private void setData() {
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Items")
                .orderByChild("category")
                .equalTo("generic medicine");

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
                                Toast.makeText(getActivity(), "" + items.get(position).getText(), Toast.LENGTH_SHORT).show();
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
                        viewHolder.txt_child_sideeffect.setText(model.getSideeffect());
                        viewHolder.txt_child_precautions.setText(model.getPrecautions());

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
                    View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_with_child_genericbranded,viewGroup,false);
                    return new ItemViewHolder(itemView, viewType == 1 ); //true
                }
            }
        };

        expandState.clear();
        for (int i=0;1<items.size();i++)
            expandState.append(i,false);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
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
    public void  onStart(){
        if (adapter != null)
            adapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop(){
        if(adapter !=null)
            adapter.stopListening();
        super.onStop();
    }
}
