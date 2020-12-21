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

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.ccube9.gochat.Challenge.Activity.ChallengetaskaddActivity;
import com.ccube9.gochat.Challenge.Adapter.ChallengeImageAdapter;
import com.ccube9.gochat.Home.HomeActivity;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.MySingleton;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.Validation;
import com.ccube9.gochat.Util.VolleyMultipartRequest;
import com.ccube9.gochat.Util.WebUrl;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Pot_Challenge_Association extends AppCompatActivity implements Spinner.OnItemSelectedListener{
    private RadioGroup publishRadioGroup;
    private RadioButton publishradioButton;
    private Spinner spinner;
    ImageView iv_back,addpicture;
    TextView texttitle;
    Button btn_next;
    EditText et_entertitle,et_shortdec,et_abtpot,dobname_editText,et_woe;
    private String currentPhotoPath;
    private RecyclerView recviewimg;
    Validation validation = new Validation();
    private int REQUEST_IMAGE_CAPTURE = 22;
    private int REQUEST_IMAGE_PICK = 11;
    private ArrayList<byte[]> imagearr=new ArrayList();
    private ArrayList<Bitmap> imagearrbitmap=new ArrayList();
    private Bitmap bitmap,resisedbitmap;
    private TransparentProgressDialog pd;
    private  String pot_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pot__challenge__association);

        if (getIntent().hasExtra("pot_id")) {
            pot_id = getIntent().getStringExtra("pot_id");
        }
       Log.d("pot_id",pot_id) ;

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);


        recviewimg = findViewById(R.id.recviewimg);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        et_woe = findViewById(R.id.et_woe);
        dobname_editText = findViewById(R.id.dobname_editText);
        et_abtpot= findViewById(R.id.et_abtpot);
        spinner= findViewById(R.id.spinner);
        publishRadioGroup = findViewById(R.id.publishRadioGroup);
        et_entertitle = findViewById(R.id.et_entertitle);
        et_shortdec = findViewById(R.id.et_shortdec);
        btn_next = findViewById(R.id.btn_next);
        texttitle = findViewById(R.id.texttitle);
        iv_back = findViewById(R.id.iv_back);
        addpicture = findViewById(R.id.addpicture);

        texttitle.setTextSize(14);
        texttitle.setText("Create a Pot Chatlenge for an association");


        spinner.setAdapter(new ArrayAdapter<String>(Pot_Challenge_Association.this, android.R.layout.simple_spinner_dropdown_item,    getResources().getStringArray(R.array.selectcategory)));

        spinner.setOnItemSelectedListener(this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pot_Challenge_Association.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        addpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(imagearr.size() == 0 ){
    if (ActivityCompat.checkSelfPermission(Pot_Challenge_Association.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (validation.neverAskAgainSelected(Pot_Challenge_Association.this, WRITE_EXTERNAL_STORAGE, "STORAGE")) {
                validation.displayNeverAskAgainDialog("storage", Pot_Challenge_Association.this);
            } else {
                ActivityCompat.requestPermissions(Pot_Challenge_Association.this,
                        new String[]{WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }

    } else {


        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_IMAGE_PICK);
    }
}else{
    Toast.makeText(Pot_Challenge_Association.this,"You can select one image",Toast.LENGTH_SHORT).show();
}


            }
        });

        dobname_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mPreviousYear = getCalculatedDate("yyyy-MM-dd", -20);

                Calendar calendar = Calendar.getInstance();
                String[] mstrDate = mPreviousYear.split("-");
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dp = new DatePickerDialog(Pot_Challenge_Association.this,android.R.style.Theme_Holo_Light_Dialog,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dobname_editText.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);

                    }
                }, Integer.parseInt(mstrDate[0]), Integer.parseInt(mstrDate[1]), Integer.parseInt(mstrDate[2]));
                dp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dp.getDatePicker().setMaxDate(System.currentTimeMillis());
                dp.show();

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = publishRadioGroup.getCheckedRadioButtonId();
                publishradioButton = (RadioButton) findViewById(selectedId);


                Validation validation = new Validation();

                if (!validation.edttxtvalidation(et_entertitle, Pot_Challenge_Association.this)) {

                } else if (!validation.edttxtvalidation(et_shortdec, Pot_Challenge_Association.this)) {

                } else if (!validation.edttxtvalidation(et_abtpot, Pot_Challenge_Association.this)) {

                } else if (!validation.edttxtvalidation(dobname_editText, Pot_Challenge_Association.this)) {

                }
                else if(imagearr.size()<1){
                    Toast.makeText(Pot_Challenge_Association.this, "Please select atleast one image", Toast.LENGTH_SHORT).show();
                }
                else if(publishradioButton==null){
                    Toast.makeText(Pot_Challenge_Association.this, "Are you creating your pot?", Toast.LENGTH_SHORT).show();
                }
                else if(spinner.getSelectedItem().equals("Select Category")){
                    Toast.makeText(Pot_Challenge_Association.this, "Please select category", Toast.LENGTH_SHORT).show();
                }

                else {
                    callapi();
                }
            }
        });
    }

    private void callapi() {


        pd.show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebUrl.create_pot_chatlenge_for_an_association,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                pd.dismiss();

                Log.d("chatlenge_association", String.valueOf(response.data));

                try {
                    String result = new String(response.data);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("1")) {
                        Intent intent = new Intent(Pot_Challenge_Association.this,Create_Pot_Challenge_2.class);
                        intent.putExtra("pot_id",pot_id);
                        Log.d("Pot_id",pot_id);
                        startActivity(intent);
//Toast.makeText(Create_Pot_Challenge.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Pot_Challenge_Association.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(Pot_Challenge_Association.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Pot_Challenge_Association.this, getResources().getString(R.string.anerroroccured), Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
//                user_id:2
//                pot_id:1
//                category:Sport
//                title:Sport
//                description:testting
//                about_pot:testing
//                date_of_events:2020-20-20
//                create_pot:yes
 //               website_url
                param.put("user_id", PrefManager.getUserId(Pot_Challenge_Association.this));
                param.put("pot_id",pot_id);
                param.put("category", String.valueOf(spinner.getSelectedItem()));
                param.put("title", et_entertitle.getText().toString());
                param.put("description", et_shortdec.getText().toString());
                param.put("about_pot", et_abtpot.getText().toString());
                param.put("date_of_events", dobname_editText.getText().toString());

                if(publishradioButton.getText().toString().equalsIgnoreCase("Yes")){
                    param.put("create_pot","Yes");
                }
                else if(publishradioButton.getText().toString().equalsIgnoreCase("No")){
                    param.put("create_pot","No");
                }


                param.put("website_url",et_woe.getText().toString());
                Log.d("params",PrefManager.getUserId(Pot_Challenge_Association.this)+" "+pot_id+" "+ String.valueOf(spinner.getSelectedItem()
                        +" "+et_entertitle.getText().toString()+" "+et_shortdec.getText().toString()+" "+et_abtpot.getText().toString()+" "+dobname_editText.getText().toString())
                +" "+et_woe.getText().toString());
                return param;
            }
            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws  AuthFailureError {

                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                for (int i=0;i<imagearr.size();i++){
                    params.put("image["+i+"]", new VolleyMultipartRequest.DataPart( "name"+i+".png",imagearr.get(i)));
Log.d("imagegoing","name"+i+".png");
                }

                return params;
            }

        };

        MySingleton.getInstance(Pot_Challenge_Association.this).addToRequestQueue(volleyMultipartRequest);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static String getCalculatedDate(String dateFormat, int YEARS) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.YEAR, YEARS);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();



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
                            Uri photoURI = FileProvider.getUriForFile(Pot_Challenge_Association.this,
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
                if(imagearr.size() < 2 && imagearr.size() == 0){
                    imagearr.add(getFileDataFromDrawable(resisedbitmap));
                    imagearrbitmap.add(bitmap);

                    recviewimg.setLayoutManager(new GridLayoutManager(Pot_Challenge_Association.this, 3));
                    ChallengeImageAdapter challengeImageAdapter=new ChallengeImageAdapter(imagearrbitmap, Pot_Challenge_Association.this);
                    recviewimg.setAdapter(challengeImageAdapter);
                    challengeImageAdapter.setOnItemClickListener(position ->{

                        imagearr.remove(position);
                        imagearrbitmap.remove(position);
                        recviewimg.getAdapter().notifyDataSetChanged();


                    });
                }else{
                    Toast.makeText(Pot_Challenge_Association.this,"You can select one image",Toast.LENGTH_SHORT).show();
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

}