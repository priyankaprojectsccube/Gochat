package com.ccube9.gochat.Pot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.Pot.Activity.Create_Pot_Challenge_2;
import com.ccube9.gochat.Pot.Activity.PotList;
import com.ccube9.gochat.Pot.Activity.Pot_Challenge_Association;
import com.ccube9.gochat.Pot.Activity.Pot_Chatlenge;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Splash.POTLIST;
import com.ccube9.gochat.Util.ParticipatedList;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class PotListAdapter extends RecyclerView.Adapter<PotListAdapter.MyViewHolder> {

    private TransparentProgressDialog pd;

    private List<POTLIST> potListList = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;


    public PotListAdapter(List<POTLIST> potListList, Context context) {
        this.potListList = potListList;
        this.context = context;
        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profile_image;
        TextView username,amount;
        LinearLayout linearLayout;
        TextView txtview;

        MyViewHolder(View view) {
            super(view);
            profile_image = view.findViewById(R.id.profile_image);

            username=(TextView) view.findViewById(R.id.username);
            amount=(TextView) view.findViewById(R.id.amount);

            linearLayout = view.findViewById(R.id.ll);


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
    public PotListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_potlist, parent, false);

        return new PotListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PotListAdapter.MyViewHolder holder, final int position) {




        holder.username.setText(potListList.get(position).getFirst_name());
        Log.d("username",potListList.get(position).getFirst_name());




        if(potListList.get(position).getBanner_for_association()!=null) {
            String imageurl = Base_url.concat(potListList.get(position).getBanner_for_association());
            Log.d("img",potListList.get(position).getBanner_for_association());
            Picasso.with(context).load(imageurl).error(R.drawable.splashscreen).into(holder.profile_image);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pot_id =  potListList.get(position).getId();
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
        return potListList.size();
    }
}
