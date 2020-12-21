package com.ccube9.gochat.Profile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ccube9.gochat.Challenge.Activity.ChallengeCategoryActivity;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.News.Activity.NewsPreview;
import com.ccube9.gochat.News.Adapter.NewsPreviewAdapter;
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.Profile.Adapter.UsertypeAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.PreviewImageDetails;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.Validation;
import com.ccube9.gochat.Util.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ccube9.gochat.Util.WebUrl.Change_password;

public class ChangeSettingActivity extends AppCompatActivity {

    private Switch sync_notify_switch;
    private ImageView iv_back;
    private TextView texttitle;
    private EditText pusnotification,changepass,msgnotification;
    private TransparentProgressDialog pd;
    private ArrayList<String> pushnotificationarr=new ArrayList<>();
    private ArrayList<String> messagenotificationarr=new ArrayList<>();
    private String messagenotification=null;
    private String pushnotification=null;
    private  Switch my_contact_switch_push,my_member_switch_push,my_follower_switch_push,my_contact_switch_msg,my_member_switch_msg,my_follower_switch_msg;
    private String push_mycontact,push_mymember,push_myfollower,msg_mycontact,msg_mymember,msg_myfollower,synccontacnotificationt;
    private ArrayList<POJO> usertypearrayList=new ArrayList<>();
    private Dialog dialog,dialogmsg;
    private Button okbutton_push,okbutton_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changesetting);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);
        iv_back=findViewById(R.id.iv_back);
        sync_notify_switch=findViewById(R.id.sync_notify_switch);
        texttitle=findViewById(R.id.texttitle);
        changepass=findViewById(R.id.changepass);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        pusnotification=findViewById(R.id.pushnotification);
        msgnotification = findViewById(R.id.msgnotification);

        texttitle.setText("Change Settings");




        getdata();

         dialog=new Dialog(ChangeSettingActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_push_notification);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


         okbutton_push=dialog.findViewById(R.id.okbutton);
        my_contact_switch_push=dialog.findViewById(R.id.my_contact_switch);
        my_member_switch_push=dialog.findViewById(R.id.my_member_switch);
        my_follower_switch_push=dialog.findViewById(R.id.my_follower_switch);



        dialogmsg=new Dialog(ChangeSettingActivity.this);

        dialogmsg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogmsg.setContentView(R.layout.dialog_message_notification);
        dialogmsg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        okbutton_msg=dialogmsg.findViewById(R.id.okbutton);
        my_contact_switch_msg=dialogmsg.findViewById(R.id.my_contact_switch);
        my_member_switch_msg=dialogmsg.findViewById(R.id.my_member_switch);
        my_follower_switch_msg=dialogmsg.findViewById(R.id.my_follower_switch);


        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final EditText dia_pass,dia_con_pass,dia_new_pass;
                Button btn_submit;

                final Dialog dialog=new Dialog(ChangeSettingActivity.this);
                dialog.setContentView(R.layout.dialog_change_password);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


                dia_pass=dialog.findViewById(R.id.dia_pass);
                dia_con_pass=dialog.findViewById(R.id.dia_con_pass);
                dia_new_pass=dialog.findViewById(R.id.dia_new_pass);
                btn_submit=dialog.findViewById(R.id.btn_submit);
                ImageView close=dialog.findViewById(R.id.close);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        Validation validation=new Validation();
                        if(!validation.edttxtvalidation(dia_pass,ChangeSettingActivity.this)){

                        }
                        else if(!validation.passvalidation(dia_pass,ChangeSettingActivity.this)){

                        }
                        else if(!validation.edttxtvalidation(dia_new_pass,ChangeSettingActivity.this)){

                        }
                        else if(!validation.passvalidation(dia_new_pass,ChangeSettingActivity.this)){

                        }
                        else if(!validation.edttxtvalidation(dia_con_pass,ChangeSettingActivity.this)){

                        }
                        else if(!validation.conpassvalidation(dia_new_pass,dia_con_pass,ChangeSettingActivity.this)){

                        }
                        else {

                            pd.show();

                            StringRequest stringRequest = new StringRequest(Request.Method.POST,Change_password , new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    pd.dismiss();

                                    Log.d("Change_password",response);

                                    try {
                                        JSONObject jsonObject=new JSONObject(response);
                                        if(jsonObject.getString("status").equals("1")) {
                                            dialog.dismiss();

                                        }
                                        Toast.makeText(ChangeSettingActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    pd.dismiss();

                                    Log.d("dfdsff",volleyError.toString());

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

                                        Toast.makeText(ChangeSettingActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChangeSettingActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }) {

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<>();

                                    param.put("user_id", PrefManager.getUserId(ChangeSettingActivity.this));
                                    param.put("current_password",dia_pass.getText().toString());
                                    param.put("new_password",dia_new_pass.getText().toString());
                                    param.put("confirm_password",dia_con_pass.getText().toString());
                                    param.put("key","1");
                                    param.put("language","1");

                                    return param;
                                }
                            };

                            MySingleton.getInstance(ChangeSettingActivity.this).addToRequestQueue(stringRequest);





                        }
                    }
                });





            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ChangeSettingActivity.this,ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });




