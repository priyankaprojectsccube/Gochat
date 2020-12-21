package com.ccube9.gochat.News.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.News.Activity.CommentsActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.Replies;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.MyViewHolder> {

    private TransparentProgressDialog pd;
    private List<Replies> replyList = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;
    String strfollowerid;
    String adv_id,user_id,profile;
    int checkarraysize;

    public RepliesAdapter(List<Replies> replyList, Context context, String adv_id, String user_id, String profile) {
        this.replyList = replyList;
        this.context = context;
        this.adv_id = adv_id;
        this.user_id = user_id;
        this.profile = profile;
        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profile_image,replyprofilepic,replyprofilepic1;
        TextView replypersoncmt,replypersonname,replypersoncmt1,replypersonname1,likecount,likecount1,like1,reply1,like,reply;
        LinearLayout firstreply,otherreply;
        TextView txtview;

        MyViewHolder(View view) {
            super(view);


            replyprofilepic = view.findViewById(R.id.replyprofilepic);
            replyprofilepic1 = view.findViewById(R.id.replyprofilepic1);

            likecount= view.findViewById(R.id.likecount);
            likecount1 = view.findViewById(R.id.likecount1);

            replypersonname=(TextView) view.findViewById(R.id.replypersonname);
            replypersonname1 = (TextView)  view.findViewById(R.id.replypersonname1);


            replypersoncmt=(TextView) view.findViewById(R.id.replypersoncmt);
            replypersoncmt1 = (TextView) view.findViewById(R.id.replypersoncmt1);

            like = view.findViewById(R.id.like);
            like1 = view.findViewById(R.id.like1);

            reply = view.findViewById(R.id.reply);
            reply1 = view.findViewById(R.id.reply1);




            firstreply =(LinearLayout)view.findViewById(R.id.firstreply) ;
            otherreply = (LinearLayout) view.findViewById(R.id.otherreply);




            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.OnItemClick(getAdapterPosition());
            }
        }

    }



    @NonNull
    @Override
    public RepliesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_replies, parent, false);

        return new RepliesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RepliesAdapter.MyViewHolder holder, final int position) {

String replystrfname = replyList.get(position).getReplay_first_name();
        Log.d("replystrfname",replystrfname);





            if(replyList.get(position).getReplay_first_name() != null) {
                holder.replypersonname1.setText(replyList.get(position).getReplay_first_name() + "" + replyList.get(position).getReplay_last_name());
            }
            if(replyList.get(position).getReplay_comments() != null){
                holder.replypersoncmt1.setText(replyList.get(position).getReplay_comments());
            }

            if(replyList.get(position).getReplay_profile_image()!=null) {
                String imageurl = Base_url.concat(replyList.get(position).getReplay_profile_image());
                Picasso.with(context).load(imageurl).error(R.drawable.splashscreen).into(holder.replyprofilepic1);
            }

//            if(replyList.get(position).getAdvertisement_main_like_count() != null){
//                holder.likecount1.setText(replyList.get(position).getAdvertisement_main_like_count());
//            }











        holder.firstreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_id.equals(replyList.get(position).getUser_id())){
                    String advcommentid = replyList.get(position).getAdv_comm_id();
                    String userid = replyList.get(position).getUser_id();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Are you sure, You wanted delete comment?");
                    alertDialogBuilder.setPositiveButton("Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1)
                                {
                                    deletecomment(advcommentid,userid);

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

    private void deletecomment(String advcommentid, String userid) {
        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.delete_comments_advertisement, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        Intent intent = new Intent(context, CommentsActivity.class);

                        intent.putExtra("adv_id",adv_id);
                        intent.putExtra("user_id",user_id);
                        intent.putExtra("profilepic",profile);
                        context.startActivity(intent);


                    }else{
                        Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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
                    message = context.getResources().getString(R.string.cannotconnectinternate);
                } else if (volleyError instanceof ServerError) {
                    message = context.getResources().getString(R.string.servernotfound);
                } else if (volleyError instanceof AuthFailureError) {
                    message = context.getResources().getString(R.string.loginagain);
                } else if (volleyError instanceof ParseError) {
                    message = context.getResources().getString(R.string.tryagain);
                } else if (volleyError instanceof NoConnectionError) {
                    message = context.getResources().getString(R.string.cannotconnectinternate);
                } else if (volleyError instanceof TimeoutError) {
                    message = context.getResources().getString(R.string.connectiontimeout);
                }
                if (message != null) {

                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,context.getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                //  param.put("language", "1");
                param.put("advertisement_comment_id", advcommentid);
                param.put("user_id",userid);
                return param;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    @Override
    public int getItemCount()
    {
        return replyList.size();
    }
}


