package com.takecare.takecare.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.takecare.takecare.Interface.ItemClickListener;
import com.takecare.takecare.R;


public class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView txt_item_text, txt_child_item_text, txt_child_symptoms, txt_child_causes, txt_child_prevention, txt_child_sideeffect
            ,txt_child_precautions, txt_child_scientificname, txt_child_commonname, txt_child_origin, txt_child_medicinaluses
            ,txt_item_date ,txt_child_placeofconsultation, txt_item_diagnosis, txt_child_diagnosis, txt_child_prescription, txt_child_otheradvice;

    public RelativeLayout button;
    public ExpandableLinearLayout expandableLinearLayout;

    public View mView;

    ItemClickListener iItemClickListener;

    public void setItemClickListener(ItemClickListener iItemClickListener) {
        this.iItemClickListener = iItemClickListener;
    }

    public ItemViewHolder(@NonNull View itemView, boolean isExpandable) {
        super(itemView);

        if(isExpandable){
            txt_item_text = (TextView)itemView.findViewById(R.id.txt_item_text);
            txt_child_item_text = (TextView)itemView.findViewById(R.id.txt_child_item_text);
            txt_child_symptoms = (TextView)itemView.findViewById(R.id.txt_child_symptoms);
            txt_child_causes = (TextView)itemView.findViewById(R.id.txt_child_causes);
            txt_child_prevention = (TextView)itemView.findViewById(R.id.txt_child_prevention);
            txt_child_sideeffect = (TextView)itemView.findViewById(R.id.txt_child_sideeffect);
            txt_child_precautions = (TextView)itemView.findViewById(R.id.txt_child_precautions);
            txt_child_scientificname = (TextView)itemView.findViewById(R.id.txt_child_scientificname);
            txt_child_commonname = (TextView)itemView.findViewById(R.id.txt_child_commonname);
            txt_child_origin = (TextView)itemView.findViewById(R.id.txt_child_origin);
            txt_child_medicinaluses = (TextView)itemView.findViewById(R.id.txt_child_medicinaluses);

            txt_item_date = (TextView)itemView.findViewById(R.id.txt_item_date);
            txt_item_diagnosis = (TextView)itemView.findViewById(R.id.txt_item_diagnosis);
            txt_child_placeofconsultation = (TextView)itemView.findViewById(R.id.txt_child_placeofconsultation);
            txt_child_diagnosis = (TextView)itemView.findViewById(R.id.txt_child_diagnosis);
            txt_child_prescription = (TextView)itemView.findViewById(R.id.txt_child_prescription);
            txt_child_otheradvice = (TextView)itemView.findViewById(R.id.txt_child_otheradvice);

            button = (RelativeLayout)itemView.findViewById(R.id.button);
            expandableLinearLayout = (ExpandableLinearLayout)itemView.findViewById(R.id.expandableLayout);
        }
        else {
            txt_item_text = (TextView)itemView.findViewById(R.id.txt_item_text);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iItemClickListener.onClick(view,getAdapterPosition(), false);
            }
        });
    }
}
