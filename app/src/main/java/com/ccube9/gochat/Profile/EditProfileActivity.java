package com.ccube9.gochat.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {


    private ImageView iv_back;
    private String Edittype;
    private TextView texttitle;
    private TextInputEditText schoolname_editText;
    private TextInputLayout txtInputschoolname;
    private Button pd_continue_button;
    private DrawerLayout drawer_layout;
    private String usertypeid="";
    private TransparentProgressDialog pd;
    private RecyclerView recviewusertype;
    private ArrayList<POJO> usertypearrayList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        iv_back=findViewById(R.id.iv_back);
        texttitle=findViewById(R.id.texttitle);
        recviewusertype=findViewById(R.id.recviewusertype);
        schoolname_editText=findViewById(R.id.schoolname_editText);
        txtInputschoolname=findViewById(R.id.txtInputschoolname);
        pd_continue_button=findViewById(R.id.pd_continue_button);
        drawer_layout=findViewById(R.id.drawer_layout);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);



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

        if(getIntent().hasExtra("edittype")){
            Edittype=getIntent().getStringExtra("edittype");

            if(Edittype.equals("editwenttoschoolin")){

                texttitle.setText("Edit Went to school in");
                txtInputschoolname.setHint(getResources().getString(R.string.iwenttoschoolin));
                schoolname_editText.setText(PrefManager.getSchool(EditProfileActivity.this));

                pd_continue_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        Validation validation=new Validation();
                        if(!validation.edttxtvalidation(schoolname_editText,txtInputschoolname,EditProfileActivity.this)){

                        }else{
                            pd.show();




                            StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.profile_update, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    pd.dismiss();

                                    Log.d("schooledit",response);


                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getString("status").equals("1")) {

                                            JSONArray jsonArray = jsonObject.getJSONArray("user_data");
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);


                                            if (jsonObject1.getString("flag").equals("0")) {
                                                PrefManager.setIsProfilepicUpdate(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("flag").equals("1")) {
                                                PrefManager.setIsProfilepicUpdate(EditProfileActivity.this, true);
                                            }


                                            PrefManager.setEmailId(EditProfileActivity.this, jsonObject1.getString("email_id"));
                                            PrefManager.setMobile(EditProfileActivity.this, jsonObject1.getString("mobile_number"));
                                            PrefManager.setFirstName(EditProfileActivity.this, jsonObject1.getString("first_name"));
                                            PrefManager.setLastName(EditProfileActivity.this, jsonObject1.getString("last_name"));
                                            PrefManager.setIaman(EditProfileActivity.this,jsonObject1.getString("type"));
                                            PrefManager.setAboutme(EditProfileActivity.this, jsonObject1.getString("description"));
                                            PrefManager.setDateofbirth(EditProfileActivity.this, jsonObject1.getString("date_of_birth"));
                                            PrefManager.setSchool(EditProfileActivity.this, jsonObject1.getString("school"));
                                            PrefManager.setGender(EditProfileActivity.this, jsonObject1.getString("gender"));
                                            PrefManager.setProfileImage(EditProfileActivity.this, jsonObject1.getString("profile_image"));

                                            if (!jsonObject.isNull("push_notification")) {
                                                PrefManager.setPushnotification(EditProfileActivity.this, jsonObject.getString("push_notification"));
                                            }
                                            if (!jsonObject.isNull("msg_notification")) {
                                                PrefManager.setMessagenotification(EditProfileActivity.this, jsonObject.getString("msg_notification"));
                                            }
                                            if (!jsonObject.isNull("sync_contacts")) {
                                                PrefManager.setSynccontact(EditProfileActivity.this, jsonObject.getString("sync_contacts"));
                                            }

                                            if (jsonObject1.getString("pic_flag").equals("0")) {
                                                PrefManager.setIsProfilepicUpdate(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("pic_flag").equals("1")) {
                                                PrefManager.setIsProfilepicUpdate(EditProfileActivity.this, true);
                                            }


                                            if (jsonObject1.getString("flag").equals("0")) {
                                                PrefManager.setIsProfileUpdate(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("flag").equals("1")) {
                                                PrefManager.setIsProfileUpdate(EditProfileActivity.this, true);
                                            }

                                            if (jsonObject1.getString("category_flag").equals("0")) {
                                                PrefManager.setIsChalangeUpdate(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("category_flag").equals("1")) {
                                                PrefManager.setIsChalangeUpdate(EditProfileActivity.this, true);
                                            }


                                            if (jsonObject1.getString("mobile_veri_token").equals("")) {
                                                PrefManager.setIsMobileVerified(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("mobile_veri_token").equals("1")) {
                                                PrefManager.setIsMobileVerified(EditProfileActivity.this, true);
                                            }


                                            if (jsonObject1.getString("email_veri_token") != null) {
                                                if (jsonObject1.getString("email_veri_token").equals("")) {
                                                    PrefManager.setIsEmailVerified(EditProfileActivity.this, false);
                                                } else if (jsonObject1.getString("email_veri_token").equals("1")) {
                                                    PrefManager.setIsEmailVerified(EditProfileActivity.this, true);
                                                }
                                            } else {
                                                PrefManager.setIsEmailVerified(EditProfileActivity.this, false);
                                            }


                                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);


                                        }
                                        Toast.makeText(EditProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                                        Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> param = new HashMap<>();

                                    param.put("user_id", PrefManager.getUserId(EditProfileActivity.this));
                                    param.put("school", schoolname_editText.getText().toString());
                                  //  param.put("language", "1");
                                    param.put("mobile_number", PrefManager.getMobile(EditProfileActivity.this));
                                    return param;
                                }
                            };

                            MySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);
                        }
                    }
                });




            }

            else if(Edittype.equals("editiaman")){
                texttitle.setText("Edit I am an");
                txtInputschoolname.setHint("I am an");
                schoolname_editText.setText(PrefManager.getIaman(EditProfileActivity.this));


                schoolname_editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_right, 0);
                schoolname_editText.setFocusable(false);
                schoolname_editText.setClickable(false);
                schoolname_editText.setCursorVisible(false);



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

                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(EditProfileActivity.this, LinearLayoutManager.VERTICAL, false);
                                recviewusertype.setLayoutManager(horizontalLayoutManager);
                                UsertypeAdapter usertypeAdapter=new UsertypeAdapter(usertypearrayList,EditProfileActivity.this);
                                recviewusertype.setAdapter(usertypeAdapter);
                                usertypeAdapter.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void OnItemClick(int position) {

                                        POJO pojo=usertypearrayList.get(position);
                                        schoolname_editText.setText(pojo.getUsertype());
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

                            Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                        }

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> param=new HashMap<>();
                        param.put("user_id",PrefManager.getUserId(EditProfileActivity.this));

                        return param;
                    }
                };
                MySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);


                schoolname_editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        drawer_layout.openDrawer(GravityCompat.END);
                    }
                });




                pd_continue_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Validation validation=new Validation();
                        if(!validation.edttxtvalidation(schoolname_editText,txtInputschoolname,EditProfileActivity.this)){

                        }else{


                            pd.show();




                            StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.profile_update, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    pd.dismiss();

                                    Log.d("fdgfgd",response);


                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getString("status").equals("1")) {

                                            JSONArray jsonArray = jsonObject.getJSONArray("user_data");
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);


                                            if (jsonObject1.getString("flag").equals("0")) {
                                                PrefManager.setIsProfilepicUpdate(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("flag").equals("1")) {
                                                PrefManager.setIsProfilepicUpdate(EditProfileActivity.this, true);
                                            }


                                            PrefManager.setEmailId(EditProfileActivity.this, jsonObject1.getString("email_id"));
                                            PrefManager.setMobile(EditProfileActivity.this, jsonObject1.getString("mobile_number"));
                                            PrefManager.setFirstName(EditProfileActivity.this, jsonObject1.getString("first_name"));
                                            PrefManager.setLastName(EditProfileActivity.this, jsonObject1.getString("last_name"));
                                            PrefManager.setIaman(EditProfileActivity.this,jsonObject1.getString("type"));
                                            PrefManager.setAboutme(EditProfileActivity.this, jsonObject1.getString("description"));
                                            PrefManager.setDateofbirth(EditProfileActivity.this, jsonObject1.getString("date_of_birth"));
                                            PrefManager.setSchool(EditProfileActivity.this, jsonObject1.getString("school"));
                                            PrefManager.setGender(EditProfileActivity.this, jsonObject1.getString("gender"));
                                            PrefManager.setProfileImage(EditProfileActivity.this, jsonObject1.getString("profile_image"));

                                            if (!jsonObject.isNull("push_notification")) {
                                                PrefManager.setPushnotification(EditProfileActivity.this, jsonObject.getString("push_notification"));
                                            }


                                            if (!jsonObject.isNull("msg_notification")) {
                                                PrefManager.setMessagenotification(EditProfileActivity.this, jsonObject.getString("msg_notification"));
                                            }
                                            if (!jsonObject.isNull("sync_contacts")) {
                                                PrefManager.setSynccontact(EditProfileActivity.this, jsonObject.getString("sync_contacts"));
                                            }


                                            if (jsonObject1.getString("pic_flag").equals("0")) {
                                                PrefManager.setIsProfilepicUpdate(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("pic_flag").equals("1")) {
                                                PrefManager.setIsProfilepicUpdate(EditProfileActivity.this, true);
                                            }


                                            if (jsonObject1.getString("flag").equals("0")) {
                                                PrefManager.setIsProfileUpdate(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("flag").equals("1")) {
                                                PrefManager.setIsProfileUpdate(EditProfileActivity.this, true);
                                            }

                                            if (jsonObject1.getString("category_flag").equals("0")) {
                                                PrefManager.setIsChalangeUpdate(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("category_flag").equals("1")) {
                                                PrefManager.setIsChalangeUpdate(EditProfileActivity.this, true);
                                            }


                                            if (jsonObject1.getString("mobile_veri_token").equals("")) {
                                                PrefManager.setIsMobileVerified(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("mobile_veri_token").equals("1")) {
                                                PrefManager.setIsMobileVerified(EditProfileActivity.this, true);
                                            }


                                            if (jsonObject1.getString("email_veri_token") != null) {
                                                if (jsonObject1.getString("email_veri_token").equals("")) {
                                                    PrefManager.setIsEmailVerified(EditProfileActivity.this, false);
                                                } else if (jsonObject1.getString("email_veri_token").equals("1")) {
                                                    PrefManager.setIsEmailVerified(EditProfileActivity.this, true);
                                                }
                                            } else {
                                                PrefManager.setIsEmailVerified(EditProfileActivity.this, false);
                                            }


                                            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);


                                        }
                                        Toast.makeText(EditProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                                        Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> param = new HashMap<>();

                                    param.put("user_id", PrefManager.getUserId(EditProfileActivity.this));
                                    if (usertypeid.equals("")) {
                                        param.put("user_type", "");
                                    } else {
                                        param.put("user_type", usertypeid);
                                    }
                                   // param.put("language", "1");
                                    param.put("mobile_number", PrefManager.getMobile(EditProfileActivity.this));
                                    return param;
                                }
                            };

                            MySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);




                        }
                    }
                });

            }
            else if(Edittype.equals("editaboutme")){

                texttitle.setText("Edit About me");
                txtInputschoolname.setHint("About me");
                schoolname_editText.setText(PrefManager.getAboutme(EditProfileActivity.this));

                pd_continue_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Validation validation=new Validation();
                        if(!validation.edttxtvalidation(schoolname_editText,txtInputschoolname,EditProfileActivity.this)){

                        }else{
                            pd.show();




                            StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.profile_update, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    pd.dismiss();

                                    Log.d("fdgfgd",response);


                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getString("status").equals("1")) {

                                            JSONArray jsonArray = jsonObject.getJSONArray("user_data");
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);


                                            if (jsonObject1.getString("flag").equals("0")) {
                                                PrefManager.setIsProfilepicUpdate(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("flag").equals("1")) {
                                                PrefManager.setIsProfilepicUpdate(EditProfileActivity.this, true);
                                            }


                                            PrefManager.setEmailId(EditProfileActivity.this, jsonObject1.getString("email_id"));
                                            PrefManager.setMobile(EditProfileActivity.this, jsonObject1.getString("mobile_number"));
                                            PrefManager.setFirstName(EditProfileActivity.this, jsonObject1.getString("first_name"));
                                            PrefManager.setLastName(EditProfileActivity.this, jsonObject1.getString("last_name"));
                                            PrefManager.setIaman(EditProfileActivity.this,jsonObject1.getString("type"));
                                            PrefManager.setAboutme(EditProfileActivity.this, jsonObject1.getString("description"));
                                            PrefManager.setDateofbirth(EditProfileActivity.this, jsonObject1.getString("date_of_birth"));
                                            PrefManager.setSchool(EditProfileActivity.this, jsonObject1.getString("school"));
                                            PrefManager.setGender(EditProfileActivity.this, jsonObject1.getString("gender"));
                                            PrefManager.setProfileImage(EditProfileActivity.this, jsonObject1.getString("profile_image"));


                                            if (!jsonObject1.isNull("push_notification")) {
                                                PrefManager.setPushnotification(EditProfileActivity.this, jsonObject1.getString("push_notification"));
                                            }


                                            if (!jsonObject1.isNull("msg_notification")) {
                                                PrefManager.setMessagenotification(EditProfileActivity.this, jsonObject1.getString("msg_notification"));
                                            }
                                            if (!jsonObject1.isNull("sync_contacts")) {
                                                PrefManager.setSynccontact(EditProfileActivity.this, jsonObject1.getString("sync_contacts"));
                                            }

                                            if (jsonObject1.getString("pic_flag").equals("0")) {
                                                PrefManager.setIsProfilepicUpdate(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("pic_flag").equals("1")) {
                                                PrefManager.setIsProfilepicUpdate(EditProfileActivity.this, true);
                                            }


                                            if (jsonObject1.getString("flag").equals("0")) {
                                                PrefManager.setIsProfileUpdate(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("flag").equals("1")) {
                                                PrefManager.setIsProfileUpdate(EditProfileActivity.this, true);
                                            }

                                            if (jsonObject1.getString("category_flag").equals("0")) {
                                                PrefManager.setIsChalangeUpdate(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("category_flag").equals("1")) {
                                                PrefManager.setIsChalangeUpdate(EditProfileActivity.this, true);
                                            }


                                            if (jsonObject1.getString("mobile_veri_token").equals("")) {
                                                PrefManager.setIsMobileVerified(EditProfileActivity.this, false);
                                            } else if (jsonObject1.getString("mobile_veri_token").equals("1")) {
                                                PrefManager.setIsMobileVerified(EditProfileActivity.this, true);
                                            }


                                            if (jsonObject1.getString("email_veri_token") != null) {
                                                if (jsonObject1.getString("email_veri_token").equals("")) {
                                                    PrefManager.setIsEmailVerified(EditProfileActivity.this, false);
                                                } else if (jsonObject1.getString("email_veri_token").equals("1")) {
                                                    PrefManager.setIsEmailVerified(EditProfileActivity.this, true);
                                                }
                                            } else {
                                                PrefManager.setIsEmailVerified(EditProfileActivity.this, false);
                                            }


                                            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);


                                        }
                                        Toast.makeText(EditProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                                        Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> param = new HashMap<>();

                                    param.put("user_id", PrefManager.getUserId(EditProfileActivity.this));
                                    param.put("description", schoolname_editText.getText().toString());
                                  //  param.put("language", "1");
                                    param.put("mobile_number", PrefManager.getMobile(EditProfileActivity.this));
                                    return param;
                                }
                            };

                            MySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);
                        }
                    }
                });
            }
        }



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(EditProfileActivity.this,ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(EditProfileActivity.this,ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
