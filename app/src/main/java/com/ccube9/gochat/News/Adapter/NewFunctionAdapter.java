package com.ccube9.gochat.News.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ccube9.gochat.OnItemClickListener;
import com.ccube9.gochat.News.Activity.CommentsActivity;
import com.ccube9.gochat.News.Activity.NewsFunctions;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PROMO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
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

public class NewFunctionAdapter extends RecyclerView.Adapter<NewFunctionAdapter.MyViewHolder> {

    private TransparentProgressDialog pd;
    private List<PROMO> promoArrList = Collections.emptyList();
    private Context context;
    OnItemClickListener onItemClickListener;
    private ArrayList<String> likearrylist = new ArrayList<>();
    int checkarraysize;
    public NewFunctionAdapter(List<PROMO> promoArrList, Context context) {
        this.promoArrList = promoArrList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView,imageView2,faviourate,challengetype,playimg,unfaviourate,like,unlike,commentsicon,commenticon2,videoviewnews,playimgnews,
                commenticonvideonews,imgviewnews,commenticonimgnews,like_imgnew,unlike_imgnew,unlike_videonew,like_videonew,deleteimgnews,deletevdonews;
        CircleImageView profimg,ci1,ci2,ci3,ciI1,ciI2,ciI3,profimg_news,ci1videonews,ci2videonews,ci3videonews,
                profimg_news_img,ci1imgnews,ci2imgnews,ci3imgnews;
        TextView adname,des,readmore,commentcount,subscribe,subscribed,comment,share,name,time,comment2,likes,comment22,share2,namenews,likesI,
                sharevideonews,commentvideonew,likesvideonews,captionvideonews,timenews,namenewsimg,captionimgnews,likesimgnews,commentimgnew,shareimgnews,timenewsimg;
        RelativeLayout advrelimg, advrelvideo,newsrelvideo,newsrelimage;



        MyViewHolder(View view) {
            super(view);

            likesI = view.findViewById(R.id.likesI);
            commenticon2 = view.findViewById(R.id.commenticon2);
            commentsicon = view.findViewById(R.id.commentsicon);
            unfaviourate = view.findViewById(R.id.unfaviourate);
            playimg = view.findViewById(R.id.playimg);
            share2 = view.findViewById(R.id.share2);
            comment22 = view.findViewById(R.id.comment22);

            likes = view.findViewById(R.id.likes);
            comment2 = view.findViewById(R.id.comment2);
            challengetype = view.findViewById(R.id.challengetype);
            profimg = view.findViewById(R.id.profimg);
            ciI1=view.findViewById(R.id.ciI1);
            ciI2=view.findViewById(R.id.ciI2);
            ciI3=view.findViewById(R.id.ciI3);

            ci1 = view.findViewById(R.id.ci1);
            ci2 = view.findViewById(R.id.ci2);
            ci3 = view.findViewById(R.id.ci3);
            imageView = view.findViewById(R.id.imageView);
            imageView2 = view.findViewById(R.id.imageView2);
            faviourate= view.findViewById(R.id.faviourate);
            adname=(TextView) view.findViewById(R.id.adname);
            des=(TextView) view.findViewById(R.id.des);
            readmore=(TextView) view.findViewById(R.id.readmore);

            commentcount = view.findViewById(R.id.commentcount);
            subscribe = view.findViewById(R.id.subscribe);
            comment = view.findViewById(R.id.comment);
            share = view.findViewById(R.id.share);
            advrelimg = view.findViewById(R.id.advrelimg);
            advrelvideo = view.findViewById(R.id.advrelvideo);
            name=(TextView) view.findViewById(R.id.name);
            time=(TextView) view.findViewById(R.id.time);
            subscribed = (TextView)view.findViewById(R.id.subscribed) ;

            like = (ImageView)view.findViewById(R.id.like);
            unlike = (ImageView)view.findViewById(R.id.unlike);


            //videonews
            newsrelvideo = view.findViewById(R.id.newsrelvideo);
            profimg_news = view.findViewById(R.id.profimg_news);
            namenews = view.findViewById(R.id.namenews);
            videoviewnews = view.findViewById(R.id.videoviewnews);
            playimgnews = view.findViewById(R.id.playimgnews);
            sharevideonews = view.findViewById(R.id.sharevideonews);
            commentvideonew = view.findViewById(R.id.commentvideonew);
            commenticonvideonews = view.findViewById(R.id.commenticonvideonews);
            likesvideonews = view.findViewById(R.id.likesvideonews);

            ci1videonews = view.findViewById(R.id.ci1videonews);
            ci2videonews = view.findViewById(R.id.ci2videonews);
            ci3videonews = view.findViewById(R.id.ci3videonews);
            deletevdonews = view.findViewById(R.id.deletevdonews);
            captionvideonews = view.findViewById(R.id.captionvideonews);
            timenews = view.findViewById(R.id.timenews);


            //imagenews
            newsrelimage = view.findViewById(R.id.newsrelimage);
            namenewsimg= view.findViewById(R.id.namenewsimg);
            timenewsimg = view.findViewById(R.id.timenewsimg);
            profimg_news_img = view.findViewById(R.id.profimg_news_img);
            captionimgnews = view.findViewById(R.id.captionimgnews);
            imgviewnews= view.findViewById(R.id.imgviewnews);
            ci1imgnews= view.findViewById(R.id.ci1imgnews);
            ci2imgnews = view.findViewById(R.id.ci2imgnews);
            ci3imgnews = view.findViewById(R.id.ci3imgnews);
            likesimgnews= view.findViewById(R.id.likesimgnews);
            commenticonimgnews = view.findViewById(R.id.commenticonimgnews);
            commentimgnew = view.findViewById(R.id.commentimgnew);
            shareimgnews = view.findViewById(R.id.shareimgnews);
            like_imgnew = view.findViewById(R.id.like_imgnew);
            unlike_imgnew = view.findViewById(R.id.unlike_imgnew);
            unlike_videonew = view.findViewById(R.id.unlike_videonew);
            like_videonew = view.findViewById(R.id.like_videonew);
            deleteimgnews = view.findViewById(R.id.deleteimgnews);

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
    public NewFunctionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_promofunctions, parent, false);

        return new NewFunctionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NewFunctionAdapter.MyViewHolder holder, final int position) {

          Log.d("promolistsize","size"+promoArrList.size());

          if(promoArrList.get(position).getNews_flag() != null){
//              0:advertisement 1:news

              if(promoArrList.get(position).getNews_flag().equals("0")){
                  //Flag 0 - image , 1- video
                  if(promoArrList.get(position).getFlag() != null) {


                      //forImageView

                      if (promoArrList.get(position).getFlag().equals("0")) {
                          holder.advrelimg.setVisibility(View.VISIBLE);
                          holder.advrelvideo.setVisibility(View.GONE);
                          holder.newsrelvideo.setVisibility(View.GONE);
                          holder.newsrelimage.setVisibility(View.GONE);

                          holder.adname.setText(promoArrList.get(position).getAdv_name());
                          holder.des.setText(promoArrList.get(position).getDescription());

                          Log.d("imgurladv",promoArrList.get(position).getImage());
                          if (promoArrList.get(position).getImage() != null) {
                              Picasso.with(context).load(WebUrl.Base_url.concat(promoArrList.get(position).getImage())).error(R.drawable.splashscreen).into(holder.imageView);
                          }
                          if(promoArrList.get(position).getIs_favourite().equals("0")){
                              holder.faviourate.setVisibility(View.VISIBLE);
                              holder.unfaviourate.setVisibility(View.GONE);

                          }else if(promoArrList.get(position).getIs_favourite().equals("1")){
                              holder.faviourate.setVisibility(View.GONE);
                              holder.unfaviourate.setVisibility(View.VISIBLE);
                          }

                          if(promoArrList.get(position).getIs_subcribe().equals("0")){
                              holder.subscribe.setVisibility(View.GONE);
                              holder.subscribed.setVisibility(View.VISIBLE);
                          }else if(promoArrList.get(position).getIs_subcribe().equals("1")){
                              holder.subscribe.setVisibility(View.VISIBLE);
                              holder.subscribed.setVisibility(View.GONE);
                          }
                          if(promoArrList.get(position).getAdvertisement_comment_count() != null) {
                              if (promoArrList.get(position).getAdvertisement_comment_count().equals("1")) {
                                  holder.commentsicon.setVisibility(View.VISIBLE);
                                  holder.commentcount.setText((" " + promoArrList.get(position).getAdvertisement_comment_count() + " Comment"));
                              } else if (promoArrList.get(position).getAdvertisement_comment_count().equals("0")) {
                                  holder.commentsicon.setVisibility(View.GONE);
                                  holder.commentcount.setText("");
                              } else {
                                  holder.commentsicon.setVisibility(View.VISIBLE);
                                  holder.commentcount.setText((" " + promoArrList.get(position).getAdvertisement_comment_count() + " Comments"));
                              }
                          }else {
                              Toast.makeText(context,"comment count not found",Toast.LENGTH_SHORT).show();
                          }


                          if(promoArrList.get(position).getAdvertisement_like_imgage() != null) {
                              likearrylist = promoArrList.get(position).getAdvertisement_like_imgage();
                              Log.d("positionaccept" + position, "size" + likearrylist.size());
                              checkarraysize = likearrylist.size();
                              Log.d("checkarrylist" + position, "size" + checkarraysize);
                              if (checkarraysize == 0) {
                                  holder.ciI1.setVisibility(View.GONE);
                                  holder.ciI2.setVisibility(View.GONE);
                                  holder.ciI3.setVisibility(View.GONE);
                              } else if (checkarraysize == 1) {
                                  String imageurl;
                                  holder.ciI1.setVisibility(View.VISIBLE);
                                  holder.ciI2.setVisibility(View.GONE);
                                  holder.ciI3.setVisibility(View.GONE);
                                  imageurl = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(0));
                                  Picasso.with(context).load(imageurl).error(R.drawable.default_profile).into(holder.ciI1);
                              } else if (checkarraysize == 2) {
                                  String imag1, img2;
                                  holder.ciI1.setVisibility(View.VISIBLE);
                                  holder.ciI2.setVisibility(View.VISIBLE);
                                  holder.ciI3.setVisibility(View.GONE);

                                  imag1 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(0));
                                  img2 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(1));

                                  Picasso.with(context).load(imag1).error(R.drawable.default_profile).into(holder.ciI1);
                                  Picasso.with(context).load(img2).error(R.drawable.default_profile).into(holder.ciI2);

                              } else if (checkarraysize == 3) {
                                  String imag1, img2, img3;
                                  holder.ciI1.setVisibility(View.VISIBLE);
                                  holder.ciI2.setVisibility(View.VISIBLE);
                                  holder.ciI3.setVisibility(View.VISIBLE);

                                  imag1 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(0));
                                  img2 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(1));
                                  img3 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(2));
                                  Picasso.with(context).load(imag1).error(R.drawable.default_profile).into(holder.ciI1);
                                  Picasso.with(context).load(img2).error(R.drawable.default_profile).into(holder.ciI2);
                                  Picasso.with(context).load(img3).error(R.drawable.default_profile).into(holder.ciI3);

                              }


                          }else{

                              Toast.makeText(context,"likes not found",Toast.LENGTH_SHORT).show();
                          }

                          if(promoArrList.get(position).getAdvertisement_like_count() != null) {
                              if(promoArrList.get(position).getAdvertisement_like_count().equals("1")){
                                  holder.likesI.setText(" "+promoArrList.get(position).getAdvertisement_like_count()+" "+"like");
                              }
                              else if (promoArrList.get(position).getAdvertisement_like_count().equals("0")){
                                  holder.likesI.setText("");
                              }
                              else{
                                  holder.likesI.setText(" "+promoArrList.get(position).getAdvertisement_like_count()+" "+"likes");
                              }

                          }else{
                              Toast.makeText(context,"like count not found",Toast.LENGTH_SHORT).show();
                          }

                          holder.readmore.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  String descrption = promoArrList.get(position).getDescription();
                                  showdialog(view,descrption);
                              }
                          });

                          holder.faviourate.setOnClickListener(new View.OnClickListener() {

                              @Override
                              public void onClick(View view) {
                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getUser_id();

                                  holder.unfaviourate.setVisibility(View.VISIBLE);
                                  holder.faviourate.setVisibility(View.GONE);

                                  calldislike(id,userid);

                              }
                          });

                          holder.unfaviourate.setOnClickListener(new View.OnClickListener() {


                              @Override
                              public void onClick(View view) {
                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getUser_id();

                                  holder.faviourate.setVisibility(View.VISIBLE);
                                  holder.unfaviourate.setVisibility(View.GONE);

                                  calllike(id,userid);
                              }
                          });


                          holder.subscribe.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getUser_id();


                                  holder.subscribe.setVisibility(View.GONE);
                                  holder.subscribed.setVisibility(View.VISIBLE);

                                  callsubscribe(id,userid);
                              }
                          });

                          holder.share.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  Intent intent = new Intent();
                                  intent.setAction(Intent.ACTION_SEND);
                                  intent.putExtra(Intent.EXTRA_SUBJECT, "Try This");
                                  intent.putExtra(Intent.EXTRA_TEXT, promoArrList.get(position).getWeburl());
                                  intent.setType("text/plain");
                                  context.startActivity(intent);
                              }
                          });

                          holder.commentcount.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  String profilepic = null;
                                  if(promoArrList.get(position).getApp_icon() != null){
                                      profilepic = WebUrl.Base_url.concat(promoArrList.get(position).getLoginprofilepic());
                                  }

                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getLoginuserid();

                                  Intent i = new Intent (context, CommentsActivity.class);

                                  i.putExtra("adv_id",id);
                                  i.putExtra("user_id",PrefManager.getUserId(context));
                                  i.putExtra("profilepic",profilepic);
                                  context.startActivity(i);
                              }
                          });
                          holder.comment.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  String profilepic = null;
                                  if(promoArrList.get(position).getApp_icon() != null){
                                      profilepic = WebUrl.Base_url.concat(promoArrList.get(position).getLoginprofilepic());
                                  }

                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getLoginuserid();

                                  Intent i = new Intent (context, CommentsActivity.class);

                                  i.putExtra("adv_id",id);
                                  i.putExtra("user_id",PrefManager.getUserId(context));
                                  i.putExtra("profilepic",profilepic);
                                  context.startActivity(i);
                              }
                          });

                          holder.subscribed.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getUser_id();

                                  holder.subscribe.setVisibility(View.VISIBLE);
                                  holder.subscribed.setVisibility(View.GONE);


                                  callsubscribed(id,userid);
                              }
                          });

                      }


                      //forVideoCiew

                      else  if (promoArrList.get(position).getFlag().equals("1")) {
                          holder.advrelimg.setVisibility(View.GONE);
                          holder.advrelvideo.setVisibility(View.VISIBLE);
                          holder.newsrelvideo.setVisibility(View.GONE);
                          holder.newsrelimage.setVisibility(View.GONE);


                          holder.name.setText(promoArrList.get(position).getAdv_name());


                          if(promoArrList.get(position).getAdvertisement_like_count() != null) {
                              if(promoArrList.get(position).getAdvertisement_like_count().equals("1")){
                                  holder.likes.setText(" "+promoArrList.get(position).getAdvertisement_like_count()+" "+"like");
                              }
                              else if (promoArrList.get(position).getAdvertisement_like_count().equals("0")){
                                  holder.likes.setText("");
                              }
                              else{
                                  holder.likes.setText(" "+promoArrList.get(position).getAdvertisement_like_count()+" "+"likes");
                              }

                          }


                          String input = promoArrList.get(position).getCreated_date();
                          //Date/time pattern of input date
                          DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                          //Date/time pattern of desired output date
                          DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
                          Date date = null;
                          String output = null;
                          try{
                              //Conversion of input String to date
                              date= df.parse(input);
                              //old date format to new date format
                              output = outputformat.format(date);
                              holder.time.setText(output);

                          }catch(ParseException pe){
                              pe.printStackTrace();
                          }
                          if(promoArrList.get(position).getAdvertisement_comment_count() != null) {
                              if (promoArrList.get(position).getAdvertisement_comment_count().equals("1")) {
                                  holder.commenticon2.setVisibility(View.VISIBLE);
                                  holder.comment2.setText(" " + promoArrList.get(position).getAdvertisement_comment_count() + " Comment");
                              } else if (promoArrList.get(position).getAdvertisement_comment_count().equals("0")) {
                                  holder.commenticon2.setVisibility(View.GONE);
                                  holder.comment2.setText(" ");
                              } else {
                                  holder.commenticon2.setVisibility(View.VISIBLE);
                                  holder.comment2.setText(" " + promoArrList.get(position).getAdvertisement_comment_count() + " Comment");
                              }
                          }else{
                              Toast.makeText(context,"comment count not found",Toast.LENGTH_SHORT).show();
                          }


                          if(promoArrList.get(position).getChallenge_type().equals("1")){
                              holder.challengetype.setImageResource(R.drawable.challengeicon);
                          }

                          if(promoArrList.get(position).getChallenge_type().equals("2")){
                              holder.challengetype.setImageResource(R.drawable.charityicon);
                          }

                          if(promoArrList.get(position).getChallenge_type().equals("3")){
                              holder.challengetype.setImageResource(R.drawable.trainicon);
                          }

                          if(promoArrList.get(position).getProfile_image()!=null) {
                              Picasso.with(context).load(WebUrl.Base_url.concat(promoArrList.get(position).getProfile_image())).error(R.drawable.splashscreen).into(holder.profimg);
                          }

                          if(promoArrList.get(position).getIs_favourite().equals("0")){
                              holder.like.setVisibility(View.VISIBLE);
                              holder.unlike.setVisibility(View.GONE);

                          }else if(promoArrList.get(position).getIs_favourite().equals("1")){
                              holder.like.setVisibility(View.GONE);
                              holder.unlike.setVisibility(View.VISIBLE);
                          }


                          if(promoArrList.get(position).getAdvertisement_like_imgage() != null) {
                              likearrylist = promoArrList.get(position).getAdvertisement_like_imgage();
                              Log.d("positionaccept" + position, "size" + likearrylist.size());
                              checkarraysize = likearrylist.size();
                              Log.d("checkarrylist" + position, "size" + checkarraysize);
                              if (checkarraysize == 0) {
                                  holder.ci1.setVisibility(View.GONE);
                                  holder.ci2.setVisibility(View.GONE);
                                  holder.ci3.setVisibility(View.GONE);
                              } else if (checkarraysize == 1) {
                                  String imageurl;
                                  holder.ci1.setVisibility(View.VISIBLE);
                                  holder.ci2.setVisibility(View.GONE);
                                  holder.ci3.setVisibility(View.GONE);
                                  imageurl = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(0));
                                  Picasso.with(context).load(imageurl).error(R.drawable.default_profile).into(holder.ci1);
                              } else if (checkarraysize == 2) {
                                  String imag1, img2;
                                  holder.ci1.setVisibility(View.VISIBLE);
                                  holder.ci2.setVisibility(View.VISIBLE);
                                  holder.ci3.setVisibility(View.GONE);

                                  imag1 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(0));
                                  img2 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(1));

                                  Picasso.with(context).load(imag1).error(R.drawable.default_profile).into(holder.ci1);
                                  Picasso.with(context).load(img2).error(R.drawable.default_profile).into(holder.ci2);

                              } else if (checkarraysize == 3) {
                                  String imag1, img2, img3;
                                  holder.ci1.setVisibility(View.VISIBLE);
                                  holder.ci2.setVisibility(View.VISIBLE);
                                  holder.ci3.setVisibility(View.VISIBLE);

                                  imag1 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(0));
                                  img2 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(1));
                                  img3 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(2));
                                  Picasso.with(context).load(imag1).error(R.drawable.default_profile).into(holder.ci1);
                                  Picasso.with(context).load(img2).error(R.drawable.default_profile).into(holder.ci2);
                                  Picasso.with(context).load(img3).error(R.drawable.default_profile).into(holder.ci3);

                              }


                          }else{

                              Toast.makeText(context,"likes not found",Toast.LENGTH_SHORT).show();
                          }

                          Log.d("videourladv",WebUrl.Base_url.concat(promoArrList.get(position).getImage()));
                          RequestOptions requestOptions = new RequestOptions();
                          requestOptions.placeholder(R.drawable.splashscreen);
                          requestOptions.error(R.drawable.splashscreen);


                          Glide.with(context)
                                  .load(WebUrl.Base_url.concat(promoArrList.get(position).getImage()))
                                  .apply(requestOptions)
                                  .thumbnail(Glide.with(context).load(WebUrl.Base_url.concat(promoArrList.get(position).getImage())))
                                  .into(holder.imageView2);
                          holder.playimg.setVisibility(View.VISIBLE);

                          //like_for_video
                          holder.like.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {

                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getUser_id();

                                  holder.unlike.setVisibility(View.VISIBLE);
                                  holder.like.setVisibility(View.GONE);

                                  calldislike(id,userid);


                              }
                          });

                          //unlikeforvideo
                          holder.unlike.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {

                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getUser_id();

                                  holder.like.setVisibility(View.VISIBLE);
                                  holder.unlike.setVisibility(View.GONE);

                                  calllike(id,userid);

                              }
                          });
                          holder.comment2.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  String profilepic = null;
                                  if(promoArrList.get(position).getApp_icon() != null){
                                      profilepic = WebUrl.Base_url.concat(promoArrList.get(position).getLoginprofilepic());
                                  }

                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getLoginuserid();

                                  Intent i = new Intent (context, CommentsActivity.class);

                                  i.putExtra("adv_id",id);
                                  i.putExtra("user_id",PrefManager.getUserId(context));
                                  i.putExtra("profilepic",profilepic);
                                  context.startActivity(i);
                              }
                          });
                          holder.comment22.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  String profilepic = null;
                                  if(promoArrList.get(position).getApp_icon() != null){
                                      profilepic = WebUrl.Base_url.concat(promoArrList.get(position).getLoginprofilepic());
                                  }

                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getLoginuserid();

                                  Intent i = new Intent (context, CommentsActivity.class);

                                  i.putExtra("adv_id",id);
                                  i.putExtra("user_id",PrefManager.getUserId(context));
                                  i.putExtra("profilepic",profilepic);
                                  context.startActivity(i);
                              }
                          });

                          holder.share2.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {


                                  Intent intent = new Intent();
                                  intent.setAction(Intent.ACTION_SEND);
                                  intent.putExtra(Intent.EXTRA_SUBJECT, "Try This");
                                  intent.putExtra(Intent.EXTRA_TEXT, promoArrList.get(position).getWeburl());
                                  intent.setType("text/plain");
                                  context.startActivity(intent);

                              }
                          });


                      }



                  }
                  else{
                      Toast.makeText(context,"There is no data",Toast.LENGTH_SHORT).show();
                  }
              }

              else{
                  //Flag_vi_new 0 - image , 1- video
                  if(promoArrList.get(position).getFlag_vi_new() != null) {


                      //forImageViewnews

                      if (promoArrList.get(position).getFlag_vi_new().equals("0")) {
                          holder.advrelimg.setVisibility(View.GONE);
                          holder.advrelvideo.setVisibility(View.GONE);
                          holder.newsrelimage.setVisibility(View.VISIBLE);
                          holder.newsrelvideo.setVisibility(View.GONE);

                          if(promoArrList.get(position).getUser_id().equals(PrefManager.getUserId(context))){
                              holder.deleteimgnews.setVisibility(View.VISIBLE);
                          }else{
                              holder.deleteimgnews.setVisibility(View.GONE);
                          }

                          holder.deleteimgnews.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  String id = promoArrList.get(position).getId();
                                  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                  alertDialogBuilder.setMessage("Are you sure, You want to delete news?");
                                  alertDialogBuilder.setPositiveButton("Delete",
                                          new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface arg0, int arg1)
                                              {
                                                  deleteimagenew(id);

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
                          });

                          holder.namenewsimg.setText(promoArrList.get(position).getUser_name());
                          holder.captionimgnews.setText(promoArrList.get(position).getDescription());
                          if(promoArrList.get(position).getProfile_image()!=null) {
                              Picasso.with(context).load(WebUrl.Base_url.concat(promoArrList.get(position).getProfile_image())).error(R.drawable.splashscreen).into(holder.profimg_news_img);
                          }
                          Log.d("imgurlnews",promoArrList.get(position).getImage());
                          if (promoArrList.get(position).getImage() != null) {
                              Picasso.with(context).load(WebUrl.Base_url.concat(promoArrList.get(position).getImage())).error(R.drawable.splashscreen).into(holder.imgviewnews);
                          }
                          if(promoArrList.get(position).getIs_favourite().equals("0")){
                              holder.like_imgnew.setVisibility(View.VISIBLE);
                              holder.unlike_imgnew.setVisibility(View.GONE);

                          }else if(promoArrList.get(position).getIs_favourite().equals("1")){
                              holder.like_imgnew.setVisibility(View.GONE);
                              holder.unlike_imgnew.setVisibility(View.VISIBLE);
                          }

//                          if(promoArrList.get(position).getIs_subcribe().equals("0")){
//                              holder.subscribe.setVisibility(View.GONE);
//                              holder.subscribed.setVisibility(View.VISIBLE);
//                          }else if(promoArrList.get(position).getIs_subcribe().equals("1")){
//                              holder.subscribe.setVisibility(View.VISIBLE);
//                              holder.subscribed.setVisibility(View.GONE);
//                          }
                          String input = promoArrList.get(position).getCreated_date();
                          //Date/time pattern of input date
                          DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                          //Date/time pattern of desired output date
                          DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
                          Date date = null;
                          String output = null;
                          try{
                              //Conversion of input String to date
                              date= df.parse(input);
                              //old date format to new date format
                              output = outputformat.format(date);
                              holder.timenewsimg.setText(output);

                          }catch(ParseException pe){
                              pe.printStackTrace();
                          }
                          if(promoArrList.get(position).getAdvertisement_comment_count() != null) {
                              if (promoArrList.get(position).getAdvertisement_comment_count().equals("1")) {
                                  holder.commenticonimgnews.setVisibility(View.VISIBLE);
                                  holder.commentimgnew.setText((" " + promoArrList.get(position).getAdvertisement_comment_count() + " Comment"));
                              } else if (promoArrList.get(position).getAdvertisement_comment_count().equals("0")) {
                                  holder.commenticonimgnews.setVisibility(View.VISIBLE);
                                  holder.commentimgnew.setText(" "+" Comment");
                              } else {
                                  holder.commenticonimgnews.setVisibility(View.VISIBLE);
                                  holder.commentimgnew.setText((" " + promoArrList.get(position).getAdvertisement_comment_count() + " Comments"));
                              }
                          }else {
                              Toast.makeText(context,"comment count not found",Toast.LENGTH_SHORT).show();
                          }


                          if(promoArrList.get(position).getAdvertisement_like_imgage() != null) {
                              likearrylist = promoArrList.get(position).getAdvertisement_like_imgage();
                              Log.d("positionaccept" + position, "size" + likearrylist.size());
                              checkarraysize = likearrylist.size();
                              Log.d("checkarrylist" + position, "size" + checkarraysize);
                              if (checkarraysize == 0) {
                                  holder.ci1imgnews.setVisibility(View.GONE);
                                  holder.ci2imgnews.setVisibility(View.GONE);
                                  holder.ci3imgnews.setVisibility(View.GONE);
                              } else if (checkarraysize == 1) {
                                  String imageurl;
                                  holder.ci1imgnews.setVisibility(View.VISIBLE);
                                  holder.ci2imgnews.setVisibility(View.GONE);
                                  holder.ci3imgnews.setVisibility(View.GONE);
                                  imageurl = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(0));
                                  Picasso.with(context).load(imageurl).error(R.drawable.default_profile).into(holder.ci1imgnews);
                              } else if (checkarraysize == 2) {
                                  String imag1, img2;
                                  holder.ci1imgnews.setVisibility(View.VISIBLE);
                                  holder.ci2imgnews.setVisibility(View.VISIBLE);
                                  holder.ci3imgnews.setVisibility(View.GONE);

                                  imag1 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(0));
                                  img2 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(1));

                                  Picasso.with(context).load(imag1).error(R.drawable.default_profile).into(holder.ci1imgnews);
                                  Picasso.with(context).load(img2).error(R.drawable.default_profile).into(holder.ci2imgnews);

                              } else if (checkarraysize == 3) {
                                  String imag1, img2, img3;
                                  holder.ci1imgnews.setVisibility(View.VISIBLE);
                                  holder.ci2imgnews.setVisibility(View.VISIBLE);
                                  holder.ci3imgnews.setVisibility(View.VISIBLE);

                                  imag1 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(0));
                                  img2 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(1));
                                  img3 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(2));
                                  Picasso.with(context).load(imag1).error(R.drawable.default_profile).into(holder.ci1imgnews);
                                  Picasso.with(context).load(img2).error(R.drawable.default_profile).into(holder.ci2imgnews);
                                  Picasso.with(context).load(img3).error(R.drawable.default_profile).into(holder.ci3imgnews);

                              }


                          }else{

                              Toast.makeText(context,"likes not found",Toast.LENGTH_SHORT).show();
                          }

                          if(promoArrList.get(position).getAdvertisement_like_count() != null) {
                              if(promoArrList.get(position).getAdvertisement_like_count().equals("1")){
                                  holder.likesimgnews.setText(" "+promoArrList.get(position).getAdvertisement_like_count()+" "+"like");
                              }
                              else if (promoArrList.get(position).getAdvertisement_like_count().equals("0")){
                                  holder.likesimgnews.setText("");
                              }
                              else{
                                  holder.likesimgnews.setText(" "+promoArrList.get(position).getAdvertisement_like_count()+" "+"likes");
                              }

                          }else{
                              Toast.makeText(context,"like count not found",Toast.LENGTH_SHORT).show();
                          }

