package com.ccube9.gochat.News.Adapter;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ccube9.gochat.News.Activity.StoryView;
import com.ccube9.gochat.News.Activity.ViewStory;
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.Profile.Adapter.FollowerAdapter;
import com.ccube9.gochat.Profile.FollowerActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.MyStory;
import com.ccube9.gochat.Util.OnStoryChangedCallback;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.STORY;
import com.ccube9.gochat.Util.StoryClickListeners;
import com.ccube9.gochat.Util.StoryViewHeaderInfo;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class StoryAdapter  extends RecyclerView.Adapter<StoryAdapter.MyViewHolder> {

    private TransparentProgressDialog pd;
    private List<STORY> storyList = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;
    ArrayList<MyStory> storydetailsList = new ArrayList<>();
    ArrayList<StoryViewHeaderInfo> storyViewHeaderInfoArrayList = new ArrayList<>();
    public StoryAdapter(List<STORY> storyList, Context context) {
        this.storyList = storyList;
        this.context = context;
        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profile_image;
        TextView username;
        LinearLayout borderlayout;


        MyViewHolder(View view) {
            super(view);
            profile_image = view.findViewById(R.id.profile_image);
            username=(TextView) view.findViewById(R.id.username);
            borderlayout = (LinearLayout)view.findViewById(R.id.borderlayout) ;




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
    public StoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_story_list, parent, false);

        return new StoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StoryAdapter.MyViewHolder holder, final int position) {
//0 story is there  1 story viewd
if(storyList.get(position).getFlag_story().equals("0")){
    holder.borderlayout.setBackground(context.getResources().getDrawable(R.drawable.circle_red));
}else {
    holder.borderlayout.setBackground(context.getResources().getDrawable(R.drawable.circle_grey));
}

if(storyList.get(position).getFirst_name() != null){
    holder.username.setText(storyList.get(position).getFirst_name());//+" "+storyList.get(position).getLast_name()
}


        if(storyList.get(position).getProfile_image()!=null) {
            String imageurl = Base_url.concat(storyList.get(position).getProfile_image());
            Picasso.with(context).load(imageurl).error(R.drawable.splashscreen).into(holder.profile_image);
        }


        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.borderlayout.setBackground(context.getResources().getDrawable(R.drawable.circle_grey));
                String id = null;
                if (storyList.get(position).getStory_user_id() != null){
                    id = storyList.get(position).getStory_user_id();
                    callstorydetaillist(id);
                }


//                Intent intent = new Intent(context, ViewStory.class);
//
//                intent.putExtra("idofstory",id);
//
//                context.startActivity(intent);
            }
        });
    }

    private void callstorydetaillist(String id) {
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_users_all_story, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("get_users_all_story", response);
                storyViewHeaderInfoArrayList.clear();
                storydetailsList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("user_story_details");


                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                            MyStory storydetails = new MyStory();



//                            storydetails.setFirst_name(jsonObject1.getString("first_name"));
//                            storydetails.setLast_name(jsonObject1.getString("last_name"));
//                            storydetails.setProfile_image(jsonObject1.getString("profile_image"));
//                            storydetails.setId(jsonObject1.getString("id"));
//                            storydetails.setUser_id(jsonObject1.getString("user_id"));
                            storydetails.setDescription(jsonObject1.getString("story_title"));
                            storydetails.setUrl(WebUrl.Base_url.concat(jsonObject1.getString("story_image")));
                            Log.d("checkurlstory",WebUrl.Base_url.concat(jsonObject1.getString("story_image")));
                            storydetails.setDate(jsonObject1.getString("created_date"));
//                            storydetails.setIs_delete(jsonObject1.getString("is_delete"));
//                            storydetails.setStatus(jsonObject1.getString("status"));
                            storydetailsList.add(storydetails);


                        }

                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                            StoryViewHeaderInfo storyViewHeaderInfo = new StoryViewHeaderInfo();



                            storyViewHeaderInfo.setTitle(jsonObject1.getString("first_name"));
                            storyViewHeaderInfo.setTitleIconUrl(WebUrl.Base_url.concat(jsonObject1.getString("profile_image")));
                            storyViewHeaderInfo.setSubtitle(jsonObject1.getString("created_date"));

                            storyViewHeaderInfoArrayList.add(storyViewHeaderInfo);


                        }
                        storydisaplay();



                    } else {

                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    pd.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();

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
                    pd.dismiss();
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", id);
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void storydisaplay() {

        if(storydetailsList.size() > 0) {
            new StoryView.Builder(  ((AppCompatActivity)context).getSupportFragmentManager())
                    .setStoriesList(storydetailsList)
                    .setStoryDuration(5000)
                    .setHeadingInfoList(storyViewHeaderInfoArrayList)
                    .setStoryClickListeners(new StoryClickListeners() {
                        @Override
                        public void onDescriptionClickListener(int position) {
                            // Toast.makeText(ViewStory.this, "Clicked: " + myStories.get(position).getDescription(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onTitleIconClickListener(int position) {

                        }
                    })
                    .setOnStoryChangedCallback(new OnStoryChangedCallback() {
                        @Override
                        public void storyChanged(int position) {
                            //  Toast.makeText(ViewStory.this, position + "", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setStartingIndex(0)

                    .build()
                    .show();


        }
        else {
            Toast.makeText(context,"No Story Found",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public int getItemCount()
    {
        return storyList.size();
    }
}

