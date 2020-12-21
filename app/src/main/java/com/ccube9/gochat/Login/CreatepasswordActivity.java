package com.ccube9.gochat.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.Validation;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import static com.ccube9.gochat.Util.WebUrl.Create_password;


public class CreatepasswordActivity extends AppCompatActivity {

    private TextView texttitle,termcon;
    private TextInputLayout textinputpassword,textinputconpassword,txtinput_email;
    private EditText et_password, et_conpassword,et_email;
    private String firstname,lastname,userid,mobileno;
    private ImageView iv_back;
    private CheckBox chk_update,chk_termcon;
    private Button btn_connect;
    private TransparentProgressDialog pd;
    private RadioButton engishlanguage,germanlanguage;
    private RadioGroup chooselanguage;
    String selectedlanguage;
    int selectedlanguageint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpassword);

        texttitle=findViewById(R.id.texttitle);
        txtinput_email=findViewById(R.id.txtinput_email);
        iv_back=findViewById(R.id.iv_back);
        et_email=findViewById(R.id.et_email);
        termcon=findViewById(R.id.termcon);
        textinputpassword=findViewById(R.id.textinputpassword);
        textinputconpassword=findViewById(R.id.textinputconpassword);
        et_password=findViewById(R.id.et_password);
        chk_termcon=findViewById(R.id.chk_termcon);
        btn_connect=findViewById(R.id.btn_connect);
        chk_update=findViewById(R.id.chk_update);
        et_conpassword=findViewById(R.id.et_conpassword);
        chooselanguage = findViewById(R.id.chooselanguage);
        engishlanguage = findViewById(R.id.engishlanguage);
        germanlanguage = findViewById(R.id.germanlanguage);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        texttitle.setText(R.string.createpassword);


        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "I agree to the");
        spanTxt.append(" Terms and conditions");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // startActivity(new Intent(CreatepasswordActivity.this,TermconditionActivity.class));
            }
        }, spanTxt.length() - "Terms and conditions".length(), spanTxt.length(), 0);

        termcon.setMovementMethod(LinkMovementMethod.getInstance());
        termcon.setText(spanTxt, TextView.BufferType.SPANNABLE);

        chooselanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.engishlanguage) {
                    selectedlanguage = "1";
                } else if (checkedId == R.id.germanlanguage)
            {
                  selectedlanguage = "2";
                }
            }

        });
