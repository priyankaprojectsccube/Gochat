package com.ccube9.gochat.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.android.volley.toolbox.StringRequest;
import com.ccube9.gochat.Album.Activity.AlbumActivity;
import com.ccube9.gochat.Challenge.Activity.ChallangeAcceptedActivity;
import com.ccube9.gochat.Challenge.Activity.ChallangeaddedbymeActivity;
import com.ccube9.gochat.Challenge.Activity.Challengesubcategoryaccepted;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.ccube9.gochat.Util.WebUrl.Base_url;


public class ProfileActivity extends AppCompatActivity {
    private Validation validation=new Validation();
    private int REQUEST_IMAGE_CAPTURE=22;
    private int REQUEST_IMAGE_PICK=11;
    private String currentPhotoPath;
    private Bitmap bitmap,resisedbitmap;
    byte[] dataimage;
    private CircleImageView profile_image;
    private TextView album_textView,verify,email_textView,gender_textView,dob_textView,school_textView,iaman_textView,about_me_textView,profile_name_textView,texttitle,challenged_textView,accepted_textView,challenges_choosen_textView;
    private ImageView edit_aboutMe_imageView,iv_back,edit_iaman_imageView,setting,edit_imageView_profile,edit_wenttoschoolin_imageView;
    private TransparentProgressDialog pd;
    private ConstraintLayout constraintLayout5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customtoolbar);

        profile_image = findViewById(R.id.profile_image);
        gender_textView=findViewById(R.id.gender_textView);
        email_textView=findViewById(R.id.email_textView);
        constraintLayout5 = findViewById(R.id.constraintLayout5);
        verify=findViewById(R.id.verify);
        album_textView=findViewById(R.id.album_textView);
        edit_wenttoschoolin_imageView=findViewById(R.id.edit_wenttoschoolin_imageView);
        edit_iaman_imageView=findViewById(R.id.edit_iaman_imageView);
        challenges_choosen_textView=findViewById(R.id.challenges_choosen_textView);
        dob_textView=findViewById(R.id.dob_textView);
        edit_aboutMe_imageView=findViewById(R.id.edit_aboutMe_imageView);
        texttitle=findViewById(R.id.texttitle);
        edit_imageView_profile=findViewById(R.id.edit_imageView_profile);
        setting=findViewById(R.id.setting);
        iv_back=findViewById(R.id.iv_back);
        challenged_textView=findViewById(R.id.challenged_textView);

        school_textView=findViewById(R.id.school_textView);
        iaman_textView=findViewById(R.id.iaman_textView);
        accepted_textView=findViewById(R.id.accepted_textView);
        about_me_textView=findViewById(R.id.about_me_textView);
        profile_name_textView=findViewById(R.id.profile_name_textView);

//        gender_textView.setText(PrefManager.getGender(ProfileActivity.this));
//        dob_textView.setText(PrefManager.getDateofbirth(ProfileActivity.this));
//        school_textView.setText(PrefManager.getSchool(ProfileActivity.this));
//        iaman_textView.setText(PrefManager.getIaman(ProfileActivity.this));
//        about_me_textView.setText(PrefManager.getAboutme(ProfileActivity.this));

        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);

        if(PrefManager.IsEmailVerified(ProfileActivity.this)){

            verify.setVisibility(View.GONE);
        }



//        String firstname=PrefManager.getFirstName(ProfileActivity.this);
//        firstname = Character.toUpperCase(firstname.charAt(0)) + firstname.substring(1);
//
//        String lastname=PrefManager.getLastName(ProfileActivity.this);
//        lastname = Character.toUpperCase(lastname.charAt(0)) + lastname.substring(1);
//
//        profile_name_textView.setText(firstname.concat(" "+lastname));
//
//        email_textView.setText(PrefManager.getEmailId(ProfileActivity.this));
//        texttitle.setText("My Profile");

        setting.setVisibility(View.VISIBLE);
        getProfileDetail();
        getMailVerify();


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ProfileActivity.this, ChangeSettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ismailverify();
            }
        });

        edit_wenttoschoolin_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(ProfileActivity.this,EditProfileActivity.class);
                intent.putExtra("edittype","editwenttoschoolin");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });


        album_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ProfileActivity.this, AlbumActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        edit_aboutMe_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,EditProfileActivity.class);
                intent.putExtra("edittype","editaboutme");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        edit_imageView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
