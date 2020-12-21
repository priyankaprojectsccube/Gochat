package com.ccube9.gochat.News.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ccube9.gochat.News.Activity.CommentsActivity;
import com.ccube9.gochat.News.Activity.GalleryPreview;
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PreviewImageDetails;
import com.ccube9.gochat.Util.PreviewImageHelperClass;
import com.ccube9.gochat.Util.STORY;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class GalleryPreviewAdpater  extends RecyclerView.Adapter<GalleryPreviewAdpater.MyViewHolder> {
    private ArrayList<PreviewImageHelperClass> mArrListImg = new ArrayList<>();
    private TransparentProgressDialog pd;
    private List<PreviewImageDetails> previewImageDetailsList = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;






    public GalleryPreviewAdpater(ArrayList<PreviewImageHelperClass> mArrListImg, List<PreviewImageDetails> previewImageDetailsList, GalleryPreview galleryPreview) {
        this.previewImageDetailsList = previewImageDetailsList;
        this.context = galleryPreview;
        this.mArrListImg = mArrListImg;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       ImageView iv,play,imgdelete;



        MyViewHolder(View view) {
            super(view);
            iv = view.findViewById(R.id.iv);
            play = view.findViewById(R.id.play);
            imgdelete = view.findViewById(R.id.imgdelete);




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
    public GalleryPreviewAdpater.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selectimage, parent, false);

        return new GalleryPreviewAdpater.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GalleryPreviewAdpater.MyViewHolder holder, final int position) {

if(previewImageDetailsList.get(position).getStory_image() != null) {
    String url = WebUrl.Base_url.concat(previewImageDetailsList.get(position).getStory_image());

    if (url.contains(".mp4")) {
        Log.d("checkurlv",url);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.splashscreen);
        requestOptions.error(R.drawable.splashscreen);


        Glide.with(context)
                .load(WebUrl.Base_url.concat(previewImageDetailsList.get(position).getStory_image()))
                .apply(requestOptions)
                .thumbnail(Glide.with(context).load(WebUrl.Base_url.concat(previewImageDetailsList.get(position).getStory_image())))
                .into(holder.iv);
        holder.play.setVisibility(View.VISIBLE);
    } else {
        Log.d("checkurli",url);
        holder.play.setVisibility(View.GONE);
        Picasso.with(context).load(WebUrl.Base_url.concat(previewImageDetailsList.get(position).getStory_image())).error(R.drawable.splashscreen).into(holder.iv);
    }
}else{
    Toast.makeText(context,"No Images Found",Toast.LENGTH_SHORT).show();
}

holder.imgdelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String get_id = previewImageDetailsList.get(position).getId();
        calldelete(get_id,position);

    }
});

    }

    private void calldelete(String get_id, int position) {
        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.delete_preview_images, new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("delete_preview_images_re", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {


                        mArrListImg.remove(position);
                        previewImageDetailsList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, previewImageDetailsList.size());

                        Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();




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

                param.put("id",get_id);
                return param;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    @Override
    public int getItemCount()
    {
        return previewImageDetailsList.size();
    }


}


