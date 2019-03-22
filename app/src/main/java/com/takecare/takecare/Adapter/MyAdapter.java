package com.takecare.takecare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.takecare.takecare.Model.Item;
import com.takecare.takecare.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyAdapterViewHolder> {

    public Context c;
    public ArrayList<Item> arrayList;

    public MyAdapter(Context c, ArrayList<Item> arrayList) {

        this.c=c;
        this.arrayList=arrayList;

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public MyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_with_child_healthtips,parent,false);


        return new MyAdapterViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterViewHolder holder, int position) {

        Item item = arrayList.get(position);

        holder.txt_item_text.setText(item.getText());
        holder.txt_child_item_text.setText(item.getSubText());


    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView  txt_item_text;
        public TextView txt_child_item_text;

        public MyAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_item_text = (TextView)itemView.findViewById(R.id.txt_item_text);
            txt_child_item_text = (TextView)itemView.findViewById(R.id.txt_child_item_text);
        }
    }
}
