package com.ccube9.gochat.News.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ccube9.gochat.News.Activity.NewsFunctions;
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.Profile.Adapter.FollowerAdapter;
import com.ccube9.gochat.Profile.FollowerActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.MyStory;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class MyStoryPageAdapter extends RecyclerView.Adapter<MyStoryPageAdapter.MyViewHolder> {

    private TransparentProgressDialog pd;
    private List<MyStory> myStoryList = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;
    String id;

    public MyStoryPageAdapter(List<MyStory> myStoryList, Context context) {
        this.myStoryList = myStoryList;
        this.context = context;
        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profile_image;
        TextView username;
        ImageView remove,play;
        LinearLayout linearLayout;
        TextView txtview;

        MyViewHolder(View view) {
            super(view);
            profile_image = view.findViewById(R.id.profile_image);

            username=(TextView) view.findViewById(R.id.username);
            remove=(ImageView) view.findViewById(R.id.remove);
            play =(ImageView) view.findViewById(R.id.play);

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
    public MyStoryPageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_mystorypage, parent, false);

        return new MyStoryPageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyStoryPageAdapter.MyViewHolder holder, final int position) {





        holder.username.setText(myStoryList.get(position).getFirst_name()+" "+myStoryList.get(position).getLast_name());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = myStoryList.get(position).getId();
                removefollower(id);
            }
        });


        if(myStoryList.get(position).getUrl()!=null) {
            String url = myStoryList.get(position).getUrl();
            if (url.contains(".mp4")) {
                Log.d("checkurlv",url);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.splashscreen);
                requestOptions.error(R.drawable.splashscreen);


                Glide.with(context)
                        .load(url)
                        .apply(requestOptions)
                        .thumbnail(Glide.with(context).load(url))
                        .into(holder.profile_image);
                holder.play.setVisibility(View.VISIBLE);
            } else {
                Log.d("checkurli",url);
                holder.play.setVisibility(View.GONE);
                Picasso.with(context).load(url).into(holder.profile_image);
            }
        }
    }

    private void removefollower(String id) {

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.delete_story_images, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("dfgghfghd", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
Intent i = new Intent(context, NewsFunctions.class);
context.startActivity(i);
                    }
                    else{
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("fdgfgd", volleyError.toString());
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = context.getResources().getString(R.string.cannotconnectinternate);
                } else if (volleyError instanceof ServerError) {
                    message = context.getResources().getString(R.string.servernotfound);
                } else if (volleyError instanceof AuthFailureError) {
                    message = context.getResources().getString(R.string.loginagain);
                } else if (volleyError instanceof ParseError) {
                    message = context.getResources().getString(R.string.tryagain);
                } else if (volleyError instanceof NoConnectionError) {
                    message = context.getResources().getString(R.string.cannotconnectinternate);
                } else if (volleyError instanceof TimeoutError) {
                    message =context.getResources().getString(R.string.connectiontimeout);
                }
                if (message != null) {

                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(context));
                param.put("id",id);
                // param.put("language", "1");


                return param;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);




    }


    @Override
    public int getItemCount()
    {
        return myStoryList.size();
    }
}
