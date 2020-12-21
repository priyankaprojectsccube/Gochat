package com.ccube9.gochat.Challenge.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.ccube9.gochat.Challenge.Adapter.ChallangeSubcategoryAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChallangesubcategoryActivity extends AppCompatActivity {


    private TransparentProgressDialog pd;
    private ArrayList<POJO> subcategoryArray;
    private ArrayList<String> selectedsubcatidremovable;
    private RecyclerView subcat_recyclerView;
    private String subcatid,catname;
    private Button subcat_ok_button;
    public static ArrayList<String> selectedsubcatarr=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challangesubcategory);

        selectedsubcatarr.clear();


        selectedsubcatidremovable=new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra("catid")){
            subcatid=getIntent().getStringExtra("catid");
        }

        if(getIntent().hasExtra("catname")){
            catname=getIntent().getStringExtra("catname");
        }

        if(getIntent().hasExtra("catname")){
            catname=getIntent().getStringExtra("catname");
        }

        if(getIntent().hasExtra("selectedsubcatidremovable")){
            selectedsubcatidremovable=getIntent().getStringArrayListExtra("selectedsubcatidremovable");
        }






        setTitle(catname );



        subcat_recyclerView=findViewById(R.id.subcat_recyclerView);
        subcat_ok_button=findViewById(R.id.subcat_ok_button);


        subcat_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();

                if(selectedsubcatarr.size()>0) {
                    data.putExtra("subcatarr", selectedsubcatarr);
                }
                    data.putExtra("maincatid", subcatid);
                    setResult(RESULT_OK, data);
                    finish();


             }
        });

        subcategoryArray=new ArrayList<>();

        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);

        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.Challange_subcategorylist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                subcategoryArray.clear();
                pd.dismiss();

                Log.d("sdfsfdff",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("user_data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            POJO pojo = new POJO();
                            pojo.setId(jsonObject1.getString("id"));
                            pojo.setSubcategoryname(jsonObject1.getString("sub_cat_name"));
                            subcategoryArray.add(pojo);
                        }

                        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ChallangesubcategoryActivity.this, LinearLayoutManager.VERTICAL, false);
                        subcat_recyclerView.setLayoutManager(horizontalLayoutManager);
                        ChallangeSubcategoryAdapter challangeSubcategoryAdapter=new ChallangeSubcategoryAdapter(subcategoryArray,selectedsubcatidremovable,ChallangesubcategoryActivity.this);

                        subcat_recyclerView.setAdapter(challangeSubcategoryAdapter);





                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("sdfsfdff", String.valueOf(volleyError));
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

                    Toast.makeText(ChallangesubcategoryActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChallangesubcategoryActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param=new HashMap<>();
                param.put("user_id", PrefManager.getUserId(ChallangesubcategoryActivity.this));
                param.put("cat_id",subcatid);
                return param;
            }
        };

        MySingleton.getInstance(ChallangesubcategoryActivity.this).addToRequestQueue(stringRequest);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
             finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
