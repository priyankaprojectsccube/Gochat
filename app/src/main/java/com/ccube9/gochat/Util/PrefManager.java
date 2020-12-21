package com.ccube9.gochat.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private static String IS_LOGIN = "login";
    private static String IS_PROFILEPIC_UPDATE="is_profilepic_update";
    private static String IS_PROFILE_UPDATE = "is_profile_update";
    private static String IS_CHALANGE_UPDATE = "is_challange_update";
    private static String DATE_OF_BIRTH="date_of_birth";
    private static String USER_ID="user_id";
    private static String EMAIL_ID="email_id";
    private static String MOBILE="mobile";
    private static String IAMAN="iaman";
    private static String PUSHNOTIFICATION="pushnotification";
    private static String MESSAGENOTIFICATION="messagenotification";
    private static String SYNCCONTACT="synccontact";
    private static String ABOUTME="aboutme";
    private static String FIRST_NAME="first_name";
    private static String LAST_NAME="last_name";
    private static String SCHOOL="school";
    private static String GENDER="gender";
    private static String IS_MOBILE_VERIFIED="is_mobile_verified";
    private static String IS_EMAIL_VERIFIED="is_email_verified";
    private static String IS_PROFILEPIC_SKIP="is_prrofilepic_skip";
    private static String PROFILEIMAGE="profileimage";
    private static String FCM_TOKEN = "fcmtoken";



    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE);
    }

    public static boolean IsLogin(Context context) {
        return getSharedPreferences(context).getBoolean(IS_LOGIN , false);
    }

    public static void LogOut(Context context)
    {
        getSharedPreferences(context).edit().clear().commit();
    }

    public static void setIsLogin(Context context, boolean newValue) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(IS_LOGIN , newValue);
        editor.commit();
    }


    public static boolean IsProfileUpdate(Context context) {
        return getSharedPreferences(context).getBoolean(IS_PROFILE_UPDATE , false);
    }

    public static void setIsProfileUpdate(Context context, boolean newValue) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(IS_PROFILE_UPDATE , newValue);
        editor.commit();
    }



    public static boolean IsMobileVerified(Context context) {
        return getSharedPreferences(context).getBoolean(IS_MOBILE_VERIFIED , false);
    }

    public static void setIsMobileVerified(Context context, boolean newValue) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(IS_MOBILE_VERIFIED , newValue);
        editor.commit();
    }


    public static boolean IsProfilePicskip(Context context) {
        return getSharedPreferences(context).getBoolean(IS_PROFILEPIC_SKIP , false);
    }

    public static void setIsProfilePicskip(Context context, boolean newValue) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(IS_PROFILEPIC_SKIP , newValue);
        editor.commit();
    }

    public static boolean IsEmailVerified(Context context) {
        return getSharedPreferences(context).getBoolean(IS_EMAIL_VERIFIED , false);
    }

    public static void setIsEmailVerified(Context context, boolean newValue) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(IS_EMAIL_VERIFIED , newValue);
        editor.commit();
    }

    public static boolean IsProfilepicUpdate(Context context) {
        return getSharedPreferences(context).getBoolean(IS_PROFILEPIC_UPDATE , false);
    }

    public static void setIsProfilepicUpdate(Context context, boolean newValue) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(IS_PROFILEPIC_UPDATE , newValue);
        editor.commit();
    }



    public static boolean IsChalangeUpdate(Context context) {
        return getSharedPreferences(context).getBoolean(IS_CHALANGE_UPDATE , false);
    }

    public static void setIsChalangeUpdate(Context context, boolean newValue) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(IS_CHALANGE_UPDATE , newValue);
        editor.commit();
    }



    public static void setUserId(Context context, String user_id) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_ID , user_id);
        editor.commit();
    }

    public static String getUserId(Context context) {
        return getSharedPreferences(context).getString(USER_ID , "");
    }


    public static void setFCMToken(Context context,String token) {
        // this.token = token;
        SharedPreferences.Editor edit = getSharedPreferences(context).edit();
        edit.putString(FCM_TOKEN, token);
        edit.commit();
    }

    public static String getFCM_TOKEN(Context context) {
        return getSharedPreferences(context).getString(FCM_TOKEN, "");
    }

    public static void setIaman(Context context, String iaman) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(IAMAN , iaman);
        editor.commit();
    }

    public static String getIaman(Context context) {
        return getSharedPreferences(context).getString(IAMAN , "");
    }


    public static void setAboutme(Context context, String aboutme) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(ABOUTME , aboutme);
        editor.commit();
    }


    public static String getPushnotification(Context context) {
        return getSharedPreferences(context).getString(PUSHNOTIFICATION , "");
    }


    public static void setPushnotification(Context context, String pushnotification) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PUSHNOTIFICATION , pushnotification);
        editor.commit();
    }


    public static String getMessagenotification(Context context) {
        return getSharedPreferences(context).getString(MESSAGENOTIFICATION , "");
    }


    public static void setMessagenotification(Context context, String messagenotification) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(MESSAGENOTIFICATION , messagenotification);
        editor.commit();
    }


    public static String getSynccontact(Context context) {
        return getSharedPreferences(context).getString(SYNCCONTACT , "");
    }


    public static void setSynccontact(Context context, String synccontact) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SYNCCONTACT , synccontact);
        editor.commit();
    }


    public static String getAboutme(Context context) {
        return getSharedPreferences(context).getString(ABOUTME , "");
    }



    public static void setGender(Context context, String gender) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(GENDER , gender);
        editor.commit();
    }

    public static String getGender(Context context) {
        return getSharedPreferences(context).getString(GENDER , "");
    }

    public static void setProfileImage(Context context, String profimage) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PROFILEIMAGE , profimage);
        editor.commit();
    }

    public static String getProfileImage(Context context) {
        return getSharedPreferences(context).getString(PROFILEIMAGE , "");
    }


    public static void setSchool(Context context, String school) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SCHOOL , school);
        editor.commit();
    }

    public static String getSchool(Context context) {
        return getSharedPreferences(context).getString(SCHOOL , "");
    }

    public static void setFirstName(Context context,String firstName) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(FIRST_NAME , firstName);
        editor.commit();
    }

    public static String getFirstName(Context context) {
        return getSharedPreferences(context).getString(FIRST_NAME , "");
    }

    public static void setDateofbirth(Context context,String dateofbirth) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(DATE_OF_BIRTH , dateofbirth);
        editor.commit();
    }

    public static String getDateofbirth(Context context) {
        return getSharedPreferences(context).getString(DATE_OF_BIRTH , "");
    }






    public static void setLastName(Context context,String last_name) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(LAST_NAME , last_name);
        editor.commit();

    }

    public static String getLastName(Context context) {
        return getSharedPreferences(context).getString(LAST_NAME , "");
    }



    public static void setEmailId(Context context,String email_id) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(EMAIL_ID , email_id);
        editor.commit();

    }

    public static String getEmailId(Context context) {
        return getSharedPreferences(context).getString(EMAIL_ID , "");
    }

    public static void setMobile(Context context,String mob_no) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(MOBILE , mob_no);
        editor.commit();

    }

    public static String getMobile(Context context) {
        return getSharedPreferences(context).getString(MOBILE , "");
    }




}
