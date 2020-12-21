package com.ccube9.gochat.News.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import com.ccube9.gochat.News.Adapter.StoryAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.MyStory;
import com.ccube9.gochat.Util.OnStoryChangedCallback;
import com.ccube9.gochat.Util.PROMO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.STORY;
import com.ccube9.gochat.Util.STORYDETAILS;
import com.ccube9.gochat.Util.StoryClickListeners;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





public class ViewStory extends AppCompatActivity {
String userid;
    private TransparentProgressDialog pd;
//    private List<STORYDETAILS> storydetailsList = new ArrayList<>();
ArrayList<MyStory> storydetailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);


        Intent i = getIntent();
        if (i.hasExtra("idofstory")) {

            userid = i.getStringExtra("idofstory");

            Log.d("idofstory",userid);

            callstorydetaillist(userid);
        } else {
            // Do something else
        }
    }

    private void callstorydetaillist(String userid) {

        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_users_all_story, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("get_users_all_story", response);
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
                            storydetails.setDate(jsonObject1.getString("created_date"));
//                            storydetails.setIs_delete(jsonObject1.getString("is_delete"));
//                            storydetails.setStatus(jsonObject1.getString("status"));
                            storydetailsList.add(storydetails);


                        }

                       storydisaplay();



                    } else {

                        Toast.makeText(ViewStory.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    message = getResources().getString(R.string.cannotconnectinternate);
                } else if (volleyError instanceof ServerError) {
                    message = getResources().getString(R.string.servernotfound);
                } else if (volleyError instanceof AuthFailureError) {
                    message = getResources().getString(R.string.loginagain);
                } else if (volleyError instanceof ParseError) {
                    message = getResources().getString(R.string.tryagain);
                } else if (volleyError instanceof NoConnectionError) {
                    message = getResources().getString(R.string.cannotconnectinternate);
                } else if (volleyError instanceof TimeoutError) {
                    message = getResources().getString(R.string.connectiontimeout);
                }
                if (message != null) {
                    pd.dismiss();
                    Toast.makeText(ViewStory.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ViewStory.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", userid);
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ViewStory.this).addToRequestQueue(stringRequest);

    }

    private void storydisaplay() {
        if(storydetailsList.size() > 0) {
            new StoryView.Builder(getSupportFragmentManager())
                    .setStoriesList(storydetailsList)
                    .setStoryDuration(5000)
//                    .setTitleText("")
//                    .setSubtitleText("")
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
            Toast.makeText(ViewStory.this,"No Story Found",Toast.LENGTH_SHORT).show();
        }

    }
}