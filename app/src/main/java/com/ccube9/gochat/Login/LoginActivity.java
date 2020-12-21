package com.ccube9.gochat.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.ccube9.gochat.Challenge.Activity.ChallengeCategoryActivity;
import com.ccube9.gochat.Profile.PersonalDetailActivity;
import com.ccube9.gochat.Profile.ProfilePicActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.Validation;
import com.ccube9.gochat.Home.HomeActivity;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ccube9.gochat.Util.WebUrl.Forgot_password;
import static com.ccube9.gochat.Util.WebUrl.Login_url;

public class LoginActivity extends AppCompatActivity {


    private TextView mLogin,mForgotpassword,mRegister;
    private EditText mUsername,mPassword;
    private TransparentProgressDialog pd;
    private TextInputLayout textinputusername,linlaypassword;
    private Validation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);



        mUsername=findViewById(R.id.username);
        textinputusername=findViewById(R.id.textinputusername);
        linlaypassword=findViewById(R.id.linlaypassword);
        mPassword=findViewById(R.id.password);
        mForgotpassword=findViewById(R.id.forgotpassword);
        mLogin=findViewById(R.id.login);
        mRegister=findViewById(R.id.register);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);

        mForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



        final Dialog dialog=new Dialog(LoginActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_forgot_pass);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        final EditText dia_edt_mail=dialog.findViewById(R.id.dia_edt_mail);
        Button btn_submit=dialog.findViewById(R.id.btn_submit);
        ImageView close=dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final Validation validation=new Validation();

                if(!validation.edttxtvalidation(dia_edt_mail,LoginActivity.this)){

                }
                else {

                    pd.show();

                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Forgot_password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();

                            Log.d("SDfdsf",response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("1")) {
                                    if (dia_edt_mail.getText().toString().matches(validation.email_pattern)) {
                                        dialog.dismiss();
                                    }

                                    } else if (dia_edt_mail.getText().toString().matches(validation.no_pattern)) {

                                    }

                               Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("dfdsff", String.valueOf(e));
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

                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                            }


                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param = new HashMap<>();


                            if (dia_edt_mail.getText().toString().matches(validation.email_pattern)) {
                                param.put("email_id", dia_edt_mail.getText().toString());
                                param.put("mobile_number", "");
                               // param.put("language", "1");
                            } else if(dia_edt_mail.getText().toString().matches(validation.no_pattern) ){
                                param.put("email_id", "");
                                param.put("mobile_number", dia_edt_mail.getText().toString());
                             //   param.put("language", "1");
                            }

                            return param;
                        }
                    };

                    MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest1);

                }

            }
        });




            }
        });






  mRegister.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          Intent intent=new Intent(LoginActivity.this,CreateaccountActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(intent);

      }
  });



  mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 validation = new Validation();
                if (!validation.edttxtvalidation(mUsername, textinputusername,LoginActivity.this)) {

                } else if (!validation.emailmobilevalidation(mUsername,textinputusername, LoginActivity.this)){


                } else if (!validation.edttxtvalidation(mPassword,linlaypassword, LoginActivity.this)) {

                } else {


                    pd.show();

                  StringRequest stringRequest = new StringRequest(Request.Method.POST, Login_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            pd.dismiss();

                            Log.d("fgdfg",response);


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("1")) {

                                    JSONArray jsonArray=jsonObject.getJSONArray("user_login");
                                    JSONObject jsonObject1=jsonArray.getJSONObject(0);



                                    PrefManager.setIsLogin(LoginActivity.this,true);
                                    PrefManager.setUserId(LoginActivity.this,jsonObject1.getString("id"));
                                    PrefManager.setEmailId(LoginActivity.this,jsonObject1.getString("email_id"));
                                    PrefManager.setMobile(LoginActivity.this,jsonObject1.getString("mobile_number"));
                                    PrefManager.setFirstName(LoginActivity.this,jsonObject1.getString("first_name"));
                                    PrefManager.setIaman(LoginActivity.this,jsonObject1.getString("type"));
                                    PrefManager.setAboutme(LoginActivity.this,jsonObject1.getString("description"));
                                    PrefManager.setLastName(LoginActivity.this,jsonObject1.getString("last_name"));
                                    PrefManager.setDateofbirth(LoginActivity.this,jsonObject1.getString("date_of_birth"));
                                    PrefManager.setSchool(LoginActivity.this,jsonObject1.getString("school"));
                                    PrefManager.setGender(LoginActivity.this,jsonObject1.getString("gender"));
                                    PrefManager.setProfileImage(LoginActivity.this,jsonObject1.getString("profile_image"));
                                    PrefManager.setFCMToken(LoginActivity.this,jsonObject1.getString("token_id"));


                                   if (!jsonObject1.isNull("push_notification")) {
                                        PrefManager.setPushnotification(LoginActivity.this, jsonObject1.getString("push_notification"));
                                   }
                                    if (!jsonObject1.isNull("msg_notification")) {
                                        PrefManager.setMessagenotification(LoginActivity.this, jsonObject1.getString("msg_notification"));
                                   }
                                    if (!jsonObject1.isNull("sync_contacts")) {
                                        PrefManager.setSynccontact(LoginActivity.this, jsonObject1.getString("sync_contacts"));
                                   }


                                    if(jsonObject1.getString("pic_flag").equals("0")){
                                        PrefManager.setIsProfilepicUpdate(LoginActivity.this,false);
                                    }
                                    else if(jsonObject1.getString("pic_flag").equals("1")){
                                        PrefManager.setIsProfilepicUpdate(LoginActivity.this,true);
                                    }

                                    if(jsonObject1.getString("mobile_veri_token").equals("")){
                                        PrefManager.setIsMobileVerified(LoginActivity.this,false);
                                    }
                                    else if(jsonObject1.getString("mobile_veri_token").equals("1")){
                                        PrefManager.setIsMobileVerified(LoginActivity.this,true);
                                    }


                                    if(jsonObject1.getString("email_veri_token")!=null){
                                        if(jsonObject1.getString("email_veri_token").equals("")){
                                            PrefManager.setIsEmailVerified(LoginActivity.this,false);
                                        }
                                        else if(jsonObject1.getString("email_veri_token").equals("1")){
                                            PrefManager.setIsEmailVerified(LoginActivity.this,true);
                                        }
                                    }
                                    else{
                                        PrefManager.setIsEmailVerified(LoginActivity.this,false);
                                    }

                                    if(jsonObject1.getString("flag").equals("0")) {
                                        PrefManager.setIsProfileUpdate(LoginActivity.this,false);
                                    }
                                    else if(jsonObject1.getString("flag").equals("1")) {
                                        PrefManager.setIsProfileUpdate(LoginActivity.this,true);
                                    }

                                    if(jsonObject1.getString("category_flag").equals("0")){
                                        PrefManager.setIsChalangeUpdate(LoginActivity.this,false);
                                    }
                                    else if(jsonObject1.getString("category_flag").equals("1")){
                                        PrefManager.setIsChalangeUpdate(LoginActivity.this,true);
                                    }


                                    if(jsonObject1.getString("pic_flag").equals("0") && !PrefManager.IsProfilePicskip(LoginActivity.this)){

                                        Intent  intent=new Intent(LoginActivity.this,ProfilePicActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);


                                    }
                                    else if(jsonObject1.getString("flag").equals("0")){
                                        Intent  intent=new Intent(LoginActivity.this,PersonalDetailActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }
                                    else if(jsonObject1.getString("category_flag").equals("0")){
                                        Intent  intent=new Intent(LoginActivity.this, ChallengeCategoryActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }
                                    else {
                                        Intent  intent=new Intent(LoginActivity.this,HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }





                                }
                                Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("jhgjhj",volleyError.toString());

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

                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> param = new HashMap<>();


                            if(mUsername.getText().toString().matches(validation.email_pattern)){
                                param.put("email_id", mUsername.getText().toString());
                                param.put("mobile_number", "");
                            }
                            else if(mUsername.getText().toString().matches(validation.no_pattern)){
                                param.put("email_id", "");
                                param.put("mobile_number", mUsername.getText().toString());
                            }
                            param.put("password", mPassword.getText().toString());
                            //param.put("language", "1");
                            return param;
                        }
                    };

                    MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);

                }

            }
        });


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(LoginActivity.this,SelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }
}
