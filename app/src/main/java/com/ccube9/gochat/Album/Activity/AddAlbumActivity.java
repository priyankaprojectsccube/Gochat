package com.ccube9.gochat.Album.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ccube9.gochat.Challenge.Adapter.ChallengeImageAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.Validation;
import com.ccube9.gochat.Util.VolleyMultipartRequest;
import com.ccube9.gochat.Util.WebUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddAlbumActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    ImageView addpicture;
    Validation validation=new Validation();
    private int REQUEST_IMAGE_CAPTURE = 22;
    private int REQUEST_IMAGE_PICK = 11;
    private Bitmap bitmap,resisedbitmap;
    private Spinner spinner;
    String strselectedalbumid = "",strselectedalbumname="";
    //private EditText edttxt_title;
    private TransparentProgressDialog pd;
    private Button addalbum;
    private RecyclerView recviewimg;
    private ArrayList<byte[]> imagearr=new ArrayList();
    private ArrayList<Bitmap> imagearrbitmap=new ArrayList();
    String[] albumnames = { "Challenge", "Charity", "Training"};
    TextView texttitle;
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);

        texttitle.setText("Add Photos");
        addpicture=findViewById(R.id.addpicture);
        recviewimg=findViewById(R.id.recviewimg);
        addalbum=findViewById(R.id.addalbum);
        spinner = findViewById(R.id.spinner);
     //   edttxt_title=findViewById(R.id.edttxt_title);

//        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
pd = new TransparentProgressDialog(this,R.drawable.ic_loader_image);

        spinner.setOnItemSelectedListener(this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddAlbumActivity.this, AlbumActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                albumnames);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spinner.setAdapter(ad);
        addalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(imagearr.size() > 0){
    pd.show();
    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebUrl.add_album,
            new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    pd.dismiss();



                    try {
                        String result = new String(response.data);
                        Log.d("add_album", result);
                        JSONObject jsonObject = new JSONObject(result);
                        if(jsonObject.getString("status").equals("1")) {
                            Intent intent = new Intent(AddAlbumActivity.this, AlbumActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        Toast.makeText(AddAlbumActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            pd.dismiss();

            Log.d("Fgdfg",volleyError.toString());
            String message = null;
            if (volleyError instanceof NetworkError) {
                message = getResources().getString(R.string.cannotconnectinternate);
            } else if (volleyError instanceof ServerError) {
                message = getResources().getString(R.string.servernotfound);
            } else if (volleyError instanceof AuthFailureError) {
                message = getResources().getString(R.string.loginagain);
            } else if (volleyError instanceof ParseError) {
                message = getResources().getString(R.string.tryagain);
            } else if (volleyError instanceof NoConnectionError) {
                message = getResources().getString(R.string.cannotconnectinternate);
            } else if (volleyError instanceof TimeoutError) {
                message = getResources().getString(R.string.connectiontimeout);
            }
            if (message != null) {

                Toast.makeText(AddAlbumActivity.this, message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddAlbumActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

            }

        }
    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {



            Map<String, String> param = new HashMap<>();
            param.put("user_id", PrefManager.getUserId(AddAlbumActivity.this));
            param.put("album_name", strselectedalbumname);
            param.put("challenge_type",strselectedalbumid);

            return param;
        }


        @Override
        protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws  AuthFailureError {

            Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
            for (int i=0;i<imagearr.size();i++){
                params.put("image["+i+"]", new VolleyMultipartRequest.DataPart( "name"+i+".png",imagearr.get(i)));

            }

            return params;
        }

    };

    MySingleton.getInstance(AddAlbumActivity.this).addToRequestQueue(volleyMultipartRequest);
}
else{
    Toast.makeText(AddAlbumActivity.this,"Plase Select Image",Toast.LENGTH_SHORT).show();
}




            }
        });

        addpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(AddAlbumActivity.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (validation.neverAskAgainSelected(AddAlbumActivity.this, WRITE_EXTERNAL_STORAGE, "STORAGE")) {
                            validation.displayNeverAskAgainDialog("storage", AddAlbumActivity.this);
                        } else {
                            ActivityCompat.requestPermissions(AddAlbumActivity.this,
                                    new String[]{WRITE_EXTERNAL_STORAGE},
                                    1);
                        }
                    }

                } else {


                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, REQUEST_IMAGE_PICK);
                }

            }
        });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                resisedbitmap = getResizedBitmap(bitmap, 300);
                imagearr.add(getFileDataFromDrawable(resisedbitmap));

                imagearrbitmap.add(bitmap);

                recviewimg.setLayoutManager(new GridLayoutManager(AddAlbumActivity.this, 3));
                ChallengeImageAdapter challengeImageAdapter=new ChallengeImageAdapter(imagearrbitmap, AddAlbumActivity.this);
                recviewimg.setAdapter(challengeImageAdapter);

                challengeImageAdapter.setOnItemClickListener(position ->{

                    imagearr.remove(position);
                    imagearrbitmap.remove(position);
                    recviewimg.getAdapter().notifyDataSetChanged();


                });


            } catch (IOException e) {
                e.printStackTrace();
            }

        }




    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int i) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = i;
            height = (int) (width / bitmapRatio);
        } else {
            height = i;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //1:Challenge 2:Charity 3:training 4: news 5:advertisement

        String getitem = albumnames[i];
  if(getitem.equals("Challenge")){
      strselectedalbumid = "1";
      strselectedalbumname = "Challenge";
  }
  else if (getitem.equals("Charity")){
      strselectedalbumid = "2";
      strselectedalbumname = "Charity";
  }
  else if (getitem.equals("Training")){
      strselectedalbumid = "3";
      strselectedalbumname = "Training";
  }
//  else if (getitem.equals("News")){
//      strselectedalbumid = "4";
//      strselectedalbumname = "News";
//  }
//  else if (getitem.equals("Advertisement")){
//      strselectedalbumid = "5";
//      strselectedalbumname = "Advertisement";
//  }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
