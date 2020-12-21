package com.ccube9.gochat.Challenge.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ccube9.gochat.Home.Adapter.AcceptedChallangeAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.ccube9.gochat.Util.WebUrl.Base_url;


public class AcceptedsubcatAdapter extends RecyclerView.Adapter<AcceptedsubcatAdapter.MyViewHolder> {


    private List<POJO> challengesList;
    private Context context;
 MyViewHolder myViewHolder;
    public AcceptedsubcatAdapter(List<POJO> challengesList, Context context) {
        this.challengesList = challengesList;
        this.context = context;

    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textsubcat;
        ImageView imagesubcat;
        View mMainView;
        MyViewHolder(View view) {
            super(view);
            mMainView=view;

            textsubcat = (TextView) itemView.findViewById(R.id.textsubcat);
            imagesubcat =itemView.findViewById(R.id.imagesubcat);


        }


    }



    @NonNull
    @Override
    public AcceptedsubcatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_acceptedsubcat, parent, false);

        return new AcceptedsubcatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AcceptedsubcatAdapter.MyViewHolder holder, final int position) {

        myViewHolder = holder;

        holder.textsubcat.setText(challengesList.get(position).getTitle());

        if(challengesList.get(position).getImage()!=null) {
            String imageurl = Base_url.concat(challengesList.get(position).getImage());
            new AcceptedsubcatAdapter.MyAsyncTask().execute(imageurl);
          //  Picasso.with(context).load(WebUrl.Base_url.concat(challengesList.get(position).getImage())).error(R.drawable.splashscreen).into(holder.imagesubcat);
        }

    }


    @Override
    public int getItemCount()
    {
        return challengesList.size();
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
               // imagearrbitmap.add(myBitmap);
                return myBitmap;
            } catch(IOException e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            //do what you want with your bitmap result on the UI thread


                myViewHolder.imagesubcat.setImageBitmap(result);


        }


    }
}
