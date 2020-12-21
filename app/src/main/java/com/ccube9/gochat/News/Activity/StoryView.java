package com.ccube9.gochat.News.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.ccube9.gochat.News.Adapter.ViewPagerAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MyStory;
import com.ccube9.gochat.Util.OnStoryChangedCallback;
import com.ccube9.gochat.Util.StoryCallbacks;
import com.ccube9.gochat.Util.StoryClickListeners;
import com.ccube9.gochat.Util.StoryViewHeaderInfo;

import java.util.ArrayList;
import java.util.Calendar;




import omari.hamza.storyview.callback.TouchCallbacks;

import omari.hamza.storyview.progress.StoriesProgressView;
import omari.hamza.storyview.utils.PullDismissLayout;





public class StoryView extends DialogFragment implements StoriesProgressView.StoriesListener,
        StoryCallbacks,
        PullDismissLayout.Listener,
        TouchCallbacks {

    private static final String TAG = omari.hamza.storyview.StoryView.class.getSimpleName();

    private ArrayList<MyStory> storiesList = new ArrayList<>();

    private final static String IMAGES_KEY = "IMAGES";

    private long duration = 2000;
   // private int flag = 0;
    //Default Duration

    private static final String DURATION_KEY = "DURATION";
//    private  static final String FLAG = "FLAG";

    private static final String HEADER_INFO_KEY = "HEADER_INFO";

    private static final String STARTING_INDEX_TAG = "STARTING_INDEX";

    private static final String IS_RTL_TAG = "IS_RTL";

    private StoriesProgressView storiesProgressView;

    private ViewPager mViewPager;

    private int counter = 0;

    private int startingIndex = 0;

    //Heading
    private TextView titleTextView, subtitleTextView;
    private CardView titleCardView;
    private ImageView titleIconImageView;
    private ImageButton closeImageButton,delete;

    //Touch Events
    private boolean isDownClick = false;
    private long elapsedTime = 0;
    private Thread timerThread;
    private boolean isPaused = false;
    private int width, height;
    private float xValue = 0, yValue = 0;


    private StoryClickListeners storyClickListeners;
    private OnStoryChangedCallback onStoryChangedCallback;

    private boolean isRtl;

    private StoryView() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_stories, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        height = displaymetrics.heightPixels;
        // Get field from view
        readArguments();
        setupViews(view);
        setupStories();

    }

    private void setupStories() {
        storiesProgressView.setStoriesCount(storiesList.size());
        storiesProgressView.setStoryDuration(duration);
        updateHeading();
//        if(flag == 0){
//            delete.setVisibility(View.VISIBLE);
//        }else {
//            delete.setVisibility(View.GONE);
//        }

        mViewPager.setAdapter(new ViewPagerAdapter(storiesList, getContext(), this));
    }

    private void readArguments() {
        assert getArguments() != null;
        storiesList = new ArrayList<>((ArrayList<MyStory>) getArguments().getSerializable(IMAGES_KEY));
        duration = getArguments().getLong(DURATION_KEY, 2000);
//        flag = getArguments().getInt(FLAG,0);

        startingIndex = getArguments().getInt(STARTING_INDEX_TAG, 0);
        isRtl = getArguments().getBoolean(IS_RTL_TAG, false);
    }

    private void setupViews(View view) {
        ((PullDismissLayout) view.findViewById(omari.hamza.storyview.R.id.pull_dismiss_layout)).setListener(this);
        ((PullDismissLayout) view.findViewById(omari.hamza.storyview.R.id.pull_dismiss_layout)).setmTouchCallbacks(this);
        storiesProgressView = view.findViewById(omari.hamza.storyview.R.id.storiesProgressView);
        mViewPager = view.findViewById(omari.hamza.storyview.R.id.storiesViewPager);
        titleTextView = view.findViewById(omari.hamza.storyview.R.id.title_textView);
        subtitleTextView = view.findViewById(omari.hamza.storyview.R.id.subtitle_textView);
        titleIconImageView = view.findViewById(omari.hamza.storyview.R.id.title_imageView);
        titleCardView = view.findViewById(omari.hamza.storyview.R.id.titleCardView);
        closeImageButton = view.findViewById(R.id.imageButton);
//        delete = view.findViewById(R.id.delete);
        storiesProgressView.setStoriesListener(this);
        mViewPager.setOnTouchListener((v, event) -> true);
        closeImageButton.setOnClickListener(v -> dismissAllowingStateLoss());

        if (storyClickListeners != null) {
            titleCardView.setOnClickListener(v -> storyClickListeners.onTitleIconClickListener(counter));
        }

        if (onStoryChangedCallback != null) {
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    onStoryChangedCallback.storyChanged(position);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        if (isRtl) {
            storiesProgressView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            storiesProgressView.setRotation(180);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onNext() {
        mViewPager.setCurrentItem(++counter, false);
        updateHeading();
    }

    @Override
    public void onPrev() {
        if (counter <= 0) return;
        mViewPager.setCurrentItem(--counter, false);
        updateHeading();
    }

    @Override
    public void onComplete() {
        dismissAllowingStateLoss();
    }

    @Override
    public void startStories() {
        counter = startingIndex;
        storiesProgressView.startStories(startingIndex);
        mViewPager.setCurrentItem(startingIndex, false);
        updateHeading();
    }

    @Override
    public void pauseStories() {
        storiesProgressView.pause();
    }

    private void previousStory() {
        if (counter - 1 < 0) return;
        mViewPager.setCurrentItem(--counter, false);
        storiesProgressView.setStoriesCount(storiesList.size());
        storiesProgressView.setStoryDuration(duration);
        storiesProgressView.startStories(counter);
        updateHeading();
    }

    @Override
    public void nextStory() {
        if (counter + 1 >= storiesList.size()) {
            dismissAllowingStateLoss();
            return;
        }
        mViewPager.setCurrentItem(++counter, false);
        storiesProgressView.startStories(counter);
        updateHeading();
    }

    @Override
    public void onDescriptionClickListener(int position) {
        if (storyClickListeners == null) return;
        storyClickListeners.onDescriptionClickListener(position);
    }

    @Override
    public void onDestroy() {
        timerThread = null;
        storiesList = null;
        storiesProgressView.destroy();
        super.onDestroy();
    }

    private void updateHeading() {

        Object object = getArguments().getSerializable(HEADER_INFO_KEY);

        StoryViewHeaderInfo storyHeaderInfo = null;

        if (object instanceof StoryViewHeaderInfo) {

            storyHeaderInfo = (StoryViewHeaderInfo) object;


        } else if (object instanceof ArrayList) {

            storyHeaderInfo = ((ArrayList<StoryViewHeaderInfo>) object).get(counter);
        }

        if (storyHeaderInfo == null){
            return;
        } else{

        }

        if (storyHeaderInfo.getTitleIconUrl() != null) {

            titleCardView.setVisibility(View.VISIBLE);
            if (getContext() == null) return;
            Glide.with(getContext())
                    .load(storyHeaderInfo.getTitleIconUrl())
                    .into(titleIconImageView);
        } else {

            titleCardView.setVisibility(View.GONE);
        }

        if (storyHeaderInfo.getTitle() != null) {

            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(storyHeaderInfo.getTitle());
        } else {
            titleTextView.setVisibility(View.GONE);
        }

        if (storyHeaderInfo.getSubtitle() != null) {

            subtitleTextView.setVisibility(View.VISIBLE);
            subtitleTextView.setText(storyHeaderInfo.getSubtitle());
        } else {
            subtitleTextView.setVisibility(View.GONE);
        }

//        if (storiesList.get(counter).getDate() != null) {
//            titleTextView.setText(titleTextView.getText()
//                    + " "
//                    +storiesList.get(counter).getDate() //getDurationBetweenDates(storiesList.get(counter).getDate(), Calendar.getInstance().getTime()
//            );
//        }
    }

    private void setHeadingVisibility(int visibility) {
        titleTextView.setVisibility(visibility);
        titleCardView.setVisibility(visibility);
        subtitleTextView.setVisibility(visibility);
        closeImageButton.setVisibility(visibility);

        storiesProgressView.setVisibility(visibility);
    }


    private void createTimer() {
        timerThread = new Thread(() -> {
            while (isDownClick) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                elapsedTime += 100;
                if (elapsedTime >= 500 && !isPaused) {
                    isPaused = true;
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        storiesProgressView.pause();
                        setHeadingVisibility(View.GONE);
                    });
                }
            }
            isPaused = false;
            if (getActivity() == null) return;
            if (elapsedTime < 500) return;
            getActivity().runOnUiThread(() -> {
                setHeadingVisibility(View.VISIBLE);
                storiesProgressView.resume();
            });
        });
    }

    private void runTimer() {
        isDownClick = true;
        createTimer();
        setHeadingVisibility(View.VISIBLE);
        timerThread.start();
    }

    private void stopTimer() {
        isDownClick = false;
    }

    @Override
    public void onDismissed() {
        dismissAllowingStateLoss();
    }

    @Override
    public boolean onShouldInterceptTouchEvent() {
        return false;
    }

    @Override
    public void touchPull() {
        elapsedTime = 0;
        stopTimer();
        storiesProgressView.pause();
    }

    @Override
    public void touchDown(float xValue, float yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
        if (!isDownClick) {
            runTimer();
        }
    }

    @Override
    public void touchUp() {
        if (isDownClick && elapsedTime < 500) {
            stopTimer();
            if (((int) (height - yValue) <= 0.8 * height)) {
                if ((!TextUtils.isEmpty(storiesList.get(counter).getDescription())
                        && ((int) (height - yValue) >= 0.2 * height)
                        || TextUtils.isEmpty(storiesList.get(counter).getDescription()))) {
                    if ((int) xValue <= (width / 2)) {
                        //Left
                        if (isRtl) {
                            nextStory();
                        } else {
                            previousStory();
                        }
                    } else {
                        //Right
                        if (isRtl) {
                            previousStory();
                        } else {
                            nextStory();
                        }
                    }
                }
            }
        } else {
            stopTimer();
            setHeadingVisibility(View.VISIBLE);
            storiesProgressView.resume();
        }
        elapsedTime = 0;
    }

    public void setStoryClickListeners(StoryClickListeners storyClickListeners) {
        this.storyClickListeners = storyClickListeners;
    }

    public void setOnStoryChangedCallback(OnStoryChangedCallback onStoryChangedCallback) {
        this.onStoryChangedCallback = onStoryChangedCallback;
    }

    public static class Builder {

        private  StoryView storyView;
        private FragmentManager fragmentManager;
        private Bundle bundle;
        private StoryViewHeaderInfo storyViewHeaderInfo;
        private ArrayList<StoryViewHeaderInfo> headingInfoList;
        private StoryClickListeners storyClickListeners;
        private OnStoryChangedCallback onStoryChangedCallback;

        public Builder(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
            this.bundle = new Bundle();
            this.storyViewHeaderInfo = new StoryViewHeaderInfo();
        }

        public StoryView.Builder setStoriesList(ArrayList<MyStory> storiesList) {
            bundle.putSerializable(IMAGES_KEY, storiesList);
            return this;
        }

        public StoryView.Builder setTitleText(String title) {
            storyViewHeaderInfo.setTitle(title);
            return this;
        }

        public StoryView.Builder setSubtitleText(String subtitle) {
            storyViewHeaderInfo.setSubtitle(subtitle);
            return this;
        }

        public StoryView.Builder setTitleLogoUrl(String url) {
            storyViewHeaderInfo.setTitleIconUrl(url);
            return this;
        }

        public StoryView.Builder setStoryDuration(long duration) {
            bundle.putLong(DURATION_KEY, duration);
            return this;
        }
//        public StoryView.Builder setFlag(int i) {
//            bundle.putInt(FLAG, i);
//            return this;
//        }
        public StoryView.Builder setStartingIndex(int index) {
            bundle.putInt(STARTING_INDEX_TAG, index);
            return this;
        }

        public StoryView.Builder build() {
            if (storyView != null) {
                Log.e(TAG, "The StoryView has already been built!");
                return this;
            }
            storyView = new StoryView();
            bundle.putSerializable(HEADER_INFO_KEY, headingInfoList != null ? headingInfoList : storyViewHeaderInfo);
            storyView.setArguments(bundle);
            if (storyClickListeners != null) {
                storyView.setStoryClickListeners(storyClickListeners);
            }
            if (onStoryChangedCallback != null) {
                storyView.setOnStoryChangedCallback(onStoryChangedCallback);
            }
            return this;
        }

        public StoryView.Builder setOnStoryChangedCallback(OnStoryChangedCallback onStoryChangedCallback) {
            this.onStoryChangedCallback = onStoryChangedCallback;
            return this;
        }

        public StoryView.Builder setRtl(boolean isRtl) {
            this.bundle.putBoolean(IS_RTL_TAG, isRtl);
            return this;
        }

        public StoryView.Builder setHeadingInfoList(ArrayList<StoryViewHeaderInfo> headingInfoList) {
            this.headingInfoList = headingInfoList;
            return this;
        }

        public StoryView.Builder setStoryClickListeners(StoryClickListeners storyClickListeners) {
            this.storyClickListeners = storyClickListeners;
            return this;
        }

        public void show() {
            storyView.show(fragmentManager, TAG);
        }

        public void dismiss() {
            storyView.dismiss();
        }

        public Fragment getFragment() {
            return storyView;
        }


    }

}