//                          holder.readmore.setOnClickListener(new View.OnClickListener() {
//                              @Override
//                              public void onClick(View view) {
//                                  String descrption = promoArrList.get(position).getDescription();
//                                  showdialog(view,descrption);
//                              }
//                          });
//likeimagenew
                          holder.like_imgnew.setOnClickListener(new View.OnClickListener() {

                              @Override
                              public void onClick(View view) {
                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getUser_id();

                                  holder.unfaviourate.setVisibility(View.VISIBLE);
                                  holder.faviourate.setVisibility(View.GONE);

                                  calldislike(id,userid);

                              }
                          });
//dislikeimgnew
                          holder.unlike_imgnew.setOnClickListener(new View.OnClickListener() {


                              @Override
                              public void onClick(View view) {
                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getUser_id();

                                  holder.faviourate.setVisibility(View.VISIBLE);
                                  holder.unfaviourate.setVisibility(View.GONE);

                                  calllike(id,userid);
                              }
                          });


                          holder.subscribe.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getUser_id();


                                  holder.subscribe.setVisibility(View.GONE);
                                  holder.subscribed.setVisibility(View.VISIBLE);

                                  callsubscribe(id,userid);
                              }
                          });

                          holder.share.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  Intent intent = new Intent();
                                  intent.setAction(Intent.ACTION_SEND);
                                  intent.putExtra(Intent.EXTRA_SUBJECT, "Try This");
                                  intent.putExtra(Intent.EXTRA_TEXT, promoArrList.get(position).getWeburl());
                                  intent.setType("text/plain");
                                  context.startActivity(intent);
                              }
                          });

                          holder.commentimgnew.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  String profilepic = null;
                                  if(promoArrList.get(position).getApp_icon() != null){
                                      profilepic = WebUrl.Base_url.concat(promoArrList.get(position).getLoginprofilepic());
                                  }

                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getLoginuserid();

                                  Intent i = new Intent (context, CommentsActivity.class);

                                  i.putExtra("adv_id",id);
                                  i.putExtra("user_id",PrefManager.getUserId(context));
                                  i.putExtra("profilepic",profilepic);
                                  context.startActivity(i);
                              }
                          });
