package com.ccube9.gochat.Pot.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.ccube9.gochat.Home.HomeActivity;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Create_Pot_Challenge_2 extends AppCompatActivity implements Spinner.OnItemSelectedListener{
    private RadioGroup publishRadioGroup;
    private RadioButton publishradioButton;
    private Spinner spinner;
    ImageView iv_back,addpicture,addpicture2;
    TextView texttitle;
    Button btn_next;
    String pot_id;
    EditText et_nameofassociation,et_abtpot,et_woe,et_bankaccount;
    private RecyclerView recviewimg2,recviewimg;
    Validation validation = new Validation();
    private int REQUEST_IMAGE_CAPTURE = 22;
    private int REQUEST_IMAGE_PICK = 11;
    private String currentPhotoPath;
    private int REQUEST_IMAGE_CAPTURE2 = 222;
    private int REQUEST_IMAGE_PICK2 = 111;

    private ArrayList<byte[]> imagearr=new ArrayList();
    private ArrayList<byte[]> imagearr2=new ArrayList();
    private ArrayList<Bitmap> imagearrbitmap=new ArrayList();
    private ArrayList<Bitmap> imagearrbitmap2=new ArrayList();
    private Bitmap bitmap,bitmap2,resisedbitmap,resisedbitmap2;
    private TransparentProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__pot__challenge_2);

        if (getIntent().hasExtra("pot_id")) {
            pot_id = getIntent().getStringExtra("pot_id");
        }
        Log.d("pot_id",pot_id) ;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        et_bankaccount = findViewById(R.id.et_bankaccount);
        et_woe = findViewById(R.id.et_woe);
        et_abtpot = findViewById(R.id.et_abtpot);
        btn_next = findViewById(R.id.btn_next);
        recviewimg2 = findViewById(R.id.recviewimg2);
        recviewimg = findViewById(R.id.recviewimg);
        addpicture2 = findViewById(R.id.addpicture2);
        addpicture = findViewById(R.id.addpicture);
        spinner= findViewById(R.id.spinner);
        publishRadioGroup = findViewById(R.id.publishRadioGroup);
        et_nameofassociation = findViewById(R.id.et_nameofassociation);
        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);

        texttitle.setText("Create a Pot Chatlenge");

        spinner.setAdapter(new ArrayAdapter<String>(Create_Pot_Challenge_2.this, android.R.layout.simple_spinner_dropdown_item,    getResources().getStringArray(R.array.selectpot)));

        spinner.setOnItemSelectedListener(this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create_Pot_Challenge_2.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        addpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagearr.size() == 0 ){
                    if (ActivityCompat.checkSelfPermission(Create_Pot_Challenge_2.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (validation.neverAskAgainSelected(Create_Pot_Challenge_2.this, WRITE_EXTERNAL_STORAGE, "STORAGE")) {
                                validation.displayNeverAskAgainDialog("storage", Create_Pot_Challenge_2.this);
                            } else {
                                ActivityCompat.requestPermissions(Create_Pot_Challenge_2.this,
                                        new String[]{WRITE_EXTERNAL_STORAGE},
                                        1);
                            }
                        }

                    } else {


                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, REQUEST_IMAGE_PICK);
                    }
                }else{
                    Toast.makeText(Create_Pot_Challenge_2.this,"You can select one image",Toast.LENGTH_SHORT).show();
                }


            }
        });


        addpicture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagearr2.size() == 0 ){
                    if (ActivityCompat.checkSelfPermission(Create_Pot_Challenge_2.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (validation.neverAskAgainSelected(Create_Pot_Challenge_2.this, WRITE_EXTERNAL_STORAGE, "STORAGE")) {
                                validation.displayNeverAskAgainDialog("storage", Create_Pot_Challenge_2.this);
                            } else {
                                ActivityCompat.requestPermissions(Create_Pot_Challenge_2.this,
                                        new String[]{WRITE_EXTERNAL_STORAGE},
                                        1);
                            }
                        }

                    } else {


                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, REQUEST_IMAGE_PICK2);
                    }
                }else{
                    Toast.makeText(Create_Pot_Challenge_2.this,"You can select one image",Toast.LENGTH_SHORT).show();
                }


            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = publishRadioGroup.getCheckedRadioButtonId();
                publishradioButton = (RadioButton) findViewById(selectedId);


                Validation validation = new Validation();
 if(spinner.getSelectedItem().equals("Select pot to donate")){
                    Toast.makeText(Create_Pot_Challenge_2.this, "Please select pot", Toast.LENGTH_SHORT).show();
                }
 else  if (!validation.edttxtvalidation(et_nameofassociation, Create_Pot_Challenge_2.this)) {

                }
 else if(imagearr2.size()<1){
     Toast.makeText(Create_Pot_Challenge_2.this, "Please select atleast one banner", Toast.LENGTH_SHORT).show();
 }
 else if(imagearr.size()<1){
     Toast.makeText(Create_Pot_Challenge_2.this, "Please select atleast one logo", Toast.LENGTH_SHORT).show();
 }
 else if (!validation.edttxtvalidation(et_abtpot, Create_Pot_Challenge_2.this)) {

                }

 else if (!validation.edttxtvalidation(et_woe, Create_Pot_Challenge_2.this)) {

                }
 else if (!validation.edttxtvalidation(et_bankaccount, Create_Pot_Challenge_2.this)) {

 }
                else if(publishradioButton==null){
                    Toast.makeText(Create_Pot_Challenge_2.this, "Subcribe Newsletter?", Toast.LENGTH_SHORT).show();
                }


                else {
                    callapi();
                }
            }
        });
    }

    private void callapi() {

        pd.show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebUrl.create_pot_association,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        pd.dismiss();



                        try {
                            String result = new String(response.data);
                            Log.d("create_pot_association", result);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("status").equals("1")) {
                                Intent intent = new Intent(Create_Pot_Challenge_2.this,Create_Pot_Challenge_3.class);
                                intent.putExtra("pot_id",pot_id);
                                Log.d("Pot_id",pot_id);
                                startActivity(intent);
//Toast.makeText(Create_Pot_Challenge.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Create_Pot_Challenge_2.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("chatlenge_association", volleyError.toString());
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

                    Toast.makeText(Create_Pot_Challenge_2.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Create_Pot_Challenge_2.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
//                user_id:1
//                pot_id:1
//                donate_for:dcfdss
//                name_association:256
//                banner_for_association:sfsfsgv
//                about_association:ssgrrr
//                website_for_association:sfsvgsv
//                bank_account_association:sgsgv
//                newsletter:sgssg
                param.put("user_id", PrefManager.getUserId(Create_Pot_Challenge_2.this));
                param.put("pot_id",pot_id);
                param.put("donate_for", String.valueOf(spinner.getSelectedItem()));
                param.put("name_association", et_nameofassociation.getText().toString());
                param.put("about_association", et_abtpot.getText().toString());
                param.put("website_for_association",et_woe.getText().toString());
                param.put("bank_account_association", et_bankaccount.getText().toString());
                if(publishradioButton.getText().toString().equalsIgnoreCase("Yes")){
                    param.put("newsletter","Yes");
                    Log.d("newsletter","Yes");
                }
                else if(publishradioButton.getText().toString().equalsIgnoreCase("No")){
                    param.put("newsletter","No");
                    Log.d("newsletter","Yes");
                }





                Log.d("params",PrefManager.getUserId(Create_Pot_Challenge_2.this)+" "+pot_id+" "+ String.valueOf(spinner.getSelectedItem()
                        +" "+et_nameofassociation.getText().toString()+" "+et_abtpot.getText().toString()+" "+et_bankaccount.getText().toString()
                        +" "+et_woe.getText().toString()));
                return param;
            }
            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws  AuthFailureError {

                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                for (int i=0;i<imagearr.size();i++){
                    params.put("image["+i+"]", new VolleyMultipartRequest.DataPart( "logo"+i+".png",imagearr.get(i)));
                    Log.d("imagegoing","logo"+i+".png");
                }

                for (int i=0;i<imagearr2.size();i++){
                    params.put("banner_for_association["+i+"]", new VolleyMultipartRequest.DataPart( "banner"+i+".png",imagearr2.get(i)));
                    Log.d("imagegoing2","banner"+i+".png");
                }
                return params;
            }

        };

        MySingleton.getInstance(Create_Pot_Challenge_2.this).addToRequestQueue(volleyMultipartRequest);
    }


    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                resisedbitmap = getResizedBitmap(bitmap, 300);
                if(imagearr.size() < 2 && imagearr.size() == 0){
                    imagearr.add(getFileDataFromDrawable(resisedbitmap));
                    imagearrbitmap.add(bitmap);

                    recviewimg.setLayoutManager(new GridLayoutManager(Create_Pot_Challenge_2.this, 3));
                    ChallengeImageAdapter challengeImageAdapter=new ChallengeImageAdapter(imagearrbitmap, Create_Pot_Challenge_2.this);
                    recviewimg.setAdapter(challengeImageAdapter);
                    challengeImageAdapter.setOnItemClickListener(position ->{

                        imagearr.remove(position);
                        imagearrbitmap.remove(position);
                        recviewimg.getAdapter().notifyDataSetChanged();


                    });
                }else{
                    Toast.makeText(Create_Pot_Challenge_2.this,"You can select one image",Toast.LENGTH_SHORT).show();
                }





            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        if (requestCode == REQUEST_IMAGE_PICK2 && resultCode == RESULT_OK) {

            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                resisedbitmap2 = getResizedBitmap(bitmap2, 300);
                if(imagearr2.size() < 2 && imagearr2.size() == 0){
                    imagearr2.add(getFileDataFromDrawable(resisedbitmap2));
                    imagearrbitmap2.add(bitmap2);

                    recviewimg2.setLayoutManager(new GridLayoutManager(Create_Pot_Challenge_2.this, 3));
                    ChallengeImageAdapter challengeImageAdapter=new ChallengeImageAdapter(imagearrbitmap2, Create_Pot_Challenge_2.this);
                    recviewimg2.setAdapter(challengeImageAdapter);
                    challengeImageAdapter.setOnItemClickListener(position ->{

                        imagearr2.remove(position);
                        imagearrbitmap2.remove(position);
                        recviewimg2.getAdapter().notifyDataSetChanged();


                    });
                }else{
                    Toast.makeText(Create_Pot_Challenge_2.this,"You can select one image",Toast.LENGTH_SHORT).show();
                }





            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
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
    public void onBackPressed() {



    }


    @Override
    public void onRequestPermissionsResult(int RC, @NonNull String per[], @NonNull int[] PResult) {


        switch (RC) {

            case 1:

                if(REQUEST_IMAGE_PICK == 11) {
                    if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {


                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, REQUEST_IMAGE_PICK);


                    } else {

                        validation.setShouldShowStatus(this, WRITE_EXTERNAL_STORAGE, "STORAGE");

                    }
                }else{


                    if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {


                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, REQUEST_IMAGE_PICK2);


                    } else {

                        validation.setShouldShowStatus(this, WRITE_EXTERNAL_STORAGE, "STORAGE");

                    }
                }
                break;


            case 2:
                if(REQUEST_IMAGE_PICK == 11) {
                    if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                            File photoFile = null;
                            try {
                                photoFile = createImageFile();

                            } catch (IOException ex) {
                            }

                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(Create_Pot_Challenge_2.this,
                                        "com.example.android.fileprovider", photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        }

                    } else {

                        validation.setShouldShowStatus(this, CAMERA, "CAMERA");

                    }
                }

                else{

                    if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                            File photoFile = null;
                            try {
                                photoFile = createImageFile();

                            } catch (IOException ex) {
                            }

                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(Create_Pot_Challenge_2.this,
                                        "com.example.android.fileprovider", photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE2);
                            }
                        }

                    } else {

                        validation.setShouldShowStatus(this, CAMERA, "CAMERA");

                    }
                }
                break;

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}