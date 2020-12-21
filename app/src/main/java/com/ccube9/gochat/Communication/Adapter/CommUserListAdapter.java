package com.ccube9.gochat.Communication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.Profile.Adapter.FollowerAdapter;
import com.ccube9.gochat.Profile.FollowerActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.CommUserList;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
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

public class CommUserListAdapter extends RecyclerView.Adapter<CommUserListAdapter.MyViewHolder> {

    private TransparentProgressDialog pd;
    private List<CommUserList> commUserListList = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;


    public CommUserListAdapter(List<CommUserList> commUserListList, Context context) {
        this.commUserListList = commUserListList;
        this.context = context;
        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profile_image;
        TextView username,count;
        LinearLayout layout;


        MyViewHolder(View view) {
            super(view);
            profile_image = view.findViewById(R.id.profile_image);

            username=(TextView) view.findViewById(R.id.username);
            count= (TextView)view.findViewById(R.id.count) ;
            layout = (LinearLayout) view.findViewById(R.id.layout) ;


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
    public CommUserListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_comm_user_list, parent, false);

        return new CommUserListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommUserListAdapter.MyViewHolder holder, final int position) {




          if(commUserListList.get(position).getFirst_name() != null && commUserListList.get(position).getLast_name() != null){
              holder.username.setText(commUserListList.get(position).getFirst_name()+" "+commUserListList.get(position).getLast_name());
          }else{
              holder.username.setText(" ");
          }

if(commUserListList.get(position).getCount() != null ){
    holder.count.setText(commUserListList.get(position).getCount());
}else{
    holder.count.setText("0");
}


        if(commUserListList.get(position).getProfile_image()!=null) {
            String imageurl = Base_url.concat(commUserListList.get(position).getProfile_image());
            Log.d("checkumageurl",imageurl);
            Picasso.with(context).load(imageurl).error(R.drawable.splashscreen).into(holder.profile_image);
        }


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (context, CommunicationWindow.class);
                i.putExtra("id",commUserListList.get(position).getUser_details_id());
                i.putExtra("profileimage",Base_url.concat(commUserListList.get(position).getProfile_image()));
                i.putExtra("firstname",commUserListList.get(position).getFirst_name()+" "+commUserListList.get(position).getLast_name());
                context.startActivity(i);
            }
        });
    }




    @Override
    public int getItemCount()
    {

        return commUserListList.size();
    }
}

