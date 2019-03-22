package com.takecare.takecare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.takecare.takecare.Activities.MedHistoryDetailActivity;
import com.takecare.takecare.Model.MedicalHistory;
import com.takecare.takecare.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context c;
    ArrayList<MedicalHistory> medicalhistories;

    public CustomAdapter(Context c, ArrayList<MedicalHistory> medicalhistories) {
        this.c = c;
        this.medicalhistories = medicalhistories;
    }

    @Override
    public int getCount() {
        return medicalhistories.size();
    }

    @Override
    public Object getItem(int pos) {
        return medicalhistories.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.listviewmedhistory,viewGroup,false);
        }

        TextView dateTxt = (TextView) convertView.findViewById(R.id.dateTxt);
        TextView diagnosisTxt = (TextView) convertView.findViewById(R.id.diagnosisTxt);

        final MedicalHistory s = (MedicalHistory)this.getItem(position);

        dateTxt.setText(s.getDate());
        diagnosisTxt.setText(s.getDiagnosis());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OPEN DETAIL
                openDetailActivity(s.getDate(),s.getDiagnosis(),s.getHospital(),s.getPrescriptedmedicine(),s.getOtheradvice(),s.getDoctor(),s.getFollowup());
            }
        });

        return convertView;
    }

    //OPEN DETAIL ACTIVITY
    private void openDetailActivity(String...details)
    {
        Intent i=new Intent(c, MedHistoryDetailActivity.class);
        i.putExtra("DATE_KEY",details[0]);
        i.putExtra("DIAGNOSIS_KEY",details[1]);
        i.putExtra("HOSPITAL_KEY",details[2]);
        i.putExtra("PRESCRIPTED_KEY",details[3]);
        i.putExtra("ADVICE_KEY",details[4]);
        i.putExtra("DOCTOR_KEY",details[5]);
        i.putExtra("FOLLOWUP_KEY",details[6]);

        c.startActivity(i);
    }
}
