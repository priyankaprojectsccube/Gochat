package com.ccube9.gochat.News.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ccube9.gochat.Login.CreatepasswordActivity;
import com.ccube9.gochat.News.Adapter.CommentAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.Comment;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
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

public class CommentsActivity extends AppCompatActivity {
    private TransparentProgressDialog pd;
    ImageView iv_back;
    TextView texttitle;
    private RecyclerView recvewcmt;
    private List<Comment> cmmtList=new ArrayList<>();
    private CommentAdapter commentAdapter;
    CircleImageView profile_image;
    TextView postcmt;
    EditText  writecmt;
    String adv_id,user_id,profile;
    private ArrayList<String> arrreplayfirstname;
    private ArrayList<String>  arrreplaylastname;
    private ArrayList<String> arrreplyprofileimage;
    private ArrayList<String> arrreplycomments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

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

        texttitle.setText("Comments");

        recvewcmt = findViewById(R.id.recvewcmt);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CommentsActivity.this, NewsFunctions.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });



        Intent i = getIntent();
        if (i.hasExtra("adv_id")) {

            adv_id = i.getStringExtra("adv_id");
            user_id = PrefManager.getUserId(CommentsActivity.this);
            profile = i.getStringExtra("profilepic");
            Log.d("adv_id",adv_id);
            new MyAsyncTask().execute(profile);
            callcommentslist(adv_id,user_id,profile);
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
                    Toast.makeText(CommentsActivity.this,"Please write a comment",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void postcmt(String adv_id, String user_id,String comment) {
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.comments_advertisement, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("response", response);
                cmmtList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        writecmt.setText("");

                        callcommentslist(adv_id,user_id,profile);



                    }else{
                        Toast.makeText(CommentsActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(CommentsActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommentsActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                //  param.put("language", "1");
                param.put("user_id", user_id);
                param.put("advertisement_id",adv_id);
                param.put("comments",comment);
                param.put("comment_id","0");
                Log.d("checkparamcmt",user_id+" "+adv_id);
                return param;
            }
        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(CommentsActivity.this).addToRequestQueue(stringRequest);

    }

    private void callcommentslist( String adv_id, String user_id,String profile) {
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_comments_advertisement, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("response", response);
                cmmtList.clear();
                try {


                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("main_comment_list");//advertisement_main_comment_list

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("advertisement_main_comment");  //challenge_following_user_listdata


                            JSONArray jsonArray2=jsonObject1.getJSONArray("advertisement_comment_replay_details");
                            arrreplayfirstname = new ArrayList<>();
                            arrreplaylastname = new ArrayList<>();
                            arrreplyprofileimage = new ArrayList<>();
                            arrreplycomments = new ArrayList<>();

                            for (int j = 0; j < jsonArray2.length(); j++) {
                                JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
                                arrreplayfirstname.add(jsonObject3.getString("replay_first_name"));
                                arrreplaylastname.add(jsonObject3.getString("replay_last_name"));
                                arrreplyprofileimage.add(jsonObject3.getString("replay_profile_image"));
                                arrreplycomments.add(jsonObject3.getString("replay_comments"));
                            }




                            Comment comment = new Comment();
                            comment.setIs_main_comment_like_status(jsonObject1.getString("is_main_comment_like_status"));
                            comment.setAdvertisement_main_like_count(jsonObject1.getString("advertisement_main_like_count"));
                            comment.setComments(jsonObject2.getString("comments"));
                            comment.setComments_id(jsonObject2.getString("comment_id"));
                            comment.setAdvertisement_id(jsonObject2.getString("advertisement_id"));
                            comment.setUser_id(jsonObject2.getString("user_id"));
                            comment.setFirst_name(jsonObject2.getString("first_name"));
                            comment.setLast_name(jsonObject2.getString("last_name"));
                            comment.setProfile_image(jsonObject2.getString("profile_image"));
                            comment.setAdv_comm_id(jsonObject2.getString("adv_comm_id"));
                            comment.setArrreplayfirstname(arrreplayfirstname);
                            comment.setArrreplaylastname(arrreplaylastname);
                            comment.setArrreplyprofileimage(arrreplyprofileimage);
                            comment.setArrreplycomments(arrreplycomments);


                            cmmtList.add(comment);

                        }





                     //   Log.d("activitycmtlist",""+cmmtList.size()+cmmtList.get(0).getFirst_name()+cmmtList.get(1).getFirst_name()+cmmtList.get(2).getFirst_name());
                        commentAdapter= new CommentAdapter(cmmtList, CommentsActivity.this,adv_id,user_id,profile);
                        recvewcmt .setHasFixedSize(true);
                        recvewcmt.setLayoutManager(new LinearLayoutManager(CommentsActivity.this,LinearLayoutManager.VERTICAL,false));

                        recvewcmt.setAdapter(commentAdapter);



                    }else{
                        Toast.makeText(CommentsActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(CommentsActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommentsActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                //  param.put("language", "1");
                param.put("user_id", user_id);
                param.put("advertisement_id",adv_id);
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(CommentsActivity.this).addToRequestQueue(stringRequest);


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
}