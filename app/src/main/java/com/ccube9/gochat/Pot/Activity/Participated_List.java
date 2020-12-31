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
import com.ccube9.gochat.Pot.Adapter.ParticipateListAdapter;
import com.ccube9.gochat.Profile.Adapter.FollowerAdapter;
import com.ccube9.gochat.Profile.FollowerActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.ParticipatedList;
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

public class Participated_List extends AppCompatActivity {
    ImageView iv_back;
    TextView texttitle;
    String pot_id;
    RecyclerView recviewparticipate;
    ParticipateListAdapter participateListAdapter;
    private TransparentProgressDialog pd;
    List<ParticipatedList> participatedListArrayList =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participated__list);

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

        recviewparticipate = findViewById(R.id.recviewparticipate);
        pd = new TransparentProgressDialog(Participated_List.this, R.drawable.ic_loader_image);
        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);

        texttitle.setText("Participated List");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Participated_List.this, Pot_Chatlenge.class);
                intent.putExtra("pot_id",pot_id);
                startActivity(intent);
            }
        });

        callapi();
    }

    private void callapi() {

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.pot_participated_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("pot_participated_list", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("pot_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);



                            ParticipatedList participatedList = new ParticipatedList();
                            participatedList.setId(jsonObject1.getString("id"));
                            participatedList.setUser_id(jsonObject1.getString("user_id"));
                            participatedList.setPot_id(jsonObject1.getString("pot_id"));
                            participatedList.setFirst_name(jsonObject1.getString("first_name").concat(jsonObject1.getString("last_name")));
                            participatedList.setProfile_image(jsonObject1.getString("profile_image"));
                            participatedList.setAmount(jsonObject1.getString("amount"));
                          //  participatedList.setType_of_currency(jsonObject1.getString("Type_of_currency"));

                            participatedListArrayList.add(participatedList);


                        }
                        participateListAdapter = new ParticipateListAdapter(participatedListArrayList, Participated_List.this);
                        recviewparticipate.setLayoutManager(new LinearLayoutManager(Participated_List.this,LinearLayoutManager.VERTICAL,false));
                        recviewparticipate.setAdapter(participateListAdapter);

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
                Log.d("pot_participated_list", volleyError.toString());
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

                    Toast.makeText(Participated_List.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Participated_List.this,getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("pot_id",pot_id);
                param.put("user_id", PrefManager.getUserId(Participated_List.this));

                Log.d("params",pot_id+" "+PrefManager.getUserId(Participated_List.this));


                return param;
            }
        };

        MySingleton.getInstance(Participated_List.this).addToRequestQueue(stringRequest);


    }

    @Override
    public void onBackPressed()
    {

    }
}