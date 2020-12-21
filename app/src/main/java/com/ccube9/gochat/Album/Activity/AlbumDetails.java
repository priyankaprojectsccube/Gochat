package com.ccube9.gochat.Album.Activity;

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
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ccube9.gochat.Album.Adapter.AlbumDetailsAdapter;
import com.ccube9.gochat.News.Activity.GalleryPreview;
import com.ccube9.gochat.News.Adapter.GalleryPreviewAdpater;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.AlbumDetailsImages;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.PreviewImageDetails;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.VolleyMultipartRequest;
import com.ccube9.gochat.Util.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumDetails extends AppCompatActivity {

    TextView texttitle;
    ImageView iv_back;
    private TransparentProgressDialog pd;
    RecyclerView recyclerView;
    AlbumDetailsAdapter albumDetailsAdapter;
    String strchallengetype,strchallengename;
    private List<AlbumDetailsImages> albumDetailsImagesList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);


        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);
        recyclerView = findViewById(R.id.recyclerView);
        pd = new TransparentProgressDialog(this,R.drawable.ic_loader_image);


        Intent i = getIntent();
        if (i.hasExtra("challengetype")) {

            strchallengetype = i.getStringExtra("challengetype");
            strchallengename = i.getStringExtra("challengename");

            texttitle.setText(strchallengename);
            callapi(strchallengetype);
        } else {
            // Do something else
        }




        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlbumDetails.this, AlbumActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });



    }

    private void callapi(String challengetype) {

        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_album_list_by_challenge_type,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
Log.d("get_album_list",response);
                        pd.dismiss();
                     albumDetailsImagesList.clear();


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("status").equals("1")){
                                JSONArray jsonArray = jsonObject.getJSONArray("album_list");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                    AlbumDetailsImages albumDetailsImages = new AlbumDetailsImages();


                                    albumDetailsImages.setId(jsonObject1.getString("id"));
                                    albumDetailsImages.setUser_id(jsonObject1.getString("user_id"));
                                    albumDetailsImages.setAlbum_name(jsonObject1.getString("album_name"));
                                    albumDetailsImages.setChallenge_type(jsonObject1.getString("challenge_type"));
                                    albumDetailsImages.setCreated_date(jsonObject1.getString("created_date"));
                                    albumDetailsImages.setImage(jsonObject1.getString("image"));
                                    albumDetailsImages.setStatus(jsonObject1.getString("status"));
                                    albumDetailsImages.setIs_delete(jsonObject1.getString("is_delete"));


                                    albumDetailsImagesList.add(albumDetailsImages);
                                    Log.d("getlistsize", String.valueOf(albumDetailsImagesList.size()));
                                }
                                albumDetailsAdapter = new AlbumDetailsAdapter(albumDetailsImagesList, AlbumDetails.this);
                                recyclerView.setHasFixedSize(true);
                                GridLayoutManager manager = new GridLayoutManager(AlbumDetails.this, 2, GridLayoutManager.VERTICAL, false);
                                recyclerView.setLayoutManager(manager);
                                recyclerView.setAdapter(albumDetailsAdapter);
                            }else{
                                Toast.makeText(AlbumDetails.this,"No Record Found",Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(AlbumDetails.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AlbumDetails.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {



                Map<String, String> param = new HashMap<>();
                param.put("user_id", PrefManager.getUserId(AlbumDetails.this));
                param.put("challenge_type",challengetype);

                return param;
            }



        };

        MySingleton.getInstance(AlbumDetails.this).addToRequestQueue(stringRequest);


    }
}