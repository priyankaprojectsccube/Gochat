package com.ccube9.gochat.Pot.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.ccube9.gochat.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    String weburl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        if (getIntent().hasExtra("weburl")) {
            weburl = getIntent().getStringExtra("weburl");
        }

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        if(weburl != null && !weburl.isEmpty()){
            webView.loadUrl(weburl);
        }



    }
}