package com.ccube9.gochat.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ccube9.gochat.Challenge.Activity.ChallengeCategoryActivity;
import com.ccube9.gochat.Challenge.Activity.ChallengeDetailActivity;
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.Profile.Adapter.UsertypeAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.Validation;
import com.ccube9.gochat.Util.WebUrl;
import com.ccube9.gochat.Home.HomeActivity;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonalDetailSettingActivity extends AppCompatActivity {
    private String gender,fname,lname,dob,school,push_mycontact="0",push_mymember="0",push_myfollower="0",msg_mycontact="0",msg_mymember="0",msg_myfollower="0",synccontacnotificationt="0";
    private Button next_button;
    private EditText desc_EditText,msgnotification,pusnotification,textinputiaman;
    private TransparentProgressDialog pd;
    private TextInputLayout desc_txtLayout,occupation_selection;
    private Switch sync_notify_switch;
//    private ArrayList<String> messagenotificationarr=new ArrayList<>();
//    private ArrayList<String> pushnotificationarr=new ArrayList<>();
//    private String messagenotification=null;
//    private String pushnotification=null;
    private TextView texttitle;
    private DrawerLayout drawer_layout;
    private ImageView iv_back;
    private String usertypeid="";
    private RecyclerView recviewusertype;
    private ArrayList<POJO> usertypearrayList=new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detail_setting);



        next_button=findViewById(R.id.next_button);
        desc_txtLayout=findViewById(R.id.desc_txtLayout);
        occupation_selection=findViewById(R.id.occupation_selection);
        recviewusertype=findViewById(R.id.recviewusertype);
        pusnotification=findViewById(R.id.pushnotification);
        drawer_layout=findViewById(R.id.drawer_layout);
        texttitle=findViewById(R.id.texttitle);
        textinputiaman=findViewById(R.id.textinputiaman);
        iv_back=findViewById(R.id.iv_back);
        sync_notify_switch=findViewById(R.id.sync_notify_switch);
        desc_EditText=findViewById(R.id.desc_EditText);
        msgnotification=findViewById(R.id.msgnotification);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);

//        messagenotificationarr.add(0,"0");
//        messagenotificationarr.add(1,"0");
//        messagenotificationarr.add(2,"0");
//
//        pushnotificationarr.add(0,"0");
//        pushnotificationarr.add(1,"0");
//        pushnotificationarr.add(2,"0");

        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        texttitle.setText(R.string.personaldetail);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ((DrawerLayout) findViewById(R.id.drawer_layout)).addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                // Whatever you want
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                // Whatever you want

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                // Whatever you want
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Whatever you want
            }
        });


        textinputiaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                drawer_layout.openDrawer(GravityCompat.END);

            }
        });

getUsertype();
if(getIntent().hasExtra("bundlepersonaldata")) {
            Bundle bundle = getIntent().getBundleExtra("bundlepersonaldata");

            gender = bundle.getString("gender");
            fname = bundle.getString("firstname");
            lname = bundle.getString("lastname");
            dob = bundle.getString("dob");
            school = bundle.getString("school");
        }





        sync_notify_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    synccontacnotificationt="1";

                }
                else{
                    synccontacnotificationt="0";

                }
            }
        });

           pusnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(PersonalDetailSettingActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_push_notification);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button okbutton=dialog.findViewById(R.id.okbutton);
                Switch my_contact_switch=dialog.findViewById(R.id.my_contact_switch);
                Switch my_member_switch=dialog.findViewById(R.id.my_member_switch);
                Switch my_follower_switch=dialog.findViewById(R.id.my_follower_switch);

                if(push_mycontact.equals("1")){
                    my_contact_switch.setChecked(true);
                }
                else if(push_mycontact.equals("0")){
                    my_contact_switch.setChecked(false);
                }

                if(push_mymember.equals("1")){
                    my_member_switch.setChecked(true);
                }
                else if(push_mymember.equals("0")){
                    my_member_switch.setChecked(false);
                }

                if(push_myfollower.equals("1")){
                    my_follower_switch.setChecked(true);
                }

                else if(push_myfollower.equals("0")){
                    my_follower_switch.setChecked(false);
                }


                okbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        pushnotification = null;
