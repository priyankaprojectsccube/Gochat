package com.ccube9.gochat.Album.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ccube9.gochat.Album.Adapter.AlbumDetailsAdapter;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.Profile.ProfileActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.AlbumDetailsImages;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AlbumActivity extends AppCompatActivity {

RelativeLayout relchallenge,relcharity,reltraining,relnews,reladvertise;
    private FloatingActionButton fab;
    TextView texttitle;
    ImageView iv_back,challengimg,charityimg,trainimg,newsimg,advimg;
    private TransparentProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_album);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);


        texttitle.setText("Albums");


        fab=findViewById(R.id.fab);
        relchallenge = findViewById(R.id.relchallenge);
        relcharity = findViewById(R.id.relcharity);
        reltraining = findViewById(R.id.reltraining);
        relnews = findViewById(R.id.relnews);
        reltraining = findViewById(R.id.reltraining);
        reladvertise = findViewById(R.id.reladvertise);
        challengimg = findViewById(R.id.challengimg);
        charityimg = findViewById(R.id.charityimg);
        trainimg =  findViewById(R.id.trainimg);
        newsimg= findViewById(R.id.newsimg);
        advimg = findViewById(R.id.advimg);

        pd = new TransparentProgressDialog(this,R.drawable.ic_loader_image);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlbumActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AlbumActivity.this, AddAlbumActivity.class);
                startActivity(intent);

            }
        });

        relchallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callintent("1","Challenge");
            }
        });
        relcharity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callintent("2", "Charity");
            }
        });
        reltraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callintent("3", "Training");
            }
        });
        relnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callintent("4", "News");
            }
        });
        reladvertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callintent("5", "Advertisement");
            }
        });

        callapi();
    }

    private void callapi() {

        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_album_main_page_list,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("album_first_page",response);

                        pd.dismiss();



                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("album_challenge_list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if(jsonObject1.getString("challenge_image") != null || !jsonObject1.getString("challenge_image").isEmpty())
                                {
                                    if(jsonObject1.getString("challenge_image").contains(".mp4")){
                                        RequestOptions requestOptions = new RequestOptions();
                                        requestOptions.placeholder(R.drawable.splashscreen);
                                        requestOptions.error(R.drawable.splashscreen);


                                        Glide.with(AlbumActivity.this)
                                                .load(WebUrl.Base_url.concat(jsonObject1.getString("challenge_image")))
                                                .apply(requestOptions)
                                                .thumbnail(Glide.with(AlbumActivity.this).load(WebUrl.Base_url.concat(jsonObject1.getString("challenge_image"))))
                                                        .into(challengimg);

                                    }
                                    else{
                                        Picasso.with(AlbumActivity.this).load(WebUrl.Base_url.concat(jsonObject1.getString("challenge_image"))).error(R.drawable.splashscreen).into(challengimg);
                                    }


                                }

                                if(jsonObject1.getString("charity_image") != null || !jsonObject1.getString("charity_image").isEmpty())
                                {
                                    if(jsonObject1.getString("charity_image").contains(".mp4")){
                                        RequestOptions requestOptions = new RequestOptions();
                                        requestOptions.placeholder(R.drawable.splashscreen);
                                        requestOptions.error(R.drawable.splashscreen);


                                        Glide.with(AlbumActivity.this)
                                                .load(WebUrl.Base_url.concat(jsonObject1.getString("charity_image")))
                                                .apply(requestOptions)
                                                .thumbnail(Glide.with(AlbumActivity.this).load(WebUrl.Base_url.concat(jsonObject1.getString("charity_image"))))
                                                .into(charityimg);

                                    }
                                    else {

                                        Picasso.with(AlbumActivity.this).load(WebUrl.Base_url.concat(jsonObject1.getString("charity_image"))).error(R.drawable.splashscreen).into(charityimg);
                                    }
                                }

                                if(jsonObject1.getString("training_image") != null || !jsonObject1.getString("training_image").isEmpty())
                                {
                                    if(jsonObject1.getString("training_image").contains(".mp4")){
                                        RequestOptions requestOptions = new RequestOptions();
                                        requestOptions.placeholder(R.drawable.splashscreen);
                                        requestOptions.error(R.drawable.splashscreen);


                                        Glide.with(AlbumActivity.this)
                                                .load(WebUrl.Base_url.concat(jsonObject1.getString("training_image")))
                                                .apply(requestOptions)
                                                .thumbnail(Glide.with(AlbumActivity.this).load(WebUrl.Base_url.concat(jsonObject1.getString("training_image"))))
                                                .into(trainimg);

                                    }
                                    else {

                                        Picasso.with(AlbumActivity.this).load(WebUrl.Base_url.concat(jsonObject1.getString("training_image"))).error(R.drawable.splashscreen).into(trainimg);
                                    }
                                }

                                if(jsonObject1.getString("news_image") != null || !jsonObject1.getString("news_image").isEmpty())
                                {

                                    if(jsonObject1.getString("news_image").contains(".mp4")){
                                        RequestOptions requestOptions = new RequestOptions();
                                        requestOptions.placeholder(R.drawable.splashscreen);
                                        requestOptions.error(R.drawable.splashscreen);


                                        Glide.with(AlbumActivity.this)
                                                .load(WebUrl.Base_url.concat(jsonObject1.getString("news_image")))
                                                .apply(requestOptions)
                                                .thumbnail(Glide.with(AlbumActivity.this).load(WebUrl.Base_url.concat(jsonObject1.getString("news_image"))))
                                                .into(newsimg);

                                    }
                                    else {
                                        Picasso.with(AlbumActivity.this).load(WebUrl.Base_url.concat(jsonObject1.getString("news_image"))).error(R.drawable.splashscreen).into(newsimg);
                                    }
                                }

                                if(jsonObject1.getString("advertisement_image") != null || !jsonObject1.getString("advertisement_image").isEmpty())
                                {
                                    if(jsonObject1.getString("advertisement_image").contains(".mp4")){
                                        RequestOptions requestOptions = new RequestOptions();
                                        requestOptions.placeholder(R.drawable.splashscreen);
                                        requestOptions.error(R.drawable.splashscreen);


                                        Glide.with(AlbumActivity.this)
                                                .load(WebUrl.Base_url.concat(jsonObject1.getString("advertisement_image")))
                                                .apply(requestOptions)
                                                .thumbnail(Glide.with(AlbumActivity.this).load(WebUrl.Base_url.concat(jsonObject1.getString("advertisement_image"))))
                                                .into(advimg);

                                    }
                                    else {
                                        Picasso.with(AlbumActivity.this).load(WebUrl.Base_url.concat(jsonObject1.getString("advertisement_image"))).error(R.drawable.splashscreen).into(advimg);
                                    }
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();

                Log.d("Fgdfg",volleyError.toString());
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

                    Toast.makeText(AlbumActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AlbumActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {



                Map<String, String> param = new HashMap<>();
                param.put("user_id", PrefManager.getUserId(AlbumActivity.this));

                return param;
            }



        };

        MySingleton.getInstance(AlbumActivity.this).addToRequestQueue(stringRequest);


    }

    private void callintent(String challengetype, String challengname) {


                                Intent intent = new Intent(AlbumActivity.this, AlbumDetails.class);
                                intent.putExtra("challengename",challengname);
                                intent.putExtra("challengetype",challengetype);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);






    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(AlbumActivity.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
