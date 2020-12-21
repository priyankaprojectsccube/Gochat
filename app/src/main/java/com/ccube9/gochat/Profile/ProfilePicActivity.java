package com.ccube9.gochat.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.ccube9.gochat.Challenge.Activity.ChallengeCategoryActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.Validation;
import com.ccube9.gochat.Util.VolleyMultipartRequest;
import com.ccube9.gochat.Util.WebUrl;
import com.ccube9.gochat.Home.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;



public class ProfilePicActivity extends AppCompatActivity {

    private TextView skip,gallary,camera;
    private Button btnnext;
    private int REQUEST_IMAGE_CAPTURE=22;
    private int REQUEST_IMAGE_PICK=11;
    private Validation validation=new Validation();
    private ImageView profile_image;
    private String currentPhotoPath;
    private TransparentProgressDialog pd;
    private Bitmap bitmap,resisedbitmap;
    byte[] dataimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pic);

        skip=findViewById(R.id.skip);
        btnnext=findViewById(R.id.btnnext);
        gallary=findViewById(R.id.gallary);
        camera=findViewById(R.id.camera);


        profile_image=findViewById(R.id.profile_image);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);




        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.setIsProfilePicskip(ProfilePicActivity.this,true);

                if(!PrefManager.IsProfileUpdate(ProfilePicActivity.this)){
                    Intent  intent=new Intent(ProfilePicActivity.this,PersonalDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
                else if(!PrefManager.IsChalangeUpdate(ProfilePicActivity.this)){
                    Intent  intent=new Intent(ProfilePicActivity.this, ChallengeCategoryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
                else {
                    Intent  intent=new Intent(ProfilePicActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });


        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bitmap==null){
                    Toast.makeText(ProfilePicActivity.this, getResources().getString(R.string.pleaseselectprofpic), Toast.LENGTH_SHORT).show();
                }
                else {

                    updateProfilepic(bitmap);
                }


            }
        });

        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (ActivityCompat.checkSelfPermission(ProfilePicActivity.this,WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if ( validation.neverAskAgainSelected(ProfilePicActivity.this, WRITE_EXTERNAL_STORAGE,"STORAGE")) {
                             validation.displayNeverAskAgainDialog("storage",ProfilePicActivity.this);
                        } else {
                            ActivityCompat.requestPermissions(ProfilePicActivity.this,
                                    new String[]{ WRITE_EXTERNAL_STORAGE},
                                    1);
                        }
                    }

                } else {


                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, REQUEST_IMAGE_PICK);
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(ProfilePicActivity.this, CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (validation.neverAskAgainSelected(ProfilePicActivity.this, CAMERA, "CAMERA")) {
                            validation.displayNeverAskAgainDialog("camera",ProfilePicActivity.this);
                        }
                        else {

                            ActivityCompat.requestPermissions(ProfilePicActivity.this,
                                    new String[]{CAMERA},
                                    2);


                        }
                    }

                }
                else {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                        File photoFile = null;
                        try {
                            photoFile = createImageFile();

                        } catch (IOException ex) {
                        }

                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(ProfilePicActivity.this,
                                    "com.example.android.fileprovider",photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }

                }

            }
        });

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
    public void onRequestPermissionsResult(int RC, @NonNull String per[], @NonNull int[] PResult) {


        switch (RC) {

            case 1:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED ) {


                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, REQUEST_IMAGE_PICK);


                } else {

                validation.setShouldShowStatus(this, WRITE_EXTERNAL_STORAGE,"STORAGE");

                }
                break;


            case 2:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED ) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                        File photoFile = null;
                        try {
                            photoFile = createImageFile();

                        } catch (IOException ex) {
                        }

                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(ProfilePicActivity.this,
                                    "com.example.android.fileprovider",photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }

                } else {

                validation.setShouldShowStatus(this, CAMERA,"CAMERA");

                }
                break;

        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
               bitmap=validation.setPic(currentPhotoPath);
               profile_image.setImageBitmap(bitmap);
        }

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                resisedbitmap = getResizedBitmap(bitmap, 300);
                dataimage = getFileDataFromDrawable(resisedbitmap);
                profile_image.setImageBitmap(bitmap);

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

    private void updateProfilepic(final Bitmap bitmap){


       pd.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebUrl.Update_profilepic,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        pd.dismiss();

                        Log.d("dsadd", String.valueOf(response.statusCode));
                        if (response.statusCode == 200) {
                            profile_image.setImageBitmap(bitmap);
                            String result = new String(response.data);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Log.d("dfsfdsf", String.valueOf(jsonObject));


                                Toast.makeText(ProfilePicActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                if(jsonObject.getString("status").equals("1")) {

                                    JSONArray jsonArray=jsonObject.getJSONArray("user_data");
                                    JSONObject jsonObject1=jsonArray.getJSONObject(0);


                                    PrefManager.setEmailId(ProfilePicActivity.this,jsonObject1.getString("email_id"));
                                    PrefManager.setMobile(ProfilePicActivity.this,jsonObject1.getString("mobile_number"));
                                    PrefManager.setFirstName(ProfilePicActivity.this,jsonObject1.getString("first_name"));
                                    PrefManager.setLastName(ProfilePicActivity.this,jsonObject1.getString("last_name"));
                                    PrefManager.setIaman(ProfilePicActivity.this,jsonObject1.getString("type"));
                                    PrefManager.setAboutme(ProfilePicActivity.this,jsonObject1.getString("description"));
                                    PrefManager.setDateofbirth(ProfilePicActivity.this,jsonObject1.getString("date_of_birth"));
                                    PrefManager.setSchool(ProfilePicActivity.this,jsonObject1.getString("school"));
                                    PrefManager.setGender(ProfilePicActivity.this,jsonObject1.getString("gender"));
                                    PrefManager.setProfileImage(ProfilePicActivity.this,jsonObject1.getString("profile_image"));

                                    if (!jsonObject1.isNull("push_notification")) {
                                        PrefManager.setPushnotification(ProfilePicActivity.this, jsonObject1.getString("push_notification"));
                                    }
                                    if (!jsonObject1.isNull("msg_notification")) {
                                        PrefManager.setMessagenotification(ProfilePicActivity.this, jsonObject1.getString("msg_notification"));
                                    }
                                          if (!jsonObject1.isNull("sync_contacts")) {
                                        PrefManager.setSynccontact(ProfilePicActivity.this, jsonObject1.getString("sync_contacts"));
                                    }


                                    if(jsonObject1.getString("pic_flag").equals("0")){
                                        PrefManager.setIsProfilepicUpdate(ProfilePicActivity.this,false);
                                    }
                                    else if(jsonObject1.getString("pic_flag").equals("1")){
                                    }
                                    PrefManager.setIsProfilepicUpdate(ProfilePicActivity.this,true);

                                    if(jsonObject1.getString("mobile_veri_token").equals("")){
                                        PrefManager.setIsMobileVerified(ProfilePicActivity.this,false);
                                    }
                                    else if(jsonObject1.getString("mobile_veri_token").equals("1")){
                                        PrefManager.setIsMobileVerified(ProfilePicActivity.this,true);
                                    }

                                    if(jsonObject1.getString("email_veri_token")!=null){
                                        if(jsonObject1.getString("email_veri_token").equals("")){
                                            PrefManager.setIsEmailVerified(ProfilePicActivity.this,false);
                                        }
                                        else if(jsonObject1.getString("email_veri_token").equals("1")){
                                            PrefManager.setIsEmailVerified(ProfilePicActivity.this,true);
                                        }
                                    }
                                    else{
                                        PrefManager.setIsEmailVerified(ProfilePicActivity.this,false);
                                    }


                                    if(jsonObject1.getString("flag").equals("0")) {
                                        PrefManager.setIsProfileUpdate(ProfilePicActivity.this,false);
                                    }
                                    else if(jsonObject1.getString("flag").equals("1")) {
                                        PrefManager.setIsProfileUpdate(ProfilePicActivity.this,true);
                                    }

                                    if(jsonObject1.getString("category_flag").equals("0")){
                                        PrefManager.setIsChalangeUpdate(ProfilePicActivity.this,false);
                                    }
                                    else if(jsonObject1.getString("category_flag").equals("1")){
                                        PrefManager.setIsChalangeUpdate(ProfilePicActivity.this,true);
                                    }



                                    if(jsonObject1.getString("flag").equals("0")){
                                        Intent  intent=new Intent(ProfilePicActivity.this,PersonalDetailActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }
                                    else if(jsonObject1.getString("category_flag").equals("0")){
                                        Intent  intent=new Intent(ProfilePicActivity.this, ChallengeCategoryActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }
                                    else {
                                        Intent  intent=new Intent(ProfilePicActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
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
                            Toast.makeText(ProfilePicActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfilePicActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(ProfilePicActivity.this));
           //     param.put("language", "0");
                return param;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws  AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                params.put("profile_image", new VolleyMultipartRequest.DataPart( ".png",dataimage));//getFileDataFromDrawable(bitmap)
                return params;
            }
        };

        MySingleton.getInstance(ProfilePicActivity.this).addToRequestQueue(volleyMultipartRequest);

    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
