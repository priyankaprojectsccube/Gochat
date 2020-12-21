package com.ccube9.gochat.News.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.News.Adapter.NewFunctionAdapter;
import com.ccube9.gochat.News.Adapter.StoryAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.MyStory;
import com.ccube9.gochat.Util.MyStoryDetail;
import com.ccube9.gochat.Util.OnStoryChangedCallback;
import com.ccube9.gochat.Util.PROMO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.STORY;
import com.ccube9.gochat.Util.StoryClickListeners;
import com.ccube9.gochat.Util.StoryViewHeaderInfo;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class NewsFunctions extends AppCompatActivity {
    private TransparentProgressDialog pd;
    ImageView iv_back, addpost, edit_your_story_pic;
    TextView texttitle,addstorytxt;
    LinearLayout borderlayout;
    CircleImageView your_story_pic;
    private RecyclerView recvewnews, recvewstories;
    private List<PROMO> newsArrList = new ArrayList<>();
    private NewFunctionAdapter newsFunctionAdapter;
    private StoryAdapter storyAdapter;
    private ArrayList<String> likeimgarry;
    private List<STORY> storyList = new ArrayList<>();
    private List<MyStoryDetail> myStoryDetailList = new ArrayList<>();

    ArrayList<MyStory> storydetailsList = new ArrayList<>();
    ArrayList<StoryViewHeaderInfo> storyViewHeaderInfoArrayList = new ArrayList<>();

ArrayList<String> returnValue = new ArrayList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_function);


        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar_nf);

        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);
        addpost = findViewById(R.id.addpost);
        texttitle.setText("News Functions");

        recvewnews = findViewById(R.id.recvewnews);
        recvewstories = findViewById(R.id.recvewstories);
        edit_your_story_pic = findViewById(R.id.edit_your_story_pic);
        your_story_pic = findViewById(R.id.your_story_pic);
        borderlayout = findViewById(R.id.borderlayout);
        addstorytxt = findViewById(R.id.addstorytxt);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsFunctions.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

  Intent newprivewintent = new Intent(NewsFunctions.this,NewsPreview.class);
  startActivity(newprivewintent);


            }
        });

//        addstorytxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                  Intent gallrypreview = new Intent(NewsFunctions.this,GalleryPreview.class);
//                  startActivity(gallrypreview);
//            }
//        });
        your_story_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String id = PrefManager.getUserId(NewsFunctions.this);
