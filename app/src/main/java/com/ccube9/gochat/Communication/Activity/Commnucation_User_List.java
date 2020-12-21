package com.ccube9.gochat.Communication.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import android.view.WindowManager;
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
import com.ccube9.gochat.Communication.Adapter.CommUserListAdapter;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.Profile.Adapter.FollowerAdapter;
import com.ccube9.gochat.Profile.FollowerActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.CommUserList;
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

public class Commnucation_User_List extends AppCompatActivity {
    ImageView iv_back;
    TextView texttitle;
    String strsearch = "";
    EditText edtsearch;
    CommUserListAdapter commUserListAdapter;
    private RecyclerView recviewcomm_user_list;
    private TransparentProgressDialog pd;
    List<CommUserList> commUserListArrayList =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commnucation__user__list);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        texttitle=findViewById(R.id.texttitle);
        iv_back=findViewById(R.id.iv_back);
        texttitle.setText("Communication");
        recviewcomm_user_list =findViewById(R.id.recviewcomm_user_list);
        edtsearch = findViewById(R.id.edtsearch);



        pd = new TransparentProgressDialog(Commnucation_User_List.this, R.drawable.ic_loader_image);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Commnucation_User_List.this, HomeActivity.class);
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
                        calllist(strsearch);
                    }
                }
                }
        });
        calllist(strsearch);




    }

    private void calllist(String strsearch) {
        commUserListArrayList.clear();
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_user_communication_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("get_user_comm_list", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("challenge_list");



                        for (int i = 0; i <jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2=jsonObject1.getJSONObject("user_list");



                            CommUserList commUserList = new CommUserList();
                            commUserList.setUser_details_id(jsonObject2.getString("user_details_id"));
                            commUserList.setFirst_name(jsonObject2.getString("first_name"));
                            commUserList.setLast_name(jsonObject2.getString("last_name"));
                            commUserList.setProfile_image(jsonObject2.getString("profile_image"));
                            commUserList.setCount(jsonObject1.getString("user_sms_count"));
                            commUserListArrayList.add(commUserList);


                        }
                        commUserListAdapter = new CommUserListAdapter(commUserListArrayList, Commnucation_User_List.this);
                        recviewcomm_user_list.setLayoutManager(new LinearLayoutManager(Commnucation_User_List.this,LinearLayoutManager.VERTICAL,false));
                        recviewcomm_user_list.setAdapter(commUserListAdapter);

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

                    Toast.makeText(Commnucation_User_List.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Commnucation_User_List.this,getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(Commnucation_User_List.this));
                param.put("search",strsearch);



                return param;
            }
        };

        MySingleton.getInstance(Commnucation_User_List.this).addToRequestQueue(stringRequest);
    }
//    private void calllist(String strsearch) {
//        commUserListArrayList.clear();
//        pd.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_user_communication_list, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                pd.dismiss();
//                Log.d("get_user_comm_list", response);
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("status").equals("1")) {
//
//
//                        JSONArray jsonArray = jsonObject.getJSONArray("user_list");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//
//
//                            CommUserList commUserList = new CommUserList();
//                            commUserList.setUser_details_id(jsonObject1.getString("user_details_id"));
//                            commUserList.setFirst_name(jsonObject1.getString("first_name"));
//                            commUserList.setLast_name(jsonObject1.getString("last_name"));
//                            commUserList.setProfile_image(jsonObject1.getString("profile_image"));
//                            commUserListArrayList.add(commUserList);
//
//
//                        }
//                        commUserListAdapter = new CommUserListAdapter(commUserListArrayList, Commnucation_User_List.this);
//                        recviewcomm_user_list.setLayoutManager(new LinearLayoutManager(Commnucation_User_List.this,LinearLayoutManager.VERTICAL,false));
//                        recviewcomm_user_list.setAdapter(commUserListAdapter);
//
//                    }
//                }
//
//
//                catch(JSONException e){
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                pd.dismiss();
//                Log.d("fdgfgd", volleyError.toString());
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
//                    Toast.makeText(Commnucation_User_List.this, message, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(Commnucation_User_List.this,getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> param = new HashMap<>();
//
//                param.put("user_id", PrefManager.getUserId(Commnucation_User_List.this));
//                param.put("search",strsearch);
//
//
//
//                return param;
//            }
//        };
//
//        MySingleton.getInstance(Commnucation_User_List.this).addToRequestQueue(stringRequest);
//    }
}
