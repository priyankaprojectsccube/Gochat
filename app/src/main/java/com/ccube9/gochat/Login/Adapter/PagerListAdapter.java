package com.ccube9.gochat.Login.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.ccube9.gochat.R;

import java.util.List;

public class PagerListAdapter extends LoopingPagerAdapter<String> {


    public PagerListAdapter(Context context, List<String> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }


    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {

        return LayoutInflater.from(context).inflate(R.layout.adapter_pager, container, false);
    }


    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {

        TextView description = convertView.findViewById(R.id.text_banner);
        description.setText(String.valueOf(itemList.get(listPosition)));
    }




}
