package com.ccube9.gochat.Challenge.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.asksira.loopingviewpager.LoopingViewPager;
import com.ccube9.gochat.Challenge.Adapter.PagerImageAdapter;
import com.ccube9.gochat.Home.Fragment.Winnername;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.Profile.ChallengerDetailActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class MyChallengeDetailActivity  extends AppCompatActivity implements OnMapReadyCallback {
    int checkarraysize, checkbitmapsize;
    private ArrayList imagearrbitmap = new ArrayList();
    private CircleImageView ci1,ci2,ci3,profpic;
    private ImageView iv_back;
    private RelativeLayout acceptchallenge_rellay;
    private TextView sendrequres,annousewinner,unfollowuser,texttitle,challengename,desc,txt_location,txtdatetime,accept_challenge,usernametextview,addtocontact,followuser,peoplesubscribed;
    private LoopingViewPager mPager;
    private ArrayList<String> arrayListimg=new ArrayList<>() ;
    private PageIndicatorView indicator;
    private Double lat,lon;
    private GoogleMap mMap;
    private TransparentProgressDialog pd;
    private LatLng latLng;
    private POJO pojo1;

    private String username,contactnumber,tofollowid,subscribe_count,strwinnername;
    private RelativeLayout challengerdetail,acceptedchallenge_rellay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_challenge_detail);



        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);

        ci2 = findViewById(R.id.ci2);
        ci1 = findViewById(R.id.ci1);
        ci3 = findViewById(R.id.ci3);
        profpic = findViewById(R.id.profpic);
        iv_back=findViewById(R.id.iv_back);
        addtocontact=findViewById(R.id.addtocontact);
        unfollowuser=findViewById(R.id.unfollowuser);
        followuser=findViewById(R.id.followuser);
        acceptchallenge_rellay=findViewById(R.id.acceptchallenge_rellay);
        texttitle=findViewById(R.id.texttitle);
        acceptedchallenge_rellay=findViewById(R.id.acceptedchallenge_rellay);
        txt_location=findViewById(R.id.txt_location);
        challengerdetail=findViewById(R.id.challengerdetail);
        mPager = findViewById(R.id.pager);
        sendrequres = findViewById(R.id.sendrequres);
        annousewinner = findViewById(R.id.annousewinner);
        usernametextview = findViewById(R.id.username);
        accept_challenge = findViewById(R.id.accept_challenge);
        desc = findViewById(R.id.desc);
        indicator = findViewById(R.id.indicator);
        challengename=findViewById(R.id.challengename);
        txtdatetime=findViewById(R.id.txtdatetime);
        peoplesubscribed = findViewById(R.id.peoplesubscribed);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));


        Intent intent=getIntent();

        if(intent.hasExtra("mychallengedetail")){

            pojo1= (POJO) intent.getSerializableExtra("mychallengedetail");



            StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.ChallengeDetails, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("ChallengeDetails",response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("status").equals("1")) {
                            subscribe_count = jsonObject.getString("subcribe_count_id");
                            JSONObject jsonObject1 = jsonObject.getJSONArray("challenge_detail").getJSONObject(0);

                            if (jsonObject.getString("challenge_accepted_status").equals("1")){
                                acceptedchallenge_rellay.setVisibility(View.GONE);
                            }

                            if(jsonObject.getString("challenge_accepted_status").equals("0")){
                                acceptchallenge_rellay.setVisibility(View.GONE);
                            }

                            String title = jsonObject1.getString("challenge_name");

                            texttitle.setText(Character.toUpperCase(title.charAt(0)) + title.substring(1));
                            challengename.setText(Character.toUpperCase(title.charAt(0)) + title.substring(1));
                            desc.setText(jsonObject1.getString("description"));
                            txt_location.setText(jsonObject1.getString("location"));
                            txtdatetime.setText(jsonObject1.getString("date"));
                            lat = Double.valueOf(jsonObject1.getString("lat"));
                            lon = Double.valueOf(jsonObject1.getString("lang"));
                            if(subscribe_count.equals("0")){
                                peoplesubscribed.setVisibility(View.GONE);
                                //peoplesubscribed.setText("No Subcribers");
                            }else if(subscribe_count.equals("1")){
                                peoplesubscribed.setText(subscribe_count+"subscriber");
                            }else{
                                peoplesubscribed.setText(subscribe_count+"subscribers");
                            }


                            if (lat != null && lon != null) {

                                latLng = new LatLng(lat,lon);
                            }
                            if (latLng != null) {
                                mMap.addMarker(new MarkerOptions().position(latLng).title(""));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
                                mMap.getUiSettings().setZoomGesturesEnabled(true);

                            }

                            JSONObject jsonObject3 = jsonObject.getJSONArray("user_details").getJSONObject(0);

                            contactnumber=jsonObject3.getString("mobile_number");
                            tofollowid=jsonObject3.getString("id");


                            username=jsonObject3.getString("first_name").concat(" "+jsonObject3.getString("last_name"));
                            usernametextview.setText(username);
                            Picasso.with(MyChallengeDetailActivity.this).load(Base_url.concat(jsonObject3.getString("profile_image"))).error(R.drawable.default_profile).into(profpic);

                            JSONArray jsonArray = jsonObject.getJSONArray("challenge_images");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                arrayListimg.add(jsonObject2.getString("image_name"));
                            }
                            Log.d("fsdfdfd", String.valueOf(arrayListimg));
                            mPager.setAdapter(new PagerImageAdapter(MyChallengeDetailActivity.this, arrayListimg, true));
                            indicator.setCount(mPager.getIndicatorCount());
                            mPager.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
                                @Override
                                public void onIndicatorProgress(int selectingPosition, float progress) {
                                    indicator.setProgress(selectingPosition, progress);
                                }
                                @Override
                                public void onIndicatorPageChange(int newIndicatorPosition) {
                                    //   indicatorView.setSelection(newIndicatorPosition);
                                }
                            });
                            JSONArray jsonArray4 = jsonObject.getJSONArray("subcribe_list_pic");
                            for (int i = 0; i < jsonArray4.length(); i++) {
                                JSONObject jsonObject4 = jsonArray4.getJSONObject(i);
                                String imageurl = Base_url.concat(jsonObject4.getString("profile_image"));
                                new MyChallengeDetailActivity.MyAsyncTask().execute(imageurl);
                            }
                            Log.d("fsdfdfd", "hhvh");
                        }


                    } catch(JSONException e){
                        e.printStackTrace();
                    }





                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
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

                        Toast.makeText(MyChallengeDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyChallengeDetailActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                    }

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param=new HashMap<>();

                    param.put("main_challenge_id",pojo1.getMainchallengeid());
                    param.put("user_id", PrefManager.getUserId(MyChallengeDetailActivity.this));

                    return param;
                }
            };
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    5000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(MyChallengeDetailActivity.this).addToRequestQueue(stringRequest);











        }



        sendrequres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callapi();
            }
        });

        annousewinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    callapi2();

            }
        });
        accept_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.AcceptChallenge, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("status").equals("1")){

                            }
                            Toast.makeText(MyChallengeDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
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

                            Toast.makeText(MyChallengeDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyChallengeDetailActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                        }
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> param=new HashMap<>();
                        //param.put("language","1");
                        param.put("user_id",PrefManager.getUserId(MyChallengeDetailActivity.this));
                        param.put("main_challenge_id",pojo1.getMainchallengeid());
                        return param;


                    }
                };
                MySingleton.getInstance(MyChallengeDetailActivity.this).addToRequestQueue(stringRequest);

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyChallengeDetailActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        followuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                pd.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.Followuser_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.d("dfgghfghd", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("1")) {

                            }
                            Toast.makeText(MyChallengeDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                            message =getResources().getString(R.string.connectiontimeout);
                        }
                        if (message != null) {

                            Toast.makeText(MyChallengeDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyChallengeDetailActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();

                        param.put("user_id", PrefManager.getUserId(MyChallengeDetailActivity.this));
                        param.put("follower_id",tofollowid);
                        //   param.put("language", "1");


                        return param;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(MyChallengeDetailActivity.this).addToRequestQueue(stringRequest);



            }
        });

        unfollowuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                pd.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.Unfollowuser_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.d("dfgghfghd", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("1")) {

                            }
                            Toast.makeText(MyChallengeDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                            message =getResources().getString(R.string.connectiontimeout);
                        }
                        if (message != null) {

                            Toast.makeText(MyChallengeDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyChallengeDetailActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();

                        param.put("user_id", PrefManager.getUserId(MyChallengeDetailActivity.this));
                        param.put("follower_id",tofollowid);
                        //param.put("language", "1");


                        return param;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(MyChallengeDetailActivity.this).addToRequestQueue(stringRequest);



            }
        });

        addtocontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                contactIntent
                        .putExtra(ContactsContract.Intents.Insert.NAME,username )
                        .putExtra(ContactsContract.Intents.Insert.PHONE, contactnumber);

                startActivityForResult(contactIntent, 1);

            }
        });

        profpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyChallengeDetailActivity.this, ChallengerDetailActivity.class);
                startActivity(intent);
            }
        });


    }

    private void callapi2() {
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.user_winner_challenge, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("user_winner_challenge",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){

                        JSONArray jsonArray = jsonObject.getJSONArray("Winner_name");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);



                            strwinnername = jsonObject1.getString("first_name")+" "+jsonObject1.getString("last_name");
                            Log.d("strwinnername",strwinnername);





                        }
                          Intent intent = new Intent(MyChallengeDetailActivity.this, Winnername.class);
                        intent.putExtra("winnername",strwinnername);
                        startActivity(intent);

                    }

                    else
                        {

                        Toast.makeText(MyChallengeDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Log.d("user_winner_challenge", String.valueOf(volleyError));
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

                    Toast.makeText(MyChallengeDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyChallengeDetailActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
                //param.put("language","1");
                param.put("user_id",PrefManager.getUserId(MyChallengeDetailActivity.this));
                param.put("main_challenge_id",pojo1.getMainchallengeid());
                Log.d("mainchallengeid",pojo1.getMainchallengeid());
                return param;


            }
        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(MyChallengeDetailActivity.this).addToRequestQueue(stringRequest);

    }

    private void callapi() {
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.send_winner_notification, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("send_winner",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){

                    }
                    Toast.makeText(MyChallengeDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("send_winner", String.valueOf(volleyError));
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

                    Toast.makeText(MyChallengeDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyChallengeDetailActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
                //param.put("language","1");
                param.put("user_id",PrefManager.getUserId(MyChallengeDetailActivity.this));
                param.put("main_challenge_id",pojo1.getMainchallengeid());
                Log.d("mainchallengeid",pojo1.getMainchallengeid());
                return param;


            }
        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(MyChallengeDetailActivity.this).addToRequestQueue(stringRequest);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(MyChallengeDetailActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
                ci1.setImageBitmap((Bitmap) imagearrbitmap.get(0));
                ci2.setVisibility(View.GONE);
                ci3.setVisibility(View.GONE);
            }
            else if(checkbitmapsize == 2){
                ci1.setImageBitmap((Bitmap) imagearrbitmap.get(0));
                ci2.setImageBitmap((Bitmap) imagearrbitmap.get(1));
                ci3.setVisibility(View.GONE);
            }
            else if (checkbitmapsize == 3){
                ci1.setImageBitmap((Bitmap) imagearrbitmap.get(0));
                ci2.setImageBitmap((Bitmap) imagearrbitmap.get(1));
                ci3.setImageBitmap((Bitmap) imagearrbitmap.get(2));
            }
        }

    }



}