openchooseoption();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        challenged_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, ChallangeaddedbymeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        accepted_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, ChallangeAcceptedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });



        challenges_choosen_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, Challengesubcategoryaccepted.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });




        edit_iaman_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(ProfileActivity.this,EditProfileActivity.class);
                intent.putExtra("edittype","editiaman");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });


    }

    private void openchooseoption() {
        final Dialog dialog = new Dialog(ProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialogbrand_layout);

        TextView gallary = (TextView) dialog.findViewById(R.id.gallary);
        TextView camera = (TextView)dialog.findViewById(R.id.camera) ;




        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
dialog.dismiss();
                if (ActivityCompat.checkSelfPermission(ProfileActivity.this, CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (validation.neverAskAgainSelected(ProfileActivity.this, CAMERA, "CAMERA")) {
                            validation.displayNeverAskAgainDialog("camera",ProfileActivity.this);
                        }
                        else {

                            ActivityCompat.requestPermissions(ProfileActivity.this,
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
                            Uri photoURI = FileProvider.getUriForFile(ProfileActivity.this,
                                    "com.example.android.fileprovider",photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }

                }
            }
        });




        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
dialog.dismiss();
                if (ActivityCompat.checkSelfPermission(ProfileActivity.this,WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if ( validation.neverAskAgainSelected(ProfileActivity.this, WRITE_EXTERNAL_STORAGE,"STORAGE")) {
                            validation.displayNeverAskAgainDialog("storage",ProfileActivity.this);
                        } else {
                            ActivityCompat.requestPermissions(ProfileActivity.this,
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
        dialog.show();
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
                            Uri photoURI = FileProvider.getUriForFile(ProfileActivity.this,
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
                if(bitmap==null){
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.pleaseselectprofpic), Toast.LENGTH_SHORT).show();
                }
                else {

                    updateProfilepic(bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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


                                Toast.makeText(ProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                if(jsonObject.getString("status").equals("1")) {

                                    JSONArray jsonArray=jsonObject.getJSONArray("user_data");
                                    JSONObject jsonObject1=jsonArray.getJSONObject(0);

                                    getProfileDetail();


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
                            Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(ProfileActivity.this));
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

        MySingleton.getInstance(ProfileActivity.this).addToRequestQueue(volleyMultipartRequest);

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

    private void getProfileDetail() {
        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.get_profile_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();

                Log.d("dfgghfghd",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){


                        JSONArray jsonArray=jsonObject.getJSONArray("user_data");
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);

                        String imageurl = Base_url.concat(jsonObject1.getString("profile_image"));
                        new MyAsyncTask().execute(imageurl);

                       // Picasso.with(ProfileActivity.this).load(Base_url.concat(jsonObject1.getString("profile_image"))).resize(70, 70).centerCrop().error(R.drawable.default_profile).into(profile_image);
                     email_textView.setText(jsonObject1.getString("email_id"));
                     profile_name_textView.setText(jsonObject1.getString("first_name").concat(" "+jsonObject1.getString("last_name")));

                        gender_textView.setText(jsonObject1.getString("gender"));
                        dob_textView.setText(jsonObject1.getString("date_of_birth"));
                        if (!jsonObject1.isNull("school")) {
                            school_textView.setText(jsonObject1.getString("school"));
                        }
                        if (!jsonObject1.isNull("description")) {
                            about_me_textView.setText(jsonObject1.getString("description"));
                        }
                        if (!jsonObject1.isNull("type")) {
                            if(jsonObject1.getString("type").equals("1")){
                                iaman_textView.setText("Adult");
                            }

                            else if(jsonObject1.getString("type").equals("2")){
                                iaman_textView.setText("Student");
                            }

                            else if(jsonObject1.getString("type").equals("3")){
                                iaman_textView.setText("Employee");
                            }

                            else if(jsonObject1.getString("type").equals("4")){
                                iaman_textView.setText("Independent");
                            }
                            else if (jsonObject1.getString("type").equals("5")){
                                iaman_textView.setText("Other");
                            }
                        }








                    }
//                   Toast.makeText(ProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("fdgfgd",volleyError.toString());
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

                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(ProfileActivity.this));


                return param;
            }
        };

        MySingleton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);
    }



    private void ismailverify() {
        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrl.verify_mail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();

                Log.d("dfgghfghd",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){


                        Toast.makeText(ProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("fdgfgd",volleyError.toString());
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

                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(ProfileActivity.this));
            //    param.put("language", "1");

                return param;
            }
        };

        MySingleton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);
    }

    private void getMailVerify() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.ismailverified_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("dfgghfghd", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {

                        if(jsonObject.getString("email_verify_status").equals("0")){

                            verify.setVisibility(View.VISIBLE);
                        }

                        if(jsonObject.getString("email_verify_status").equals("1")){

                            verify.setVisibility(View.GONE);

                        }
                    }
                    //      Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
             //   Log.d("dfgghfghd", volleyError.toString());

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("user_id", PrefManager.getUserId(ProfileActivity.this));
               // param.put("language", "1");

                return param;
            }
        };

        MySingleton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);

    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ProfileActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                return myBitmap;
            } catch(IOException e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            //do what you want with your bitmap result on the UI thread
           profile_image.setImageBitmap(result);
            Drawable dr = new BitmapDrawable(result);
          constraintLayout5.setBackgroundDrawable(dr);
        }

    }
}
