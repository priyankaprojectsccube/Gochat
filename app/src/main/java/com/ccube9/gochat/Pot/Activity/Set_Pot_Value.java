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
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.Validation;
import com.ccube9.gochat.Util.VolleyMultipartRequest;
import com.ccube9.gochat.Util.WebUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Set_Pot_Value extends AppCompatActivity {
    ImageView iv_back;
    TextView texttitle;
    Button setpotvalue;
    String pot_id;
    EditText et_potvalue,et_donationvalue;
    private TransparentProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__pot__value);

        if (getIntent().hasExtra("pot_id")) {
            pot_id = getIntent().getStringExtra("pot_id");
        }
     //   Log.d("pot_id",pot_id) ;

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        et_potvalue= findViewById(R.id.et_potvalue);
        et_donationvalue = findViewById(R.id.et_donationvalue);
        setpotvalue = findViewById(R.id.setpotvalue);
        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);

        texttitle.setText("Set Pot Value");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Set_Pot_Value.this, Create_Pot_Challenge_3.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        setpotvalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation validation=new Validation();
int potvalue= Integer.parseInt(et_potvalue.getText().toString());
int donationvalue = Integer.parseInt(et_donationvalue.getText().toString());



                if (!validation.edttxtvalidation(et_potvalue,Set_Pot_Value.this)){

                }
                else if (!validation.edttxtvalidation(et_donationvalue,Set_Pot_Value.this)){

                }else if(donationvalue > potvalue){
                    Toast.makeText(Set_Pot_Value.this,"Pot value should be greater than donation value",Toast.LENGTH_SHORT).show();
                }else if(potvalue <= 0){
                    Toast.makeText(Set_Pot_Value.this,"Pot value should be greater than 0",Toast.LENGTH_SHORT).show();
                }else if(donationvalue <=0){
                    Toast.makeText(Set_Pot_Value.this,"Donation value should be greater than 0",Toast.LENGTH_SHORT).show();
                }

                else {
                    callapi();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();



    }
    private void callapi() {

        pd.show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebUrl.set_pot_value,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        pd.dismiss();

                        Log.d("set_pot_value", String.valueOf(response.data));

                        try {
                            String result = new String(response.data);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("status").equals("1")) {
                                Intent intent = new Intent(Set_Pot_Value.this,PotActivity.class);
//                                intent.putExtra("pot_id",pot_id);
//                                Log.d("Pot_id",pot_id);
                                startActivity(intent);
//Toast.makeText(Create_Pot_Challenge.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Set_Pot_Value.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("chatlenge_association", volleyError.toString());
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

                    Toast.makeText(Set_Pot_Value.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Set_Pot_Value.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
//                user_id:1
//                pot_id:1
//                pot_value:1000
//                minimum_donation:100
                param.put("user_id", PrefManager.getUserId(Set_Pot_Value.this));
                param.put("pot_id",pot_id);
                param.put("pot_value", et_potvalue.getText().toString());
                param.put("minimum_donation",  et_donationvalue.getText().toString());







                Log.d("params",PrefManager.getUserId(Set_Pot_Value.this)+" "+pot_id+" "+ et_potvalue.getText().toString()
                        +" "+ et_donationvalue.getText().toString());

                return param;
            }

        };

        MySingleton.getInstance(Set_Pot_Value.this).addToRequestQueue(volleyMultipartRequest);
    }
}

