package com.ccube9.gochat.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.Validation;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ccube9.gochat.Util.WebUrl.Mobilenoalreadyregister;
import static com.ccube9.gochat.Util.WebUrl.Register_mobile_url;

public class CreateaccountActivity extends AppCompatActivity {


    private TextView texttitle;
    private Button btn_connect;
    private ImageView iv_back;
    private TextInputLayout textinputmobno,textinputusername,textinputlastname;
    private TransparentProgressDialog pd;
    private EditText et_username,et_mobileno,et_lastname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        texttitle=findViewById(R.id.texttitle);
        btn_connect=findViewById(R.id.btn_connect);
        et_lastname=findViewById(R.id.et_lastname);
        iv_back=findViewById(R.id.iv_back);

        textinputmobno=findViewById(R.id.textinputmobno);
        textinputusername=findViewById(R.id.textinputusername);
        textinputlastname=findViewById(R.id.textinputlastname);
        et_username=findViewById(R.id.et_username);
        et_mobileno=findViewById(R.id.et_mobileno);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        texttitle.setText(R.string.createaccount);





        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Validation validation=new Validation();
                if(!validation.edttxtvalidation(et_username,textinputusername,CreateaccountActivity.this)) {

                }
                else if(!validation.edttxtvalidation(et_lastname,textinputlastname,CreateaccountActivity.this)) {

                }
                else if(!validation.edttxtvalidation(et_mobileno,textinputmobno,CreateaccountActivity.this)){

                }
                else if(!validation.mobnovalidation(et_mobileno,textinputmobno,CreateaccountActivity.this)){

                }
                else {


//                    if(et_email.getText().toString().matches(validation.email_pattern)){
//                        Intent intent = new Intent(CreateaccountActivity.this, CreatepasswordActivity.class);
//                        intent.putExtra("firstname", et_username.getText().toString());
//                        intent.putExtra("lastname", et_lastname.getText().toString());
//                        intent.putExtra("email", et_email.getText().toString());
//                        startActivity(intent);
                 //   }
//                    else if(et_email.getText().toString().matches(validation.no_pattern)) {





                    StringRequest stringRequest=new StringRequest(Request.Method.POST, Mobilenoalreadyregister, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("dsadsada",response);
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.getString("status").equals("0")){
                                    pd.show();
                                    StringRequest stringRequest=new StringRequest(Request.Method.POST, Register_mobile_url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("dsadsada",response);
                                            pd.dismiss();
                                            try {
                                                JSONObject jsonObject=new JSONObject(response);
                                                if(jsonObject.getString("status").equals("1")){
                                                    JSONArray jsonArray=jsonObject.getJSONArray("user_data");
                                                    JSONObject jsonObject1=jsonArray.getJSONObject(0);
                                                    Intent intent = new Intent(CreateaccountActivity.this, VerificationActivity.class);
                                                    intent.putExtra("firstname", jsonObject1.getString("first_name"));
                                                    intent.putExtra("mobileno",jsonObject1.getString("mobile_number"));
                                                    intent.putExtra("otp",jsonObject.getString("otp"));
                                                    intent.putExtra("userid",jsonObject1.getString("id"));
                                                    startActivity(intent);
                                                }
                                                Toast.makeText(CreateaccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {

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

                                                Toast.makeText(CreateaccountActivity.this, message, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(CreateaccountActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    }){

                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String,String> param=new HashMap<>();
                                            param.put("mobile_number",et_mobileno.getText().toString());
                                            param.put("first_name",et_username.getText().toString());
                                            param.put("last_name",et_lastname.getText().toString());
                                            param.put("received_update","");
                                            param.put("language","1");
                                            return param;
                                        }
                                    };

                                    MySingleton.getInstance(CreateaccountActivity.this).addToRequestQueue(stringRequest);

                                }

                                if(jsonObject.getString("status").equals("1")) {
                                    Toast.makeText(CreateaccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(CreateaccountActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CreateaccountActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                            }

                        }
                    }){

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String,String> param=new HashMap<>();
                            param.put("mobile_number",et_mobileno.getText().toString());
                            param.put("language","1");
                            return param;
                        }
                    };

                    MySingleton.getInstance(CreateaccountActivity.this).addToRequestQueue(stringRequest);











                    }
                }

          //  }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CreateaccountActivity.this,SelectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(CreateaccountActivity.this,SelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }



}
