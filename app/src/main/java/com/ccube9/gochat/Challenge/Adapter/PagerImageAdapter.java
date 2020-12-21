package com.ccube9.gochat.Challenge.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.WebUrl;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PagerImageAdapter extends LoopingPagerAdapter<String> {


    public PagerImageAdapter(Context context, List<String> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }


    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {

        return LayoutInflater.from(context).inflate(R.layout.adapter_image_pager, container, false);
    }


    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {

        ImageView imageview = convertView.findViewById(R.id.text_banner);

        Picasso.with(context).load(WebUrl.Base_url.concat(itemList.get(listPosition))).error(R.drawable.splashscreen).into(imageview);
    }




}
