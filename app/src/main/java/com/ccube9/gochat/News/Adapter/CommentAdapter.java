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
import com.ccube9.gochat.News.Activity.RepliesActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.Comment;
import com.ccube9.gochat.Util.MySingleton;
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

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private TransparentProgressDialog pd;
    private List<Comment> cmtlist = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;
    String strfollowerid;
    String adv_id,user_id,profile;
    int checkarraysize;


    public CommentAdapter(List<Comment> cmtlist, Context context, String adv_id, String user_id, String profile) {
        this.cmtlist = cmtlist;
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
        CircleImageView commentprofilepic,replyprofilepic,replyprofilepic1;
        TextView username,comment,replypersoncmt,replypersonname,replypersoncmt1,replypersonname1,likecount,readmorereply,reply,like,liked;


        LinearLayout l1;
        MyViewHolder(View view) {
            super(view);

            liked = view.findViewById(R.id.liked);
            like = view.findViewById(R.id.like);
            reply = view.findViewById(R.id.reply);
            commentprofilepic = view.findViewById(R.id.commentprofilepic);
            replyprofilepic = view.findViewById(R.id.replyprofilepic);
            replyprofilepic1 = view.findViewById(R.id.replyprofilepic1);
            likecount= view.findViewById(R.id.likecount);
            readmorereply = view.findViewById(R.id.readmorereply);

            username=(TextView) view.findViewById(R.id.username);
            comment=(TextView) view.findViewById(R.id.comment);

            replypersoncmt =(TextView) view.findViewById(R.id.replypersoncmt);
            replypersoncmt1 = (TextView) view.findViewById(R.id.replypersoncmt1);

            replypersonname = (TextView)  view.findViewById(R.id.replypersonname);
            replypersonname1 = (TextView)  view.findViewById(R.id.replypersonname1);

          l1 =(LinearLayout)view.findViewById(R.id.l1) ;

//            linearLayout = view.findViewById(R.id.challenge_linearLayout);




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
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_comment, parent, false);

        return new CommentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.MyViewHolder holder, final int position) {
        if(cmtlist.get(position).getAdvertisement_main_like_count() != null){

            holder.likecount.setText(cmtlist.get(position).getAdvertisement_main_like_count());
        }

        if(cmtlist.get(position).getFirst_name() != null){
            holder.username.setText(cmtlist.get(position).getFirst_name()+" "+cmtlist.get(position).getLast_name());
        }

        if(cmtlist.get(position).getComments() != null){
            holder.comment.setText(cmtlist.get(position).getComments());
        }

        if(cmtlist.get(position).getProfile_image()!=null) {
            String imageurl = Base_url.concat(cmtlist.get(position).getProfile_image());
            Picasso.with(context).load(imageurl).error(R.drawable.splashscreen).into(holder.commentprofilepic);
        }

        if(cmtlist.get(position).getIs_main_comment_like_status() != null){
          if(cmtlist.get(position).getIs_main_comment_like_status().equals("1"))  {
              holder.liked.setVisibility(View.GONE);
              holder.like.setVisibility(View.VISIBLE);

            }
          else if(cmtlist.get(position).getIs_main_comment_like_status().equals("0")){

              holder.like.setVisibility(View.GONE);
              holder.liked.setVisibility(View.VISIBLE);
          }
        }else{
            Toast.makeText(context,"like status",Toast.LENGTH_SHORT).show();
        }

        holder.liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                holder.liked.setVisibility(View.GONE);
//                holder.like.setVisibility(View.VISIBLE);
                String    struserid = cmtlist.get(position).getUser_id();
                String  stradvertisementid = cmtlist.get(position).getAdvertisement_id();
                String stradvertisementcommentid = cmtlist.get(position).getAdv_comm_id();

                dislikecomment(struserid,stradvertisementid,stradvertisementcommentid);
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                holder.like.setVisibility(View.GONE);
//                holder.liked.setVisibility(View.VISIBLE);

             String    struserid = cmtlist.get(position).getUser_id();
             String  stradvertisementid = cmtlist.get(position).getAdvertisement_id();
             String stradvertisementcommentid = cmtlist.get(position).getAdv_comm_id();

                likecomment(struserid,stradvertisementid,stradvertisementcommentid);


            }
        });

        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ceckdata",cmtlist.get(position).getAdvertisement_id()+" "+cmtlist.get(position).getUser_id()+" "+cmtlist.get(position).getAdv_comm_id());
                String advcommentid = cmtlist.get(position).getAdvertisement_id();
                String userid = cmtlist.get(position).getUser_id();
                String profile = WebUrl.Base_url.concat(cmtlist.get(position).getProfile_image());
                String cmt_id = cmtlist.get(position).getAdv_comm_id();//cmtlist.get(position).getComments_id();



                Intent intent = new Intent(context, RepliesActivity.class);

                intent.putExtra("advcommentid_reply",advcommentid);
                intent.putExtra("userid_reply",userid);
                intent.putExtra("profilepic_reply",profile);
                intent.putExtra("commentid_reply",cmt_id);



                context.startActivity(intent);
