package com.ccube9.gochat.Challenge.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.ccube9.gochat.Challenge.Adapter.AcceptedsubcatAdapter;
import com.ccube9.gochat.Challenge.Adapter.ChallangeSubcategoryAdapter;
import com.ccube9.gochat.Profile.ProfileActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Challengesubcategoryaccepted extends AppCompatActivity {

    private RecyclerView recvewacceptedchallengesubcategory;
    private ArrayList<POJO> arrlistacceptecat;
    private TransparentProgressDialog pd;
    private TextView texttitle;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_challengesubcategoryaccepted);

        arrlistacceptecat=new ArrayList<>();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customtoolbar);

        iv_back=findViewById(R.id.iv_back);
        texttitle=findViewById(R.id.texttitle);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Challengesubcategoryaccepted.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        texttitle.setText("Preferred Challenges");

        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        recvewacceptedchallengesubcategory=findViewById(R.id.recvewacceptedchallengesubcategory);


        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.AcceptedChallengesubcategory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();

                Log.d("fdfdfs",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if(jsonObject.getString("status").equals("1")){
                        JSONArray jsonArray=jsonObject.getJSONArray("challenge_category");
                        for(int i=0;i<jsonArray.length();i++){

                            POJO pojo=new POJO();
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            pojo.setTitle(jsonObject1.getString("sub_cat_name"));
                            pojo.setImage(jsonObject1.getString("image"));
                            arrlistacceptecat.add(pojo);

                        }



                        AcceptedsubcatAdapter acceptedsubcatAdapter=new AcceptedsubcatAdapter(arrlistacceptecat,Challengesubcategoryaccepted.this);
                        recvewacceptedchallengesubcategory.setLayoutManager(new GridLayoutManager(Challengesubcategoryaccepted.this,2));
                        recvewacceptedchallengesubcategory.setAdapter(acceptedsubcatAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("sdfsfdff", String.valueOf(volleyError));
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

                    Toast.makeText(Challengesubcategoryaccepted.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Challengesubcategoryaccepted.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String,String> param=new HashMap<>();
                param.put("user_id", PrefManager.getUserId(Challengesubcategoryaccepted.this));
               // param.put("language","1");
                return param;
            }
        };

        MySingleton.getInstance(Challengesubcategoryaccepted.this).addToRequestQueue(stringRequest);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(Challengesubcategoryaccepted.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
