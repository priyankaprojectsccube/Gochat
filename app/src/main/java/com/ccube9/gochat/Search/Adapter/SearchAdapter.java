package com.ccube9.gochat.Search.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.ccube9.gochat.Challenge.Activity.ChallengeEditActivity;
import com.ccube9.gochat.Challenge.Activity.CharityEditActivity;
import com.ccube9.gochat.Challenge.Activity.TrainingEditActivity;
import com.ccube9.gochat.Home.Adapter.ChallengesRecyclerAdapter;
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {



    private Context context;
    OnItemClickListener onItemClickListener;

    private ArrayList<String> subarrylist = new ArrayList<>();
    private List<POJO> searchchallengesList = Collections.emptyList();
    int checkarraysize;
    private TransparentProgressDialog pd;
    SearchAdapter.MyViewHolder myViewHolder;

    public SearchAdapter(List<POJO> searchchallengesList, Context context) {
        this.searchchallengesList = searchchallengesList;
        this.context = context;

    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView,reward_imageView1,faviourate_imageView1,delete_imageView1;
        TextView challenge_title,location_imageView1;
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
            pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
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
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search, parent, false);

        return new SearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SearchAdapter.MyViewHolder holder, final int position) {
        myViewHolder = holder;


        if (searchchallengesList.get(position).getFavourite().equals("1")) {
            holder.faviourate_imageView1.setImageResource(R.drawable.heart_redfill);
        }

        if (searchchallengesList.get(position).getFavourite().equals("0")) {
            holder.faviourate_imageView1.setImageResource(R.drawable.heart_outline);
        }

        holder.challenge_title.setText(searchchallengesList.get(position).getTitle());
        holder.location_imageView1.setText(searchchallengesList.get(position).getLocation());

        if(searchchallengesList.get(position).getSubcribeImages() != null) {
            subarrylist = searchchallengesList.get(position).getSubcribeImages();
            Log.d("positionchallenge"+position,"size"+subarrylist.size());
            checkarraysize = subarrylist.size();
            if (checkarraysize == 0) {
                holder.ci1.setVisibility(View.INVISIBLE);
                holder.ci2.setVisibility(View.INVISIBLE);
                holder.ci3.setVisibility(View.INVISIBLE);
            } else if (checkarraysize == 1) {
                String imageurl;
                holder.ci1.setVisibility(View.VISIBLE);
                holder.ci2.setVisibility(View.INVISIBLE);
                holder.ci3.setVisibility(View.INVISIBLE);

                imageurl = Base_url.concat(searchchallengesList.get(position).getSubcribeImages().get(0));
                Picasso.with(context).load(imageurl).error(R.drawable.default_profile).into(holder.ci1);
//                for (int i = 0; i < checkarraysize; i++) {
//                    String imageurl = Base_url.concat(challengesList.get(position).getSubcribeImages().get(i));
//                    new ChallengesRecyclerAdapter.MyAsyncTask().execute(imageurl);
//                }
            } else if (checkarraysize == 2) {
                String imag1,img2;
                holder.ci1.setVisibility(View.VISIBLE);
                holder.ci2.setVisibility(View.VISIBLE);
                holder.ci3.setVisibility(View.INVISIBLE);
//                for (int i = 0; i < checkarraysize; i++) {
//                    String imageurl = Base_url.concat(challengesList.get(position).getSubcribeImages().get(i));
//                    new ChallengesRecyclerAdapter.MyAsyncTask().execute(imageurl);
//                }

                imag1 = Base_url.concat(searchchallengesList.get(position).getSubcribeImages().get(0));
                img2 =  Base_url.concat(searchchallengesList.get(position).getSubcribeImages().get(1));

                Picasso.with(context).load(imag1).error(R.drawable.default_profile).into(holder.ci1);
                Picasso.with(context).load(img2).error(R.drawable.default_profile).into(holder.ci2);

            } else if (checkarraysize == 3) {
                String imag1,img2,img3;

                holder.ci1.setVisibility(View.VISIBLE);
                holder.ci2.setVisibility(View.VISIBLE);
                holder.ci3.setVisibility(View.VISIBLE);

                imag1 = Base_url.concat(searchchallengesList.get(position).getSubcribeImages().get(0));
                img2 =  Base_url.concat(searchchallengesList.get(position).getSubcribeImages().get(1));
                img3 =  Base_url.concat(searchchallengesList.get(position).getSubcribeImages().get(2));
                Picasso.with(context).load(imag1).error(R.drawable.default_profile).into(holder.ci1);
                Picasso.with(context).load(img2).error(R.drawable.default_profile).into(holder.ci2);
                Picasso.with(context).load(img3).error(R.drawable.default_profile).into(holder.ci3);
//                for (int i = 0; i < checkarraysize; i++) {
//                    String imageurl = Base_url.concat(challengesList.get(position).getSubcribeImages().get(i));
//                    new ChallengesRecyclerAdapter.MyAsyncTask().execute(imageurl);
//                }
            }
//            for (int i = 0; i < checkarraysize; i++) {
//                String imageurl = Base_url.concat(challengesList.get(position).getSubcribeImages().get(i));
//                new ChallengesRecyclerAdapter.MyAsyncTask().execute(imageurl);
//            }
        }




        holder.faviourate_imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if (type == 0) {

                    if(searchchallengesList.get(position).getFavourite().equals("0")) {
                        pd.show();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.add_to_fav, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pd.dismiss();
                                Log.d("dfgghfghd", response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("1")) {
                                        searchchallengesList.get(position).setFavourite("1");
                                        holder.faviourate_imageView1.setImageResource(R.drawable.heart_redfill);

                                    }
                                    //      Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                                    message = context.getResources().getString(R.string.connectiontimeout);
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
                                param.put("language", "1");
                                param.put("main_challenge_id", searchchallengesList.get(position).getMainchallengeid());

                                return param;
                            }
                        };

                        MySingleton.getInstance(context).addToRequestQueue(stringRequest);


                    }

                    else if(searchchallengesList.get(position).getFavourite().equals("1")){
                        pd.show();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.remove_from_fav, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pd.dismiss();

                                Log.d("dfgghfghd", response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("1")) {

                                        searchchallengesList.get(position).setFavourite("0");
                                        holder.faviourate_imageView1.setImageResource(R.drawable.heart_outline);
                                    }
                                    // Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                                    message = context.getResources().getString(R.string.connectiontimeout);
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
                                // param.put("language", "1");
                                param.put("main_challenge_id", searchchallengesList.get(position).getMainchallengeid());

                                return param;
                            }
                        };

                        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
                    }

      //          }

