package com.ccube9.gochat.Pot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.Pot.Activity.Pot_Chatlenge;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MYPOTLIST;
import com.ccube9.gochat.Util.SEARCHPOTLIST;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class SearchPotAdapter extends RecyclerView.Adapter<SearchPotAdapter.MyViewHolder> {


    private List<SEARCHPOTLIST> searchpotlists = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;


    public SearchPotAdapter(List<SEARCHPOTLIST> searchpotlists, Context context) {
        this.searchpotlists = searchpotlists;
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
    public SearchPotAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_pots_adapter, parent, false);

        return new SearchPotAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SearchPotAdapter.MyViewHolder holder, final int position) {




        holder.firstname.setText(searchpotlists.get(position).getFirst_name());
        holder.about.setText(searchpotlists.get(position).getAbout_pot());


        if(searchpotlists.get(position).getBanner_for_association()!=null) {
            Picasso.with(context).load(WebUrl.Base_url.concat(searchpotlists.get(position).getBanner_for_association())).error(R.drawable.splashscreen).into(holder.imageView);
        }

        holder.category_cardview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pot_id =  searchpotlists.get(position).getId();
                Intent intent = new Intent(context, Pot_Chatlenge.class);
                intent.putExtra("pot_id",pot_id);
                Log.d("Pot_id",pot_id);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return searchpotlists.size();
    }
}