//                          holder.comment.setOnClickListener(new View.OnClickListener() {
//                              @Override
//                              public void onClick(View view) {
//                                  String profilepic = null;
//                                  if(promoArrList.get(position).getApp_icon() != null){
//                                      profilepic = WebUrl.Base_url.concat(promoArrList.get(position).getLoginprofilepic());
//                                  }
//
//                                  String id = promoArrList.get(position).getId();
//                                  String userid = promoArrList.get(position).getLoginuserid();
//
//                                  Intent i = new Intent (context, CommentsActivity.class);
//
//                                  i.putExtra("adv_id",id);
//                                  i.putExtra("user_id",userid);
//                                  i.putExtra("profilepic",profilepic);
//                                  context.startActivity(i);
//                              }
//                          });

//                          holder.subscribed.setOnClickListener(new View.OnClickListener() {
//                              @Override
//                              public void onClick(View view) {
//                                  String id = promoArrList.get(position).getId();
//                                  String userid = promoArrList.get(position).getUser_id();
//
//                                  holder.subscribe.setVisibility(View.VISIBLE);
//                                  holder.subscribed.setVisibility(View.GONE);
//
//
//                                  callsubscribed(id,userid);
//                              }
//                          });

                      }


                      //forVideonews

                      else  if (promoArrList.get(position).getFlag_vi_new().equals("1")) {
                          holder.advrelimg.setVisibility(View.GONE);
                          holder.advrelvideo.setVisibility(View.GONE);
                          holder.newsrelimage.setVisibility(View.GONE);
                          holder.newsrelvideo.setVisibility(View.VISIBLE);

                          if(promoArrList.get(position).getUser_id().equals(PrefManager.getUserId(context))){
                              holder.deletevdonews.setVisibility(View.VISIBLE);
                          }else{
                              holder.deletevdonews.setVisibility(View.GONE);
                          }
                          holder.deletevdonews.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  String id = promoArrList.get(position).getId();
                                  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                  alertDialogBuilder.setMessage("Are you sure, You want to delete news?");
                                  alertDialogBuilder.setPositiveButton("Delete",
                                          new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface arg0, int arg1)
                                              {
                                                  deleteimagenew(id);

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
                          });
                          holder.namenews.setText(promoArrList.get(position).getUser_name());
                          holder.captionvideonews.setText(promoArrList.get(position).getDescription());


                          Log.d("videourlnews",WebUrl.Base_url.concat(promoArrList.get(position).getImage()));
                          RequestOptions requestOptions = new RequestOptions();
                          requestOptions.placeholder(R.drawable.splashscreen);
                          requestOptions.error(R.drawable.splashscreen);
                          Glide.with(context)
                                  .load(WebUrl.Base_url.concat(promoArrList.get(position).getImage()))
                                  .apply(requestOptions)
                                  .thumbnail(Glide.with(context).load(WebUrl.Base_url.concat(promoArrList.get(position).getImage())))
                                  .into(holder.videoviewnews);
                          holder.playimgnews.setVisibility(View.VISIBLE);

                          if(promoArrList.get(position).getAdvertisement_like_count() != null) {
                              if(promoArrList.get(position).getAdvertisement_like_count().equals("1")){
                                  holder.likesvideonews.setText(" "+promoArrList.get(position).getAdvertisement_like_count()+" "+"like");
                              }
                              else if (promoArrList.get(position).getAdvertisement_like_count().equals("0")){
                                  holder.likesvideonews.setText("");
                              }
                              else{
                                  holder.likesvideonews.setText(" "+promoArrList.get(position).getAdvertisement_like_count()+" "+"likes");
                              }

                          }


                          String input = promoArrList.get(position).getCreated_date();
                          //Date/time pattern of input date
                          DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                          //Date/time pattern of desired output date
                          DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
                          Date date = null;
                          String output = null;
                          try{
                              //Conversion of input String to date
                              date= df.parse(input);
                              //old date format to new date format
                              output = outputformat.format(date);
                              holder.timenews.setText(output);

                          }catch(ParseException pe){
                              pe.printStackTrace();
                          }
                          if(promoArrList.get(position).getAdvertisement_comment_count() != null) {
                              if (promoArrList.get(position).getAdvertisement_comment_count().equals("1")) {
                                  holder.commenticonvideonews.setVisibility(View.VISIBLE);
                                  holder.commentvideonew.setText(" " + promoArrList.get(position).getAdvertisement_comment_count() + " Comment");
                              } else if (promoArrList.get(position).getAdvertisement_comment_count().equals("0")) {
                                  holder.commenticonvideonews.setVisibility(View.VISIBLE);
                                  holder.commentvideonew.setText(" "+" Comment");
                              } else {
                                  holder.commenticonvideonews.setVisibility(View.VISIBLE);
                                  holder.commentvideonew.setText(" " + promoArrList.get(position).getAdvertisement_comment_count() + " Comment");
                              }
                          }else{
                              Toast.makeText(context,"comment count not found",Toast.LENGTH_SHORT).show();
                          }


