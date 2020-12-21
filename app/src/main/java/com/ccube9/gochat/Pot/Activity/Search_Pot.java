package com.ccube9.gochat.Pot.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ccube9.gochat.Challenge.Activity.ChallengeDetailActivity;
import com.ccube9.gochat.Challenge.Activity.FavouriteChallengeActivity;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.Pot.Adapter.MyPotAdapter;
import com.ccube9.gochat.Pot.Adapter.SearchPotAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Search.Activity.SearchActivity;
import com.ccube9.gochat.Search.Adapter.SearchAdapter;
import com.ccube9.gochat.Splash.POTLIST;
import com.ccube9.gochat.Util.MYPOTLIST;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.SEARCHPOTLIST;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search_Pot extends AppCompatActivity {

    private TransparentProgressDialog pd;
    private RecyclerView recvewsearchpot;
private TextView texttitle,find;
    List<SEARCHPOTLIST> searchpotlists =new ArrayList<>();

    private SearchPotAdapter searchPotAdapter;
    String strlocation="",strpottitle="";
    EditText location,pottitle;
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search__pot);




        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        recvewsearchpot = findViewById(R.id.recvewsearchpot);
        location = findViewById(R.id.location);
        pottitle= findViewById(R.id.pottitle);
        find= findViewById(R.id.find);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        texttitle = findViewById(R.id.texttitle);

        iv_back = findViewById(R.id.iv_back);

        texttitle.setText("Find");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Search_Pot.this, PotActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

         find.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                strlocation = location.getText().toString();
                strpottitle = pottitle.getText().toString();
                
                if(strpottitle.equals("")  && strlocation.equals("") ){
                   Toast.makeText(Search_Pot.this,"Enter title or location",Toast.LENGTH_SHORT).show(); 
                }else{
                    callapi(strlocation,strpottitle);
                }
             }
         });


    }

    private void callapi(String strlocation, String strpottitle) {

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.search_pot_chatlenge_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                searchpotlists.clear();
                pd.dismiss();
                Log.d("search_pot_list", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("pot_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);



                            SEARCHPOTLIST searchpotlist = new SEARCHPOTLIST();
                            searchpotlist.setId(jsonObject1.getString("id"));
                            searchpotlist.setUserid(jsonObject1.getString("user_id"));
                            searchpotlist.setFirst_name(jsonObject1.getString("first_name")+" "+jsonObject1.getString("last_name"));
                            searchpotlist.setLogo_association(jsonObject1.getString("logo_association"));
                            searchpotlist.setBanner_for_association(jsonObject1.getString("banner_for_association"));
                            searchpotlist.setAbout_pot(jsonObject1.getString("title"));
                            searchpotlists.add(searchpotlist);


                        }
                        Log.d("searchpotlist", String.valueOf(searchpotlists.size()));
                        searchPotAdapter = new SearchPotAdapter(searchpotlists, Search_Pot.this);
                        recvewsearchpot.setLayoutManager(new GridLayoutManager(Search_Pot.this,2));
                       // recvewsearchpot.setLayoutManager(new LinearLayoutManager(Search_Pot.this,LinearLayoutManager.HORIZONTAL,false));
                        recvewsearchpot.setAdapter(searchPotAdapter);

                    }
                    else {
                        Toast.makeText(Search_Pot.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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
                Log.d("search_pot_list", volleyError.toString());
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

                    Toast.makeText(Search_Pot.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Search_Pot.this,getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(Search_Pot.this));
                param.put("title",strpottitle);
                param.put("country",strlocation);

Log.d("params",PrefManager.getUserId(Search_Pot.this)+" "+strpottitle+" "+strlocation);
                return param;
            }
        };

        MySingleton.getInstance(Search_Pot.this).addToRequestQueue(stringRequest);
    }


}