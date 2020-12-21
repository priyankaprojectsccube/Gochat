package com.ccube9.gochat.Login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.asksira.loopingviewpager.LoopingViewPager;
import com.ccube9.gochat.Challenge.Activity.ChallangesubcategoryActivity;
import com.ccube9.gochat.Challenge.Activity.ChallengeCategoryActivity;
import com.ccube9.gochat.Profile.PersonalDetailActivity;
import com.ccube9.gochat.Profile.ProfilePicActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.Login.Adapter.PagerListAdapter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.rd.PageIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.ccube9.gochat.Util.WebUrl.Social_login;

public class SelectActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 1;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    String fbname,fbemail,gmailname,gmailemail;
    private TransparentProgressDialog pd;
    private Button mBtnregister,mBtnlogin;
    private LoopingViewPager mPager;
    private PageIndicatorView indicator;
    private ArrayList<String> arrayListimg;
    private LoginButton loginButton;
    CallbackManager callbackManager;
    private void init(){
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.loginfacebook);
        signInButton = findViewById(R.id.logingoogle);
        mPager = findViewById(R.id.pager);
        mBtnregister=findViewById(R.id.btnregister);
        mBtnlogin=findViewById(R.id.btnlogin);
        indicator = findViewById(R.id.indicator);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onclick() {

        mBtnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectActivity.this,CreateaccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        mBtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SelectActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        arrayListimg=new ArrayList<>();
        arrayListimg.add(0,getResources().getString(R.string.stringone));
        arrayListimg.add(1,getResources().getString(R.string.stringtwo));
        arrayListimg.add(2,getResources().getString(R.string.stringthree));

        mPager.setAdapter(new PagerListAdapter(SelectActivity.this,arrayListimg,true));

        indicator.setCount(mPager.getIndicatorCount());
          mPager.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
            @Override
            public void onIndicatorProgress(int selectingPosition, float progress) {
                indicator.setProgress(selectingPosition, progress);
            }

            @Override
            public void onIndicatorPageChange(int newIndicatorPosition) {
//                indicatorView.setSelection(newIndicatorPosition);
            }
        });




    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_select);


        init();
        onclick();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
       signInButton.setOnClickListener(this);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        loginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    fbemail = object.getString("email");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    fbname = object.getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                social_login_facebook();

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();



            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logingoogle:
                signIn();
                break;
            // ...
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }


    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
           gmailname = account.getGivenName();
          gmailemail= account.getEmail();

          social_login_gmail();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
        //    Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    private void social_login_gmail() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Social_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();

                Log.d("fgdfg",response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        JSONArray jsonArray=jsonObject.getJSONArray("user_login");
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);



                        PrefManager.setIsLogin(SelectActivity.this,true);
                        PrefManager.setUserId(SelectActivity.this,jsonObject1.getString("id"));
                        PrefManager.setEmailId(SelectActivity.this,jsonObject1.getString("email_id"));
                        PrefManager.setMobile(SelectActivity.this,jsonObject1.getString("mobile_number"));
                        PrefManager.setFirstName(SelectActivity.this,jsonObject1.getString("first_name"));
                        PrefManager.setIaman(SelectActivity.this,jsonObject1.getString("type"));
                        PrefManager.setAboutme(SelectActivity.this,jsonObject1.getString("description"));
                        PrefManager.setLastName(SelectActivity.this,jsonObject1.getString("last_name"));
                        PrefManager.setDateofbirth(SelectActivity.this,jsonObject1.getString("date_of_birth"));
                        PrefManager.setSchool(SelectActivity.this,jsonObject1.getString("school"));
                        PrefManager.setGender(SelectActivity.this,jsonObject1.getString("gender"));
                        PrefManager.setProfileImage(SelectActivity.this,jsonObject1.getString("profile_image"));
                        PrefManager.setFCMToken(SelectActivity.this,jsonObject1.getString("token_id"));

                        if (!jsonObject.isNull("push_notification")) {
                            PrefManager.setPushnotification(SelectActivity.this, jsonObject.getString("push_notification"));
                        }
                        if (!jsonObject.isNull("msg_notification")) {
                            PrefManager.setMessagenotification(SelectActivity.this, jsonObject.getString("msg_notification"));
                        }
                        if (!jsonObject.isNull("sync_contacts")) {
                            PrefManager.setSynccontact(SelectActivity.this, jsonObject.getString("sync_contacts"));
                        }


                        if(jsonObject1.getString("pic_flag").equals("0")){
                            PrefManager.setIsProfilepicUpdate(SelectActivity.this,false);
                        }
                        else if(jsonObject1.getString("pic_flag").equals("1")){
                            PrefManager.setIsProfilepicUpdate(SelectActivity.this,true);
                        }

                        if(jsonObject1.getString("mobile_veri_token").equals("")){
                            PrefManager.setIsMobileVerified(SelectActivity.this,false);
                        }
                        else if(jsonObject1.getString("mobile_veri_token").equals("1")){
                            PrefManager.setIsMobileVerified(SelectActivity.this,true);
                        }


                        if(jsonObject1.getString("email_veri_token")!=null){
                            if(jsonObject1.getString("email_veri_token").equals("")){
                                PrefManager.setIsEmailVerified(SelectActivity.this,false);
                            }
                            else if(jsonObject1.getString("email_veri_token").equals("1")){
                                PrefManager.setIsEmailVerified(SelectActivity.this,true);
                            }
                        }
                        else{
                            PrefManager.setIsEmailVerified(SelectActivity.this,false);
                        }

                        if(jsonObject1.getString("flag").equals("0")) {
                            PrefManager.setIsProfileUpdate(SelectActivity.this,false);
                        }
                        else if(jsonObject1.getString("flag").equals("1")) {
                            PrefManager.setIsProfileUpdate(SelectActivity.this,true);
                        }

                        if(jsonObject1.getString("category_flag").equals("0")){
                            PrefManager.setIsChalangeUpdate(SelectActivity.this,false);
                        }
                        else if(jsonObject1.getString("category_flag").equals("1")){
                            PrefManager.setIsChalangeUpdate(SelectActivity.this,true);
                        }


                        if(jsonObject1.getString("pic_flag").equals("0") && !PrefManager.IsProfilePicskip(SelectActivity.this)){

                            Intent  intent=new Intent(SelectActivity.this, ProfilePicActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        }
                        else if(jsonObject1.getString("flag").equals("0")){
                            Intent  intent=new Intent(SelectActivity.this, PersonalDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        else if(jsonObject1.getString("category_flag").equals("0")){
                            Intent  intent=new Intent(SelectActivity.this, ChallengeCategoryActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        else {
                            Intent  intent=new Intent(SelectActivity.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }





                    }
                    Toast.makeText(SelectActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(SelectActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SelectActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();



                param.put("email_id", gmailemail);
                param.put("password","");
               // param.put("language", "1");
                param.put("first_name",gmailname);
                param.put("platform","gmail");
                param.put("token_id",PrefManager.getFCM_TOKEN(SelectActivity.this));
                return param;
            }
        };

        MySingleton.getInstance(SelectActivity.this).addToRequestQueue(stringRequest);

    }

    public  void social_login_facebook(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Social_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();

                Log.d("fgdfg",response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        JSONArray jsonArray=jsonObject.getJSONArray("user_login");
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);



                        PrefManager.setIsLogin(SelectActivity.this,true);
                        PrefManager.setUserId(SelectActivity.this,jsonObject1.getString("id"));
                        PrefManager.setEmailId(SelectActivity.this,jsonObject1.getString("email_id"));
                        PrefManager.setMobile(SelectActivity.this,jsonObject1.getString("mobile_number"));
                        PrefManager.setFirstName(SelectActivity.this,jsonObject1.getString("first_name"));
                        PrefManager.setIaman(SelectActivity.this,jsonObject1.getString("type"));
                        PrefManager.setAboutme(SelectActivity.this,jsonObject1.getString("description"));
                        PrefManager.setLastName(SelectActivity.this,jsonObject1.getString("last_name"));
                        PrefManager.setDateofbirth(SelectActivity.this,jsonObject1.getString("date_of_birth"));
                        PrefManager.setSchool(SelectActivity.this,jsonObject1.getString("school"));
                        PrefManager.setGender(SelectActivity.this,jsonObject1.getString("gender"));
                        PrefManager.setProfileImage(SelectActivity.this,jsonObject1.getString("profile_image"));
                        PrefManager.setFCMToken(SelectActivity.this,jsonObject1.getString("token_id"));

                        if (!jsonObject1.isNull("push_notification")) {
                            PrefManager.setPushnotification(SelectActivity.this, jsonObject1.getString("push_notification"));
                        }
                        if (!jsonObject1.isNull("msg_notification")) {
                            PrefManager.setMessagenotification(SelectActivity.this, jsonObject1.getString("msg_notification"));
                        }
                        if (!jsonObject1.isNull("sync_contacts")) {
                            PrefManager.setSynccontact(SelectActivity.this, jsonObject1.getString("sync_contacts"));
                        }


                        if(jsonObject1.getString("pic_flag").equals("0")){
                            PrefManager.setIsProfilepicUpdate(SelectActivity.this,false);
                        }
                        else if(jsonObject1.getString("pic_flag").equals("1")){
                            PrefManager.setIsProfilepicUpdate(SelectActivity.this,true);
                        }

                        if(jsonObject1.getString("mobile_veri_token").equals("")){
                            PrefManager.setIsMobileVerified(SelectActivity.this,false);
                        }
                        else if(jsonObject1.getString("mobile_veri_token").equals("1")){
                            PrefManager.setIsMobileVerified(SelectActivity.this,true);
                        }


                        if(jsonObject1.getString("email_veri_token")!=null){
                            if(jsonObject1.getString("email_veri_token").equals("")){
                                PrefManager.setIsEmailVerified(SelectActivity.this,false);
                            }
                            else if(jsonObject1.getString("email_veri_token").equals("1")){
                                PrefManager.setIsEmailVerified(SelectActivity.this,true);
                            }
                        }
                        else{
                            PrefManager.setIsEmailVerified(SelectActivity.this,false);
                        }

                        if(jsonObject1.getString("flag").equals("0")) {
                            PrefManager.setIsProfileUpdate(SelectActivity.this,false);
                        }
                        else if(jsonObject1.getString("flag").equals("1")) {
                            PrefManager.setIsProfileUpdate(SelectActivity.this,true);
                        }

                        if(jsonObject1.getString("category_flag").equals("0")){
                            PrefManager.setIsChalangeUpdate(SelectActivity.this,false);
                        }
                        else if(jsonObject1.getString("category_flag").equals("1")){
                            PrefManager.setIsChalangeUpdate(SelectActivity.this,true);
                        }


                        if(jsonObject1.getString("pic_flag").equals("0") && !PrefManager.IsProfilePicskip(SelectActivity.this)){

                            Intent  intent=new Intent(SelectActivity.this, ProfilePicActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        }
                        else if(jsonObject1.getString("flag").equals("0")){
                            Intent  intent=new Intent(SelectActivity.this, PersonalDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        else if(jsonObject1.getString("category_flag").equals("0")){
                            Intent  intent=new Intent(SelectActivity.this, ChallengeCategoryActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        else {
                            Intent  intent=new Intent(SelectActivity.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }





                    }
                    Toast.makeText(SelectActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(SelectActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SelectActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();



                    param.put("email_id", fbemail);
                    param.put("password","");
                    //param.put("language", "1");
                    param.put("first_name",fbname);
                    param.put("platform","facebook");
                    param.put("token_id",PrefManager.getFCM_TOKEN(SelectActivity.this));
                return param;
            }
        };

        MySingleton.getInstance(SelectActivity.this).addToRequestQueue(stringRequest);

    }

}