//
//                        for (int j = 0; j < pushnotificationarr.size(); j++) {
//                            if (pushnotificationarr.get(j).equals("1")) {
//
//                                if (pushnotification == null) {
//                                    pushnotification = "" + j;
//                                    Log.d("pushnotificationarri",pushnotification);
//                                } else if (pushnotification != null) {
//                                    pushnotification = pushnotification.concat("," + j);
//                                    Log.d("pushnotificationarre",pushnotification);
//                                }
//
//                            }
//                        }

                        dialog.dismiss();
                    }
                });

                my_contact_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            push_mycontact = "1";
//                            pushnotificationarr.remove(0);
//                            pushnotificationarr.add(0,"1");
                        }
                        else{
                            push_mycontact = "0";
//                            pushnotificationarr.remove(0);
//                            pushnotificationarr.add(0,"0");
                        }



                    }
                });

                my_member_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            push_mymember = "1";
//                            pushnotificationarr.remove(1);
//                            pushnotificationarr.add(1,"1");
                        }
                        else{
                            push_mymember = "0";
//                            pushnotificationarr.remove(1);
//                            pushnotificationarr.add(1,"0");
                        }
                    }
                });

                my_follower_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            push_myfollower = "1";
//                            pushnotificationarr.remove(2);
//                            pushnotificationarr.add(2,"1");
                        }
                        else{
                            push_myfollower = "0";
//                            pushnotificationarr.remove(2);
//                            pushnotificationarr.add(2,"0");
                        }

                    }
                });

            }
        });



            msgnotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog=new Dialog(PersonalDetailSettingActivity.this);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_message_notification);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    Button okbutton=dialog.findViewById(R.id.okbutton);
                    Switch my_contact_switch=dialog.findViewById(R.id.my_contact_switch);
                    Switch my_member_switch=dialog.findViewById(R.id.my_member_switch);
                    Switch my_follower_switch=dialog.findViewById(R.id.my_follower_switch);

                    if(msg_mycontact.equals("1")){
                        my_contact_switch.setChecked(true);
                    }
                    else if(msg_mycontact.equals("0")){
                        my_contact_switch.setChecked(false);
                    }

                    if(msg_mymember.equals("1")){
                        my_member_switch.setChecked(true);
                    }
                    else if(msg_mymember.equals("0")){
                        my_member_switch.setChecked(false);
                    }

                    if(msg_myfollower.equals("1")){
                        my_follower_switch.setChecked(true);
                    }

                    else if(msg_myfollower.equals("0")){
                        my_follower_switch.setChecked(false);
                    }
//

                    okbutton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
//                            messagenotification = null;
//
//                            for (int i = 0; i < messagenotificationarr.size(); i++) {
//                                if (messagenotificationarr.get(i).equals("1")) {
//
//                                    if (messagenotification == null) {
//                                        messagenotification = "" + i;
//                                        Log.d("msgnotarri",messagenotification);
//
//                                    } else if (messagenotification != null) {
//                                        messagenotification = messagenotification.concat("," + i);
//                                        Log.d("msgnotarre",messagenotification);
//                                    }
//
//                                }
//                            }
                            dialog.dismiss();
                        }
                    });

                    my_contact_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                msg_mycontact = "1";
//                                    messagenotificationarr.remove(0);
//                                    messagenotificationarr.add(0,"1");
                                }
                                else{
                                msg_mycontact = "0";
//                                    messagenotificationarr.remove(0);
//                                    messagenotificationarr.add(0,"0");
                                }



                        }
                    });

                    my_member_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                msg_mymember = "1";
//                                messagenotificationarr.remove(1);
//                                messagenotificationarr.add(1,"1");
                            }
                            else{
                                msg_mymember = "0";
//                                messagenotificationarr.remove(1);
//                                messagenotificationarr.add(1,"0");
                            }
                        }
                    });

                    my_follower_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                msg_myfollower = "1";
//                                messagenotificationarr.remove(2);
//                                messagenotificationarr.add(2,"1");
                            }
                            else{
                                msg_myfollower = "0";
//                                messagenotificationarr.remove(2);
//                                messagenotificationarr.add(2,"0");
                            }

                        }
                    });

                }
            });





            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    messagenotification = null;
//                    pushnotification = null;

//                    for (int i = 0; i < messagenotificationarr.size(); i++) {
//                        if (messagenotificationarr.get(i).equals("1")) {
//
//                            if (messagenotification == null) {
//                                messagenotification = "" + i;
//                                Log.d("msgnotarri",messagenotification);
//
//                            } else if (messagenotification != null) {
//                                messagenotification = messagenotification.concat("," + i);
//                                Log.d("msgnotarr",messagenotification);
//                            }
//
//                        }
//                    }


