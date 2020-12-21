package com.ccube9.gochat.Util;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

public class PreviewImageHelperClass {
    public boolean isVideoOrImg() {
        return isVideoOrImg;
    }

    public void setVideoOrImg(boolean videoOrImg) {
        isVideoOrImg = videoOrImg;
    }

    private boolean isVideoOrImg;
    public String getmStrImgOrVideoURl() {
        return mStrImgOrVideoURl;
    }

    public void setmStrImgOrVideoURl(String mStrImgOrVideoURl) {
        this.mStrImgOrVideoURl = mStrImgOrVideoURl;
    }

    public Uri getmUri() {
        return mUri;
    }

    public void setmUri(Uri mUri) {
        this.mUri = mUri;
    }

    private String mStrImgOrVideoURl;
    private Uri mUri;

    public byte[] getmArrImgByte() {
        return mArrImgByte;
    }

    public void setmArrImgByte(byte[] mArrImgByte) {
        this.mArrImgByte = mArrImgByte;
    }

    byte[] mArrImgByte;

    public ArrayList<PreviewImageHelperClass> getmImgArr() {
        return mImgArr;
    }

    public void setmImgArr(ArrayList<PreviewImageHelperClass> mImgArr) {
        this.mImgArr = mImgArr;
    }

    public String getmImgId() {
        return mImgId;
    }

    public void setmImgId(String mImgId) {
        this.mImgId = mImgId;
    }

    public String getmImgFrom() {
        return mImgFrom;
    }

    public void setmImgFrom(String mImgFrom) {
        this.mImgFrom = mImgFrom;
    }

    public String getmImvByte() {
        return mImvByte;
    }

    public void setmImvByte(String mImvByte) {
        this.mImvByte = mImvByte;
    }

    private ArrayList<PreviewImageHelperClass> mImgArr = new ArrayList<>();
    private String mImgId, mImgFrom;
    private String mImvByte;

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    private Bitmap mBitmap;
}
