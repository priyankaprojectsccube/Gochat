package com.ccube9.gochat.Profile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.ccube9.gochat.Challenge.Activity.ChallangeAcceptedActivity;
import com.ccube9.gochat.Challenge.Activity.ChallengeDetailActivity;
import com.ccube9.gochat.Challenge.Activity.MyChallengeDetailActivity;
import com.ccube9.gochat.Home.Adapter.AcceptedChallangeAdapter;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.Profile.Adapter.FollowerAdapter;
import com.ccube9.gochat.Profile.Adapter.MyChallengeAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
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

public class MyChallengeActivity extends AppCompatActivity {
    private MyChallengeAdapter myChallengeAdapter;
    private RecyclerView recviewmychallenge;
    private TransparentProgressDialog pd;
    private TextView texttitle;
    private ImageView iv_back;
    private ArrayList<String> subcribeimgarry;
    List<POJO> mychallengelist =new ArrayList<>();
    private ArrayList<String> imgarraccepted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_challenge);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        iv_back=findViewById(R.id.iv_back);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        recviewmychallenge=findViewById(R.id.recviewmychallenge);
        texttitle=findViewById(R.id.texttitle);


        Log.d("dfdfg", PrefManager.getUserId(MyChallengeActivity.this));

        texttitle.setText("My Challenges");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyChallengeActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.My_Challenge_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mychallengelist.clear();
                pd.dismiss();

                Log.d("fsfsdfdsff",response);

                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);

                    if(jsonObject.getString("status").equals("1")){

                        JSONArray jsonArray = jsonObject.getJSONArray("challenge_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("challenge_data");  //challenge_following_user_listdata


                            JSONArray jsonArray2=jsonObject1.getJSONArray("image");
                            imgarraccepted=new ArrayList<>();
                            for (int j = 0; j < jsonArray2.length(); j++) {
                                JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
                                imgarraccepted.add(jsonObject3.getString("image_name"));
                            }



                            JSONArray jsonArray4 = jsonObject1.getJSONArray("subcribe_list_pic");
                            subcribeimgarry = new ArrayList<>();
                            for (int k=0 ;k < jsonArray4.length(); k++){
                                JSONObject jsonObject5 = jsonArray4.getJSONObject(k);
                                subcribeimgarry.add(jsonObject5.getString("profile_image"));
                            }

                            POJO pojo = new POJO();
                            pojo.setImages(imgarraccepted);
                            if(imgarraccepted.size()>0) {
                                pojo.setImage(imgarraccepted.get(0));
                            }
                            pojo.setSubcribeImages(subcribeimgarry);
                            pojo.setId(jsonObject2.getString("id"));
                            pojo.setMainchallengeid(jsonObject2.getString("main_challenge_id"));
                            pojo.setTitle(jsonObject2.getString("challenge_name"));
                            pojo.setFavourite(jsonObject1.getString("is_fav"));
                            pojo.setSubcribeCount(jsonObject1.getString("subscrib_user_count"));
                            pojo.setChallengetype(jsonObject2.getString("challenge_type"));
                            pojo.setTitle(jsonObject2.getString("challenge_name"));
                            pojo.setDate(jsonObject2.getString("date"));
                            pojo.setId(jsonObject2.getString("user_id"));
                            pojo.setDescription(jsonObject2.getString("description"));
                            pojo.setLocation(jsonObject2.getString("location"));
                            pojo.setLatitude(jsonObject2.getString("lat"));
                            pojo.setLongitude(jsonObject2.getString("lang"));

                            if (!jsonObject2.isNull("name_of_association")) {
                                pojo.setAssociationname(jsonObject2.getString("name_of_association"));
                            }
                            if (!jsonObject2.isNull("details_of_association")) {
                                pojo.setDetailofassociation(jsonObject2.getString("details_of_association"));
                            }
                            if (!jsonObject2.isNull("website_link")) {
                                pojo.setWebsitelink(jsonObject2.getString("website_link"));
                            }
                            if (!jsonObject2.isNull("iban")) {
                                pojo.setIban(jsonObject2.getString("iban"));
                            }
                            if (!jsonObject2.isNull("swift_code")) {
                                pojo.setSWiftcode(jsonObject2.getString("swift_code"));
                            }

                            mychallengelist.add(pojo);

                        }

                        Log.d("jjghg", String.valueOf(mychallengelist.size()));
                        myChallengeAdapter = new MyChallengeAdapter(mychallengelist,MyChallengeActivity.this);
                        recviewmychallenge.setLayoutManager(new GridLayoutManager(MyChallengeActivity.this,2));
                        recviewmychallenge.setAdapter(myChallengeAdapter);
//                        ChallengesRecyclerAdapter challengesRecyclerAdapter = new ChallengesRecyclerAdapter(acceptchallengelist,ChallangeAcceptedActivity.this,0);
//                        recchallangebyme.setLayoutManager(new GridLayoutManager(ChallangeAcceptedActivity.this,2));
//                        recchallangebyme.setAdapter(challengesRecyclerAdapter);

                        myChallengeAdapter.setOnItemClickListener(position -> {


                            Intent intent=new Intent(MyChallengeActivity.this, MyChallengeDetailActivity.class);
                            intent.putExtra("mychallengedetail",mychallengelist.get(position));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        });
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

                    Toast.makeText(MyChallengeActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyChallengeActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param=new HashMap<>();
                //  param.put("language","1");
                param.put("user_id", PrefManager.getUserId(MyChallengeActivity.this));
                return param;
            }
        };

        MySingleton.getInstance(MyChallengeActivity.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(MyChallengeActivity.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
