package com.ccube9.gochat.News.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ccube9.gochat.News.Activity.MyStoriesPage;
import com.ccube9.gochat.News.Activity.NewsFunctions;
import com.ccube9.gochat.News.Activity.ViewStory;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MyStory;
import com.ccube9.gochat.Util.StoryCallbacks;

import java.util.ArrayList;

import omari.hamza.storyview.utils.PaletteExtraction;


public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<MyStory> images;
    private Context context;
    private StoryCallbacks storyCallbacks;
    private boolean storiesStarted = false;


    public ViewPagerAdapter(ArrayList<MyStory> images, Context context, StoryCallbacks storyCallbacks) {
        this.images = images;
        this.context = context;
        this.storyCallbacks = storyCallbacks;

    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, final int position) {

        LayoutInflater inflater = LayoutInflater.from(context);

        MyStory currentStory = images.get(position);

        final View view = inflater.inflate(R.layout.layout_viewpager, collection, false);

        final ImageView mImageView = view.findViewById(R.id.mImageView);


        if (!TextUtils.isEmpty(currentStory.getDescription())) {
            TextView textView = view.findViewById(R.id.descriptionTextView);
            textView.setVisibility(View.VISIBLE);
            textView.setText(currentStory.getDescription());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storyCallbacks.onDescriptionClickListener(position);
                }
            });
        }

        Glide.with(context)
                .load(currentStory.getUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        storyCallbacks.nextStory();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource != null) {
                            PaletteExtraction pe = new PaletteExtraction(view.findViewById(omari.hamza.storyview.R.id.relativeLayout),
                                    ((BitmapDrawable) resource).getBitmap());
                            pe.execute();
                        }
                        if (!storiesStarted) {
                            storiesStarted = true;
                            storyCallbacks.startStories();
                        }
                        return false;
                    }
                })
                .into(mImageView);

        collection.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View) object);

    }
}

