package com.ccube9.gochat.Profile.Adapter;

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
import com.ccube9.gochat.Challenge.Activity.ChallengeDetailActivity;
import com.ccube9.gochat.Challenge.Activity.FavouriteChallengeActivity;
import com.ccube9.gochat.Challenge.Adapter.FavouriteChallengeAdapter;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.MyViewHolder> {

    private TransparentProgressDialog pd;
    private List<POJO> followerArrList = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;
    String strfollowerid;

    public FollowerAdapter(List<POJO> followerArrList, Context context) {
        this.followerArrList = followerArrList;
        this.context = context;
        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profile_image;
        TextView username,remove;
        LinearLayout linearLayout;
        TextView txtview;

        MyViewHolder(View view) {
            super(view);
            profile_image = view.findViewById(R.id.profile_image);

            username=(TextView) view.findViewById(R.id.username);
            remove=(TextView) view.findViewById(R.id.remove);

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
    public FollowerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_follower, parent, false);

        return new FollowerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FollowerAdapter.MyViewHolder holder, final int position) {




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

        holder.username.setText(followerArrList.get(position).getUsername());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strfollowerid = followerArrList.get(position).getFollowerID();
                 removefollower(strfollowerid);
            }
        });


        if(followerArrList.get(position).getUserprofpic()!=null) {
            String imageurl = Base_url.concat(followerArrList.get(position).getUserprofpic());
            Picasso.with(context).load(imageurl).error(R.drawable.splashscreen).into(holder.profile_image);
        }
    }

    private void removefollower(String strfollowerid) {

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.Unfollowuser_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("dfgghfghd", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        Intent intent = new Intent(context, FollowerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                param.put("follower_id",strfollowerid);
               // param.put("language", "1");


                return param;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);




    }


    @Override
    public int getItemCount()
    {
        return followerArrList.size();
    }
}