//                    for (int j = 0; j < pushnotificationarr.size(); j++) {
//                        if (pushnotificationarr.get(j).equals("1")) {
//
//                            if (pushnotification == null) {
//                                pushnotification = "" + j;
//
//                            } else if (pushnotification != null) {
//                                pushnotification = pushnotification.concat("," + j);
//                                Log.d("pushnotificationarri",pushnotification);
//                            }
//
//                        }
//                    }

                    Validation validation = new Validation();


                    if (!validation.edttxtvalidation(textinputiaman, occupation_selection, PersonalDetailSettingActivity.this)) {

                    }

                    else {

                        pd.show();
//profile_update
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


                                        if (jsonObject1.getString("flag").equals("0")) {
                                            PrefManager.setIsProfilepicUpdate(PersonalDetailSettingActivity.this, false);
                                        } else if (jsonObject1.getString("flag").equals("1")) {
                                            PrefManager.setIsProfilepicUpdate(PersonalDetailSettingActivity.this, true);
                                        }


                                        PrefManager.setEmailId(PersonalDetailSettingActivity.this, jsonObject1.getString("email_id"));
                                        PrefManager.setMobile(PersonalDetailSettingActivity.this, jsonObject1.getString("mobile_number"));
                                        PrefManager.setFirstName(PersonalDetailSettingActivity.this, jsonObject1.getString("first_name"));
                                        PrefManager.setLastName(PersonalDetailSettingActivity.this, jsonObject1.getString("last_name"));
                                        PrefManager.setIaman(PersonalDetailSettingActivity.this,jsonObject1.getString("type"));
                                        PrefManager.setAboutme(PersonalDetailSettingActivity.this, jsonObject1.getString("description"));
                                        PrefManager.setDateofbirth(PersonalDetailSettingActivity.this, jsonObject1.getString("date_of_birth"));
                                        PrefManager.setSchool(PersonalDetailSettingActivity.this, jsonObject1.getString("school"));
                                        PrefManager.setGender(PersonalDetailSettingActivity.this, jsonObject1.getString("gender"));
                                        PrefManager.setProfileImage(PersonalDetailSettingActivity.this, jsonObject1.getString("profile_image"));
                                        if (!jsonObject1.isNull("push_notification")) {
                                            PrefManager.setPushnotification(PersonalDetailSettingActivity.this, jsonObject1.getString("push_notification"));
                                        }


                                        if (!jsonObject1.isNull("msg_notification")) {
                                            PrefManager.setMessagenotification(PersonalDetailSettingActivity.this, jsonObject1.getString("msg_notification"));
                                        }
                                        if (!jsonObject1.isNull("sync_contacts")) {
                                            PrefManager.setSynccontact(PersonalDetailSettingActivity.this, jsonObject1.getString("sync_contacts"));
                                        }

                                        if (jsonObject1.getString("pic_flag").equals("0")) {
                                            PrefManager.setIsProfilepicUpdate(PersonalDetailSettingActivity.this, false);
                                        } else if (jsonObject1.getString("pic_flag").equals("1")) {
                                            PrefManager.setIsProfilepicUpdate(PersonalDetailSettingActivity.this, true);
                                        }


                                        if (jsonObject1.getString("flag").equals("0")) {
                                            PrefManager.setIsProfileUpdate(PersonalDetailSettingActivity.this, false);
                                        } else if (jsonObject1.getString("flag").equals("1")) {
                                            PrefManager.setIsProfileUpdate(PersonalDetailSettingActivity.this, true);
                                        }

                                        if (jsonObject1.getString("category_flag").equals("0")) {
                                            PrefManager.setIsChalangeUpdate(PersonalDetailSettingActivity.this, false);
                                        } else if (jsonObject1.getString("category_flag").equals("1")) {
                                            PrefManager.setIsChalangeUpdate(PersonalDetailSettingActivity.this, true);
                                        }


                                        if (jsonObject1.getString("mobile_veri_token").equals("")) {
                                            PrefManager.setIsMobileVerified(PersonalDetailSettingActivity.this, false);
                                        } else if (jsonObject1.getString("mobile_veri_token").equals("1")) {
                                            PrefManager.setIsMobileVerified(PersonalDetailSettingActivity.this, true);
                                        }


                                        if (jsonObject1.getString("email_veri_token") != null) {
                                            if (jsonObject1.getString("email_veri_token").equals("")) {
                                                PrefManager.setIsEmailVerified(PersonalDetailSettingActivity.this, false);
                                            } else if (jsonObject1.getString("email_veri_token").equals("1")) {
                                                PrefManager.setIsEmailVerified(PersonalDetailSettingActivity.this, true);
                                            }
                                        } else {
                                            PrefManager.setIsEmailVerified(PersonalDetailSettingActivity.this, false);
                                        }


                                        if (jsonObject1.getString("category_flag").equals("0")) {
                                            Intent intent = new Intent(PersonalDetailSettingActivity.this, ChallengeCategoryActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);

                                        } else {
                                            Intent intent = new Intent(PersonalDetailSettingActivity.this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }

                                    }
                                    Toast.makeText(PersonalDetailSettingActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                                    Toast.makeText(PersonalDetailSettingActivity.this, message, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PersonalDetailSettingActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                                }
                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> param = new HashMap<>();
//                                user_id:44
//                                mobile_number:7785575757
//                                first_name:Aarti
//                                last_name:Dongare
//                                gender:male
//                                country:india
//                                country_code:IN
//                                date_of_birth:2020-20-10
//                                school:1
//                                user_type:1
//                                description:
//                                sync_contacts:
//                                msg_notification:1,1,0
//                                push_notification:1,1,0
                                param.put("user_id", PrefManager.getUserId(PersonalDetailSettingActivity.this));
                                param.put("first_name", fname);
                                param.put("last_name", lname);
                                param.put("gender", gender);
                                param.put("date_of_birth", dob);
                                param.put("school", school);
                                if (usertypeid.equals("")) {
                                    param.put("user_type", "");
                                } else {
                                    param.put("user_type", usertypeid);
                                }
                                param.put("description", desc_EditText.getText().toString());

//                                if (messagenotification != null) {
//                                    param.put("msg_notification", messagenotification);
//                                    Log.d("param_msgnot",messagenotification);
//                                }
//                                if (messagenotification == null) {
//                                    param.put("msg_notification", "");
//                                }
//
//
//                                if (pushnotification != null) {
//                                    param.put("push_notification", pushnotification);
//                                    Log.d("param_pushnot",pushnotification);
//                                }
//                                if (pushnotification == null) {
//                                    param.put("push_notification", "");
//                                }



                                param.put("country","");
                                param.put("country_code","");
                                param.put("sync_contacts", synccontacnotificationt);
                              //  param.put("language", "1");
                                param.put("mobile_number",PrefManager.getMobile(PersonalDetailSettingActivity.this));

                                param.put("push_mycontact",push_mycontact);
                                param.put("push_mymember",push_mymember);
                                param.put("push_myfollower",push_myfollower);

                                param.put("msg_mycontact",msg_mycontact);
                                param.put("msg_mymember",msg_mymember);
                                param.put("msg_myfollower",msg_myfollower);

                                return param;
                            }
                        };

                        MySingleton.getInstance(PersonalDetailSettingActivity.this).addToRequestQueue(stringRequest);

                    }
                }
            });

        }

    private void getUsertype() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.usertype, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("usertype",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){

                        JSONArray jsonArray=jsonObject.getJSONArray("user_type");


                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            POJO pojo=new POJO();
                            pojo.setId(jsonObject1.getString("id"));
                            pojo.setUsertype(jsonObject1.getString("name"));
                            usertypearrayList.add(pojo);
                        }

                        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(PersonalDetailSettingActivity.this, LinearLayoutManager.VERTICAL, false);
                        recviewusertype.setLayoutManager(horizontalLayoutManager);
                        UsertypeAdapter usertypeAdapter=new UsertypeAdapter(usertypearrayList,PersonalDetailSettingActivity.this);
                        recviewusertype.setAdapter(usertypeAdapter);
                        usertypeAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void OnItemClick(int position) {

                                POJO pojo=usertypearrayList.get(position);
                                textinputiaman.setText(pojo.getUsertype());
                                usertypeid=pojo.getId();
                                drawer_layout.closeDrawer(GravityCompat.END);

                                Log.d("dfdsff",usertypeid);
                                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                            }
                        });



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

                    Toast.makeText(PersonalDetailSettingActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonalDetailSettingActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param=new HashMap<>();
                param.put("user_id",PrefManager.getUserId(PersonalDetailSettingActivity.this));

                return param;
            }
        };
        MySingleton.getInstance(PersonalDetailSettingActivity.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
