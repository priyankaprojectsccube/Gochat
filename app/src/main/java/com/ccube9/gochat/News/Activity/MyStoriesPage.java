package com.ccube9.gochat.News.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.ccube9.gochat.News.Adapter.MyStoryPageAdapter;
import com.ccube9.gochat.News.Adapter.NewFunctionAdapter;
import com.ccube9.gochat.News.Adapter.StoryAdapter;
import com.ccube9.gochat.Profile.Adapter.FollowerAdapter;
import com.ccube9.gochat.Profile.FollowerActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.MyStory;
import com.ccube9.gochat.Util.MyStoryDetail;
import com.ccube9.gochat.Util.PROMO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.STORY;
import com.ccube9.gochat.Util.StoryViewHeaderInfo;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyStoriesPage extends AppCompatActivity {
    private TransparentProgressDialog pd;
    ImageView iv_back;
    TextView texttitle;

    private RecyclerView recviewstories;
    MyStoryPageAdapter myStoryPageAdapter;


    ArrayList<MyStory> storydetailsList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stories_page);

Log.d("testactivity","activity");
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);

        texttitle.setText("My Stories");
recviewstories = findViewById(R.id.recviewstories);
        pd = new TransparentProgressDialog(MyStoriesPage.this, R.drawable.ic_loader_image);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyStoriesPage.this,NewsFunctions.class);
                startActivity(i);
            }
        });

        callstorydetaillist();

    }

    private void callstorydetaillist() {
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_users_all_story, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("get_users_all_story_p", response);

                storydetailsList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("user_story_details");


                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                            MyStory storydetails = new MyStory();



                            storydetails.setFirst_name(jsonObject1.getString("first_name"));
                            storydetails.setLast_name(jsonObject1.getString("last_name"));
                            storydetails.setProfile_image(jsonObject1.getString("profile_image"));
                            storydetails.setId(jsonObject1.getString("id"));
                            storydetails.setUser_id(jsonObject1.getString("user_id"));
                            storydetails.setDescription(jsonObject1.getString("story_title"));
                            storydetails.setUrl(WebUrl.Base_url.concat(jsonObject1.getString("story_image")));
                            storydetails.setDate(jsonObject1.getString("created_date"));
                            storydetails.setIs_delete(jsonObject1.getString("is_delete"));
                            storydetails.setStatus(jsonObject1.getString("status"));
                            storydetailsList.add(storydetails);


                        }

if(storydetailsList.size() > 0){
    myStoryPageAdapter = new MyStoryPageAdapter(storydetailsList, MyStoriesPage.this);
    recviewstories.setLayoutManager(new LinearLayoutManager(MyStoriesPage.this,LinearLayoutManager.VERTICAL,false));
    recviewstories.setAdapter(myStoryPageAdapter);
}else {
    Toast.makeText(MyStoriesPage.this,"No Stories Found",Toast.LENGTH_SHORT).show();
}




                    } else {

                        Toast.makeText(MyStoriesPage.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    message =getResources().getString(R.string.tryagain);
                } else if (volleyError instanceof NoConnectionError) {
                    message = getResources().getString(R.string.cannotconnectinternate);
                } else if (volleyError instanceof TimeoutError) {
                    message = getResources().getString(R.string.connectiontimeout);
                }
                if (message != null) {
                    pd.dismiss();
                    Toast.makeText(MyStoriesPage.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyStoriesPage.this,getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(MyStoriesPage.this));
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(MyStoriesPage.this).addToRequestQueue(stringRequest);
    }
}