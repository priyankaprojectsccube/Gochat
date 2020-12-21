package com.ccube9.gochat.Search.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.ccube9.gochat.Challenge.Adapter.FavouriteChallengeAdapter;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.News.Activity.CommentsActivity;
import com.ccube9.gochat.News.Activity.NewsFunctions;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Search.Adapter.SearchAdapter;
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

public class SearchActivity extends AppCompatActivity {
    private ArrayList<String> imgarr;
    private ArrayList<String>  imgarrid;
    private TransparentProgressDialog pd;
    private RecyclerView recvewsearch;

    private ArrayList<String> imgarraccepted;
    private ArrayList<String> subcribeimgarry;
    private ArrayList<String> subcribeacceptimgarray;
    private List<POJO> searchchallangeArrList=new ArrayList<>();
    private SearchAdapter searchAdapter;
    String strsearch;
EditText edtsearch;
ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbarsearch);


        edtsearch = findViewById(R.id.edtsearch);
        recvewsearch = findViewById(R.id.recvewsearch);
        iv_back = findViewById(R.id.iv_back);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SearchActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                strsearch = edtsearch.getText().toString();
//                Log.d("strsearch",strsearch);
//                if (strsearch != null  || strsearch.isEmpty()) {
//                    calllist(strsearch);
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 3) {
                    strsearch = edtsearch.getText().toString();
                    Log.d("strsearch",strsearch);
                    if (strsearch != null  || strsearch.isEmpty()) {
                        callsearchlist(strsearch);
                    }
                }
            }
        });


    }

    private void callsearchlist(String strsearch) {
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.Search_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("search_for_home_page", response);
                searchchallangeArrList.clear();
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("challenge_list");



                        for (int i = 0; i <jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2=jsonObject1.getJSONObject("challenge_data");

                            JSONArray jsonArray2=jsonObject1.getJSONArray("image");
                            imgarr=new ArrayList<>();
                            imgarrid = new ArrayList<>();

                            for (int j = 0; j < jsonArray2.length(); j++) {

                                JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
                                imgarr.add(jsonObject3.getString("image_name"));
                                imgarrid.add(jsonObject3.getString("id"));
                            }

                            JSONArray jsonArray3=jsonObject1.getJSONArray("user_details");
                            JSONObject jsonObject4 = jsonArray3.getJSONObject(0);

                            JSONArray jsonArray4 = jsonObject1.getJSONArray("subcribe_list_pic");
                            subcribeimgarry = new ArrayList<>();
                            for (int k=0 ;k < jsonArray4.length(); k++){
                                JSONObject jsonObject5 = jsonArray4.getJSONObject(k);
                                subcribeimgarry.add(jsonObject5.getString("profile_image"));
                            }

                            POJO pojo = new POJO();

                            pojo.setImages(imgarr);
                            pojo.setImagesId(imgarrid);
                            if(imgarr.size()>0) {
                                pojo.setImage(imgarr.get(0));
                            }
                            pojo.setSubcribeImages(subcribeimgarry);
                            pojo.setChallengeid(jsonObject2.getString("id"));
                            pojo.setFavourite(jsonObject1.getString("is_fav"));
                            pojo.setSubcribeCount(jsonObject1.getString("subscrib_user_count"));
                            pojo.setMainchallengeid(jsonObject2.getString("main_challenge_id"));

                            pojo.setUsername(jsonObject4.getString("first_name").concat(" "+jsonObject4.getString("last_name")));
                            pojo.setMobileno(jsonObject4.getString("mobile_number"));
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
                            if(!jsonObject2.isNull("swift_code")){
                                pojo.setSWiftcode(jsonObject2.getString("swift_code"));
                            }

                            searchchallangeArrList.add(pojo);

                        }


                        searchAdapter = new SearchAdapter(searchchallangeArrList, SearchActivity.this);
                        recvewsearch.setLayoutManager(new GridLayoutManager(SearchActivity.this,2));
                        recvewsearch.setAdapter(searchAdapter);

                        searchAdapter.setOnItemClickListener(position ->
                        {

                            Intent intent = new Intent(SearchActivity.this, ChallengeDetailActivity.class);
                            intent.putExtra("challengedetail", searchchallangeArrList.get(position));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        });

                    }else{
                        pd.dismiss();
                        Toast.makeText(SearchActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(SearchActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", PrefManager.getUserId(SearchActivity.this));
                param.put("search", strsearch);
                return param;
            }
        };

        MySingleton.getInstance(SearchActivity.this).addToRequestQueue(stringRequest);
    }

}
