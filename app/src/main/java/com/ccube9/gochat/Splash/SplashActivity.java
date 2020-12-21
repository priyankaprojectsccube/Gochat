package com.ccube9.gochat.Splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ccube9.gochat.Challenge.Activity.ChallengeCategoryActivity;
import com.ccube9.gochat.Profile.PersonalDetailActivity;
import com.ccube9.gochat.Profile.ProfilePicActivity;
import com.ccube9.gochat.Util.MyFirebaseMessagingService;
import com.ccube9.gochat.Util.NetworkConnection;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Login.SelectActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.jaeger.library.StatusBarUtil;

public class SplashActivity extends AppCompatActivity {

    private int SPLASH_TIME_OUT=6000;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        StatusBarUtil.setTransparent(SplashActivity.this);

        if (new NetworkConnection().checkInternetConnection(SplashActivity.this)){

            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    token =  task.getResult().getToken();
                    Log.e("gettoken",token);
                    PrefManager.setFCMToken(SplashActivity.this,token);
                }
            });
        }else{
            Toast.makeText(SplashActivity.this,"Check Internet Connectivity",Toast.LENGTH_LONG).show();
        }
        if (token == null){
            startService(new Intent(SplashActivity.this, MyFirebaseMessagingService.class));
        } else{
            Log.e("Splash Activity", "Token: " + token);
        }

              new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                 if(PrefManager.IsLogin(SplashActivity.this)){


                     if(!PrefManager.IsProfilepicUpdate(SplashActivity.this) && !PrefManager.IsProfilePicskip(SplashActivity.this)){

                         Intent  intent = new Intent(SplashActivity.this,ProfilePicActivity.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);


                     }
                     else if(!PrefManager.IsProfileUpdate(SplashActivity.this)){
                         Intent  intent=new Intent(SplashActivity.this,PersonalDetailActivity.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);

                     }

                     else if(!PrefManager.IsChalangeUpdate(SplashActivity.this)){
                         Intent  intent=new Intent(SplashActivity.this, ChallengeCategoryActivity.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);

                     }
                     else {

                         Intent  intent=new Intent(SplashActivity.this,HomeActivity.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);

                     }

                  }

                    else {

                     Intent intent=new Intent(SplashActivity.this, SelectActivity.class);
                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     startActivity(intent);

                    }

            }

        }, SPLASH_TIME_OUT);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();

    }
}
