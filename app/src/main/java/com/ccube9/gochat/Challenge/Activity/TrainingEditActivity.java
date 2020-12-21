package com.ccube9.gochat.Challenge.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.ccube9.gochat.Challenge.Adapter.ChallengeImageAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.POJO;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.Validation;
import com.ccube9.gochat.Util.VolleyMultipartRequest;
import com.ccube9.gochat.Util.WebUrl;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.rd.PageIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.ccube9.gochat.Util.WebUrl.Base_url;

public class TrainingEditActivity extends AppCompatActivity {

    ChallengeImageAdapter challengeImageAdapter;
    private TextView texttitle;

    private RecyclerView recviewimg;
    private RadioGroup publishRadioGroup;
    private RadioButton publishradioButton, all_radioButton, contact_radioButton, subscriber_radioButton;
    private EditText challengeName_textInputLayout, challengeDate_textInputLayout, challengeDesc_textInputLayout, challengeLocation_textInputLayout;
    private ImageView iv_back, addpicture;
    private Button postChallengeButton;
    private TransparentProgressDialog pd;
    private int REQUEST_IMAGE_CAPTURE = 22;
    private int REQUEST_IMAGE_PICK = 11;
    private String currentPhotoPath, mainchallengeid;
    private ArrayList<byte[]> imagearr = new ArrayList();
    private ArrayList imagearrbitmap = new ArrayList();
    private Bitmap bitmap,resisedbitmap;
    private PageIndicatorView indicator;
    private Double lat, lng;
    int checkarraysize, checkbitmapsize;

