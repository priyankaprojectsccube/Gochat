package com.ccube9.gochat.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.ccube9.gochat.Challenge.Activity.ChallengetaskaddActivity;
import com.ccube9.gochat.Challenge.Activity.CharitytaskaddActivity;
import com.ccube9.gochat.Challenge.Activity.TrainningtaskaddActivity;
import com.ccube9.gochat.Communication.Activity.Commnucation_User_List;
import com.ccube9.gochat.Home.Fragment.NotificationActivity;
import com.ccube9.gochat.Profile.ProfileActivity;
import com.ccube9.gochat.News.Activity.NewsFunctions;
import com.ccube9.gochat.R;

import com.ccube9.gochat.Search.Activity.SearchActivity;
import com.ccube9.gochat.Util.CurvedBottomNavigationView;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.ccube9.gochat.Home.Fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TransparentProgressDialog pd;
    private CircleImageView imageView_profile;
    private FloatingActionButton fab;
    private Dialog dialog;
    private TextView txtsearch,notificationcount;
    private ImageView notification;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        notification = findViewById(R.id.notification);
        notificationcount = findViewById(R.id.notificationcount);
        imageView_profile=findViewById(R.id.imageView_profile);
        fab=findViewById(R.id.fab);
        txtsearch=findViewById(R.id.txtsearch);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);

        Window window = this.getWindow();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhite));


        getSupportFragmentManager().beginTransaction().replace(R.id.rl_main, new HomeFragment()).commit();

 getprofilepic();

 notification.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
Intent i  = new Intent(HomeActivity.this, NotificationActivity.class);
startActivity(i);
     }
 });

        imageView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);

            }
        });

        txtsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(HomeActivity.this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectPostingDailog();
            }
        });


        CurvedBottomNavigationView mView = findViewById(R.id.customBottomBar);

        mView.setOnNavigationItemSelectedListener(this);

        getnotificationcountapi();


    }

    private void getnotificationcountapi() {
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.get_notification_list_count, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();

                Log.d("get_not_list_count",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){

if(jsonObject.getString("notification_count") !=null){
    notificationcount.setText(jsonObject.getString("notification_count"));
}else{
    notificationcount.setText("0");
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

                    Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(HomeActivity.this));


                return param;
            }
        };

        MySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);
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
                        new HomeActivity.MyAsyncTask().execute(imageurl);


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

                    Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(HomeActivity.this));


                return param;
            }
        };

        MySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }


    public void showSelectPostingDailog() {

        dialog = new BottomSheetDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_challenge);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        RadioButton rb_shop_posting = dialog.findViewById(R.id.rb_shop_posting);
        RadioButton rb_sale_posting = dialog.findViewById(R.id.rb_sale_posting);
        RadioButton rb_product_posting = dialog.findViewById(R.id.rb_product_posting);

        rb_shop_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, ChallengetaskaddActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        rb_sale_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, CharitytaskaddActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        rb_product_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, TrainningtaskaddActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        dialog.show();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.rl_main, new HomeFragment()).commit();
                break;
            case R.id.action_message:
                Intent intent2=new Intent(HomeActivity.this, Commnucation_User_List.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
                break;
            case R.id.action_coupon:
                Intent intent1=new Intent(HomeActivity.this, NewsFunctions.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
               // getSupportFragmentManager().beginTransaction().replace(R.id.rl_main, new PromoFunction()).commit();
                break;
            case R.id.action_menu:

                Intent intent=new Intent(HomeActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


                break;

        }
        return true;
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
            imageView_profile.setImageBitmap(result);
        }

    }
}
