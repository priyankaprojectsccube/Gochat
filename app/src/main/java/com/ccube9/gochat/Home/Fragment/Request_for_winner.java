package com.ccube9.gochat.Home.Fragment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.ccube9.gochat.Home.Adapter.NotificationAdapter;
import com.ccube9.gochat.Home.Adapter.RequestWinnerAdapter;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.Profile.Adapter.FollowerAdapter;
import com.ccube9.gochat.Profile.FollowerActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.Notification;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.ccube9.gochat.Util.WinnerList;
import com.ccube9.gochat.WinnerInterface;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request_for_winner extends AppCompatActivity  {
    ImageView iv_back;
    TextView texttitle,winner;
    RequestWinnerAdapter requestwinnerAdapter;
    private ListView recviewreqwin;

    private TransparentProgressDialog pd;
    String mainchallengeid,strmainchallengeid,stracceptedchallengesid,strwinneruserid;
    List<WinnerList> winnerListArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_winner);


        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);
        texttitle.setText("Please Select Winner");
        recviewreqwin = findViewById(R.id.recviewreqwin);
        winner = findViewById(R.id.winner);
        pd = new TransparentProgressDialog(Request_for_winner.this, R.drawable.ic_loader_image);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Request_for_winner.this, NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        getList();

        if (getIntent().hasExtra("mainchallengeid")) {
            mainchallengeid = getIntent().getStringExtra("mainchallengeid");
        }



        winner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(strmainchallengeid != null || !strmainchallengeid.isEmpty() || stracceptedchallengesid != null || !stracceptedchallengesid.isEmpty() || strwinneruserid != null || !strwinneruserid.isEmpty()){
                    callapi(strmainchallengeid,stracceptedchallengesid,strwinneruserid);
                }

            }
        });

    }

    private void callapi(String strmainchallengeid, String stracceptedchallengesid, String strwinneruserid) {
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.send_winner_notification_replay, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();

                Log.d("send_winner", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                            Intent intent = new Intent(Request_for_winner.this,NotificationActivity.class);
                            startActivity(intent);


                        }

Toast.makeText(Request_for_winner.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();


                }


                catch(JSONException e){
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
                    message = getResources().getString(R.string.connectiontimeout);
                }
                if (message != null) {

                    Toast.makeText(Request_for_winner.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Request_for_winner.this,getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(Request_for_winner.this));
                param.put("main_challenge_id",strmainchallengeid);
                param.put("accepted_challenges_id",stracceptedchallengesid);
                param.put("winner_user_id",strwinneruserid);


                return param;
            }
        };

        MySingleton.getInstance(Request_for_winner.this).addToRequestQueue(stringRequest);

    }


    private void getList() {
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_accepted_challenges_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
           winnerListArrayList.clear();
                Log.d("getacceptedchallenge", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("accepted_challenges_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);



                            WinnerList winnerList = new WinnerList();
                            winnerList.setAccpt_id(jsonObject1.getString("accpt_id"));
                            winnerList.setFirst_name(jsonObject1.getString("first_name").concat(" "+jsonObject1.getString("last_name")));
                            winnerList.setUser_id(jsonObject1.getString("user_id"));
                            winnerList.setMain_challenge_id(jsonObject1.getString("main_challenge_id"));
                            winnerList.setId(jsonObject1.getString("id"));
                            winnerList.setProfile_image(jsonObject1.getString("profile_image"));
                            winnerListArrayList.add(winnerList);


                        }
  if(winnerListArrayList.size() >0){
      requestwinnerAdapter = new RequestWinnerAdapter(Request_for_winner.this,R.layout.item_listview, winnerListArrayList);

      recviewreqwin.setAdapter(requestwinnerAdapter);
  }else{
      Toast.makeText(Request_for_winner.this,"No Data",Toast.LENGTH_SHORT).show();
  }


//                        followerAdapter = new FollowerAdapter(followerArrList, Request_for_winner.this);
//                        recviewfollower.setLayoutManager(new LinearLayoutManager(Request_for_winner.this,LinearLayoutManager.VERTICAL,false));
//                        recviewfollower.setAdapter(followerAdapter);

                    }
                }


                catch(JSONException e){
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
                    message = getResources().getString(R.string.connectiontimeout);
                }
                if (message != null) {

                    Toast.makeText(Request_for_winner.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Request_for_winner.this,getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(Request_for_winner.this));
                param.put("main_challenge_id",mainchallengeid);
                Log.d("main_challenge_id",mainchallengeid);
                // param.put("language", "1");


                return param;
            }
        };

        MySingleton.getInstance(Request_for_winner.this).addToRequestQueue(stringRequest);


    }



    public void updateData(String main_challenge_id, String accpt_id, String id) {
        strmainchallengeid = main_challenge_id;
        stracceptedchallengesid = accpt_id;
        strwinneruserid = id;
    }
}
