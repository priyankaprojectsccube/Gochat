package com.ccube9.gochat.News.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.ccube9.gochat.News.Adapter.RepliesAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.Replies;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RepliesActivity  extends AppCompatActivity {
    private TransparentProgressDialog pd;
    ImageView iv_back;
    TextView texttitle;
    private RecyclerView recvewrply;
    private List<Replies> replyList=new ArrayList<>();
    private RepliesAdapter repliesAdapter;
    CircleImageView profile_image,replyprofilepic;
    TextView postcmt,replypersonname,replypersoncmt,replylikecount,reply;
    EditText writecmt;
    String adv_id,user_id,profile,strfirstname,strlastname,strprofileimage,strcomments,strlikecount,commentid;
    private ArrayList<String> arrreplayfirstname;
    private ArrayList<String>  arrreplaylastname;
    private ArrayList<String> arrreplyprofileimage;
    private ArrayList<String> arrreplycomments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replies);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        postcmt = findViewById(R.id.postcmt);
        texttitle=findViewById(R.id.texttitle);
        iv_back=findViewById(R.id.iv_back);
        writecmt = findViewById(R.id.writecmt);
        profile_image = findViewById(R.id.profile_image);

        replyprofilepic = findViewById(R.id.replyprofilepic);
        replypersonname = findViewById(R.id.replypersonname);
        replypersoncmt = findViewById(R.id.replypersoncmt);
        replylikecount = findViewById(R.id.replylikecount);
        recvewrply = findViewById(R.id.recvewrply);
        reply = findViewById(R.id.reply);


        texttitle.setText("Replies");


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RepliesActivity.this, NewsFunctions.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

reply.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

    }
});


        Intent i = getIntent();
        if (i.hasExtra("advcommentid_reply")) {

            adv_id = i.getStringExtra("advcommentid_reply");
            user_id = i.getStringExtra("userid_reply");
            profile = i.getStringExtra("profilepic_reply");
            commentid = i.getStringExtra("commentid_reply");
            Log.d("commentid_reply",commentid+" "+adv_id+" "+user_id);
            new RepliesActivity.MyAsyncTask().execute(profile);
            callreplylist(adv_id,user_id,profile,commentid);
        } else {
            // Do something else
        }

        postcmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String writecmtstr = writecmt.getText().toString();
                if(!adv_id.isEmpty() &&  adv_id != null && !user_id.isEmpty() && user_id != null && !writecmtstr.isEmpty() && writecmtstr != null){
                    postcmt(adv_id,user_id,writecmtstr);
                }else{
                    Toast.makeText(RepliesActivity.this,"Please write a comment",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void postcmt(String adv_id, String user_id,String comment) {
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.comments_advertisement_reply, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("response", response);
                replyList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        writecmt.setText("");

                        callreplylist(adv_id,user_id,profile,commentid);



                    }else{
                        Toast.makeText(RepliesActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(RepliesActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RepliesActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", user_id);
                param.put("advertisement_id","0");
                param.put("comments",comment);
                param.put("comment_id",commentid);
                Log.d("checkparamrly",user_id+" "+adv_id+" "+commentid);
                return param;
            }
        };

        MySingleton.getInstance(RepliesActivity.this).addToRequestQueue(stringRequest);

    }

    private void callreplylist( String adv_id, String user_id,String profile,String commentid) {
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.getadvertisementcommentsreplies, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("response", response);
                replyList.clear();
                try {


                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("main_comment_replies_list");//advertisement_main_comment_list

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("advertisement_main_comment_replies");  //challenge_following_user_listdata

                              strfirstname = jsonObject2.getString("first_name");
                              strlastname = jsonObject2.getString("last_name");
                            strprofileimage = WebUrl.Base_url.concat(jsonObject2.getString("profile_image"));
                           strcomments = jsonObject2.getString("comments");
                            strlikecount = jsonObject1.getString("advertisement_main_like_count");


                            JSONArray jsonArray2 = jsonObject1.getJSONArray("advertisement_comment_replay_details_replies");


                            for (int j = 0; j < jsonArray2.length(); j++) {
                                JSONObject jsonObject3 = jsonArray2.getJSONObject(j);


                                Replies replies = new Replies();
                                replies.setReplay_id(jsonObject3.getString("replay_id"));
                                replies.setReplay_first_name(jsonObject3.getString("replay_first_name"));
                                replies.setReplay_last_name(jsonObject3.getString("replay_last_name"));
                                replies.setReplay_profile_image(jsonObject3.getString("replay_profile_image"));
                                replies.setReplay_adv_comm_id(jsonObject3.getString("replay_adv_comm_id"));
                                replies.setReplay_user_id(jsonObject3.getString("replay_user_id"));
                                replies.setReplay_comment_id(jsonObject3.getString("replay_comment_id"));
                                replies.setReplay_advertisement_id(jsonObject3.getString("replay_advertisement_id"));
                                replies.setReplay_comments(jsonObject3.getString("replay_comments"));



                                replyList.add(replies);

                            }

                        }

                        replypersonname.setText(strfirstname+" "+strlastname);
                        replypersoncmt.setText(strcomments);
                        replylikecount.setText(strlikecount);
                        new RepliesActivity.MyAsyncTask1().execute(strprofileimage);



                           Log.d("activityreplylist",""+replyList.size());
                        repliesAdapter= new RepliesAdapter(replyList, RepliesActivity.this,adv_id,user_id,profile);
                        recvewrply .setHasFixedSize(true);
                        recvewrply.setLayoutManager(new LinearLayoutManager(RepliesActivity.this,LinearLayoutManager.VERTICAL,false));

                        recvewrply.setAdapter(repliesAdapter);



                    }else{
                        Toast.makeText(RepliesActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Log.d("catch", String.valueOf(e));
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

                    Toast.makeText(RepliesActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RepliesActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                //  param.put("language", "1");
                param.put("user_id", user_id);
                param.put("advertisement_comment_id",commentid);
                Log.d("getparmreply",commentid+" "+user_id);
                return param;
            }
        };

        MySingleton.getInstance(RepliesActivity.this).addToRequestQueue(stringRequest);

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

                return myBitmap;
            } catch(IOException e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            //do what you want with your bitmap result on the UI thread
            profile_image.setImageBitmap(result);
        }

    }

    private class MyAsyncTask1 extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                return myBitmap;
            } catch(IOException e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            //do what you want with your bitmap result on the UI thread
            replyprofilepic.setImageBitmap(result);
        }

    }
}
