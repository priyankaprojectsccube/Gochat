package com.ccube9.gochat.Challenge.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.ccube9.gochat.Challenge.Adapter.ChallangeCategoryAdapter;
import com.ccube9.gochat.Profile.PersonalDetailSettingActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.ccube9.gochat.Home.HomeActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChallengeCategoryActivity extends AppCompatActivity {

    private TransparentProgressDialog pd;
    private ArrayList<POJO> challengecategoryList;
    private ArrayList<String> challangesubcategoryList;
    private ArrayList<String> selectedsubcatremovable;
    private RecyclerView choose_challenges_recyclerView;
    private Button finish_button;
    private String requestBody = " ";
    JSONObject jsonObject = new JSONObject();
    String maincatid="",subcatid="";

    private JSONArray jsonArrayselectedchallange;

    JSONObject jsonObjectuserid=new JSONObject();
    JSONObject jsonObjectlanguage=new JSONObject();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challengecategory);

        jsonArrayselectedchallange=new JSONArray();
        challengecategoryList=new ArrayList<>();
        challangesubcategoryList=new ArrayList<>();



        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        choose_challenges_recyclerView=findViewById(R.id.choose_challenges_recyclerView);
        finish_button=findViewById(R.id.finish_button);


        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.Challange_categorylist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                challengecategoryList.clear();
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
                           pojo.setMainchallengeid(jsonObject1.getString("main_cat_id"));
                           pojo.setCategoryname(jsonObject1.getString("category_name"));
                           challengecategoryList.add(pojo);
                       }

                       LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ChallengeCategoryActivity.this, LinearLayoutManager.VERTICAL, false);
                       choose_challenges_recyclerView.setLayoutManager(horizontalLayoutManager);
                       ChallangeCategoryAdapter  challangeCategoryAdapter= new ChallangeCategoryAdapter(challengecategoryList,ChallengeCategoryActivity.this);
                       choose_challenges_recyclerView.setAdapter(challangeCategoryAdapter);


                       challangeCategoryAdapter.setOnItemClickListener(position -> {


                           selectedsubcatremovable=new ArrayList<>();

                           try {

                               for(int j=0;j<jsonArrayselectedchallange.length();j++) {
                                   JSONObject jsonObject1 = jsonArrayselectedchallange.getJSONObject(j);
                                   if (jsonObject1.getString("main_cat").equals(challengecategoryList.get(position).getId())) {

                                       if(jsonObject1.has("sub_cat")){
                                           JSONArray jsonArray1=jsonObject1.getJSONArray("sub_cat");

                                           for(int k=0;k<jsonObject1.length();k++){
                                               selectedsubcatremovable.add(jsonArray1.getString(k));
                                           }
                                       }

                                       Log.d("dfdsfdsdfdsf", String.valueOf(selectedsubcatremovable.toArray()));
                                       jsonArrayselectedchallange.remove(j);

                                   }
                               }





                           } catch (JSONException e) {
                               e.printStackTrace();
                           }

                           Intent intent = new Intent(ChallengeCategoryActivity.this, ChallangesubcategoryActivity.class);
                           String catid = challengecategoryList.get(position).getMainchallengeid();
                           intent.putExtra("catid",catid);
                           intent.putExtra("catname",challengecategoryList.get(position).getCategoryname());
                           intent.putExtra("selectedsubcatidremovable",selectedsubcatremovable);

                           Log.d("dgdfgfdg", String.valueOf(selectedsubcatremovable));
                           startActivityForResult(intent,1);

                       });


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

                    Toast.makeText(ChallengeCategoryActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChallengeCategoryActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param=new HashMap<>();

                param.put("user_id",PrefManager.getUserId(ChallengeCategoryActivity.this));
                return param;
            }
        };

        MySingleton.getInstance(ChallengeCategoryActivity.this).addToRequestQueue(stringRequest);

        finish_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
