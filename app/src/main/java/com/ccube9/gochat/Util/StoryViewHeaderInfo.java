package com.ccube9.gochat.Util;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class StoryViewHeaderInfo implements Serializable {

    @Nullable
    private String title;

    @Nullable
    private String subtitle;

    @Nullable
    private String titleIconUrl;


    public StoryViewHeaderInfo() {
    }




    @Nullable
    public String getTitle() {
        return title;
    }


    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;
    }

    @Nullable
    public String getTitleIconUrl() {
        return titleIconUrl;
    }

    public void setTitleIconUrl(@Nullable String titleIconUrl) {
        this.titleIconUrl = titleIconUrl;
    }


}