//                callstorydetaillist(id);

                opendialog();
            }
        });
        callyourstory();


    }

    private void opendialog() {
        final String[] fonts = {
                "Add Story", "View Story", "Delete Story", "Cancel"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(NewsFunctions.this);
        builder.setTitle("");
        builder.setItems(fonts, new DialogInterface.OnClickListener() {@
                Override
        public void onClick(DialogInterface dialog, int which) {
            if ("Add Story".equals(fonts[which])) {
                Intent gallrypreview = new Intent(NewsFunctions.this,GalleryPreview.class);
                startActivity(gallrypreview);
            } else if ("View Story".equals(fonts[which])) {
                String id = PrefManager.getUserId(NewsFunctions.this);
                callstorydetaillist(id);
            } else if ("Delete Story".equals(fonts[which])) {
                Intent i= new Intent(NewsFunctions.this,MyStoriesPage.class);
                startActivity(i);
            } else if ("Cancel".equals(fonts[which])) {
              dialog.dismiss();
            }
            // the user clicked on colors[which]

        }
        });
        builder.show();
    }

    private void callstorydetaillist(String id) {
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_users_all_story, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("get_users_all_story", response);
                storyViewHeaderInfoArrayList.clear();
                storydetailsList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        borderlayout.setBackgroundResource(0);
                        JSONArray jsonArray = jsonObject.getJSONArray("user_story_details");


                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                            MyStory storydetails = new MyStory();



//                            storydetails.setFirst_name(jsonObject1.getString("first_name"));
//                            storydetails.setLast_name(jsonObject1.getString("last_name"));
//                            storydetails.setProfile_image(jsonObject1.getString("profile_image"));
                           storydetails.setId(jsonObject1.getString("id"));
//                            storydetails.setUser_id(jsonObject1.getString("user_id"));
                            storydetails.setDescription(jsonObject1.getString("story_title"));
                            storydetails.setUrl(WebUrl.Base_url.concat(jsonObject1.getString("story_image")));
                            storydetails.setDate(jsonObject1.getString("created_date"));
//                            storydetails.setIs_delete(jsonObject1.getString("is_delete"));
//                            storydetails.setStatus(jsonObject1.getString("status"));
                            storydetailsList.add(storydetails);


                        }

                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                            StoryViewHeaderInfo storyViewHeaderInfo = new StoryViewHeaderInfo();



                            storyViewHeaderInfo.setTitle(jsonObject1.getString("first_name")+" "+jsonObject1.getString("last_name"));

                            storyViewHeaderInfo.setTitleIconUrl(WebUrl.Base_url.concat(jsonObject1.getString("profile_image")));
                            storyViewHeaderInfo.setSubtitle(jsonObject1.getString("created_date"));
                            Log.d("checkurlstory",WebUrl.Base_url.concat(jsonObject1.getString("story_image")));
                            storyViewHeaderInfoArrayList.add(storyViewHeaderInfo);


                        }
                        storydisaplay();



                    } else {

                        Toast.makeText(NewsFunctions.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

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
                    message =getResources().getString(R.string.tryagain);
                } else if (volleyError instanceof NoConnectionError) {
                    message = getResources().getString(R.string.cannotconnectinternate);
                } else if (volleyError instanceof TimeoutError) {
                    message = getResources().getString(R.string.connectiontimeout);
                }
                if (message != null) {
                    pd.dismiss();
                    Toast.makeText(NewsFunctions.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewsFunctions.this,getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", id);
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(NewsFunctions.this).addToRequestQueue(stringRequest);
    }

    private void storydisaplay() {

        if(storydetailsList.size() > 0) {
            new StoryView.Builder(getSupportFragmentManager())
                    .setStoriesList(storydetailsList)
                    .setStoryDuration(5000)
                    .setHeadingInfoList(storyViewHeaderInfoArrayList)

                    .setStoryClickListeners(new StoryClickListeners() {
                        @Override
                        public void onDescriptionClickListener(int position) {
                            // Toast.makeText(ViewStory.this, "Clicked: " + myStories.get(position).getDescription(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onTitleIconClickListener(int position) {

                        }
                    })
                    .setOnStoryChangedCallback(new OnStoryChangedCallback() {
                        @Override
                        public void storyChanged(int position) {
                //      Toast.makeText(NewsFunctions.this,storydetailsList.get(position).getId(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setStartingIndex(0)

                    .build()
                    .show();


        }
        else {
            Toast.makeText(NewsFunctions.this,"No Stories Found",Toast.LENGTH_SHORT).show();
        }

    }

    private void callyourstory() {

        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_your_story_picture, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("get_your_story_picture", response);
                myStoryDetailList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("login_details");


                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                            MyStoryDetail myStoryDetail = new MyStoryDetail();


                            myStoryDetail.setUser_details_id(jsonObject1.getString("id"));
                            myStoryDetail.setFirst_name(jsonObject1.getString("first_name"));
                            myStoryDetail.setLast_name(jsonObject1.getString("last_name"));
                            myStoryDetail.setFlag_story(jsonObject1.getString("flag_story"));
                            myStoryDetail.setProfile_image(jsonObject1.getString("profile_image"));


                            myStoryDetailList.add(myStoryDetail);


                        }

                        if (myStoryDetailList.get(0).getFlag_story() != null) {
                            if (myStoryDetailList.get(0).getFlag_story().equals("0")) {
                                Log.d("flag", myStoryDetailList.get(0).getFlag_story());

                                your_story_pic.setPadding(15, 15, 15, 15);
                                borderlayout.setBackground(NewsFunctions.this.getResources().getDrawable(R.drawable.circle_red));

                            } else {
                                borderlayout.setBackgroundResource(0);
                            }
                        }
                        Picasso.with(NewsFunctions.this).load(WebUrl.Base_url.concat(myStoryDetailList.get(0).getProfile_image())).error(R.drawable.splashscreen).into(your_story_pic);
                        callstorylist();


                    } else {
                        Toast.makeText(NewsFunctions.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                callstorylist();
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

                    Toast.makeText(NewsFunctions.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewsFunctions.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                //  param.put("language", "1");
                param.put("user_id", PrefManager.getUserId(NewsFunctions.this));
                return param;
            }
        };

        MySingleton.getInstance(NewsFunctions.this).addToRequestQueue(stringRequest);
    }

    private void callstorylist() {


        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_user_story_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("get_user_story_list", response);
                storyList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("story_list");


                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                            STORY story = new STORY();


                            story.setStory_details_id(jsonObject1.getString("story_id"));
                            story.setStory_user_id(jsonObject1.getString("user_id"));
                            story.setFirst_name(jsonObject1.getString("first_name"));
                            story.setLast_name(jsonObject1.getString("last_name"));
                            story.setFlag_story(jsonObject1.getString("flag_story"));
                            story.setProfile_image(jsonObject1.getString("profile_image"));


                            storyList.add(story);


                        }


                        storyAdapter = new StoryAdapter(storyList, NewsFunctions.this);
                        recvewstories.setHasFixedSize(true);
                        recvewstories.setLayoutManager(new LinearLayoutManager(NewsFunctions.this, LinearLayoutManager.HORIZONTAL, false));

                        recvewstories.setAdapter(storyAdapter);
                        calladlist();


                    } else {
                        calladlist();
                       // Toast.makeText(NewsFunctions.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    calladlist();
                    pd.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                calladlist();
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
                    pd.dismiss();
                    Toast.makeText(NewsFunctions.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewsFunctions.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", PrefManager.getUserId(NewsFunctions.this));
                return param;
            }
        };

        MySingleton.getInstance(NewsFunctions.this).addToRequestQueue(stringRequest);
    }

    private void calladlist() {

        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_advertisement_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               pd.dismiss();
                Log.d("get_advertisement_list", response);
                newsArrList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("advertisement_list");


                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("advertisement_data");



                            JSONArray jsonArray2 = jsonObject1.getJSONArray("advertisement_like_imgage");
                            likeimgarry = new ArrayList<>();


                            for (int k = 0; k < jsonArray2.length(); k++) {
                                JSONObject jsonObject3 = jsonArray2.getJSONObject(k);
                                likeimgarry.add(jsonObject3.getString("profile_image"));
                            }


                            PROMO promo = new PROMO();


                            promo.setId(jsonObject2.getString("id"));
                            promo.setAdv_name(jsonObject2.getString("adv_name"));
                            promo.setUser_name(jsonObject2.getString("user_name"));
                            promo.setDescription(jsonObject2.getString("description"));
                            promo.setChallenge_type(jsonObject2.getString("challenge_type"));
                            promo.setCategory_id(jsonObject2.getString("category_id"));
                            promo.setSub_cat_id(jsonObject2.getString("sub_cat_id"));
                            promo.setType(jsonObject2.getString("type"));
                            promo.setWebsite_url(jsonObject2.getString("website_url"));
                            promo.setImage(jsonObject2.getString("image"));
                            promo.setApp_icon(jsonObject2.getString("app_icon"));
                            promo.setApp_name(jsonObject2.getString("app_name"));
                            promo.setFlag(jsonObject2.getString("flag"));
                            promo.setUser_id(jsonObject2.getString("user_id"));
                            promo.setAdmin_id(jsonObject2.getString("admin_id"));
                            promo.setLocation(jsonObject2.getString("location"));
                            promo.setRequest(jsonObject2.getString("request"));
                            promo.setCountry(jsonObject2.getString("country"));
                            promo.setLat(jsonObject2.getString("lat"));
                            promo.setLang(jsonObject2.getString("lang"));
                            promo.setCreated_date(jsonObject2.getString("created_date"));
                            promo.setModified_date(jsonObject2.getString("modified_date"));
                            promo.setUser_type_id(jsonObject2.getString("user_type_id"));
                            promo.setPublished_to(jsonObject2.getString("published_to"));
                            promo.setNews_flag(jsonObject2.getString("news_flag"));
                            promo.setFlag_vi_new(jsonObject2.getString("flag_vi_new"));
                            promo.setLang_id(jsonObject2.getString("lang_id"));
                            promo.setIs_delete(jsonObject2.getString("is_delete"));
                            promo.setStatus(jsonObject2.getString("status"));
                            promo.setProfile_image(jsonObject2.getString("profile_image"));
                            promo.setChallenge_type(jsonObject2.getString("challenge_type"));
                            promo.setAdvertisement_like_count(jsonObject1.getString("advertisement_like_count"));
                            promo.setLoginuserid(jsonObject1.getString("login_id"));
                            promo.setLoginprofilepic(jsonObject1.getString("login_profile_image"));
                            promo.setAdvertisement_comment_count(jsonObject1.getString("advertisement_main_comment_count"));
                            promo.setAdvertisement_like_imgage(likeimgarry);
                            promo.setIs_favourite(jsonObject1.getString("is_favorite"));
                            promo.setIs_subcribe(jsonObject1.getString("is_subcribe"));
                            promo.setWeburl(jsonObject1.getString("weburl"));


                            newsArrList.add(promo);

                        }


                        newsFunctionAdapter = new NewFunctionAdapter(newsArrList, NewsFunctions.this);
                        recvewnews.setHasFixedSize(true);
                        recvewnews.setLayoutManager(new LinearLayoutManager(NewsFunctions.this, LinearLayoutManager.VERTICAL, false));

                        recvewnews.setAdapter(newsFunctionAdapter);


                    } else {
                        Toast.makeText(NewsFunctions.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
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

                    Toast.makeText(NewsFunctions.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewsFunctions.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                //  param.put("language", "1");
                param.put("user_id", PrefManager.getUserId(NewsFunctions.this));
                return param;
            }
        };

        MySingleton.getInstance(NewsFunctions.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Log.d("listimgv", String.valueOf(returnValue.size()));
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    openGallery();
//                    Pix.start(NewsFunctions.this, Options.init().setRequestCode(100));
//                } else {
//                    Toast.makeText(NewsFunctions.this, "Approve permissions to open gallery", Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//        }
//    }

//    private void openGallery() {
//        Options options = Options.init()
//                .setRequestCode(100)                                           //Request code for activity results
//                .setCount(3)                                                   //Number of images to restict selection count
//                .setFrontfacing(false)                                         //Front Facing camera on start
//                .setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
//                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
//                .setExcludeVideos(false)                                       //Option to exclude videos
//                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
//                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
//                .setPath("/pix/images");                                       //Custom Path For media Storage
//
//        Pix.start(NewsFunctions.this, options);
//    }
}