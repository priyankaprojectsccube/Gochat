package com.ccube9.gochat.Profile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ccube9.gochat.R;
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.Util.POJO;

import java.util.List;

public class UsertypeAdapter extends RecyclerView.Adapter<UsertypeAdapter.MyViewHolder> {


    private List<POJO> challengesList;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public UsertypeAdapter(List<POJO> challengesList, Context context) {
        this.challengesList = challengesList;
        this.context = context;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView username;

        MyViewHolder(View view) {
            super(view);

            username = (TextView) view.findViewById(R.id.username);
            itemView.setOnClickListener(this);
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
    public UsertypeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_usertype, parent, false);

        return new UsertypeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UsertypeAdapter.MyViewHolder holder, final int position) {

        holder.username.setText(challengesList.get(position).getUsertype());

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

