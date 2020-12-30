package com.ccube9.gochat.Pot.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.ccube9.gochat.Pot.Fragment.OneFragment;
import com.ccube9.gochat.Pot.Fragment.ThreeFragment;
import com.ccube9.gochat.Pot.Fragment.TwoFragment;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class ProfileInstaActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView texttitle,name,my_accpet_challenges_count,my_pot_count,my_challenges_count;
    String id;
    CircleImageView profile_image;
    private TransparentProgressDialog pd;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_insta);

        if (getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
        }
        Log.d("id",id) ;

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        my_challenges_count = findViewById(R.id.my_challenges_count);
        my_accpet_challenges_count = findViewById(R.id.my_accpet_challenges_count);
        my_pot_count = findViewById(R.id.my_pot_count);
        name = findViewById(R.id.name);
        profile_image = findViewById(R.id.profile_image);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        texttitle=findViewById(R.id.texttitle);
        iv_back=findViewById(R.id.iv_back);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        callapipersonaldetail();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileInstaActivity.this, PotActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void callapipersonaldetail() {
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.personal_timeline_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("personal_timeline",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("1")) {

                        JSONObject jsonObject1 = jsonObject.getJSONArray("personal_details").getJSONObject(0);



                        name.setText(jsonObject1.getString("first_name")+" "+jsonObject1.getString("last_name"));
                        if(jsonObject1.getString("profile_image") != null){
                            Picasso.with(ProfileInstaActivity.this).load(Base_url.concat(jsonObject1.getString("profile_image"))).error(R.drawable.default_profile).into(profile_image);
                        }else{
                            Toast.makeText(ProfileInstaActivity.this,jsonObject1.getString("profile_image"),Toast.LENGTH_SHORT).show();
                        }

                        texttitle.setText(jsonObject1.getString("first_name")+" "+jsonObject1.getString("last_name"));
                        my_challenges_count.setText(jsonObject.getString("my_challenges_count"));
                        my_accpet_challenges_count.setText(jsonObject.getString("my_accpet_challenges_count"));
                        my_pot_count.setText(jsonObject.getString("my_pot_count"));

                    }else{
                        Toast.makeText(ProfileInstaActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }


                } catch(JSONException e){
                    pd.dismiss();
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

                    Toast.makeText(ProfileInstaActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileInstaActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();


                param.put("user_id", id);

                return param;
            }
        };
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    5000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ProfileInstaActivity.this).addToRequestQueue(stringRequest);

    }
    public String sendData() {
        return id;
    }
    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_baseline_grid_on_24,
                R.drawable.ic_baseline_insert_emoticon_24,
                R.drawable.ic_baseline_person_pin_24
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "ONE");
        adapter.addFrag(new TwoFragment(), "TWO");
        adapter.addFrag(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }

    @Override
    public void onBackPressed() {




    }

}