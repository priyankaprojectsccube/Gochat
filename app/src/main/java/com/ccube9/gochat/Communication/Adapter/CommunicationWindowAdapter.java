package com.ccube9.gochat.Communication.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ccube9.gochat.Communication.Activity.CommunicationWindow;
import com.ccube9.gochat.News.Activity.CommentsActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.CommunicationWindowList;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class CommunicationWindowAdapter extends RecyclerView.Adapter<CommunicationWindowAdapter.ViewHolder> {

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<CommunicationWindowList> communicationWindowListList;
    private TransparentProgressDialog pd;
    private  String profileimage,id,firstname;




    public CommunicationWindowAdapter(List<CommunicationWindowList> mChat, Context mContext, String profileimage, String id, String firstname){
        this.communicationWindowListList = mChat;
        this.mContext = mContext;
        this.profileimage = profileimage;
        this.id = id;
        this.firstname = firstname;

    }

    @NonNull
    @Override
    public CommunicationWindowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new CommunicationWindowAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new CommunicationWindowAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CommunicationWindowAdapter.ViewHolder holder, int position) {


        Date date = null;



//        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//        cal.setTimeInMillis(Long.parseLong(communicationWindowListList.get(position).getCreated_date()));

//        String date = DateFormat.format("hh:mm aa", cal).toString();
//        String date1 = DateFormat.format("dd MMM yyyy", cal).toString();
Log.d("datelsit",communicationWindowListList.get(position).getCreated_date());//2020-10-15 09:55:52
        String strdate = communicationWindowListList.get(position).getCreated_date();
        SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
           date = formatter6.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        String stringDate = dateFormat.format(date);
                   holder.show_time.setText(stringDate);

        holder.show_message.setText(communicationWindowListList.get(position).getMessage());
        Log.d("showMessage",communicationWindowListList.get(position).getMessage());




//
//        if (imageurl.equals("default")){
//            //holder.profile_image.setImageResource(R.mipmap.ic_launcher);
//        } else {
//            //Glide.with(mContext).load(imageurl).into(holder.profile_image);
//        }
        holder.laylin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(PrefManager.getUserId(mContext).equals(communicationWindowListList.get(position).getTo_user_id())){
                    String fromuserid = communicationWindowListList.get(position).getFrom_user_id();
                    String usercommunicationid = communicationWindowListList.get(position).getId();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setMessage("Are you sure, You wanted delete comment?");
                    alertDialogBuilder.setPositiveButton("Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1)
                                {
                                    deletecomment(fromuserid,usercommunicationid);

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
                else{

                }
            }
        });


    }

    private void deletecomment(String fromuserid, String usercommunicationid) {

            pd = new TransparentProgressDialog(mContext, R.drawable.ic_loader_image);
            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.delete_single_message, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    Log.d("delsinglemsg", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("1")) {

                            Intent intent = new Intent(mContext, CommunicationWindow.class);

                            intent.putExtra("id",id);
                            intent.putExtra("profileimage",profileimage);
                            intent.putExtra("firstname",firstname);
                            mContext.startActivity(intent);


                        }else{
                            Toast.makeText(mContext,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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
                        message = mContext.getResources().getString(R.string.cannotconnectinternate);
                    } else if (volleyError instanceof ServerError) {
                        message = mContext.getResources().getString(R.string.servernotfound);
                    } else if (volleyError instanceof AuthFailureError) {
                        message = mContext.getResources().getString(R.string.loginagain);
                    } else if (volleyError instanceof ParseError) {
                        message = mContext.getResources().getString(R.string.tryagain);
                    } else if (volleyError instanceof NoConnectionError) {
                        message = mContext.getResources().getString(R.string.cannotconnectinternate);
                    } else if (volleyError instanceof TimeoutError) {
                        message = mContext.getResources().getString(R.string.connectiontimeout);
                    }
                    if (message != null) {

                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                    }

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    //  param.put("language", "1");
                    param.put("from_user_id", fromuserid);
                    param.put("user_communication_id",usercommunicationid);
                    return param;
                }
            };

            MySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
        }


    @Override
    public int getItemCount() {
        return communicationWindowListList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message,show_time;
        //   public ImageView profile_image;
        public TextView txt_seen;

        public LinearLayout laylin;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            //  profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            show_time = itemView.findViewById(R.id.show_time);
            laylin = itemView.findViewById(R.id.laylin);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (communicationWindowListList.get(position).getTo_user_id().equals(PrefManager.getUserId(mContext))){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
