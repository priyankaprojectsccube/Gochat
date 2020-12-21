package com.ccube9.gochat.Challenge.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.ccube9.gochat.Challenge.Adapter.FavouriteChallengeAdapter;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.Profile.FollowerActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavouriteChallengeActivity extends AppCompatActivity {
    private ArrayList<String> imgarr;
    private ArrayList<String>  imgarrid;
    ImageView iv_back;
    TextView texttitle;
    private RecyclerView recvewfavchallenge;
    private List<POJO> favoritechallangeArrList=new ArrayList<>();
    private FavouriteChallengeAdapter favouriteChallengeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_challenge);


        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        texttitle=findViewById(R.id.texttitle);
        iv_back=findViewById(R.id.iv_back);
        texttitle.setText("Favorite Challenges");

        recvewfavchallenge = findViewById(R.id.recvewfavchallenge);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FavouriteChallengeActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.fav_challenge_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jghghjghjghj", response);
                favoritechallangeArrList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("challenge_list");


                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("challenge_data");

                            JSONArray jsonArray2 = jsonObject1.getJSONArray("image");
                            imgarr = new ArrayList<>();
                            imgarrid = new ArrayList<>();

                            for (int j = 0; j < jsonArray2.length(); j++) {

                                JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
                                imgarr.add(jsonObject3.getString("image_name"));
                                imgarrid.add(jsonObject3.getString("id"));
                            }

//                            JSONArray jsonArray3 = jsonObject1.getJSONArray("user_details");
//                            JSONObject jsonObject4 = jsonArray3.getJSONObject(0);


                            POJO pojo = new POJO();

                            pojo.setImages(imgarr);
                            pojo.setImagesId(imgarrid);
                            if (imgarr.size() > 0) {
                                pojo.setImage(imgarr.get(0));
                            }
                            pojo.setChallengeid(jsonObject2.getString("id"));
                            //pojo.setFavourite(jsonObject1.getString("is_fav"));
                            pojo.setMainchallengeid(jsonObject2.getString("main_challenge_id"));

//                            pojo.setUsername(jsonObject4.getString("first_name").concat(" " + jsonObject4.getString("last_name")));
//                            pojo.setMobileno(jsonObject4.getString("mobile_number"));
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

                            favoritechallangeArrList.add(pojo);

                        }


                        favouriteChallengeAdapter = new FavouriteChallengeAdapter(favoritechallangeArrList, FavouriteChallengeActivity.this);
                        recvewfavchallenge.setLayoutManager(new GridLayoutManager(FavouriteChallengeActivity.this,2));
                        recvewfavchallenge.setAdapter(favouriteChallengeAdapter);

                        favouriteChallengeAdapter.setOnItemClickListener(position ->
                        {

                            Intent intent = new Intent(FavouriteChallengeActivity.this, ChallengeDetailActivity.class);
                            intent.putExtra("challengedetail", favoritechallangeArrList.get(position));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        });

                    }else{
                        Toast.makeText(FavouriteChallengeActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
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

                    Toast.makeText(FavouriteChallengeActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FavouriteChallengeActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
              //  param.put("language", "1");
                param.put("user_id", PrefManager.getUserId(FavouriteChallengeActivity.this));
                return param;
            }
        };

        MySingleton.getInstance(FavouriteChallengeActivity.this).addToRequestQueue(stringRequest);
    }
//        StringRequest stringRequest1=new StringRequest(Request.Method.POST, WebUrl.fav_challenge_list, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("dfdsfdf",response);
//                acceptedchallangeArrList.clear();
//
//                try {
//                    JSONObject jsonObject=new JSONObject(response);
//                    if(jsonObject.getString("status").equals("1")){
//
//                        JSONArray jsonArray = jsonObject.getJSONArray("message");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                           // JSONObject jsonObject2=jsonObject1.getJSONObject("challenge_data");
//
////                            JSONArray jsonArray2=jsonObject1.getJSONArray("image");
////                            imgarraccepted=new ArrayList<>();
////
////
////                            for (int j = 0; j < jsonArray2.length(); j++) {
////
////                                JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
////                                imgarraccepted.add(jsonObject3.getString("image_name"));
////
////                            }
//
//                            POJO pojo = new POJO();
//                            pojo.setId(jsonObject1.getString("id"));
//                            pojo.setMainchallengeid(jsonObject1.getString("main_challenge_id"));
//                            pojo.setTitle(jsonObject1.getString("challenge_name"));
////                            pojo.setLatitude(jsonObject1.getString("lat"));
////                            pojo.setLongitude(jsonObject1.getString("lang"));
//                            pojo.setChallengetype(jsonObject1.getString("challenge_type"));
//                           // pojo.setImages(imgarraccepted);
//
////                            if(imgarraccepted.size()>0) {
////                                pojo.setImage(imgarraccepted.get(0));
////                            }
//
//                            acceptedchallangeArrList.add(pojo);
//
//
//                        }
//                        favouriteChallengeAdapter = new FavouriteChallengeAdapter(acceptedchallangeArrList, FavouriteChallengeActivity.this);
//                        recvewfavchallenge.setLayoutManager(new LinearLayoutManager(FavouriteChallengeActivity.this,RecyclerView.HORIZONTAL,false));
//                        recvewfavchallenge.setAdapter(favouriteChallengeAdapter);
//
//                        favouriteChallengeAdapter.setOnItemClickListener(position -> {
//
//
//                            Intent intent=new Intent(FavouriteChallengeActivity.this,ChallengeDetailActivity.class);
//                            intent.putExtra("challengedetail",acceptedchallangeArrList.get(position));
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//
//
//                        });
//
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                String message = null;
//                if (volleyError instanceof NetworkError) {
//                    message = getResources().getString(R.string.cannotconnectinternate);
//                } else if (volleyError instanceof ServerError) {
//                    message = getResources().getString(R.string.servernotfound);
//                } else if (volleyError instanceof AuthFailureError) {
//                    message = getResources().getString(R.string.loginagain);
//                } else if (volleyError instanceof ParseError) {
//                    message = getResources().getString(R.string.tryagain);
//                } else if (volleyError instanceof NoConnectionError) {
//                    message = getResources().getString(R.string.cannotconnectinternate);
//                } else if (volleyError instanceof TimeoutError) {
//                    message = getResources().getString(R.string.connectiontimeout);
//                }
//                if (message != null) {
//
//                    Toast.makeText(FavouriteChallengeActivity.this, message, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(FavouriteChallengeActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> param=new HashMap<>();
//                param.put("language","0");
//                param.put("user_id", PrefManager.getUserId(FavouriteChallengeActivity.this));
//
//                return param;
//            }
//        };
//
//        MySingleton.getInstance(FavouriteChallengeActivity.this).addToRequestQueue(stringRequest1);
//
//
//
//
//    }
}