if(jsonArrayselectedchallange.length() > 0){
    finishapi();
}else{
    Toast.makeText(ChallengeCategoryActivity.this,"Please Select Challenges",Toast.LENGTH_SHORT).show();
}



            }
        });

    }

    private void finishapi() {
//        try {
//
//                jsonObjectuserid.put("user_id",PrefManager.getUserId(ChallengeCategoryActivity.this));
//                jsonObjectlanguage.put("language","0");
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            jsonArrayselectedchallange.put(jsonObjectuserid);
//
//            jsonArrayselectedchallange.put(jsonObjectlanguage);
//
//        try {
//            jsonObject.put("name", jsonArrayselectedchallange);
//            requestBody = jsonObject.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


           // requestBody = jsonArrayselectedchallange.toString();


        pd.show();

//                JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST, WebUrl.Update_challange_category, jsonArrayselectedchallange,
//                        new Response.Listener<JSONArray>() {
//                            @Override
//                            public void onResponse(JSONArray response) {
//
//                                challengecategoryList.clear();
//                                pd.dismiss();
//
//                                Log.d("sdfsfdff", String.valueOf(response));
//
//                                try {
//                                    JSONObject jsonObject=new JSONObject((Map) response);
//                                    if(jsonObject.getString("status").equals("1")) {
//
//
//
//                          Intent intent=new Intent(ChallengeCategoryActivity.this, HomeActivity.class);
//                          PrefManager.setIsChalangeUpdate(ChallengeCategoryActivity.this,true);
//                          startActivity(intent);
//
//
//                                    }
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        pd.dismiss();
//                        Log.d("sdfsfdff", String.valueOf(volleyError));
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
//                            Toast.makeText(ChallengeCategoryActivity.this, message, Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(ChallengeCategoryActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                }){
//
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String,String> params = new HashMap<String, String>();
//                        params.put("Content-Type","application/json");
//                        return params;
//                    }
//                };
//
//
//                MySingleton.getInstance(ChallengeCategoryActivity.this).addToRequestQueue(jsonArrayRequest);


        StringRequest stringRequest1=new StringRequest(Request.Method.POST, WebUrl.Update_challange_category, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("sdsddfsfdff", response);


                challengecategoryList.clear();
                pd.dismiss();

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")) {




                        Intent intent=new Intent(ChallengeCategoryActivity.this, HomeActivity.class);
                        PrefManager.setIsChalangeUpdate(ChallengeCategoryActivity.this,true);
                        startActivity(intent);


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
                    Toast.makeText(ChallengeCategoryActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChallengeCategoryActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();
                }
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param=new HashMap<>();
                param.put("main_cat",maincatid);
                param.put("sub_cat",subcatid);
param.put("user_id",PrefManager.getUserId(ChallengeCategoryActivity.this));
              //  param.put("language","1");
                return param;
            }
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    Log.d("sdsdsdd",requestBody);
//                    return requestBody == null ? null : requestBody.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                    return null;
//                }
//
//            }
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded";//application/json; charset=utf-8
//
//            }
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                String responseString = "";
//                if (response != null) {
//                    responseString = String.valueOf(response);
//                    // can get more details such as response.headers
//                }
//                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//            }
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> param=new HashMap<>();
//                param.put("Content-Type","application/json");
//
//                return param;
//            }
        };
    //    stringRequest1.setShouldCache(false);
        MySingleton.getInstance(ChallengeCategoryActivity.this).addToRequestQueue(stringRequest1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK){

            JSONObject jsonObjectmaincat = new JSONObject();
            JSONArray jsonArraysubcat=new JSONArray();


            Boolean isPresent=false;
            int position=-1;





            if(data.hasExtra("maincatid") && data.hasExtra("subcatarr")) {

                maincatid=data.getStringExtra("maincatid");
                challangesubcategoryList=data.getStringArrayListExtra("subcatarr");
                subcatid = challangesubcategoryList.toString();

                try {

                    if(jsonArrayselectedchallange.length()>0) {

                        for (int i = 0; i < jsonArrayselectedchallange.length(); i++) {
                            JSONObject jsonObjectcat = jsonArrayselectedchallange.getJSONObject(i);
                            if (jsonObjectcat.has("main_cat")) {

                                if (jsonObjectcat.getString("main_cat").equals(maincatid)) {
                                      isPresent=true;
                                      position=i;


                                }
                            }
                        }


                        if(isPresent){


                            JSONObject jsonObjectmain=jsonArrayselectedchallange.getJSONObject(position);

                            if(challangesubcategoryList.size()>0) {
                            for(int i=0;i<challangesubcategoryList.size();i++) {
                                jsonArraysubcat.put(challangesubcategoryList.get(i));

                            }

                                jsonObjectmain.put("sub_cat",jsonArraysubcat);
                            }


                        }
                        else{

                            jsonObjectmaincat.put("main_cat", maincatid);

                            if(challangesubcategoryList.size()>0) {
                                for (int i = 0; i < challangesubcategoryList.size(); i++) {
                                    jsonArraysubcat.put(challangesubcategoryList.get(i));
                                }
                                jsonObjectmaincat.put("sub_cat", jsonArraysubcat);
                            }

                            jsonArrayselectedchallange.put(jsonObjectmaincat);

                        }

                    }
                    else{
                        jsonObjectmaincat.put("main_cat", maincatid);

                        if(challangesubcategoryList.size()>0) {
                            for (int i = 0; i < challangesubcategoryList.size(); i++) {
                                jsonArraysubcat.put(challangesubcategoryList.get(i));
                            }
                            jsonObjectmaincat.put("sub_cat", jsonArraysubcat);
                        }

                        jsonArrayselectedchallange.put(jsonObjectmaincat);

                    }


                    Log.d("rettret", String.valueOf(jsonArrayselectedchallange));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