//                String advcommentid = cmtlist.get(position).getAdv_comm_id();
//                String userid = cmtlist.get(position).getUser_id();
//                String profile = WebUrl.Base_url.concat(cmtlist.get(position).getProfile_image());
//                String cmt_id = cmtlist.get(position).getComments_id();
//
//
//                Intent intent = new Intent(context, RepliesActivity.class);
//
//                intent.putExtra("advcommentid",advcommentid);
//                intent.putExtra("userid",userid);
//                intent.putExtra("profilepic",profile);
//                intent.putExtra("commentid",cmt_id);
//
//
//                context.startActivity(intent);
            }
        });
        holder.readmorereply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ceckdata",cmtlist.get(position).getAdvertisement_id()+" "+cmtlist.get(position).getUser_id()+" "+cmtlist.get(position).getAdv_comm_id());
                String advcommentid = cmtlist.get(position).getAdvertisement_id();
                String userid = cmtlist.get(position).getUser_id();
                String profile = WebUrl.Base_url.concat(cmtlist.get(position).getProfile_image());
                String cmt_id = cmtlist.get(position).getAdv_comm_id();//cmtlist.get(position).getComments_id();



                Intent intent = new Intent(context, RepliesActivity.class);

                intent.putExtra("advcommentid_reply",advcommentid);
                intent.putExtra("userid_reply",userid);
                intent.putExtra("profilepic_reply",profile);
                intent.putExtra("commentid_reply",cmt_id);



                context.startActivity(intent);
            }
        });


holder.l1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


          if(user_id.equals(cmtlist.get(position).getUser_id())){
              String advcommentid = cmtlist.get(position).getAdv_comm_id();
              String userid = cmtlist.get(position).getUser_id();
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


        checkarraysize = cmtlist.get(position).getArrreplayfirstname().size();
       // Log.d("arrayfirstname", String.valueOf(checkarraysize)+cmtlist.get(position).getArrreplayfirstname().get(0));

        if(checkarraysize == 1) {
            holder.readmorereply.setVisibility(View.GONE);
            holder.replypersonname.setVisibility(View.VISIBLE);
            holder.replypersoncmt.setVisibility(View.VISIBLE);
            holder.replyprofilepic.setVisibility(View.VISIBLE);


            holder.replypersonname1.setVisibility(View.GONE);
            holder.replypersoncmt1.setVisibility(View.GONE);
            holder.replyprofilepic1.setVisibility(View.GONE);

            holder.replypersonname.setText(cmtlist.get(position).getArrreplayfirstname().get(0)+" "+cmtlist.get(position).getArrreplaylastname().get(0));
            holder.replypersoncmt.setText(cmtlist.get(position).getArrreplycomments().get(0));
             String reimageurl = Base_url.concat(cmtlist.get(position).getArrreplyprofileimage().get(0));
            Picasso.with(context).load(reimageurl).error(R.drawable.splashscreen).into(holder.replyprofilepic);


        }else if( checkarraysize == 2){

            holder.readmorereply.setVisibility(View.VISIBLE);

            holder.replypersonname.setVisibility(View.VISIBLE);
            holder.replypersoncmt.setVisibility(View.VISIBLE);
            holder.replyprofilepic.setVisibility(View.VISIBLE);

            holder.replypersonname1.setVisibility(View.VISIBLE);
            holder.replypersoncmt1.setVisibility(View.VISIBLE);
            holder.replyprofilepic1.setVisibility(View.VISIBLE);

            holder.replypersonname.setText(cmtlist.get(position).getArrreplayfirstname().get(0)+" "+cmtlist.get(position).getArrreplaylastname().get(0));
            holder.replypersoncmt.setText(cmtlist.get(position).getArrreplycomments().get(0));
            String reimageurl = Base_url.concat(cmtlist.get(position).getArrreplyprofileimage().get(0));
            Picasso.with(context).load(reimageurl).error(R.drawable.splashscreen).into(holder.replyprofilepic);

            holder.replypersonname1.setText(cmtlist.get(position).getArrreplayfirstname().get(1)+" "+cmtlist.get(position).getArrreplaylastname().get(1));
            holder.replypersoncmt1.setText(cmtlist.get(position).getArrreplycomments().get(1));
            String reimageurl1 = Base_url.concat(cmtlist.get(position).getArrreplyprofileimage().get(1));
            Picasso.with(context).load(reimageurl1).error(R.drawable.splashscreen).into(holder.replyprofilepic1);

        }else if(checkarraysize == 0){
            holder.readmorereply.setVisibility(View.GONE);

            holder.replypersonname.setVisibility(View.GONE);
            holder.replypersoncmt.setVisibility(View.GONE);
            holder.replyprofilepic.setVisibility(View.GONE);

            holder.replypersonname1.setVisibility(View.GONE);
            holder.replypersoncmt1.setVisibility(View.GONE);
            holder.replyprofilepic1.setVisibility(View.GONE);

        }





    }

    private void dislikecomment(String struserid, String stradvertisementid, String stradvertisementcommentid) {

        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.dislike_advertisement_comment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        Intent intent = new Intent(context,CommentsActivity.class);

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

                param.put("advertisement_comment_id", stradvertisementcommentid);
                param.put("user_id",struserid);
                param.put("advertisement_id",stradvertisementid);
                return param;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void likecomment(String struserid, String stradvertisementid, String stradvertisementcommentid) {

        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.add_like_advertisement_comment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        Intent intent = new Intent(context,CommentsActivity.class);

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
                param.put("advertisement_comment_id", stradvertisementcommentid);
                param.put("user_id",struserid);
                param.put("advertisement_id",stradvertisementid);
                return param;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

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

Intent intent = new Intent(context,CommentsActivity.class);

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
        return cmtlist.size();
    }
}

