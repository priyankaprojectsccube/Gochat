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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ccube9.gochat.Util.WebUrl.Verify_otp;

public class VerificationActivity extends AppCompatActivity {

    TextView texttitle,textname;
    EditText otpview;
    Button btn_verify;
    ImageView iv_back;
    String mobileno,name,userid;
    private TransparentProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        texttitle=findViewById(R.id.texttitle);
        btn_verify=findViewById(R.id.btn_verify);
        textname=findViewById(R.id.textname);
        iv_back=findViewById(R.id.iv_back);
        otpview=findViewById(R.id.otp_view);

        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        texttitle.setText(R.string.verificationcode);


        if(getIntent().hasExtra("mobileno") && getIntent().hasExtra("firstname") && getIntent().hasExtra("otp")&& getIntent().hasExtra("userid")) {
            mobileno = getIntent().getStringExtra("mobileno");
            name = getIntent().getStringExtra("firstname");
            userid=getIntent().getStringExtra("userid");

            textname.setText(getResources().getString(R.string.hi).concat(" "+name));
            otpview.setText(getIntent().getStringExtra("otp"));


            btn_verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Validation validation=new Validation();
                    if(!validation.edttxtvalidation(otpview,VerificationActivity.this)){

                    }
                    else {

                    pd.show();

                    StringRequest stringRequest=new StringRequest(Request.Method.POST, Verify_otp, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();

                            Log.d("Dfdsfsfs",response);

                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.getString("status").equals("1")){
                                    Intent intent = new Intent(VerificationActivity.this, CreatepasswordActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("mobileno", mobileno);
                                    intent.putExtra("userid",userid);
                                    startActivity(intent);
                                    finish();
                                }
                                Toast.makeText(VerificationActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(VerificationActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(VerificationActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                            }

                        }
                    }){

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> param=new HashMap<>();

                            param.put("otp",otpview.getText().toString());
                            param.put("language","1");
                            param.put("mobile_number",mobileno);


                            return param;
                        }
                    };

                    MySingleton.getInstance(VerificationActivity.this).addToRequestQueue(stringRequest);


                }}
            });
        }
        else {
            finish();
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }


    @Override
    public void onBackPressed() {

    }
}
