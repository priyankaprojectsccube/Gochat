package com.ccube9.gochat.Pot.Fragment;

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
import com.ccube9.gochat.Pot.Activity.PotActivity;
import com.ccube9.gochat.Pot.Activity.ProfileInstaActivity;
import com.ccube9.gochat.Pot.Adapter.AlbumListAdapter;
import com.ccube9.gochat.Pot.Adapter.MyPotAdapter;
import com.ccube9.gochat.Pot.Adapter.MyPotListAdapter;
import com.ccube9.gochat.Profile.MyChallengeActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.AlbumList;
import com.ccube9.gochat.Util.MYPOTLIST;
import com.ccube9.gochat.Util.MySingleton;
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


public class TwoFragment extends Fragment {
    RecyclerView recvewtwo;

    String getData;
    private TransparentProgressDialog pd;
    MyPotListAdapter myPotListAdapter;
    List<MYPOTLIST> mypotListArrayList =new ArrayList<>();
    public TwoFragment() {
        // Required empty public constructor
    }


    public static TwoFragment newInstance(String param1, String param2) {
        TwoFragment fragment = new TwoFragment();
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
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        recvewtwo = view.findViewById(R.id.recvewtwo);
        pd = new TransparentProgressDialog(getContext(), R.drawable.ic_loader_image);
        ProfileInstaActivity activity = (ProfileInstaActivity) getActivity();
        getData = activity.sendData();
        Log.d("getdata",getData);
        callapi();
        return view;
    }

    private void callapi() {

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.my_pot_chatlenge_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mypotListArrayList.clear();
                pd.dismiss();
                Log.d("my_chatlenge_list", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("my_pot_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);



                            MYPOTLIST potList = new MYPOTLIST();
                            potList.setId(jsonObject1.getString("id"));
                            potList.setUserid(jsonObject1.getString("user_id"));
                            potList.setFirst_name(jsonObject1.getString("first_name")+" "+jsonObject1.getString("last_name"));
                            potList.setLogo_association(jsonObject1.getString("logo_association"));
                            potList.setBanner_for_association(jsonObject1.getString("banner_for_association"));
                            potList.setAbout_pot(jsonObject1.getString("title"));
                            mypotListArrayList.add(potList);


                        }
                        Log.d("mypotlist", String.valueOf(mypotListArrayList.size()));
                        myPotListAdapter = new MyPotListAdapter(mypotListArrayList, getContext());
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);
                        recvewtwo.setLayoutManager(gridLayoutManager);
                     //   recvewtwo.setLayoutManager(new GridLayoutManager(getContext(),2));
                        recvewtwo.setAdapter(myPotListAdapter);

                    }
                    else {
                        Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                }


                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("pot_chatlenge_list", volleyError.toString());
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
                    Toast.makeText(getContext(),getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", getData);
                // param.put("language", "1");


                return param;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}