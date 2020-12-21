package com.ccube9.gochat.Communication.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import com.ccube9.gochat.Communication.Adapter.CommUserListAdapter;
import com.ccube9.gochat.Communication.Adapter.CommunicationWindowAdapter;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.Home.MenuActivity;
import com.ccube9.gochat.News.Activity.CommentsActivity;
import com.ccube9.gochat.News.Adapter.CommentAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.CommUserList;
import com.ccube9.gochat.Util.Comment;
import com.ccube9.gochat.Util.CommunicationWindowList;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class CommunicationWindow extends AppCompatActivity {
    ImageView iv_back;
    private TransparentProgressDialog pd;
    EditText writemsg;
    TextView texttitle,postmessage;
    CircleImageView profile_image,profile_image_mine;
    String profileimage,firstname,id,strmessage,myloginid;
    RecyclerView recvewcom;
    private List<CommunicationWindowList> communicationWindowLists=new ArrayList<>();
    CommunicationWindowAdapter communicationWindowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_window);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbarcomwin);

        texttitle=findViewById(R.id.texttitle);
        iv_back=findViewById(R.id.iv_back);
        profile_image= findViewById(R.id.profile_image);

        if(getIntent().hasExtra("profileimage") && getIntent().hasExtra("firstname")) {

            profileimage = getIntent().getStringExtra("profileimage");
            firstname = getIntent().getStringExtra("firstname");
            id = getIntent().getStringExtra("id");


            texttitle.setText(firstname);
            Picasso.with(CommunicationWindow.this).load(profileimage).error(R.drawable.splashscreen).into(profile_image);

        }
        myloginid = PrefManager.getUserId(CommunicationWindow.this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CommunicationWindow.this, Commnucation_User_List.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        recvewcom= findViewById(R.id.recvewcom);
        postmessage = findViewById(R.id.postmessage);
        writemsg = findViewById(R.id.writemsg);
        profile_image_mine= findViewById(R.id.profile_image_mine);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        getprofilepic();

        postmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strmessage = writemsg.getText().toString();
                if(!id.isEmpty() &&  id != null && !myloginid.isEmpty() && myloginid != null && !strmessage.isEmpty() && strmessage != null){
                    postmessage(id,myloginid,strmessage);
                }else{
                    Toast.makeText(CommunicationWindow.this,"Please write a comment",Toast.LENGTH_SHORT).show();
                }

            }
        });

        if(id != null || myloginid != null || !id.isEmpty() || !myloginid.isEmpty()){
            callchatlist();
        }else{
           Toast.makeText(CommunicationWindow.this,"No Data Found",Toast.LENGTH_SHORT).show();
        }

    }

    private void callchatlist() {
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.get_user_chat_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                Log.d("get_user_chat_list", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        communicationWindowLists.clear();

                        JSONArray jsonArray = jsonObject.getJSONArray("user_chat_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            CommunicationWindowList communicationWindowList = new CommunicationWindowList();
                            communicationWindowList.setId(jsonObject1.getString("id"));
                            communicationWindowList.setFrom_user_id(jsonObject1.getString("from_user_id"));
                            communicationWindowList.setTo_user_id(jsonObject1.getString("to_user_id"));
                            communicationWindowList.setMessage(jsonObject1.getString("message"));
                            communicationWindowList.setCreated_date(jsonObject1.getString("created_date"));
                            communicationWindowList.setIs_delete(jsonObject1.getString("is_delete"));
                            communicationWindowList.setStatus(jsonObject1.getString("status"));
                            communicationWindowLists.add(communicationWindowList);


                        }
                        communicationWindowAdapter = new CommunicationWindowAdapter(communicationWindowLists, CommunicationWindow.this,profileimage,id,firstname);
                        recvewcom.setLayoutManager(new LinearLayoutManager(CommunicationWindow.this,LinearLayoutManager.VERTICAL,false));
                        recvewcom.setAdapter(communicationWindowAdapter);

                    }
                }
                catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Log.d("catch", String.valueOf(e));
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

                    Toast.makeText(CommunicationWindow.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommunicationWindow.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                //  param.put("language", "1");
//                param.put("from_user_id", id);
//                param.put("to_user_id",myloginid);

                param.put("from_user_id", myloginid);
                param.put("to_user_id",id);
                return param;
            }
        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(CommunicationWindow.this).addToRequestQueue(stringRequest);

    }

    private void postmessage(String id, String myloginid, String strmessage) {

        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.send_user_communication, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("send_user_communication", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        writemsg.setText("");


                        if(id != null || myloginid != null || !id.isEmpty() || !myloginid.isEmpty()){
                            callchatlist();
                        }else{
                            Toast.makeText(CommunicationWindow.this,"No Data Found",Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        Toast.makeText(CommunicationWindow.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(CommunicationWindow.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommunicationWindow.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("from_user_id", id);
                param.put("to_user_id",myloginid);
                param.put("message",strmessage);

                return param;
            }
        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(CommunicationWindow.this).addToRequestQueue(stringRequest);
    }

    private void getprofilepic() {

        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.get_profile_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();

                Log.d("get_profile_update",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){


                        JSONArray jsonArray=jsonObject.getJSONArray("user_data");
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);

                        String imageurl = Base_url.concat(jsonObject1.getString("profile_image"));
                        new MyAsyncTask().execute(imageurl);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("fdgfgd",volleyError.toString());
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

                    Toast.makeText(CommunicationWindow.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommunicationWindow.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(CommunicationWindow.this));


                return param;
            }
        };

        MySingleton.getInstance(CommunicationWindow.this).addToRequestQueue(stringRequest);
    }

    private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                return myBitmap;
            } catch(IOException e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            //do what you want with your bitmap result on the UI thread
            profile_image_mine.setImageBitmap(result);
        }

    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            case R.id.item1:
//                callalert();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void callalert() {


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CommunicationWindow.this);
            alertDialogBuilder.setMessage("Are you sure, You wanted delete chat?");
            alertDialogBuilder.setPositiveButton("Delete",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1)
                        {
                            if(id != null || myloginid != null || !id.isEmpty() || !myloginid.isEmpty()){
                                deletechat(id,myloginid);
                            }else{
                                Toast.makeText(CommunicationWindow.this,"No Data Found",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

            alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

    private void deletechat(String id, String myloginid) {
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.delete_multi_by_user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("delete_multiuser", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {


                        Intent intent2=new Intent(CommunicationWindow.this, Commnucation_User_List.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);


                    }else {
                        Toast.makeText(CommunicationWindow.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Log.d("catch", String.valueOf(e));
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

                    Toast.makeText(CommunicationWindow.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommunicationWindow.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                //  param.put("language", "1");
                param.put("from_user_id", id);
                param.put("to_user_id",myloginid);
                return param;
            }
        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(CommunicationWindow.this).addToRequestQueue(stringRequest);
    }


}