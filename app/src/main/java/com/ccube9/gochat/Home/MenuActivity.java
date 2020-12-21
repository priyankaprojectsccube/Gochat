package com.ccube9.gochat.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.ccube9.gochat.Challenge.Activity.FavouriteChallengeActivity;
import com.ccube9.gochat.Pot.Activity.PotActivity;
import com.ccube9.gochat.Profile.FollowerActivity;
import com.ccube9.gochat.Profile.MyChallengeActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Login.LoginActivity;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class MenuActivity extends AppCompatActivity {
    private TransparentProgressDialog pd;
    private TextView text_follower,text_logout,text_favchallenges,text_mychallenges,text_pots;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorred));

        text_mychallenges = findViewById(R.id.text_mychallenges);
        text_follower=findViewById(R.id.text_follower);
        text_logout=findViewById(R.id.text_logout);
        text_favchallenges=findViewById(R.id.text_favchallenges);
        profile_image = findViewById(R.id.profile_image);
        text_pots = findViewById(R.id.text_pots);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        getprofilepic();

//        profile_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MenuActivity.this,ProfileInstaActivity.class);
//                startActivity(intent);
//            }
//        });
        text_mychallenges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MenuActivity.this, MyChallengeActivity.class);
                startActivity(intent);
            }
        });

        text_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(MenuActivity.this, FollowerActivity.class);
                startActivity(intent);

            }
        });


        text_favchallenges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuActivity.this, FavouriteChallengeActivity.class);
                startActivity(intent);
            }
        });

        text_pots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MenuActivity.this, PotActivity.class);
                startActivity(intent);
            }
        });
        text_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             PrefManager.LogOut(MenuActivity.this);
             Intent intent=new Intent(MenuActivity.this, LoginActivity.class);
             startActivity(intent);

            }
        });
    }

    private void getprofilepic() {

        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.get_profile_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();

                Log.d("dfgghfghd",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){


                        JSONArray jsonArray=jsonObject.getJSONArray("user_data");
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);

                        String imageurl = Base_url.concat(jsonObject1.getString("profile_image"));
                        new MyAsyncTask().execute(imageurl);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("fdgfgd",volleyError.toString());
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

                    Toast.makeText(MenuActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MenuActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(MenuActivity.this));


                return param;
            }
        };

        MySingleton.getInstance(MenuActivity.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MenuActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                return myBitmap;
            } catch(IOException e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            //do what you want with your bitmap result on the UI thread
            profile_image.setImageBitmap(result);
        }

    }
}
