package com.ccube9.gochat.Home.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ccube9.gochat.Challenge.Activity.ChallengeDetailActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.WebUrl;
import com.ccube9.gochat.Home.Adapter.AcceptedChallangeAdapter;
import com.ccube9.gochat.Home.Adapter.ChallengesRecyclerAdapter;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

    private RecyclerView challenge_accepted_recycler,challenge_recycler;
  //  private TextView new_challenge_textView;
    private ChallengesRecyclerAdapter challengesRecyclerAdapter;
    private AcceptedChallangeAdapter acceptedChallangeAdapter;
    private ArrayList<String> imgarr;
    private ArrayList<String> imgarraccepted;
    private ArrayList<String> subcribeimgarry;
    private ArrayList<String> subcribeacceptimgarray;
    List<POJO> challangeArrList=new ArrayList<>();
    List<POJO> acceptedchallangeArrList=new ArrayList<>();
    private ArrayList<String>  imgarrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View  view=inflater.inflate(R.layout.fragment_home, container, false);



       // new_challenge_textView=view.findViewById(R.id.new_challenge_textView);
        challenge_accepted_recycler=view.findViewById(R.id.challenge_accepted_recycler);
        challenge_recycler=view.findViewById(R.id.challenge_recycler);
        acceptedchallangeArrList.clear();
        challangeArrList.clear();




        StringRequest stringRequest1=new StringRequest(Request.Method.POST, WebUrl.Acceptedchallangebyuser, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("acceptedchallenges",response);
                acceptedchallangeArrList.clear();

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){

                        JSONArray jsonArray = jsonObject.getJSONArray("challenge_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2=jsonObject1.getJSONObject("challenge_data");


                            JSONArray jsonArray2=jsonObject1.getJSONArray("image");
                            imgarraccepted=new ArrayList<>();


                            for (int j = 0; j < jsonArray2.length(); j++) {

                                JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
                                imgarraccepted.add(jsonObject3.getString("image_name"));

                            }
                            JSONArray jsonArray4 = jsonObject1.getJSONArray("subcribe_list_pic");
                            subcribeacceptimgarray = new ArrayList<>();
                            for (int k=0 ;k < jsonArray4.length(); k++){
                                JSONObject jsonObject5 = jsonArray4.getJSONObject(k);
                                subcribeacceptimgarray.add(jsonObject5.getString("profile_image"));
                            }

                            POJO pojo = new POJO();
                            pojo.setSubcribeImages(subcribeacceptimgarray);
                            pojo.setId(jsonObject2.getString("id"));
                            pojo.setFavourite(jsonObject1.getString("is_fav"));
                            pojo.setSubcribeCount(jsonObject1.getString("subscrib_user_count"));
                            pojo.setMainchallengeid(jsonObject2.getString("main_challenge_id"));
                            pojo.setTitle(jsonObject2.getString("challenge_name"));
                            pojo.setLatitude(jsonObject2.getString("lat"));
                            pojo.setLongitude(jsonObject2.getString("lang"));
                            pojo.setChallengetype(jsonObject2.getString("challenge_type"));

                            pojo.setTitle(jsonObject2.getString("challenge_name"));
                            pojo.setDate(jsonObject2.getString("date"));

                            pojo.setId(jsonObject2.getString("user_id"));
                            pojo.setDescription(jsonObject2.getString("description"));
                            pojo.setLocation(jsonObject2.getString("location"));
                            pojo.setLatitude(jsonObject2.getString("lat"));
                            pojo.setLongitude(jsonObject2.getString("lang"));

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
                            if (!jsonObject2.isNull("swift_code")) {
                                pojo.setSWiftcode(jsonObject2.getString("swift_code"));
                            }
                            pojo.setImages(imgarraccepted);

                            if(imgarraccepted.size()>0) {
                                pojo.setImage(imgarraccepted.get(0));
                            }

                            acceptedchallangeArrList.add(pojo);


                        }
                        acceptedChallangeAdapter = new AcceptedChallangeAdapter(acceptedchallangeArrList, getActivity());
                        challenge_accepted_recycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
                        challenge_accepted_recycler.setAdapter(acceptedChallangeAdapter);

                        acceptedChallangeAdapter.setOnItemClickListener(position -> {


                            Intent intent=new Intent(getActivity(),ChallengeDetailActivity.class);
                            intent.putExtra("challengedetail",acceptedchallangeArrList.get(position));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        });


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

                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
              //  param.put("language","1");
                param.put("user_id", PrefManager.getUserId(getActivity()));

                return param;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest1);


        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.ChallangeList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("get_challenge_list",response);
                challangeArrList.clear();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){

                        JSONArray jsonArray = jsonObject.getJSONArray("challenge_list");



                        for (int i = 0; i <jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2=jsonObject1.getJSONObject("challenge_data");

                            JSONArray jsonArray2=jsonObject1.getJSONArray("image");
                            imgarr=new ArrayList<>();
                            imgarrid = new ArrayList<>();

                                for (int j = 0; j < jsonArray2.length(); j++) {

                                    JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
                                    imgarr.add(jsonObject3.getString("image_name"));
                                    imgarrid.add(jsonObject3.getString("id"));
                                }

                            JSONArray jsonArray3=jsonObject1.getJSONArray("user_details");
                            JSONObject jsonObject4 = jsonArray3.getJSONObject(0);

                            JSONArray jsonArray4 = jsonObject1.getJSONArray("subcribe_list_pic");
                            subcribeimgarry = new ArrayList<>();
                            for (int k=0 ;k < jsonArray4.length(); k++){
                                JSONObject jsonObject5 = jsonArray4.getJSONObject(k);
                                subcribeimgarry.add(jsonObject5.getString("profile_image"));
                            }

                            POJO pojo = new POJO();

                            pojo.setImages(imgarr);
                            pojo.setImagesId(imgarrid);
                            if(imgarr.size()>0) {
                                pojo.setImage(imgarr.get(0));
                            }
                            pojo.setSubcribeImages(subcribeimgarry);
                            pojo.setChallengeid(jsonObject2.getString("id"));
                            pojo.setFavourite(jsonObject1.getString("is_fav"));
                            pojo.setSubcribeCount(jsonObject1.getString("subscrib_user_count"));
                            pojo.setMainchallengeid(jsonObject2.getString("main_challenge_id"));

                            pojo.setUsername(jsonObject4.getString("first_name").concat(" "+jsonObject4.getString("last_name")));
                            pojo.setMobileno(jsonObject4.getString("mobile_number"));
                            pojo.setChallengetype(jsonObject2.getString("challenge_type"));
                            pojo.setTitle(jsonObject2.getString("challenge_name"));
                            pojo.setDate(jsonObject2.getString("date"));

                            pojo.setId(jsonObject2.getString("user_id"));
                            pojo.setDescription(jsonObject2.getString("description"));
                            pojo.setLocation(jsonObject2.getString("location"));
                            pojo.setLatitude(jsonObject2.getString("lat"));
                            pojo.setLongitude(jsonObject2.getString("lang"));

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

                            challangeArrList.add(pojo);

                        }




                        challengesRecyclerAdapter = new ChallengesRecyclerAdapter(challangeArrList, getActivity(),0);
                        challenge_recycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
                        challenge_recycler.setAdapter(challengesRecyclerAdapter);
                        challengesRecyclerAdapter.setOnItemClickListener(position -> {


                            Intent intent=new Intent(getActivity(),ChallengeDetailActivity.class);
                            intent.putExtra("challengedetail",challangeArrList.get(position));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        });

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

                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
             //   param.put("language","1");
                param.put("user_id", PrefManager.getUserId(getActivity()));
                return param;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);



        return view;
    }



}