    Validation validation = new Validation();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainningtaskadd);
        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorofwhite)));

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);


        Window window = this.getWindow();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);

        texttitle = findViewById(R.id.texttitle);

        all_radioButton = findViewById(R.id.all_radioButton);
        contact_radioButton = findViewById(R.id.contact_radioButton);
        subscriber_radioButton = findViewById(R.id.subscriber_radioButton);
        indicator = findViewById(R.id.indicator);
        challengeName_textInputLayout = findViewById(R.id.challengeName_textInputLayout);
        challengeDate_textInputLayout = findViewById(R.id.challengeDate_textInputLayout);
        challengeDesc_textInputLayout = findViewById(R.id.challengeDesc_textInputLayout);
        publishRadioGroup = findViewById(R.id.publishRadioGroup);
        recviewimg = findViewById(R.id.recviewimg);
        recviewimg.setLayoutManager(new GridLayoutManager(TrainingEditActivity.this, 3));
        challengeImageAdapter = new ChallengeImageAdapter(imagearrbitmap, TrainingEditActivity.this);
        challengeLocation_textInputLayout = findViewById(R.id.challengeLocation_textInputLayout);
        addpicture = findViewById(R.id.addpicture);

        iv_back = findViewById(R.id.iv_back);
        postChallengeButton = findViewById(R.id.postChallengeButton);

        texttitle.setText("Edit Challenge");



        if (getIntent().hasExtra("challengedetails")) {

            POJO pojo = (POJO) getIntent().getSerializableExtra("challengedetails");


            checkarraysize = pojo.getImages().size();
            for (int i = 0; i < checkarraysize; i++) {
                String imageurl = Base_url.concat(pojo.getImages().get(i));
                new TrainingEditActivity.MyAsyncTask().execute(imageurl);

//                try {
//                    Uri uri = Uri.parse(Base_url.concat(pojo.getImages().get(i)));
//                       // Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
//
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                        ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), uri);
//                        @SuppressLint("WrongThread")
//                        Bitmap bitmap2 = ImageDecoder.decodeBitmap(source);
//                        imagearrbitmap.add(bitmap2);
//                    }
//                    else{
//
//
//                    }
//
//                } catch (IOException e) {
//                    Log.e("bitmap error", String.valueOf(e));
//                    e.printStackTrace();
//                }
            }


            mainchallengeid = pojo.getMainchallengeid();
            challengeName_textInputLayout.setText(pojo.getTitle());
            challengeDesc_textInputLayout.setText(pojo.getDescription());
            challengeLocation_textInputLayout.setText(pojo.getLocation());
            challengeDate_textInputLayout.setText(pojo.getDate());

            lat = Double.valueOf(pojo.getLatitude());
            lng = Double.valueOf(pojo.getLongitude());

            if (pojo.getPublishto().equals("1")) {
                all_radioButton.setChecked(true);
            } else if (pojo.getPublishto().equals("2")) {
                contact_radioButton.setChecked(true);
            } else if (pojo.getPublishto().equals("3")) {
                subscriber_radioButton.setChecked(true);
            }





            challengeImageAdapter.setOnItemClickListener(position -> {
                String  imgidstr = pojo.getImagesId().get(position);

                delimgfrmdb(imgidstr,position);

//                imagearrbitmap.remove(position);
//                recviewimg.getAdapter().notifyDataSetChanged();


            });




        }


        postChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = publishRadioGroup.getCheckedRadioButtonId();
                publishradioButton = (RadioButton) findViewById(selectedId);


                Validation validation = new Validation();

                if (!validation.edttxtvalidation(challengeName_textInputLayout, TrainingEditActivity.this)) {

                } else if (!validation.edttxtvalidation(challengeDesc_textInputLayout, TrainingEditActivity.this)) {

                } else if (!validation.edttxtvalidation(challengeLocation_textInputLayout, TrainingEditActivity.this)) {

                } else if (!validation.edttxtvalidation(challengeDate_textInputLayout, TrainingEditActivity.this)) {

                } else if (imagearrbitmap.size() < 2) {
                    //imagearr
                    Toast.makeText(TrainingEditActivity.this, "Please select atleast two images", Toast.LENGTH_SHORT).show();
                } else if (publishradioButton == null) {
                    Toast.makeText(TrainingEditActivity.this, "Please select publish to", Toast.LENGTH_SHORT).show();
                }

                else {
                    pd.show();


                    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebUrl.Update_challenge,
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    pd.dismiss();


                                    try {
                                        String result = new String(response.data);
                                        JSONObject jsonObject = new JSONObject(result);
                                        if (jsonObject.getString("status").equals("1")) {
                                            // finish();

                                            Intent intent=new Intent(TrainingEditActivity.this, ChallangeaddedbymeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                        Toast.makeText(TrainingEditActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
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

                                Toast.makeText(TrainingEditActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TrainingEditActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                            }

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> param = new HashMap<>();

                            param.put("main_challenge_id", mainchallengeid);
                            param.put("user_id", PrefManager.getUserId(TrainingEditActivity.this));
                            param.put("challenge_type", "3");  //typeforchallenge
                            param.put("challenge_name", challengeName_textInputLayout.getText().toString());
                            param.put("description", challengeDesc_textInputLayout.getText().toString());
                            param.put("location", challengeLocation_textInputLayout.getText().toString());
                            param.put("lat", String.valueOf(lat));
                            param.put("lang", String.valueOf(lng));
                            param.put("date", challengeDate_textInputLayout.getText().toString());

                            if (publishradioButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.all))) {
                                param.put("published_to", "1");
                            } else if (publishradioButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.contact))) {
                                param.put("published_to", "2");
                            } else if (publishradioButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.subscriber))) {
                                param.put("published_to", "3");
                            }
                        //    param.put("challenges_for", String.valueOf(challengeFor_textInputLayout.getSelectedItem()));
                        //    param.put("language", "1");

                            return param;
                        }


                        @Override
                        protected Map<String, DataPart> getByteData() throws AuthFailureError {

                            Map<String, DataPart> params = new HashMap<>();
                            for (int i = 0; i < imagearr.size(); i++) {
                                params.put("image[" + i + "]", new DataPart("name" + i + ".png", imagearr.get(i)));

                            }

                            return params;
                        }

                    };
                    volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                            5000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(TrainingEditActivity.this).addToRequestQueue(volleyMultipartRequest);
                }

            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        challengeLocation_textInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(TrainingEditActivity.this);
                startActivityForResult(intent, 33);

            }
        });


        addpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ActivityCompat.checkSelfPermission(TrainingEditActivity.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (validation.neverAskAgainSelected(TrainingEditActivity.this, WRITE_EXTERNAL_STORAGE, "STORAGE")) {
                            validation.displayNeverAskAgainDialog("storage", TrainingEditActivity.this);
                        } else {
                            ActivityCompat.requestPermissions(TrainingEditActivity.this,
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


        challengeDate_textInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mPreviousYear = getCalculatedDate("yyyy-MM-dd", -20);

                Calendar calendar = Calendar.getInstance();
                String[] mstrDate = mPreviousYear.split("-");
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dp = new DatePickerDialog(TrainingEditActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                        TimePickerDialog tp = new TimePickerDialog(TrainingEditActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {


                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                challengeDate_textInputLayout.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " " + hourOfDay + ":" + minute + " ");

                            }
                        }, 0, 0, false);

                        tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        tp.show();


                    }
                }, Integer.parseInt(mstrDate[0]), Integer.parseInt(mstrDate[1]), Integer.parseInt(mstrDate[2]));
                dp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dp.getDatePicker().setMinDate(System.currentTimeMillis());
                dp.show();

            }
        });


    }

    private void delimgfrmdb(String imgidstr, int position) {

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.deletechallengeimage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("dfgghfghd", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        imagearrbitmap.remove(position);
                        recviewimg.getAdapter().notifyDataSetChanged();
                    }
                    //Toast.makeText(ChallengeEditActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("fdgfgd", volleyError.toString());
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
                    message =getResources().getString(R.string.connectiontimeout);
                }
                if (message != null) {

                    Toast.makeText(TrainingEditActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TrainingEditActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("image_id", imgidstr);
                param.put("user_id", PrefManager.getUserId(TrainingEditActivity.this));


                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(TrainingEditActivity.this).addToRequestQueue(stringRequest);

    }



    public static String getCalculatedDate(String dateFormat, int YEARS) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.YEAR, YEARS);
        return s.format(new Date(cal.getTimeInMillis()));
    }


    @Override
    public void onRequestPermissionsResult(int RC, @NonNull String per[], @NonNull int[] PResult) {


        switch (RC) {

            case 1:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {


                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, REQUEST_IMAGE_PICK);


                } else {

                    validation.setShouldShowStatus(this, WRITE_EXTERNAL_STORAGE, "STORAGE");

                }
                break;


            case 2:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                        File photoFile = null;
                        try {
                            photoFile = createImageFile();

                        } catch (IOException ex) {
                        }

                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(TrainingEditActivity.this,
                                    "com.example.android.fileprovider", photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }

                } else {

                    validation.setShouldShowStatus(this, CAMERA, "CAMERA");

                }
                break;

        }


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
                imagearr.add(getFileDataFromDrawable(resisedbitmap));

                imagearrbitmap.add(bitmap);

                recviewimg.setLayoutManager(new GridLayoutManager(TrainingEditActivity.this, 3));
                ChallengeImageAdapter challengeImageAdapter = new ChallengeImageAdapter(imagearrbitmap, TrainingEditActivity.this);
                recviewimg.setAdapter(challengeImageAdapter);

                challengeImageAdapter.setOnItemClickListener(position -> {

                    imagearr.remove(position);
                    imagearrbitmap.remove(position);
                    recviewimg.getAdapter().notifyDataSetChanged();


                });


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        if (requestCode == 33) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);


                challengeLocation_textInputLayout.setText(place.getAddress());

                lat = place.getLatLng().latitude;
                lng = place.getLatLng().longitude;

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {
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
    public void onBackPressed() {
        finish();
    }

//    @Override
//    public void OnItemClick(int position) {
//                imagearr.remove(position);
//                imagearrbitmap.remove(position);
//                recviewimg.getAdapter().notifyDataSetChanged();
//    }

    private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                imagearrbitmap.add(myBitmap);
                return myBitmap;
            } catch(IOException e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            //do what you want with your bitmap result on the UI thread
            checkbitmapsize = imagearrbitmap.size();
            if (checkbitmapsize == checkarraysize) {


                recviewimg.setAdapter(challengeImageAdapter);
            }
        }

    }

}
