package com.ccube9.gochat.Pot.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
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
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.Validation;
import com.ccube9.gochat.Util.WebUrl;
import com.stripe.android.Stripe;
import com.stripe.android.model.Token;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContributionActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView texttitle;
    Button contribute;
    String pot_id;
    EditText textinputcontriamount;
    private TransparentProgressDialog pd;
    int REQUEST_CODE = 0077;
    public String stripe_token = "pk_test_51HNJgEBooowUoBZA8V6r7WMJftSu0Dqq2oxgFJMDLow7r6iykc8GgBCF3Rp8Sj0FLVV9TgKtjfQ1zchRPXS54jKx00Nc5mbKFm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribution);

        if (getIntent().hasExtra("pot_id")) {
            pot_id = getIntent().getStringExtra("pot_id");
        }

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        textinputcontriamount= findViewById(R.id.textinputcontriamount);

        contribute = findViewById(R.id.contribute);
        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);

        texttitle.setText("Contribution");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContributionActivity.this, Pot_Chatlenge.class);
                intent.putExtra("pot_id",pot_id);
                startActivity(intent);
            }
        });

        contribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation validation=new Validation();
                int contriamt= Integer.parseInt(textinputcontriamount.getText().toString());




                if (!validation.edttxtvalidation(textinputcontriamount,ContributionActivity.this)){

                }
              else if(contriamt <= 0){
                    Toast.makeText(ContributionActivity.this,"Amount should be greater than 0",Toast.LENGTH_SHORT).show();
                }

                else {
                   // callapi();

                    Intent intent = new Intent(ContributionActivity.this, CheckoutActivityJava.class); //WebViewActivity
                    intent.putExtra("pot_id",pot_id);
                    intent.putExtra("amount",textinputcontriamount.getText().toString());
                    Log.d("amount",textinputcontriamount.getText().toString());
                    startActivity(intent);

//                    Map<String, Object> card = new HashMap<>();
//                    card.put("number", "4242424242424242");
//                    card.put("exp_month", 12);
//                    card.put("exp_year", 2021);
//                    card.put("cvc", "314");
//                    Map<String, Object> params = new HashMap<>();
//                    params.put("card", card);
//
//                    Token token = Token.create(params);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {


            stripe_token = data.getStringExtra("stripe_token");
            Log.d("stripetoke", stripe_token);
            if (stripe_token.length() > 1){

            }


        }

    }

    private void callapi() {

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();

                Log.d("get_payment_getway_url", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        String weburl = jsonObject.getString("getway_url");

                        Intent intent = new Intent(ContributionActivity.this, WebViewActivity.class); //WebViewActivity
                        intent.putExtra("weburl",weburl);
                        startActivity(intent);

//Toast.makeText(Create_Pot_Challenge.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ContributionActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("get_payment_getway_url", volleyError.toString());
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

                    Toast.makeText(ContributionActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ContributionActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(ContributionActivity.this));



                return param;
            }
        };

        MySingleton.getInstance(ContributionActivity.this).addToRequestQueue(stringRequest);
    }
}