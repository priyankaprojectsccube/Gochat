package com.ccube9.gochat.Challenge.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ccube9.gochat.Challenge.Activity.ChallangesubcategoryActivity;
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.POJO;

import java.util.Collections;
import java.util.List;

public class ChallangeCategoryAdapter  extends RecyclerView.Adapter<ChallangeCategoryAdapter.MyViewHolder> {


    private List<POJO> challengesList;
    private Context context;
    OnItemClickListener onItemClickListener;

    public ChallangeCategoryAdapter(List<POJO> challengesList, Context context) {
        this.challengesList = challengesList;
        this.context = context;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView challenge;
        View mMainView;
        MyViewHolder(View view) {
            super(view);
            mMainView=view;

            challenge = (TextView) view.findViewById(R.id.challenge_textView);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.OnItemClick(getAdapterPosition());
            }
        }
    }



    @NonNull
    @Override
    public ChallangeCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_challengecategory, parent, false);

        return new ChallangeCategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChallangeCategoryAdapter.MyViewHolder holder, final int position) {

        holder.challenge.setText(challengesList.get(position).getCategoryname());

    }


    @Override

    public int getItemCount()
    {
        return challengesList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