//                          if(promoArrList.get(position).getChallenge_type().equals("1")){
//                              holder.challengetype.setImageResource(R.drawable.challengeicon);
//                          }
//
//                          if(promoArrList.get(position).getChallenge_type().equals("2")){
//                              holder.challengetype.setImageResource(R.drawable.charityicon);
//                          }
//
//                          if(promoArrList.get(position).getChallenge_type().equals("3")){
//                              holder.challengetype.setImageResource(R.drawable.trainicon);
//                          }

                          if(promoArrList.get(position).getProfile_image()!=null) {
                              Picasso.with(context).load(WebUrl.Base_url.concat(promoArrList.get(position).getProfile_image())).error(R.drawable.splashscreen).into(holder.profimg_news);
                          }

                          if(promoArrList.get(position).getIs_favourite().equals("0")){
                              holder.like_videonew.setVisibility(View.VISIBLE);
                              holder.unlike_videonew.setVisibility(View.GONE);

                          }else if(promoArrList.get(position).getIs_favourite().equals("1")){
                              holder.like_videonew.setVisibility(View.GONE);
                              holder.unlike_videonew.setVisibility(View.VISIBLE);
                          }


                          if(promoArrList.get(position).getAdvertisement_like_imgage() != null) {
                              likearrylist = promoArrList.get(position).getAdvertisement_like_imgage();
                              Log.d("positionaccept" + position, "size" + likearrylist.size());
                              checkarraysize = likearrylist.size();
                              Log.d("checkarrylist" + position, "size" + checkarraysize);
                              if (checkarraysize == 0) {
                                  holder.ci1videonews.setVisibility(View.GONE);
                                  holder.ci2videonews.setVisibility(View.GONE);
                                  holder.ci3videonews.setVisibility(View.GONE);
                              } else if (checkarraysize == 1) {
                                  String imageurl;
                                  holder.ci1videonews.setVisibility(View.VISIBLE);
                                  holder.ci2videonews.setVisibility(View.GONE);
                                  holder.ci3videonews.setVisibility(View.GONE);
                                  imageurl = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(0));
                                  Picasso.with(context).load(imageurl).error(R.drawable.default_profile).into(holder.ci1videonews);
                              } else if (checkarraysize == 2) {
                                  String imag1, img2;
                                  holder.ci1videonews.setVisibility(View.VISIBLE);
                                  holder.ci2videonews.setVisibility(View.VISIBLE);
                                  holder.ci3videonews.setVisibility(View.GONE);

                                  imag1 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(0));
                                  img2 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(1));

                                  Picasso.with(context).load(imag1).error(R.drawable.default_profile).into(holder.ci1videonews);
                                  Picasso.with(context).load(img2).error(R.drawable.default_profile).into(holder.ci2videonews);

                              } else if (checkarraysize == 3) {
                                  String imag1, img2, img3;
                                  holder.ci1videonews.setVisibility(View.VISIBLE);
                                  holder.ci2videonews.setVisibility(View.VISIBLE);
                                  holder.ci3videonews.setVisibility(View.VISIBLE);

                                  imag1 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(0));
                                  img2 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(1));
                                  img3 = Base_url.concat(promoArrList.get(position).getAdvertisement_like_imgage().get(2));
                                  Picasso.with(context).load(imag1).error(R.drawable.default_profile).into(holder.ci1videonews);
                                  Picasso.with(context).load(img2).error(R.drawable.default_profile).into(holder.ci2videonews);
                                  Picasso.with(context).load(img3).error(R.drawable.default_profile).into(holder.ci3videonews);

                              }


                          }else{

                              Toast.makeText(context,"likes not found",Toast.LENGTH_SHORT).show();
                          }


                          //like_for_videonews
                          holder.like_videonew.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {

                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getUser_id();

                                  holder.unlike.setVisibility(View.VISIBLE);
                                  holder.like.setVisibility(View.GONE);

                                  calldislike(id,userid);


                              }
                          });

                          //unlikeforvideonews
                          holder.unlike_videonew.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {

                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getUser_id();

                                  holder.like.setVisibility(View.VISIBLE);
                                  holder.unlike.setVisibility(View.GONE);

                                  calllike(id,userid);

                              }
                          });
                          holder.commentvideonew.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  String profilepic = null;
                                  if(promoArrList.get(position).getApp_icon() != null){
                                      profilepic = WebUrl.Base_url.concat(promoArrList.get(position).getLoginprofilepic());
                                  }

                                  String id = promoArrList.get(position).getId();
                                  String userid = promoArrList.get(position).getLoginuserid();

                                  Intent i = new Intent (context, CommentsActivity.class);

                                  i.putExtra("adv_id",id);
                                  i.putExtra("user_id",PrefManager.getUserId(context));
                                  i.putExtra("profilepic",profilepic);
                                  context.startActivity(i);
                              }
                          });
