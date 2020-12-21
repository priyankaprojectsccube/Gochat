package com.ccube9.gochat.Pot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
import com.ccube9.gochat.Pot.Activity.Pot_Chatlenge;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Splash.POTLIST;
import com.ccube9.gochat.Util.AlbumList;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.MyViewHolder> {

    private TransparentProgressDialog pd;

    private List<AlbumList> albumLists = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;


    public AlbumListAdapter(List<AlbumList> albumLists, Context context) {
        this.albumLists = albumLists;
        this.context = context;
        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView1;

        LinearLayout linearLayout;


        MyViewHolder(View view) {
            super(view);
            imageView1 = view.findViewById(R.id.imageView1);


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
    public AlbumListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_albumlist, parent, false);

        return new AlbumListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlbumListAdapter.MyViewHolder holder, final int position) {









        if(albumLists.get(position).getImage()!=null) {
            String imageurl = Base_url.concat(albumLists.get(position).getImage());
            Log.d("img",albumLists.get(position).getImage());
            Picasso.with(context).load(imageurl).error(R.drawable.splashscreen).into(holder.imageView1);
        }


    }




    @Override
    public int getItemCount()
    {
        return albumLists.size();
    }
}

