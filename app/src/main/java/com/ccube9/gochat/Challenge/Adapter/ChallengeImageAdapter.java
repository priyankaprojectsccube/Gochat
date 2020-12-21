package com.ccube9.gochat.Challenge.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.R;
import java.io.File;
import java.util.ArrayList;

public class ChallengeImageAdapter extends RecyclerView.Adapter<ChallengeImageAdapter.MyViewHolder> {

    private ArrayList<Bitmap> arrayListbitmap=new ArrayList<>();
    private Context context;
    OnItemClickListener onItemClickListener;


    public ChallengeImageAdapter(ArrayList<Bitmap> arrayListbitmap, Context context) {
        this.arrayListbitmap = arrayListbitmap;
        this.context = context;


    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ChallengeImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_challengeimage, viewGroup, false);

        return new ChallengeImageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeImageAdapter.MyViewHolder myViewHolder, final int i) {


        myViewHolder.imgvew.setImageBitmap(arrayListbitmap.get(i));

//         myViewHolder.imgdelete.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 arrayListbitmap.remove(i);
//                 notifyDataSetChanged();
//             }
//         });

    }

    @Override
    public int getItemCount() {
        Log.d("hjhjgj", String.valueOf(arrayListbitmap.size()));
        return arrayListbitmap.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private ImageView imgvew,imgdelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgvew=itemView.findViewById(R.id.imgvew);
            imgdelete = itemView.findViewById(R.id.imgdelete);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (onItemClickListener != null) {
                onItemClickListener.OnItemClick(getAdapterPosition());
            }
        }
    }
}


