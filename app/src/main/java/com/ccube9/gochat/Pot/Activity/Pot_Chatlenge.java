package com.ccube9.gochat.Pot.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.asksira.loopingviewpager.LoopingViewPager;
import com.ccube9.gochat.Challenge.Activity.MyChallengeDetailActivity;
import com.ccube9.gochat.Challenge.Adapter.PagerImageAdapter;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class Pot_Chatlenge extends AppCompatActivity {
    ImageView iv_back,imageView;
    SeekBar seekBar;
    TextView texttitle,closepot,potname,des,invite,share,raiseof,raisedby,about,contribute;
    String pot_id,minimum_donation,sumamt,strshareurl;
    private TransparentProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pot__chatlenge);

        if (getIntent().hasExtra("pot_id")) {
            pot_id = getIntent().getStringExtra("pot_id");
        }
        Log.d("pot_id",pot_id) ;

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);

        seekBar = findViewById(R.id.seekbar);
        contribute = findViewById(R.id.contribute);
        about = findViewById(R.id.about);
        raisedby = findViewById(R.id.raisedby);
        raiseof= findViewById(R.id.raiseof);
        share = findViewById(R.id.share);
        invite = findViewById(R.id.invite);
        des= findViewById(R.id.des);
        imageView = findViewById(R.id.imageView);
        potname = findViewById(R.id.potname);
        closepot = findViewById(R.id.closepot);
        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);

        texttitle.setText("Pot Chatlenge");


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pot_Chatlenge.this, PotActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        callapi();

        closepot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closepotapi();
            }
        });

        contribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pot_Chatlenge.this, ContributionActivity.class);
                intent.putExtra("pot_id",pot_id);
                startActivity(intent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(strshareurl != null ){

    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_SEND);
    intent.putExtra(Intent.EXTRA_SUBJECT, "Try This");
    intent.putExtra(Intent.EXTRA_TEXT, strshareurl);
    intent.setType("text/plain");
    startActivity(intent);
}else{
    Toast.makeText(Pot_Chatlenge.this,"Share link not available",Toast.LENGTH_SHORT).show();
}
            }
        });
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callinviteapi();
            }
        });

        raisedby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pot_Chatlenge.this, Participated_List.class);
                intent.putExtra("pot_id",pot_id);
                startActivity(intent);
            }
        });
    }

    private void callinviteapi() {
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.send_donation_request_notification, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("send_donation",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("1")) {



                        Toast.makeText(Pot_Chatlenge.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(Pot_Chatlenge.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }


                } catch(JSONException e){
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

                    Toast.makeText(Pot_Chatlenge.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Pot_Chatlenge.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();

                param.put("pot_id",pot_id);
                param.put("user_id", PrefManager.getUserId(Pot_Chatlenge.this));

                return param;
            }
        };

        MySingleton.getInstance(Pot_Chatlenge.this).addToRequestQueue(stringRequest);
    }

    private void closepotapi() {
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.close_my_pot, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("close_my_pot",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("1")) {

closepot.setVisibility(View.GONE);
contribute.setVisibility(View.GONE);

                        Toast.makeText(Pot_Chatlenge.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(Pot_Chatlenge.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }


                } catch(JSONException e){
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

                    Toast.makeText(Pot_Chatlenge.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Pot_Chatlenge.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();

                param.put("pot_id",pot_id);
                param.put("user_id", PrefManager.getUserId(Pot_Chatlenge.this));

                return param;
            }
        };

        MySingleton.getInstance(Pot_Chatlenge.this).addToRequestQueue(stringRequest);

    }

    private void callapi() {
pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.pot_chatlenge_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("pot_chatlenge_details",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("1")) {

                        JSONObject jsonObject1 = jsonObject.getJSONArray("pot_details").getJSONObject(0);


                           if(jsonObject1.getString("user_id").equals(PrefManager.getUserId(Pot_Chatlenge.this))){
                               closepot.setVisibility(View.VISIBLE);
                           }else{
                               closepot.setVisibility(View.GONE);
                           }
                           if(jsonObject1.getString("status").equals("0")){
                               closepot.setVisibility(View.VISIBLE);
                               contribute.setVisibility(View.VISIBLE);
                           }else{
                               closepot.setVisibility(View.GONE);
                               contribute.setVisibility(View.GONE);
                           }
                           strshareurl = jsonObject.getString("weburl");
                        potname.setText(jsonObject1.getString("first_name")+" "+jsonObject1.getString("last_name"));
                        des.setText(jsonObject1.getString("description"));

                        about.setText(jsonObject1.getString("about_pot"));



if(jsonObject1.getString("banner_for_association") != null){
    Picasso.with(Pot_Chatlenge.this).load(Base_url.concat(jsonObject1.getString("banner_for_association"))).error(R.drawable.default_profile).into(imageView);
}else{
    Toast.makeText(Pot_Chatlenge.this,jsonObject1.getString("banner_for_association"),Toast.LENGTH_SHORT).show();
}

callanotherapi();

                    }else{
                      Toast.makeText(Pot_Chatlenge.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }


                } catch(JSONException e){
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

                    Toast.makeText(Pot_Chatlenge.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Pot_Chatlenge.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();

                param.put("pot_id",pot_id);
                param.put("user_id", PrefManager.getUserId(Pot_Chatlenge.this));

                return param;
            }
        };
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    5000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(Pot_Chatlenge.this).addToRequestQueue(stringRequest);
    }

    private void callanotherapi() {
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.raise_by_people, new Response.Listener<String>() {
            @SuppressLint("Range")
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("raise_by_people",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("raise_by_people");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);



                            JSONArray jsonArray2 = jsonObject1.getJSONArray("pot_id_sum_amount");



                            for (int j = 0; j < jsonArray2.length(); j++) {

                                JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
                                if(jsonObject3 != null){
                                    if (jsonObject3.getString("sum(amount)") != null || !jsonObject3.getString("sum(amount)").isEmpty()){
                                        sumamt = jsonObject3.getString("sum(amount)");
                                        seekBar.setProgress(Integer.parseInt(sumamt));
//
//                                        seekBar.setEnabled(false);
                                        seekBar.setOnTouchListener(new View.OnTouchListener(){
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                return true;
                                            }
                                        });
                                    }else{
                                        sumamt = "0";
                                    }
                                }else{
                                    sumamt = "0";
                                }


                             //   raiseof.setText(jsonObject3.getString("sum(amount)")+" €"+" Raise of "+" €");

                            }

                            minimum_donation = jsonObject1.getString("minimum_donation");
//                            minimum_donation
                            raiseof.setText(sumamt+" €"+" Raise of "+minimum_donation+" €");
                            raisedby.setText("Raised by"+" "+jsonObject1.getString("pot_id_amount_count")+" "+"People in "+jsonObject1.getString("days_count")+" days");

                        }






                    }else{
                        Toast.makeText(Pot_Chatlenge.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }


                } catch(JSONException e){
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

                    Toast.makeText(Pot_Chatlenge.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Pot_Chatlenge.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();

                param.put("pot_id",pot_id);
                param.put("user_id", PrefManager.getUserId(Pot_Chatlenge.this));

                return param;
            }
        };
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    5000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(Pot_Chatlenge.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {


    }
}