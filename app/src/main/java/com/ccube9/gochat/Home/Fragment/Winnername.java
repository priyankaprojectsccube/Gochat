package com.ccube9.gochat.Home.Fragment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.Profile.FollowerActivity;
import com.ccube9.gochat.R;

public class Winnername extends AppCompatActivity {
    ImageView iv_back;
    TextView texttitle,txtwinnername;
    String strwinnername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winnername);



        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        txtwinnername = findViewById(R.id.txtwinnername);
        texttitle=findViewById(R.id.texttitle);
        iv_back=findViewById(R.id.iv_back);
        texttitle.setText("Winner");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Winnername.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        if (getIntent().hasExtra("winnername")) {
           strwinnername = getIntent().getStringExtra("winnername");
        }

        txtwinnername.setText(strwinnername);


    }


}