package com.ccube9.gochat.Home.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ccube9.gochat.Home.Fragment.NotificationActivity;
import com.ccube9.gochat.Home.Fragment.Request_for_winner;
import com.ccube9.gochat.Pot.Activity.Pot_Chatlenge;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.Notification;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private List<Notification> notificationArrList = Collections.emptyList();

    private Context context;



    public NotificationAdapter(List<Notification> notificationArrList, NotificationActivity context) {
        this.notificationArrList = notificationArrList;
        this.context = context;
    }


    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_notilayout, viewGroup, false);

        return new NotificationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder myViewHolder, final int i) {
        final Notification notification=notificationArrList.get(i);


        //unread
        if(notification.getRead_status().equals("0")){
            myViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.quantum_grey));

        }
        //read
        if(notification.getRead_status().equals("1")){
            myViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));

        }

        myViewHolder.txtnoti.setText(notification.getFirst_name()+" "+notification.getLast_name()+" "+notification.getNoti_title());

        String strdate = notification.getCreated_date();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date=formatter.parse(strdate);
            String strDate = formatter.format(date);
            myViewHolder.date.setText(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(notificationArrList.get(i).getProfile_image()!=null) {
            String imageurl = Base_url.concat(notificationArrList.get(i).getProfile_image());
            Picasso.with(context).load(imageurl).error(R.drawable.splashscreen).into(myViewHolder.profile_image);
        }
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // context.startActivity(new Intent(context, UserAntigaspiHomeActivity.class));
                if(notification.getRead_status().equals("0")) {

                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.change_notifi_status, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("dfdf",response);

                            try {
                                JSONObject jsonObject=new JSONObject(response);

                                if(jsonObject.getString("status").equals("1")){
                                    myViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                                    if(notification.getRequest_winner().equals("1")){
                                        Intent intent = new Intent(context, Request_for_winner.class);
                                        intent.putExtra("mainchallengeid",notification.getMain_challenge_id());
                                        context.startActivity(intent);
                                    }
                                    else if (notification.getIntitatiofundgo().equals("1")){
                                        Intent intent = new Intent(context, Pot_Chatlenge.class);
                                        intent.putExtra("pot_id",notification.getPot_id());
                                        context.startActivity(intent);
                                    }
                                }
                                else {
                                    Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> param = new HashMap<>();

                        param.put("user_id",notification.getUser_id());
                            param.put("noti_id", notification.getNoti_id());
                            param.put("read_status", "1");
                            return param;
                        }
                    };
                    requestQueue.add(stringRequest);

                }
                else {
//                    if(notification.getRequest_winner().equals("0")){
//
//                    }else if(notification.getRequest_winner().equals("1")){
//                        Intent intent = new Intent(context, Request_for_winner.class);
//                        intent.putExtra("mainchallengeid",notification.getMain_challenge_id());
//                        context.startActivity(intent);
//                    }

                    if(notification.getRequest_winner().equals("1")){
                        Intent intent = new Intent(context, Request_for_winner.class);
                        intent.putExtra("mainchallengeid",notification.getMain_challenge_id());
                        context.startActivity(intent);
                    }
                    else if (notification.getIntitatiofundgo().equals("1")){
                        Intent intent = new Intent(context, Pot_Chatlenge.class);
                        intent.putExtra("pot_id",notification.getPot_id());
                        context.startActivity(intent);
                    }
                 //   Toast.makeText(context,"Notification status not update",Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    @Override
    public int getItemCount() {

        return notificationArrList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView txtnoti,date;
        CardView cardView;
        CircleImageView profile_image;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image= itemView.findViewById(R.id.profile_image);
            txtnoti=itemView.findViewById(R.id.txtnoti);
            date=itemView.findViewById(R.id.date);
            cardView=itemView.findViewById(R.id.cardview);

        }
    }




}