//        sync_notify_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    Log.d("checksync","1");
//                    synccontacnotificationt="1";
//             callEditProfileforpnmnot();
//                }
//                else{
//                Log.d("unchecksync","0");
//                    synccontacnotificationt="0";
//             callEditProfileforpnmnot();
//                }
//            }
//        });
        sync_notify_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean on = ((Switch) view).isChecked();
                if(on)
                {
                    Log.d("checksync","1");
                    synccontacnotificationt="1";
                    callEditProfileforpnmnot();
                }
                else
                {
                    Log.d("unchecksync","0");
                    synccontacnotificationt="0";
                    callEditProfileforpnmnot();
                }
            }
        });

        pusnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                if(push_mycontact.equals("1")){
                    my_contact_switch_push.setChecked(true);
                }
                else if(push_mycontact.equals("0")){
                    my_contact_switch_push.setChecked(false);
                }

                if(push_mymember.equals("1")){
                    my_member_switch_push.setChecked(true);
                }
                else if(push_mymember.equals("0")){
                    my_member_switch_push.setChecked(false);
                }

                if(push_myfollower.equals("1")){
                    my_follower_switch_push.setChecked(true);
                }

                else if(push_myfollower.equals("0")){
                    my_follower_switch_push.setChecked(false);
                }
                okbutton_push.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callEditProfileforpnmnot();
                        dialog.dismiss();
                    }
                });

                my_contact_switch_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked){
                            push_mycontact = "1";
                        }
                        else{
                            push_mycontact = "0";
                        }




                    }
                });

                my_member_switch_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                 push_mymember = "1";
                        }
                        else{
                            push_mymember = "0";
                        }
                    }
                });

                my_follower_switch_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                        push_myfollower ="1";
                        }
                        else{
                            push_myfollower ="0";
                        }

                    }
                });

            }
        });

        msgnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogmsg.show();

                if(msg_mycontact.equals("1")){
                    my_contact_switch_msg.setChecked(true);
                }
                else if(msg_mycontact.equals("0")){
                    my_contact_switch_msg.setChecked(false);
                }

                if(msg_mymember.equals("1")){
                    my_member_switch_msg.setChecked(true);
                }
                else if(msg_mymember.equals("0")){
                    my_member_switch_msg.setChecked(false);
                }

                if(msg_myfollower.equals("1")){
                    my_follower_switch_msg.setChecked(true);
                }

                else if(msg_myfollower.equals("0")){
                    my_follower_switch_msg.setChecked(false);
                }

                okbutton_msg.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {

                        callEditProfileforpnmnot();
                        dialogmsg.dismiss();
                    }
                });

                my_contact_switch_msg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            msg_mycontact = "1";
                        }
                        else{
                            msg_mycontact = "0";
                        }



                    }
                });

                my_follower_switch_msg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            msg_myfollower = "1";
                        }
                        else{
                            msg_myfollower = "0";
                        }
                    }
                });

                my_member_switch_msg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            msg_mymember = "1";
                        }
                        else{
                            msg_mymember = "0";
                        }

                    }
                });

            }
        });

    }

    private void getdata() {

        pd.show();
        RequestQueue requestQueue1 = Volley.newRequestQueue(ChangeSettingActivity.this);
        //  CustomUtil.ShowDialog(MyGardenActivity.this, true);


        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, WebUrl.get_notification_settings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                Log.d("get_not_settings", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("notification_settings");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
push_mycontact = jsonObject1.getString("push_mycontact");
push_mymember = jsonObject1.getString("push_mymember");
push_myfollower = jsonObject1.getString("push_myfollower");


                        msg_mycontact = jsonObject1.getString("msg_mycontact");
                        msg_mymember = jsonObject1.getString("msg_mymember");
                        msg_myfollower = jsonObject1.getString("msg_myfollower");
synccontacnotificationt = jsonObject1.getString("sync_contacts");

                    }
setData(push_mycontact,push_mymember,push_myfollower,msg_mycontact,msg_mymember,msg_myfollower,synccontacnotificationt);



                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("dfddf", volleyError.toString());
                pd.dismiss();
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                if (message != null) {
                    Toast.makeText(ChangeSettingActivity.this, message, Toast.LENGTH_SHORT).show();

                } else {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", PrefManager.getUserId(ChangeSettingActivity.this));
                Log.d("useid",PrefManager.getUserId(ChangeSettingActivity.this));
                return param;
            }
        };