//                          holder.comment22.setOnClickListener(new View.OnClickListener() {
//                              @Override
//                              public void onClick(View view) {
//                                  String profilepic = null;
//                                  if(promoArrList.get(position).getApp_icon() != null){
//                                      profilepic = WebUrl.Base_url.concat(promoArrList.get(position).getLoginprofilepic());
//                                  }
//
//                                  String id = promoArrList.get(position).getId();
//                                  String userid = promoArrList.get(position).getLoginuserid();
//
//                                  Intent i = new Intent (context, CommentsActivity.class);
//
//                                  i.putExtra("adv_id",id);
//                                  i.putExtra("user_id",userid);
//                                  i.putExtra("profilepic",profilepic);
//                                  context.startActivity(i);
//                              }
//                          });

                          holder.sharevideonews.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {


                                  Intent intent = new Intent();
                                  intent.setAction(Intent.ACTION_SEND);
                                  intent.putExtra(Intent.EXTRA_SUBJECT, "Try This");
                                  intent.putExtra(Intent.EXTRA_TEXT, promoArrList.get(position).getWeburl());
                                  intent.setType("text/plain");
                                  context.startActivity(intent);

                              }
                          });


                      }



                  }
                  else{
                      Toast.makeText(context,"There is no data",Toast.LENGTH_SHORT).show();
                  }

              }
          }else{
              Toast.makeText(context,"There is no data",Toast.LENGTH_SHORT).show();
          }






    }

    private void deleteimagenew(String id) {
        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.delete_news, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
Log.d("delete_news",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){
                        Intent i = new Intent(context, NewsFunctions.class);
                        context.startActivity(i);
                    }else{
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
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
                    Toast.makeText(context, context.getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();

                param.put("user_id",PrefManager.getUserId(context));
                param.put("news_id",id);


                return param;


            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void callsubscribed(String id, String userid) {

                pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.unsubscribe_advertisement, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();

                try {
                    JSONObject jsonObject=new JSONObject(response);

                  //  Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
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
                    Toast.makeText(context, context.getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();

                param.put("user_id", PrefManager.getUserId(context));
                param.put("advertisement_id",id);

                return param;


            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);



    }

    private void callsubscribe(String id, String userid) {
        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.subscrib_advertisement, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();

                try {
                    JSONObject jsonObject=new JSONObject(response);

                   // Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
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
                    Toast.makeText(context, context.getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();

                param.put("user_id", PrefManager.getUserId(context));
                param.put("advertisement_id",id);

                return param;


            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);



    }

    private void calllike(String id,String userid) {
        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.like_advertisement, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
Log.d("like_ad",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                      Intent i = new Intent(context, NewsFunctions.class);
                      context.startActivity(i);
                  //  Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
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
                    Toast.makeText(context, context.getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();

                param.put("user_id",PrefManager.getUserId(context));
                param.put("advertisement_id",id);


                return param;


            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }


    private void calldislike(String id,String userid) {
        pd = new TransparentProgressDialog(context, R.drawable.ic_loader_image);
        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.dislike_advertisement, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Intent i = new Intent(context, NewsFunctions.class);
                    context.startActivity(i);
                    //Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
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
                    Toast.makeText(context, context.getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();

                param.put("user_id", PrefManager.getUserId(context));
                param.put("advertisement_id",id);


                return param;


            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void showdialog(View view, String descrption) {
        TextView textView = new TextView(context);
        textView.setText("Read Here....");
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0,10,0,0);
        textView.setTextSize(20F);

        textView.setTextColor(Color.parseColor("#D9475C"));
        AlertDialog.Builder alertDialog = new AlertDialog.Builder((Activity) view.getContext());{

        alertDialog.setCustomTitle(textView);
        alertDialog.setMessage(descrption);


        alertDialog.setPositiveButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do the stuff..
                    }
                }
        );


    }

            alertDialog.show();
    }


    @Override
    public int getItemCount()
    {
        return promoArrList.size();
    }
}
