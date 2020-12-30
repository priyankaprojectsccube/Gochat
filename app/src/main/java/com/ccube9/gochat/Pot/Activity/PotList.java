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
import com.ccube9.gochat.Pot.Adapter.PotListAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Splash.POTLIST;
import com.ccube9.gochat.Util.MySingleton;
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

public class PotList extends AppCompatActivity {
    ImageView iv_back;
    TextView texttitle;
    RecyclerView recviewpotlist;
    PotListAdapter potListAdapter;
    private TransparentProgressDialog pd;
    List<POTLIST> potListArrayList =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pot_list);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        recviewpotlist = findViewById(R.id.recviewpotlist);
        pd = new TransparentProgressDialog(PotList.this, R.drawable.ic_loader_image);
        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);

        texttitle.setText("Pot List");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PotList.this, Pot_Chatlenge.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                            potListArrayList.add(potList);


                        }
                        Log.d("potlist", String.valueOf(potListArrayList.size()));
                        potListAdapter = new PotListAdapter(potListArrayList, PotList.this);
                        recviewpotlist.setLayoutManager(new LinearLayoutManager(PotList.this,LinearLayoutManager.VERTICAL,false));
                        recviewpotlist.setAdapter(potListAdapter);

                    }
                    else {
                        Toast.makeText(PotList.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(PotList.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PotList.this,getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(PotList.this));
                // param.put("language", "1");


                return param;
            }
        };

        MySingleton.getInstance(PotList.this).addToRequestQueue(stringRequest);


    }

    @Override
    public void onBackPressed() {

    }


}