//        if(getIntent().hasExtra("email") && getIntent().hasExtra("firstname") && getIntent().hasExtra("lastname")){
//            email=getIntent().getStringExtra("email");
//            firstname=getIntent().getStringExtra("firstname");
//            lastname=getIntent().getStringExtra("lastname");
//
//
//            btn_connect.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//
//            Validation validation=new Validation();
//
//            if(!validation.edttxtvalidation(et_password,textinputpassword,CreatepasswordActivity.this)){
//
//            }
//            else if(!validation.passvalidation(et_password,textinputpassword,CreatepasswordActivity.this)){
//
//            }
//            else if(!validation.edttxtvalidation(et_conpassword,textinputconpassword,CreatepasswordActivity.this)){
//
//            }
//            else if(!validation.conpassvalidation(et_password,et_conpassword,textinputconpassword,CreatepasswordActivity.this)){
//
//            }
//            else if(!chk_termcon.isChecked()){
//                Toast.makeText(CreatepasswordActivity.this, getResources().getString(R.string.pleaseaccept), Toast.LENGTH_LONG).show();
//            }
//            else {
//
//                pd.show();
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, Register_url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pd.dismiss();
//
//                        Log.d("dfdsff",response);
//
//                        try {
//                            JSONObject jsonObject=new JSONObject(response);
//                            if(jsonObject.getString("status").equals("1")) {
//                                Intent intent=new Intent(CreatepasswordActivity.this,LoginActivity.class);
//                                startActivity(intent);
//                                finish();
//
//                            }
//                            Toast.makeText(CreatepasswordActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        pd.dismiss();
//
//                        Log.d("dfdsff",volleyError.toString());
//
//                        String message = null;
//                        if (volleyError instanceof NetworkError) {
//                            message = getResources().getString(R.string.cannotconnectinternate);
//                        } else if (volleyError instanceof ServerError) {
//                            message = getResources().getString(R.string.servernotfound);
//                        } else if (volleyError instanceof AuthFailureError) {
//                            message = getResources().getString(R.string.loginagain);
//                        } else if (volleyError instanceof ParseError) {
//                            message = getResources().getString(R.string.tryagain);
//                        } else if (volleyError instanceof NoConnectionError) {
//                            message = getResources().getString(R.string.cannotconnectinternate);
//                        } else if (volleyError instanceof TimeoutError) {
//                            message = getResources().getString(R.string.connectiontimeout);
//                        }
//                        if (message != null) {
//
//                            Toast.makeText(CreatepasswordActivity.this, message, Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(CreatepasswordActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    }
//                }) {
//
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//
//                        Map<String, String> param = new HashMap<>();
//                        param.put("email_id", email);
//                        param.put("mobile_number", "");
//                        param.put("first_name", firstname);
//                        param.put("last_name",lastname);
//                        if(chk_update.isChecked()){
//                            param.put("received_update","1" );
//                        }else {
//                            param.put("received_update","0" );
//                        }
//                        param.put("password",et_password.getText().toString() );
//                        param.put("language", "1");
//
//                        return param;
//                    }
//                };
//
//                MySingleton.getInstance(CreatepasswordActivity.this).addToRequestQueue(stringRequest);
//
//            }
//                }
//            });
//
//        }

         if(getIntent().hasExtra("mobileno") && getIntent().hasExtra("name")) {

            mobileno = getIntent().getStringExtra("mobileno");
            firstname = getIntent().getStringExtra("name");
            userid= getIntent().getStringExtra("userid");


            btn_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     selectedlanguageint = chooselanguage.getCheckedRadioButtonId();
            Validation validation=new Validation();

            if(!validation.edttxtvalidation(et_password,textinputpassword,CreatepasswordActivity.this)){

            }
            else if(!validation.passvalidation(et_password,textinputpassword,CreatepasswordActivity.this)){

            }
            else if(!validation.edttxtvalidation(et_conpassword,textinputconpassword,CreatepasswordActivity.this)){

            }
            else if(!validation.conpassvalidation(et_password,et_conpassword,textinputconpassword,CreatepasswordActivity.this)){

            }
            else if(!validation.edttxtvalidation(et_email,txtinput_email,CreatepasswordActivity.this)){

            }
            else if(!validation.emailvalidation(et_email,txtinput_email,CreatepasswordActivity.this)){

            }
            else if(!chk_termcon.isChecked()){
                Toast.makeText(CreatepasswordActivity.this, getResources().getString(R.string.pleaseaccept), Toast.LENGTH_LONG).show();
            } else if(chooselanguage.getCheckedRadioButtonId() == -1)
            {
                Toast.makeText(CreatepasswordActivity.this, "Please Choose Language", Toast.LENGTH_LONG).show();
            }

            else {

                pd.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST,Create_password , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();

                        Log.d("dfdsff",response);

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("status").equals("1")) {
                                Intent intent=new Intent(CreatepasswordActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);


                            }else{
                                Toast.makeText(CreatepasswordActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(CreatepasswordActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                            Toast.makeText(CreatepasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CreatepasswordActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                        }

                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> param = new HashMap<>();
                        param.put("user_id", userid);
                        param.put("new_password",et_password.getText().toString());
                        param.put("confirm_password",et_conpassword.getText().toString());
                        param.put("email_id",et_email.getText().toString());
                        if(chk_termcon.isChecked())
                        {
                            param.put("received_update","1");
                        }
                        else
                            {
                            param.put("received_update","0");
                        }
                        param.put("language",selectedlanguage);
                        param.put("token_id", PrefManager.getFCM_TOKEN(CreatepasswordActivity.this));
                        return param;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(CreatepasswordActivity.this).addToRequestQueue(stringRequest);




            }
                }
            });


        }
         else{
             finish();
         }


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

    }



    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//return;
       // finish();
    }
}
