package com.ccube9.gochat.Challenge.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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
import com.ccube9.gochat.Challenge.Adapter.ChallengeImageAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.Validation;
import com.ccube9.gochat.Util.VolleyMultipartRequest;
import com.ccube9.gochat.Util.WebUrl;
import com.ccube9.gochat.Home.HomeActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TrainningtaskaddActivity extends AppCompatActivity {

    private ImageView iv_back,addpicture;
    private TextView texttitle;
    private EditText challengeName_textInputLayout,challengeDesc_textInputLayout,challengeDate_textInputLayout;
    private Button postChallengeButton;
    private RadioGroup publishRadioGroup;
    private RadioButton publishradioButton;
    private EditText challengeLocation_textInputLayout;
    private TransparentProgressDialog pd;
    private ArrayList<Bitmap> imagearrbitmap=new ArrayList();
    private ArrayList<byte[]> imagearr=new ArrayList();
    private Double lat,lng;
    private RecyclerView recviewimg;
    private Bitmap bitmap,resisedbitmap;
    private int REQUEST_IMAGE_CAPTURE = 22;
    private int REQUEST_IMAGE_PICK = 11;
    Validation validation = new Validation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainningtaskadd);

        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        Window window = this.getWindow();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        iv_back = findViewById(R.id.iv_back);
        challengeDate_textInputLayout = findViewById(R.id.challengeDate_textInputLayout);
        challengeName_textInputLayout = findViewById(R.id.challengeName_textInputLayout);
        texttitle = findViewById(R.id.texttitle);
        addpicture = findViewById(R.id.addpicture);

        challengeDesc_textInputLayout= findViewById(R.id.challengeDesc_textInputLayout);
        recviewimg = findViewById(R.id.recviewimg);
        publishRadioGroup = findViewById(R.id.publishRadioGroup);
        postChallengeButton = findViewById(R.id.postChallengeButton);
        challengeLocation_textInputLayout = findViewById(R.id.challengeLocation_textInputLayout);


        texttitle.setText("Add Training");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TrainningtaskaddActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });


        addpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ActivityCompat.checkSelfPermission(TrainningtaskaddActivity.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (validation.neverAskAgainSelected(TrainningtaskaddActivity.this, WRITE_EXTERNAL_STORAGE, "STORAGE")) {
                            validation.displayNeverAskAgainDialog("storage", TrainningtaskaddActivity.this);
                        } else {
                            ActivityCompat.requestPermissions(TrainningtaskaddActivity.this,
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

                DatePickerDialog dp = new DatePickerDialog(TrainningtaskaddActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {



                        TimePickerDialog tp = new TimePickerDialog(TrainningtaskaddActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {


                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                challengeDate_textInputLayout.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth+" "+hourOfDay+":"+minute+" ");

                            }
                        },0,0,false);

                        tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        tp.show();


                    }
                }, Integer.parseInt(mstrDate[0]), Integer.parseInt(mstrDate[1]), Integer.parseInt(mstrDate[2]));
                dp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dp.getDatePicker().setMinDate(System.currentTimeMillis());
                dp.show();

            }
        });


        challengeLocation_textInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(TrainningtaskaddActivity.this);
                startActivityForResult(intent, 33);

            }
        });





        postChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = publishRadioGroup.getCheckedRadioButtonId();
                publishradioButton = (RadioButton) findViewById(selectedId);




                Validation validation = new Validation();

                if (!validation.edttxtvalidation(challengeName_textInputLayout, TrainningtaskaddActivity.this)) {

                } else if (!validation.edttxtvalidation(challengeDesc_textInputLayout, TrainningtaskaddActivity.this)) {

                } else if (!validation.edttxtvalidation(challengeLocation_textInputLayout, TrainningtaskaddActivity.this)) {

                } else if (!validation.edttxtvalidation(challengeDate_textInputLayout, TrainningtaskaddActivity.this)) {

                }
                else if(imagearr.size()<2){
                    Toast.makeText(TrainningtaskaddActivity.this, "Please select atleast two images", Toast.LENGTH_SHORT).show();
                }
                else if(publishradioButton==null){
                    Toast.makeText(TrainningtaskaddActivity.this, "Please select publish to", Toast.LENGTH_SHORT).show();
                }


                else {
                    pd.show();

                    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebUrl.Add_challenge,
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    pd.dismiss();



                                    try {
                                        String result = new String(response.data);
                                        JSONObject jsonObject = new JSONObject(result);
                                        if(jsonObject.getString("status").equals("1")) {
                                            Intent intent = new Intent(TrainningtaskaddActivity.this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                        Toast.makeText(TrainningtaskaddActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(TrainningtaskaddActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TrainningtaskaddActivity.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                            }

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> param = new HashMap<>();

                            param.put("challenge_name", challengeName_textInputLayout.getText().toString());
                            param.put("description", challengeDesc_textInputLayout.getText().toString());
                            param.put("location", challengeLocation_textInputLayout.getText().toString());
                            param.put("lat", String.valueOf(lat));
                            param.put("lang", String.valueOf(lng));
                            param.put("date", challengeDate_textInputLayout.getText().toString());
                            if(publishradioButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.all))){
                                param.put("published_to","1");
                            }
                            else if(publishradioButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.contact))){
                                param.put("published_to","2");
                            }
                            else if(publishradioButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.subscriber))){
                                param.put("published_to","3");
                            }

                            param.put("user_id", PrefManager.getUserId(TrainningtaskaddActivity.this));
                            param.put("challenge_type", "3");
                          //  param.put("language", "1");

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
                    volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                            5000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(TrainningtaskaddActivity.this).addToRequestQueue(volleyMultipartRequest);
                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {

            try {
                String s = String.valueOf(data.getData());
                Log.d("checkpath",s);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                resisedbitmap = getResizedBitmap(bitmap, 300);
                imagearr.add(getFileDataFromDrawable(resisedbitmap));
                imagearrbitmap.add(bitmap);

                recviewimg.setLayoutManager(new GridLayoutManager(TrainningtaskaddActivity.this, 3));
                ChallengeImageAdapter challengeImageAdapter=new ChallengeImageAdapter(imagearrbitmap, TrainningtaskaddActivity.this);
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


        if (requestCode == 33) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);


                challengeLocation_textInputLayout.setText(place.getAddress());
                challengeName_textInputLayout.setCursorVisible(false);
                challengeDesc_textInputLayout.setCursorVisible(false);
                lat= place.getLatLng().latitude;
                lng= place.getLatLng().longitude;

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(TrainningtaskaddActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public static String getCalculatedDate(String dateFormat, int YEARS) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.YEAR, YEARS);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