//                else{
//
//
//                    if(searchchallengesList.get(position).getChallengetype().equals("1")) {
//                        Intent intent = new Intent(context, ChallengeEditActivity.class);
//                        intent.putExtra("challengedetails",searchchallengesList.get(position));
//                        context.startActivity(intent);
//                    }
//
//                    if(searchchallengesList.get(position).getChallengetype().equals("2")) {
//                        Intent intent = new Intent(context, CharityEditActivity.class);
//                        intent.putExtra("challengedetails",searchchallengesList.get(position));
//                        context.startActivity(intent);
//                    }
//
//                    if(searchchallengesList.get(position).getChallengetype().equals("3")) {
//                        Intent intent = new Intent(context, TrainingEditActivity.class);
//                        intent.putExtra("challengedetails",searchchallengesList.get(position));
//                        context.startActivity(intent);
//                    }
//                }
            }

        });



        if(searchchallengesList.get(position).getChallengetype().equals("1")){
            holder.reward_imageView1.setImageResource(R.drawable.challengeicon);
        }

        if(searchchallengesList.get(position).getChallengetype().equals("2")){
            holder.reward_imageView1.setImageResource(R.drawable.charityicon);
        }

        if(searchchallengesList.get(position).getChallengetype().equals("3")){
            holder.reward_imageView1.setImageResource(R.drawable.trainicon);
        }

        if(searchchallengesList.get(position).getImage()!=null) {
            Picasso.with(context).load(WebUrl.Base_url.concat(searchchallengesList.get(position).getImage())).error(R.drawable.splashscreen).into(holder.imageView);
        }
    }


    @Override
    public int getItemCount()
    {
        return searchchallengesList.size();
    }
}
