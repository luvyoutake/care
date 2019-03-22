package com.takecare.takecare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.takecare.takecare.Activities.EditSubProfileActivity;
import com.takecare.takecare.Activities.MedHistoryDetailActivity;
import com.takecare.takecare.Activities.SubProfileActivity;
import com.takecare.takecare.Model.MedicalHistory;
import com.takecare.takecare.Model.SubProfile;
import com.takecare.takecare.R;

import java.util.ArrayList;

public class CustomAdapterSubProfile extends BaseAdapter {

    Context c;
    ArrayList<SubProfile> subprofiles;

    public CustomAdapterSubProfile(Context c, ArrayList<SubProfile> subprofiles) {
        this.c = c;
        this.subprofiles = subprofiles;
    }

    @Override
    public int getCount() {
        return subprofiles.size();
    }

    @Override
    public Object getItem(int pos) {
        return subprofiles.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.listviewsubprofile,viewGroup,false);
        }

        TextView nameTxt = (TextView) convertView.findViewById(R.id.nameTxt);
        TextView phoneTxt = (TextView) convertView.findViewById(R.id.phoneTxt);

        final SubProfile s = (SubProfile) this.getItem(position);

        nameTxt.setText(s.getFirstname() +" "+s.getLastname());
        phoneTxt.setText(s.getPhone());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OPEN DETAIL
                openDetailActivity(s.getFirstname(),s.getLastname(),s.getPhone());
            }
        });

        return convertView;
    }

    //OPEN DETAIL ACTIVITY
    private void openDetailActivity(String...details)
    {
        Intent i=new Intent(c, EditSubProfileActivity.class);
        i.putExtra("FIRST_KEY",details[0]);
        i.putExtra("LAST_KEY",details[1]);
        i.putExtra("PHONE_KEY",details[2]);

        c.startActivity(i);
    }
}
