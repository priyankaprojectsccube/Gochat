package com.ccube9.gochat.Pot.Adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccube9.gochat.Pot.Activity.ProfileInstaActivity;
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.Pot.Activity.Pot_Chatlenge;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Splash.POTLIST;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class RecentPotsAdapter extends RecyclerView.Adapter<RecentPotsAdapter.MyViewHolder> {


    private List<POTLIST> potListArrayList = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;


    public RecentPotsAdapter(List<POTLIST> potListArrayList, Context context) {
        this.potListArrayList = potListArrayList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView firstname,about;

        CardView category_cardview1;

        MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView1);

            firstname=(TextView) view.findViewById(R.id.firstname);
            about=(TextView) view.findViewById(R.id.about);

            category_cardview1 = view.findViewById(R.id.category_cardview1);


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
    public RecentPotsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_pots_adapter, parent, false);

        return new RecentPotsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecentPotsAdapter.MyViewHolder holder, final int position) {




        holder.firstname.setText(potListArrayList.get(position).getFirst_name());
        holder.about.setText(potListArrayList.get(position).getAbout_pot());


        if(potListArrayList.get(position).getBanner_for_association()!=null) {
            Picasso.with(context).load(WebUrl.Base_url.concat(potListArrayList.get(position).getBanner_for_association())).error(R.drawable.splashscreen).into(holder.imageView);
        }

        holder.category_cardview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pot_id =  potListArrayList.get(position).getId();
                Intent intent = new Intent(context, Pot_Chatlenge.class);
                intent.putExtra("pot_id",pot_id);

                Log.d("Pot_id",pot_id);
                context.startActivity(intent);
            }
        });


        holder.firstname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id =  potListArrayList.get(position).getUserid();
                Intent intent = new Intent(context, ProfileInstaActivity.class);
                intent.putExtra("id",id);
                Log.d("id",id);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return potListArrayList.size();
    }
}
