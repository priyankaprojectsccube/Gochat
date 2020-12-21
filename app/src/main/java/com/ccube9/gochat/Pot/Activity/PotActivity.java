package com.ccube9.gochat.Pot.Activity;

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
import android.widget.EditText;
import android.widget.ImageView;
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
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.News.Activity.NewsFunctions;
import com.ccube9.gochat.Pot.Adapter.MyPotAdapter;
import com.ccube9.gochat.Pot.Adapter.PotListAdapter;
import com.ccube9.gochat.Pot.Adapter.RecentPotsAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Splash.POTLIST;
import com.ccube9.gochat.Util.MYPOTLIST;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView search;
    TextView texttitle,startnow;
    RecyclerView recvewrecentpots,recvewmypots;
    private TransparentProgressDialog pd;
    RecentPotsAdapter recentPotsAdapter;
    MyPotAdapter myPotAdapter;
    List<POTLIST> potListArrayList =new ArrayList<>();
    List<MYPOTLIST> mypotListArrayList =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pot);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        search = findViewById(R.id.search);
        recvewmypots = findViewById(R.id.recvewmypots);
        pd = new TransparentProgressDialog(PotActivity.this, R.drawable.ic_loader_image);
        recvewrecentpots = findViewById(R.id.recvewrecentpots);
        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);
        startnow = findViewById(R.id.startnow);

        texttitle.setText("Pots");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PotActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        startnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PotActivity.this, Create_Pot_Challenge.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(PotActivity.this,Search_Pot.class);
               startActivity(intent);
            }
        });
        callmypots();
        callrecentpots();
    }

    private void callmypots() {


        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.my_pot_chatlenge_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mypotListArrayList.clear();
                pd.dismiss();
                Log.d("my_chatlenge_list", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("my_pot_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);



                            MYPOTLIST potList = new MYPOTLIST();
                            potList.setId(jsonObject1.getString("id"));
                            potList.setUserid(jsonObject1.getString("user_id"));
                            potList.setFirst_name(jsonObject1.getString("first_name")+" "+jsonObject1.getString("last_name"));
                            potList.setLogo_association(jsonObject1.getString("logo_association"));
                            potList.setBanner_for_association(jsonObject1.getString("banner_for_association"));
                            potList.setAbout_pot(jsonObject1.getString("title"));
                            mypotListArrayList.add(potList);


                        }
                        Log.d("mypotlist", String.valueOf(mypotListArrayList.size()));
                        myPotAdapter = new MyPotAdapter(mypotListArrayList, PotActivity.this);
                        recvewmypots.setLayoutManager(new LinearLayoutManager(PotActivity.this,LinearLayoutManager.HORIZONTAL,false));
                        recvewmypots.setAdapter(myPotAdapter);

                    }
                    else {
                        Toast.makeText(PotActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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
                Log.d("pot_chatlenge_list", volleyError.toString());
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

                    Toast.makeText(PotActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PotActivity.this,getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(PotActivity.this));
                // param.put("language", "1");


                return param;
            }
        };

        MySingleton.getInstance(PotActivity.this).addToRequestQueue(stringRequest);
    }

    private void callrecentpots() {

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.pot_chatlenge_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                potListArrayList.clear();
                pd.dismiss();
                Log.d("pot_chatlenge_list", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("pot_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);



                            POTLIST potList = new POTLIST();
                            potList.setId(jsonObject1.getString("id"));
                            potList.setUserid(jsonObject1.getString("user_id"));
                            potList.setFirst_name(jsonObject1.getString("first_name")+" "+jsonObject1.getString("last_name"));
                            potList.setLogo_association(jsonObject1.getString("logo_association"));
                            potList.setBanner_for_association(jsonObject1.getString("banner_for_association"));
                            potList.setAbout_pot(jsonObject1.getString("title"));
                            potListArrayList.add(potList);


                        }
                        Log.d("potlist", String.valueOf(potListArrayList.size()));
                        recentPotsAdapter = new RecentPotsAdapter(potListArrayList, PotActivity.this);
                        recvewrecentpots.setLayoutManager(new LinearLayoutManager(PotActivity.this,LinearLayoutManager.HORIZONTAL,false));
                        recvewrecentpots.setAdapter(recentPotsAdapter);

                    }
                    else {
                        Toast.makeText(PotActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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
                Log.d("pot_chatlenge_list", volleyError.toString());
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

                    Toast.makeText(PotActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PotActivity.this,getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(PotActivity.this));
                // param.put("language", "1");


                return param;
            }
        };

        MySingleton.getInstance(PotActivity.this).addToRequestQueue(stringRequest);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}