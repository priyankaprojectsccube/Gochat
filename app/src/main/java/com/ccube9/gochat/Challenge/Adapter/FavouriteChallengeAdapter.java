package com.ccube9.gochat.Challenge.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class FavouriteChallengeAdapter extends RecyclerView.Adapter<FavouriteChallengeAdapter.MyViewHolder> {


    private List<POJO> favoritechallangeArrList = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;


    public FavouriteChallengeAdapter(List<POJO> favoritechallangeArrList, Context context) {
        this.favoritechallangeArrList = favoritechallangeArrList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView,reward_imageView1,faviourate_imageView1,delete_imageView1;
        TextView challenge_title,location_imageView1;
        LinearLayout linearLayout;
        TextView txtview;

        MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView1);
            delete_imageView1= view.findViewById(R.id.delete_imageView1);
            challenge_title=(TextView) view.findViewById(R.id.challenge_title_textView1);
            location_imageView1=(TextView) view.findViewById(R.id.location_imageView1);
            reward_imageView1=(ImageView) view.findViewById(R.id.reward_imageView1);
            faviourate_imageView1=(ImageView) view.findViewById(R.id.faviourate_imageView1);
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
    public FavouriteChallengeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_favoritechallange, parent, false);

        return new FavouriteChallengeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavouriteChallengeAdapter.MyViewHolder holder, final int position) {
        Log.d("sdfdsfdsf",favoritechallangeArrList.get(position).getTitle());


//        if(favoritechallangeArrList.get(position).getImage()!=null){
//            Picasso.with(context).load(WebUrl.Base_url.concat(favoritechallangeArrList.get(position).getImage())).into(new Target() {
//
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    holder.linearLayout.setBackground(new BitmapDrawable(context.getResources(), bitmap));
//                }
//
//                @Override
//                public void onBitmapFailed(final Drawable errorDrawable) {
//                    holder.linearLayout.setBackground(context.getDrawable(R.drawable.splashscreen));
//                }
//
//                @Override
//                public void onPrepareLoad(final Drawable placeHolderDrawable) {
//
//                }
//            });
    //    }

        holder.challenge_title.setText(favoritechallangeArrList.get(position).getTitle());
        holder.location_imageView1.setText(favoritechallangeArrList.get(position).getLocation());

        if(favoritechallangeArrList.get(position).getChallengetype().equals("1")){
            holder.reward_imageView1.setImageResource(R.drawable.challengeicon);
        }

        if(favoritechallangeArrList.get(position).getChallengetype().equals("2")){
            holder.reward_imageView1.setImageResource(R.drawable.charityicon);
        }

        if(favoritechallangeArrList.get(position).getChallengetype().equals("3")){
            holder.reward_imageView1.setImageResource(R.drawable.trainicon);
        }

        if(favoritechallangeArrList.get(position).getImage()!=null) {
            Picasso.with(context).load(WebUrl.Base_url.concat(favoritechallangeArrList.get(position).getImage())).error(R.drawable.splashscreen).into(holder.imageView);
        }
    }


    @Override
    public int getItemCount()
    {
        return favoritechallangeArrList.size();
    }
}
