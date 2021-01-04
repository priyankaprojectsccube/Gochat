package com.ccube9.gochat.Home.Fragment;

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
import com.ccube9.gochat.Home.Adapter.NotificationAdapter;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.Profile.Adapter.FollowerAdapter;
import com.ccube9.gochat.Profile.FollowerActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.Notification;
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

public class NotificationActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView texttitle;
   NotificationAdapter notificationAdapter;
    private RecyclerView recviewnotification;
    private TransparentProgressDialog pd;
    List<Notification> notificationArrList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        texttitle=findViewById(R.id.texttitle);
        iv_back=findViewById(R.id.iv_back);
        texttitle.setText("Notifications");
        recviewnotification=findViewById(R.id.recviewnotification);
        pd = new TransparentProgressDialog(NotificationActivity.this, R.drawable.ic_loader_image);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NotificationActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_notification_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                notificationArrList.clear();
                Log.d("get_notification_list", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("notification_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);



                            Notification notification = new Notification();
                            notification.setId(jsonObject1.getString("id"));
                            notification.setFirst_name(jsonObject1.getString("first_name"));
                            notification.setLast_name(jsonObject1.getString("last_name"));
                            notification.setMobile_number(jsonObject1.getString("mobile_number"));
                            notification.setEmail_id(jsonObject1.getString("email_id"));
                            notification.setPassword(jsonObject1.getString("password"));
                            notification.setMobile_veri_token("mobile_veri_token");
                            notification.setEmail_veri_token("email_veri_token");
                            notification.setForgot_pass_token("forgot_pass_token");
                            notification.setCreated_date(jsonObject1.getString("created_date"));
                            notification.setIs_delete(jsonObject1.getString("is_delete"));
                            notification.setStatus(jsonObject1.getString("status"));
                            notification.setReceived_update(jsonObject1.getString("received_update"));
                            notification.setOauth_provider(jsonObject1.getString("oauth_provider"));
                            notification.setGender(jsonObject1.getString("gender"));
                            notification.setCountry_code(jsonObject1.getString("country_code"));
                            notification.setCountry(jsonObject1.getString("country"));
                            notification.setDate_of_birth(jsonObject1.getString("date_of_birth"));
                            notification.setSchool(jsonObject1.getString("school"));
                            notification.setType(jsonObject1.getString("type"));
                            notification.setDescription(jsonObject1.getString("description"));

                            notification.setSync_contacts(jsonObject1.getString("sync_contacts"));
                            notification.setFlag(jsonObject1.getString("flag"));
                            notification.setProfile_image(jsonObject1.getString("profile_image"));
                            notification.setCategory_flag(jsonObject1.getString("category_flag"));
                            notification.setPic_flag(jsonObject1.getString("pic_flag"));
                            notification.setToken_id(jsonObject1.getString("token_id"));
                            notification.setRead_notifi_status(jsonObject1.getString("read_notifi_status"));
                            notification.setAdvertise_request(jsonObject1.getString("advertise_request"));
                            notification.setLang_id(jsonObject1.getString("lang_id"));
                            notification.setNoti_id(jsonObject1.getString("noti_id"));
                            notification.setUser_id(jsonObject1.getString("user_id"));
                            notification.setNoti_user_id(jsonObject1.getString("noti_user_id"));
                            notification.setNoti_title(jsonObject1.getString("noti_title"));
                            notification.setNoti_url(jsonObject1.getString("noti_url"));
                            notification.setRead_status(jsonObject1.getString("read_status"));
                            notification.setRequest_winner(jsonObject1.getString("request_winner"));
                            notification.setMain_challenge_id(jsonObject1.getString("main_challenge_id"));
                            notification.setPot_id(jsonObject1.getString("pot_id"));
                            notification.setIntitatiofundgo(jsonObject1.getString("intitatiofundgo"));



                            notificationArrList.add(notification);


                        }
                        notificationAdapter = new NotificationAdapter(notificationArrList, NotificationActivity.this);
                        recviewnotification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this,LinearLayoutManager.VERTICAL,false));
                        recviewnotification.setAdapter(notificationAdapter);

                    }
                    else{
                        Toast.makeText(NotificationActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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
                Log.d("fdgfgd", volleyError.toString());
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

                    Toast.makeText(NotificationActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NotificationActivity.this,getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(NotificationActivity.this));
                // param.put("language", "1");


                return param;
            }
        };

        MySingleton.getInstance(NotificationActivity.this).addToRequestQueue(stringRequest);


    }
}