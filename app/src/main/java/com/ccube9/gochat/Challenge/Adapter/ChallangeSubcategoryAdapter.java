package com.ccube9.gochat.Challenge.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.POJO;

import java.util.ArrayList;
import java.util.List;
import static com.ccube9.gochat.Challenge.Activity.ChallangesubcategoryActivity.selectedsubcatarr;

public class ChallangeSubcategoryAdapter extends RecyclerView.Adapter<ChallangeSubcategoryAdapter.MyViewHolder> {


    private List<POJO> challengesList;
    private ArrayList<String> selectedsubcatidremovable;
    private Context context;

    public ChallangeSubcategoryAdapter(List<POJO> challengesList,ArrayList<String> selectedsubcatidremovable, Context context) {
        this.challengesList = challengesList;
        this.context = context;
        this.selectedsubcatidremovable=selectedsubcatidremovable;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView challenge_textView;
        CheckBox subcat_checkBox;
        View mMainView;
        MyViewHolder(View view) {
            super(view);
            mMainView=view;

            challenge_textView = (TextView) itemView.findViewById(R.id.textsubcat);
            subcat_checkBox = (CheckBox) itemView.findViewById(R.id.subcat_checkBox);


        }


    }



    @NonNull
    @Override
    public ChallangeSubcategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_challengesubcat, parent, false);

        return new ChallangeSubcategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChallangeSubcategoryAdapter.MyViewHolder holder, final int position) {

        for(int i=0;i<selectedsubcatidremovable.size();i++){
            if (!selectedsubcatarr.contains(selectedsubcatidremovable.get(i))) {
                selectedsubcatarr.add(selectedsubcatidremovable.get(i));
            }

            if(challengesList.get(position).getId().equals(selectedsubcatidremovable.get(i))){
                holder.subcat_checkBox.setChecked(true);
            }

        }


        holder.challenge_textView.setText(challengesList.get(position).getSubcategoryname());

        holder.subcat_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = holder.subcat_checkBox.isChecked();

                    if (isChecked) {
                        if (!selectedsubcatarr.contains(challengesList.get(position).getId()))
                            selectedsubcatarr.add( challengesList.get(position).getId());

                    } else {
                        selectedsubcatarr.remove(challengesList.get(position).getId());

                    }


            }
        });

    }


    @Override
    public int getItemCount()
    {
        return challengesList.size();
    }
}
