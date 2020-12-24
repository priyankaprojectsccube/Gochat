package com.ccube9.gochat.Pot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.Profile.Adapter.FollowerAdapter;
import com.ccube9.gochat.Profile.FollowerActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.ParticipatedList;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class ParticipateListAdapter extends RecyclerView.Adapter<ParticipateListAdapter.MyViewHolder> {

    private TransparentProgressDialog pd;

    private List<ParticipatedList> participatedLists = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;
    String strfollowerid;

    public ParticipateListAdapter(List<ParticipatedList> participatedLists, Context context) {
        this.participatedLists = participatedLists;
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

//            linearLayout = view.findViewById(R.id.challenge_linearLayout);


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
    public ParticipateListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_participatedlist, parent, false);

        return new ParticipateListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ParticipateListAdapter.MyViewHolder holder, final int position) {




        holder.username.setText(participatedLists.get(position).getFirst_name());
        holder.amount.setText(participatedLists.get(position).getAmount()+" "+"€");



        if(participatedLists.get(position).getProfile_image()!=null) {
            String imageurl = Base_url.concat(participatedLists.get(position).getProfile_image());
            Picasso.with(context).load(imageurl).error(R.drawable.splashscreen).into(holder.profile_image);
        }
    }




    @Override
    public int getItemCount()
    {
        return participatedLists.size();
    }
}
