package com.ccube9.gochat.Pot.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ccube9.gochat.Challenge.Activity.MyChallengeDetailActivity;
import com.ccube9.gochat.Pot.Activity.PotActivity;
import com.ccube9.gochat.Pot.Activity.ProfileInstaActivity;
import com.ccube9.gochat.Pot.Adapter.MyPotAdapter;
import com.ccube9.gochat.Profile.Adapter.MyChallengeAdapter;
import com.ccube9.gochat.Profile.MyChallengeActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MYPOTLIST;
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
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThreeFragment extends Fragment {
RecyclerView recvewthree;
    String getData;
    MyPotAdapter myPotAdapter;
    private TransparentProgressDialog pd;
    private ArrayList<String> subcribeimgarry;
    List<POJO> mychallengelist =new ArrayList<>();
    private ArrayList<String> imgarraccepted;
    private MyChallengeAdapter myChallengeAdapter;
    List<MYPOTLIST> mypotListArrayList =new ArrayList<>();
    public ThreeFragment() {
        // Required empty public constructor
    }


    public static ThreeFragment newInstance(String param1, String param2) {
        ThreeFragment fragment = new ThreeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_three, container, false);

        recvewthree = view.findViewById(R.id.recvewthree);
//        recvewfour = view.findViewById(R.id.recvewfour);
        pd = new TransparentProgressDialog(getContext(), R.drawable.ic_loader_image);
        ProfileInstaActivity activity = (ProfileInstaActivity) getActivity();
        getData = activity.sendData();
        Log.d("getdata",getData);
        callapi();
        return view;
    }

    private void callapi() {
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.My_Challenge_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mychallengelist.clear();
                pd.dismiss();

                Log.d("fsfsdfdsff",response);

                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);

                    if(jsonObject.getString("status").equals("1")){

                        JSONArray jsonArray = jsonObject.getJSONArray("challenge_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("challenge_data");  //challenge_following_user_listdata


                            JSONArray jsonArray2=jsonObject1.getJSONArray("image");
                            imgarraccepted=new ArrayList<>();
                            for (int j = 0; j < jsonArray2.length(); j++) {
                                JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
                                imgarraccepted.add(jsonObject3.getString("image_name"));
                            }



                            JSONArray jsonArray4 = jsonObject1.getJSONArray("subcribe_list_pic");
                            subcribeimgarry = new ArrayList<>();
                            for (int k=0 ;k < jsonArray4.length(); k++){
                                JSONObject jsonObject5 = jsonArray4.getJSONObject(k);
                                subcribeimgarry.add(jsonObject5.getString("profile_image"));
                            }

                            POJO pojo = new POJO();
                            pojo.setImages(imgarraccepted);
                            if(imgarraccepted.size()>0) {
                                pojo.setImage(imgarraccepted.get(0));
                            }
                            pojo.setSubcribeImages(subcribeimgarry);
                            pojo.setId(jsonObject2.getString("id"));
                            pojo.setMainchallengeid(jsonObject2.getString("main_challenge_id"));
                            pojo.setTitle(jsonObject2.getString("challenge_name"));
                            pojo.setFavourite(jsonObject1.getString("is_fav"));
                            pojo.setSubcribeCount(jsonObject1.getString("subscrib_user_count"));
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

                            mychallengelist.add(pojo);

                        }

                        Log.d("jjghg", String.valueOf(mychallengelist.size()));
//  recvewthree.setNestedScrollingEnabled(true);
//                        myChallengeAdapter = new MyChallengeAdapter(mychallengelist, getContext());
//                        recvewthree.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
//                        recvewthree.setAdapter(myChallengeAdapter);


                        myChallengeAdapter = new MyChallengeAdapter(mychallengelist, getContext());
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);
                        recvewthree.setLayoutManager(gridLayoutManager);
                        recvewthree.setAdapter(myChallengeAdapter);

//                        ChallengesRecyclerAdapter challengesRecyclerAdapter = new ChallengesRecyclerAdapter(acceptchallengelist,ChallangeAcceptedActivity.this,0);
//                        recchallangebyme.setLayoutManager(new GridLayoutManager(ChallangeAcceptedActivity.this,2));
//                        recchallangebyme.setAdapter(challengesRecyclerAdapter);

                        myChallengeAdapter.setOnItemClickListener(position -> {


                            Intent intent=new Intent(getContext(), MyChallengeDetailActivity.class);
                            intent.putExtra("mychallengedetail",mychallengelist.get(position));
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

                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param=new HashMap<>();
                //  param.put("language","1");
                param.put("user_id", getData);
                return param;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

//    private void callmypots() {
//
//        pd.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.my_pot_chatlenge_list, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                mypotListArrayList.clear();
//                pd.dismiss();
//                Log.d("my_chatlenge_list", response);
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("status").equals("1")) {
//
//
//                        JSONArray jsonArray = jsonObject.getJSONArray("my_pot_list");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//
//
//                            MYPOTLIST potList = new MYPOTLIST();
//                            potList.setId(jsonObject1.getString("id"));
//                            potList.setUserid(jsonObject1.getString("user_id"));
//                            potList.setFirst_name(jsonObject1.getString("first_name")+" "+jsonObject1.getString("last_name"));
//                            potList.setLogo_association(jsonObject1.getString("logo_association"));
//                            potList.setBanner_for_association(jsonObject1.getString("banner_for_association"));
//                            potList.setAbout_pot(jsonObject1.getString("title"));
//                            mypotListArrayList.add(potList);
//
//
//                        }
//                        Log.d("mypotlist", String.valueOf(mypotListArrayList.size()));
//                        myPotAdapter = new MyPotAdapter(mypotListArrayList, getContext());
//                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);
//                        recvewfour.setLayoutManager(gridLayoutManager);
//                        recvewfour.setAdapter(myPotAdapter);
////                        recvewfour.setNestedScrollingEnabled(true);
////                        myPotAdapter = new MyPotAdapter(mypotListArrayList, getContext());
////                        recvewfour.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
////                        recvewfour.setAdapter(myPotAdapter);
//
//                    }
//                    else {
//                        //   Toast.makeText(PotActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//
//                catch(JSONException e){
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                pd.dismiss();
//                Log.d("pot_chatlenge_list", volleyError.toString());
//                String message = null;
//                if (volleyError instanceof NetworkError) {
//                    message = getResources().getString(R.string.cannotconnectinternate);
//                } else if (volleyError instanceof ServerError) {
//                    message = getResources().getString(R.string.servernotfound);
//                } else if (volleyError instanceof AuthFailureError) {
//                    message = getResources().getString(R.string.loginagain);
//                } else if (volleyError instanceof ParseError) {
//                    message = getResources().getString(R.string.tryagain);
//                } else if (volleyError instanceof NoConnectionError) {
//                    message = getResources().getString(R.string.cannotconnectinternate);
//                } else if (volleyError instanceof TimeoutError) {
//                    message = getResources().getString(R.string.connectiontimeout);
//                }
//                if (message != null) {
//
//                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(),getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> param = new HashMap<>();
//
//                param.put("user_id", getData);
//                // param.put("language", "1");
//
//
//                return param;
//            }
//        };
//
//        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
//    }
}
