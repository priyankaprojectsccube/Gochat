package com.ccube9.gochat.Profile.Adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ccube9.gochat.Home.Adapter.AcceptedChallangeAdapter;
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.Profile.FollowerActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class MyChallengeAdapter extends RecyclerView.Adapter<MyChallengeAdapter.MyViewHolder> {
    private ArrayList imagearrbitmap = new ArrayList();
    private ArrayList<String> subarrylist = new ArrayList<>();
    private List<POJO> acceptchallengelist = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;
    MyChallengeAdapter.MyViewHolder myViewHolder;
    int checkarraysize, checkbitmapsize;

    public MyChallengeAdapter(List<POJO> acceptchallengelist, Context context) {
        this.acceptchallengelist = acceptchallengelist;
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
        private CircleImageView ci1,ci2,ci3;
        MyViewHolder(View view) {
            super(view);

            ci1 = view.findViewById(R.id.ci1);
            ci2 = view.findViewById(R.id.ci2);
            ci3 = view.findViewById(R.id.ci3);

            imageView = view.findViewById(R.id.imageView1);
            delete_imageView1= view.findViewById(R.id.delete_imageView1);
            challenge_title=(TextView) view.findViewById(R.id.challenge_title_textView1);
            location_imageView1=(TextView) view.findViewById(R.id.location_imageView1);
            reward_imageView1=(ImageView) view.findViewById(R.id.reward_imageView1);
            faviourate_imageView1=(ImageView) view.findViewById(R.id.faviourate_imageView1);

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
    public MyChallengeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_my_challenge_adapter, parent, false);

        return new MyChallengeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyChallengeAdapter.MyViewHolder holder, final int position) {
        Log.d("sdfdsfdsf",acceptchallengelist.get(position).getTitle());

        myViewHolder = holder;
//        if(acceptchallengelist.get(position).getImage()!=null){
//            Picasso.with(context).load(WebUrl.Base_url.concat(acceptchallengelist.get(position).getImage())).into(new Target() {
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
//        }

        holder.challenge_title.setText(acceptchallengelist.get(position).getTitle());
        holder.location_imageView1.setText(acceptchallengelist.get(position).getLocation());

        if(acceptchallengelist.get(position).getChallengetype().equals("1")){
            holder.reward_imageView1.setImageResource(R.drawable.challengeicon);
        }

        if(acceptchallengelist.get(position).getChallengetype().equals("2")){
            holder.reward_imageView1.setImageResource(R.drawable.charityicon);
        }

        if(acceptchallengelist.get(position).getChallengetype().equals("3")){
            holder.reward_imageView1.setImageResource(R.drawable.trainicon);
        }

        if(acceptchallengelist.get(position).getImage()!=null) {
            Picasso.with(context).load(WebUrl.Base_url.concat(acceptchallengelist.get(position).getImage())).error(R.drawable.splashscreen).into(holder.imageView);
        }

        subarrylist = acceptchallengelist.get(position).getSubcribeImages();
        checkarraysize = subarrylist.size();
        if(checkarraysize == 0){
            holder.ci1.setVisibility(View.INVISIBLE);
            holder.ci2.setVisibility(View.INVISIBLE);
            holder.ci3.setVisibility(View.INVISIBLE);
        }
        else if(checkarraysize == 1){
            holder.ci1.setVisibility(View.VISIBLE);
            holder.ci2.setVisibility(View.INVISIBLE);
            holder.ci3.setVisibility(View.INVISIBLE);
        }
        else  if(checkarraysize == 2)
        {
            holder.ci1.setVisibility(View.VISIBLE);
            holder.ci2.setVisibility(View.VISIBLE);
            holder.ci3.setVisibility(View.INVISIBLE);
        }
        else if(checkarraysize == 3){
            holder.ci1.setVisibility(View.VISIBLE);
            holder.ci2.setVisibility(View.VISIBLE);
            holder.ci3.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < checkarraysize; i++) {
            String imageurl = Base_url.concat(acceptchallengelist.get(position).getSubcribeImages().get(i));
            new MyChallengeAdapter.MyAsyncTask().execute(imageurl);
        }


    }


    @Override
    public int getItemCount()
    {
        return acceptchallengelist.size();
    }
    private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                imagearrbitmap.add(myBitmap);
                return myBitmap;
            } catch(IOException e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            //do what you want with your bitmap result on the UI thread
            checkbitmapsize = imagearrbitmap.size();
            if (checkbitmapsize == 1) {
                Log.d("test","adapter");
                myViewHolder.ci1.setImageBitmap((Bitmap) imagearrbitmap.get(0));
                myViewHolder.ci2.setVisibility(View.INVISIBLE);
                myViewHolder.ci3.setVisibility(View.INVISIBLE);
            }
            else if(checkbitmapsize == 2){
                myViewHolder.ci1.setImageBitmap((Bitmap) imagearrbitmap.get(0));
                myViewHolder.ci2.setImageBitmap((Bitmap) imagearrbitmap.get(1));
                myViewHolder.ci3.setVisibility(View.INVISIBLE);
            }
            else if (checkbitmapsize == 3){
                myViewHolder.ci1.setImageBitmap((Bitmap) imagearrbitmap.get(0));
                myViewHolder.ci2.setImageBitmap((Bitmap) imagearrbitmap.get(1));
                myViewHolder.ci3.setImageBitmap((Bitmap) imagearrbitmap.get(2));
            }
        }


    }
}