//        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(
//                5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(stringRequest1);
    }

    private void setData(String push_mycontact,String push_mymember,String push_myfollower,String msg_mycontact,String msg_mymember,String msg_myfollower,String synccontacnotificationt) {

        if(push_mycontact.equals("1")){
            my_contact_switch_push.setChecked(true);
        }
        else if(push_mycontact.equals("0")){
            my_contact_switch_push.setChecked(false);
        }

        if(push_mymember.equals("1")){
            my_member_switch_push.setChecked(true);
        }
        else if(push_mymember.equals("0")){
            my_member_switch_push.setChecked(false);
        }

        if(push_myfollower.equals("1")){
            my_follower_switch_push.setChecked(true);
        }

        else if(push_myfollower.equals("0")){
            my_follower_switch_push.setChecked(false);
        }

        if(msg_mycontact.equals("1")){
            my_contact_switch_msg.setChecked(true);
        }
        else if(msg_mycontact.equals("0")){
            my_contact_switch_msg.setChecked(false);
        }

        if(msg_mymember.equals("1")){
            my_member_switch_msg.setChecked(true);
        }
        else if(msg_mymember.equals("0")){
            my_member_switch_msg.setChecked(false);
        }

        if(msg_myfollower.equals("1")){
            my_follower_switch_msg.setChecked(true);
        }

        else if(msg_myfollower.equals("0")){
            my_follower_switch_msg.setChecked(false);
        }

        Log.d("strgetsync",synccontacnotificationt);
        if(synccontacnotificationt.equals("0")){
         //   sync_notify_switch.setOnCheckedChangeListener (null);
            sync_notify_switch.setChecked(false);

        }else{
            //sync_notify_switch.setOnCheckedChangeListener (null);
            sync_notify_switch.setChecked(true);

        }

    }



    private void callEditProfileforpnmnot() {


            pd.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.update_profile_settings, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();

                    Log.d("update_profile_settings", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("1")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("update_profile_settings");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                                Intent intent = new Intent(ChangeSettingActivity.this, ChangeSettingActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);


                        }
                        Toast.makeText(ChangeSettingActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    pd.dismiss();
                    Log.d("dsad", volleyError.toString());
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

                        Toast.makeText(ChangeSettingActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangeSettingActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                    }
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();

                    param.put("user_id", PrefManager.getUserId(ChangeSettingActivity.this));
                    param.put("first_name", PrefManager.getFirstName(ChangeSettingActivity.this));
                    param.put("last_name", PrefManager.getLastName(ChangeSettingActivity.this));
                    param.put("gender", PrefManager.getGender(ChangeSettingActivity.this));
                    param.put("date_of_birth", PrefManager.getDateofbirth(ChangeSettingActivity.this));
                    param.put("school", PrefManager.getSchool(ChangeSettingActivity.this));

                    if (PrefManager.getIaman(ChangeSettingActivity.this).equals("null") || PrefManager.getIaman(ChangeSettingActivity.this).isEmpty() || PrefManager.getIaman(ChangeSettingActivity.this).equals("") || PrefManager.getIaman(ChangeSettingActivity.this) == null) {
                        param.put("user_type", "");
                    } else {
                       // {"id":"1","name":"Adult"},{"id":"2","name":"Student"},{"id":"3","name":"Employee"},{"id":"4","name":"Independent"},{"id":"5","name":"Other"}
                       if(PrefManager.getIaman(ChangeSettingActivity.this).equals("Adult")){
                           param.put("user_type", "1");
                       }
                       else if(PrefManager.getIaman(ChangeSettingActivity.this).equals("Student")){
                           param.put("user_type", "2");
                       }
                       else if(PrefManager.getIaman(ChangeSettingActivity.this).equals("Employee")){
                           param.put("user_type", "3");
                       }
                       else if(PrefManager.getIaman(ChangeSettingActivity.this).equals("Independent")){
                           param.put("user_type", "4");
                       }
                       else if(PrefManager.getIaman(ChangeSettingActivity.this).equals("Other")){
                           param.put("user_type", "5");
                       }

                    }
                    param.put("description", PrefManager.getAboutme(ChangeSettingActivity.this));
                    param.put("country","");
                    param.put("country_code","");
                    param.put("sync_contacts",synccontacnotificationt);
                    Log.d("synctosend",synccontacnotificationt);
                    param.put("mobile_number",PrefManager.getMobile(ChangeSettingActivity.this));
                    param.put("push_mycontact",push_mycontact);
                    param.put("push_mymember",push_mymember);
                    param.put("push_myfollower",push_myfollower);
                    Log.d("valesofpush",push_mycontact+push_mymember+push_myfollower);
                    param.put("msg_mycontact",msg_mycontact);
                    param.put("msg_mymember",msg_mymember);
                    param.put("msg_myfollower",msg_myfollower);
                    Log.d("valesofmsg",msg_mycontact+msg_mymember+msg_myfollower);
                    return param;
                }
            };

            MySingleton.getInstance(ChangeSettingActivity.this).addToRequestQueue(stringRequest);

        }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(ChangeSettingActivity.this,ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
