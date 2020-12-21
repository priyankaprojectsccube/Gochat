package com.ccube9.gochat.Pot.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.Profile.PersonalDetailActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.Validation;
import com.ccube9.gochat.Util.WebUrl;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Create_Pot_Challenge extends AppCompatActivity {
    ImageView iv_back;
    String pot_id;
    TextView texttitle;
    EditText dobname_editText,et_firstname,et_lastname,et_middlename,et_country;
    private TextInputLayout textinputfirstname,textinputmiddlename,textinputlastname,txtInputdob,textinputcountry;
    private String gender="";
    private Button male_button,female_button,btn_confirm;
    private TransparentProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__pot__challenge);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        textinputcountry = findViewById(R.id.textinputcountry);
        et_country = findViewById(R.id.et_country);
        txtInputdob = findViewById(R.id.txtInputdob);
        textinputlastname = findViewById(R.id.textinputlastname);
        textinputmiddlename = findViewById(R.id.textinputmiddlename);
        textinputfirstname = findViewById(R.id.textinputfirstname);
        et_middlename = findViewById(R.id.et_middlename);
        et_lastname = findViewById(R.id.et_lastname);
        et_firstname = findViewById(R.id.et_firstname);
        dobname_editText = findViewById(R.id.dobname_editText);
        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);
        male_button=findViewById(R.id.male_button);
        female_button=findViewById(R.id.female_button);
        btn_confirm=findViewById(R.id.btn_confirm);

        texttitle.setText("Create a Pot Chatlenge");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create_Pot_Challenge.this, PotActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation validation=new Validation();


                 if (gender.equals("")){
                     Toast.makeText(Create_Pot_Challenge.this, getResources().getString(R.string.pleaseselectgender), Toast.LENGTH_SHORT).show();

                }

                else if (!validation.edttxtvalidation(et_firstname,textinputfirstname,Create_Pot_Challenge.this)){

                }
                else if (!validation.edttxtvalidation(et_lastname,textinputlastname,Create_Pot_Challenge.this)){

                }
                else if (!validation.edttxtvalidation(dobname_editText,txtInputdob,Create_Pot_Challenge.this)){

                }

                 else if (!validation.edttxtvalidation(et_middlename,textinputmiddlename,Create_Pot_Challenge.this)){

                 }
                 else if (!validation.edttxtvalidation(et_country,textinputcountry,Create_Pot_Challenge.this)){

                 }
                    else {
                        callapi();
                 }
            }
        });

        male_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_button.setBackground(getDrawable(R.drawable.backgroundfilled_round));
                female_button.setBackground(getDrawable(R.drawable.background_round));

                male_button.setTextColor(getResources().getColor(R.color.colorWhite));
                female_button.setTextColor(getResources().getColor(R.color.colorblack));
                gender="Male";
            }
        });


        female_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_button.setBackground(getDrawable(R.drawable.background_round));
                female_button.setBackground(getDrawable(R.drawable.backgroundfilled_round));

                male_button.setTextColor(getResources().getColor(R.color.colorblack));
                female_button.setTextColor(getResources().getColor(R.color.colorWhite));
                gender="Female";

            }
        });


        dobname_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mPreviousYear = getCalculatedDate("yyyy-MM-dd", -20);

                Calendar calendar = Calendar.getInstance();
                String[] mstrDate = mPreviousYear.split("-");
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dp = new DatePickerDialog(Create_Pot_Challenge.this,android.R.style.Theme_Holo_Light_Dialog,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dobname_editText.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);

                    }
                }, Integer.parseInt(mstrDate[0]), Integer.parseInt(mstrDate[1]), Integer.parseInt(mstrDate[2]));
                dp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dp.getDatePicker().setMaxDate(System.currentTimeMillis());
                dp.show();

            }
        });

    }

    private void callapi() {

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.create_pot_chatlenge, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();

                Log.d("create_pot_chatlenge", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("pot_created");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                           pot_id =     (jsonObject1.getString("id"));

                        }

                        Intent intent = new Intent(Create_Pot_Challenge.this,Pot_Challenge_Association.class);
                        intent.putExtra("pot_id",pot_id);
                        Log.d("Pot_id",pot_id);
                        startActivity(intent);

//Toast.makeText(Create_Pot_Challenge.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Create_Pot_Challenge.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("create_pot_chatlenge", volleyError.toString());
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

                    Toast.makeText(Create_Pot_Challenge.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Create_Pot_Challenge.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(Create_Pot_Challenge.this));
                param.put("first_name", et_firstname.getText().toString());
                param.put("middle_name", et_middlename.getText().toString());
                param.put("last_name", et_lastname.getText().toString());
                param.put("date_of_birth", dobname_editText.getText().toString());
                param.put("gender", gender);
                param.put("country",et_country.getText().toString());

                Log.d("params",PrefManager.getUserId(Create_Pot_Challenge.this)+" "+et_firstname.getText().toString()+" "+ et_middlename.getText().toString()
                        +" "+et_lastname.getText().toString()+" "+dobname_editText.getText().toString()+" "+gender+" "+et_country.getText().toString());
                return param;
            }
        };

        MySingleton.getInstance(Create_Pot_Challenge.this).addToRequestQueue(stringRequest);





    }

    public static String getCalculatedDate(String dateFormat, int YEARS) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.YEAR, YEARS);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}