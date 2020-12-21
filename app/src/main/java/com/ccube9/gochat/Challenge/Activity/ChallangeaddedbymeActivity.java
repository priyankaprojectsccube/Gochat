package com.ccube9.gochat.Challenge.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.ccube9.gochat.Profile.ProfileActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.ccube9.gochat.Home.Adapter.ChallengesRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChallangeaddedbymeActivity extends AppCompatActivity {


    private RecyclerView recchallangebyme,challenge_accepted_recycler;
    private TransparentProgressDialog pd;
    private TextView texttitle;
    private ImageView iv_back;
    List<POJO> challangeArrList=new ArrayList<>();
    private ArrayList<String> imgarraccepted;
    private ArrayList<String>  imgarrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challangebyme);

        iv_back=findViewById(R.id.iv_back);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        challenge_accepted_recycler=findViewById(R.id.challenge_accepted_recycler);
        recchallangebyme=findViewById(R.id.recchallangebyme);
        texttitle=findViewById(R.id.texttitle);


        Log.d("dfdfg",PrefManager.getUserId(ChallangeaddedbymeActivity.this));

        texttitle.setText("Challenged");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ChallangeaddedbymeActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.Challangeaddedbyme, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                challangeArrList.clear();
                pd.dismiss();

                Log.d("get_challenge_by_user",response);

                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);

                if(jsonObject.getString("status").equals("1")){

                JSONArray jsonArray = jsonObject.getJSONArray("challenge_list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("challenge_data");


                    JSONArray jsonArray2=jsonObject1.getJSONArray("image");
                    imgarraccepted=new ArrayList<>();
                    imgarrid = new ArrayList<>();

                    for (int j = 0; j < jsonArray2.length(); j++) {

                        JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
                        imgarraccepted.add(jsonObject3.getString("image_name"));
                        imgarrid.add(jsonObject3.getString("id"));

                    }

                    POJO pojo = new POJO();
                    pojo.setId(jsonObject2.getString("id"));
                    pojo.setMainchallengeid(jsonObject2.getString("main_challenge_id"));
                    pojo.setTitle(jsonObject2.getString("challenge_name"));
                    pojo.setDescription(jsonObject2.getString("description"));
                    pojo.setLocation(jsonObject2.getString("location"));
                    pojo.setLatitude(jsonObject2.getString("lat"));
                    pojo.setLongitude(jsonObject2.getString("lang"));
                    pojo.setDate(jsonObject2.getString("date"));
                    pojo.setPublishto(jsonObject2.getString("published_to"));
                    pojo.setChallangesfor(jsonObject2.getString("challenges_for"));
                    pojo.setChallengetype(jsonObject2.getString("challenge_type"));
                    if (!jsonObject2.isNull("name_of_association")) {
                        pojo.setAssociationname(jsonObject2.getString("name_of_association"));
                    }
                    if (!jsonObject2.isNull("details_of_association")) {
                        pojo.setDetailofassociation(jsonObject2.getString("details_of_association"));
                    }
                    if (!jsonObject2.isNull("website_link")) {
                        pojo.setWebsitelink(jsonObject2.getString("website_link"));
                    }
                    if (!jsonObject2.isNull("iban")) {
                        pojo.setIban(jsonObject2.getString("iban"));
                    }
                    if(!jsonObject2.isNull("swift_code")){
                        pojo.setSWiftcode(jsonObject2.getString("swift_code"));
                    }
                    pojo.setImages(imgarraccepted);
                    pojo.setImagesId(imgarrid);
                    if(imgarraccepted.size()>0) {
                        pojo.setImage(imgarraccepted.get(0));
                    }

                    challangeArrList.add(pojo);

                }

                Log.d("jjghg", String.valueOf(challangeArrList.size()));

                    ChallengesRecyclerAdapter challengesRecyclerAdapter = new ChallengesRecyclerAdapter(challangeArrList,ChallangeaddedbymeActivity.this,1);
                    recchallangebyme.setLayoutManager(new GridLayoutManager(ChallangeaddedbymeActivity.this,2));
                    recchallangebyme.setAdapter(challengesRecyclerAdapter);
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

                    Toast.makeText(ChallangeaddedbymeActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChallangeaddedbymeActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param=new HashMap<>();
              //  param.put("language","1");
                param.put("user_id", PrefManager.getUserId(ChallangeaddedbymeActivity.this));
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ChallangeaddedbymeActivity.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ChallangeaddedbymeActivity.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
