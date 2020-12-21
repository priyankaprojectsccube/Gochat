package com.ccube9.gochat.News.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ccube9.gochat.News.Adapter.GalleryPreviewAdpater;
import com.ccube9.gochat.News.Adapter.NewsPreviewAdapter;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.PreviewImageDetails;
import com.ccube9.gochat.Util.PreviewImageHelperClass;
import com.ccube9.gochat.Util.TransparentProgressDialog;
import com.ccube9.gochat.Util.VolleyMultipartRequest;
import com.ccube9.gochat.Util.WebUrl;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsPreview extends AppCompatActivity {
    private Bitmap bitmap;
    private long lengthbmp;
    RecyclerView recyclerView;
    private TransparentProgressDialog pd;
    TextView texttitle,poststorytxt,noimgselect;
    ImageView iv_back;
    EditText edtcaption;
    private ArrayList<PreviewImageHelperClass> mArrListImg = new ArrayList<>();
    private List<PreviewImageDetails> previewImageDetailsList = new ArrayList<>();
    ArrayList<String> returnValue = new ArrayList();
    NewsPreviewAdapter newsPreviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_preview);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorofwhite));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorofwhite)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.customwhitetoolbar);

        texttitle=findViewById(R.id.texttitle);
        iv_back=findViewById(R.id.iv_back);


        texttitle.setText("Post");

        recyclerView = findViewById(R.id.recyclerView);
        poststorytxt = findViewById(R.id.poststorytxt);
        edtcaption = findViewById(R.id.edtcaption);
        noimgselect = findViewById(R.id.noimgselect);
        pd = new TransparentProgressDialog(this, R.drawable.ic_loader_image);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NewsPreview.this, NewsFunctions.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        poststorytxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strcaption = edtcaption.getText().toString();
                if(strcaption != null || !strcaption.isEmpty()){
                    callpoststory(strcaption);
                }else{
                    Toast.makeText(NewsPreview.this,"Please enter caption",Toast.LENGTH_SHORT).show();
                }

            }
        });
        openGallery();
    }

    private void callpoststory(String strcaption) {
        if(mArrListImg.size() > 0) {
            pd.show();
            RequestQueue requestQueue1 = Volley.newRequestQueue(NewsPreview.this);
            //  CustomUtil.ShowDialog(MyGardenActivity.this, true);


            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebUrl.upload_news_images,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            pd.dismiss();
                            //progressDialog.dismiss();
                            Log.d("upload_news_img_re", String.valueOf(response.statusCode));
                            if (response.statusCode == 200) {

                                String stry = new String(response.data);
                                Log.d("fdfdsfd", stry);

                                Intent intent = new Intent(NewsPreview.this, NewsFunctions.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //   progressDialog.dismiss();
                            pd.dismiss();

                            Log.v("ResError", "" + volleyError.toString());
                        }
                    }
            ) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    String username = PrefManager.getFirstName(NewsPreview.this)+" "+PrefManager.getLastName(NewsPreview.this);
                    param.put("user_id", PrefManager.getUserId(NewsPreview.this));
                    param.put("adv_name","abc");
                    param.put("user_name",username);
                    param.put("description",strcaption);
                    Log.d("mArrListImgsize", String.valueOf(mArrListImg.size()));
                    for (int i = 0; i < mArrListImg.size(); i++)
                    {
                        Log.d("checkimgpost", String.valueOf(mArrListImg.get(i)));
                        if (!mArrListImg.get(i).getmImgFrom().equalsIgnoreCase("Api")) {
                            if (mArrListImg.get(i).isVideoOrImg()) {
                                param.put("flag", "1");
                                param.put("flag_vi_new","1");
                                Log.d("flagvideo","1");
                            } else {
                                param.put("flag", "0");
                                param.put("flag_vi_new","0");
                                Log.d("flagimg","0");
                            }
                        }
                    }
                    return param;
                }
                @Override
                protected Map<String, DataPart> getByteData() throws AuthFailureError {
                    Map<String, DataPart> params = new HashMap<>();
                    Log.d("mArrListImgsize", String.valueOf(mArrListImg.size()));
                    for (int i = 0; i < mArrListImg.size(); i++) {
                        Log.d("checkimgpost", String.valueOf(mArrListImg.get(i)));
                        if (!mArrListImg.get(i).getmImgFrom().equalsIgnoreCase("Api")) {
                            if (mArrListImg.get(i).isVideoOrImg()) {
                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                params.put("image[" + i + "]", new VolleyMultipartRequest.DataPart("" + timeStamp + String.valueOf(i) + ".mp4", mArrListImg.get(i).getmArrImgByte()));

                            } else {
                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                params.put("image[" + i + "]", new VolleyMultipartRequest.DataPart(timeStamp + String.valueOf(i) + ".png", mArrListImg.get(i).getmArrImgByte()));
                                Log.d("checkimgnamepost", timeStamp + String.valueOf(i) + ".png");

                            }
                        }

                    }
                    return params;
                }
            };
            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue1.add(volleyMultipartRequest);
        }
        else{
            Toast.makeText(NewsPreview.this,"Select images/videos",Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Options options = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setExcludeVideos(false)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage

        Pix.start(NewsPreview.this, options);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Log.d("listimgv", String.valueOf(returnValue.size()));
            mArrListImg.clear();
            for(int i=0 ;i < returnValue.size(); i++){
                String path = returnValue.get(i);
                Log.d("testpath",returnValue.get(i));
                if(path.contains(".mp4")){
                    File imageFile = new File(getRealPathFromURI(Uri.parse(returnValue.get(i))));
                    byte[] mByteArr = convertVideoToBytes(imageFile);

                    PreviewImageHelperClass previewImageHelperClass = new PreviewImageHelperClass();
                    previewImageHelperClass.setmImgId("");
                    previewImageHelperClass.setmImgFrom("storage");
                    previewImageHelperClass.setmArrImgByte(mByteArr);
                    previewImageHelperClass.setVideoOrImg(true);
                    previewImageHelperClass.setmImvByte("");
                    previewImageHelperClass.setmBitmap(bitmap);

                    mArrListImg.add(previewImageHelperClass);

                }else{

                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(path,bmOptions);
                    //  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(returnValue.get(i)));
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    byte[] mByteArr = getFileDataFromDrawable(bitmap);

                    PreviewImageHelperClass previewImageHelperClass = new PreviewImageHelperClass();
                    previewImageHelperClass.setmImgId("");
                    previewImageHelperClass.setmImgFrom("storage");
                    previewImageHelperClass.setVideoOrImg(false);

                    previewImageHelperClass.setmImvByte("");
                    previewImageHelperClass.setmArrImgByte(mByteArr);
                    previewImageHelperClass.setmBitmap(bitmap);
                    mArrListImg.add(previewImageHelperClass);


                }
            }

            calluploadprevwimgs();

        }
    }

    private void calluploadprevwimgs() {

        recyclerView.setVisibility(View.VISIBLE);
        noimgselect.setVisibility(View.GONE);

        pd.show();
        RequestQueue requestQueue1 = Volley.newRequestQueue(NewsPreview.this);
        //  CustomUtil.ShowDialog(MyGardenActivity.this, true);


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebUrl.add_preview_news1,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        pd.dismiss();
                        //progressDialog.dismiss();
                        Log.d("add_preview_news_re", String.valueOf(response.statusCode));
                        String stry=new String(response.data);
                        if (response.statusCode == 200) {


                            Log.d("add_preview_code",stry);

                            getpreviewimg();
                        }else{
                            Log.d("add_preview_codee",stry);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //   progressDialog.dismiss();
                        pd.dismiss();

                        Log.v("ResError", "" + volleyError.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", PrefManager.getUserId(NewsPreview.this));
                return param;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                for (int i = 0; i < mArrListImg.size(); i++) {
                    Log.d("checkimg", String.valueOf(mArrListImg.get(i)));
                    if (!mArrListImg.get(i).getmImgFrom().equalsIgnoreCase("Api")) {
                        if (mArrListImg.get(i).isVideoOrImg()) {
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            params.put("image[" + i + "]", new VolleyMultipartRequest.DataPart("" + timeStamp +String.valueOf(i)+".mp4", mArrListImg.get(i).getmArrImgByte()));

                        } else {
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            params.put("image[" + i + "]", new VolleyMultipartRequest.DataPart(timeStamp+String.valueOf(i)+".png", mArrListImg.get(i).getmArrImgByte()));
                            Log.d("checkimgname",timeStamp+String.valueOf(i)+".png");

                        }
                    }

                }
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(volleyMultipartRequest);
    }

    private void getpreviewimg() {
        poststorytxt.setVisibility(View.VISIBLE);

        pd.show();
        RequestQueue requestQueue1 = Volley.newRequestQueue(NewsPreview.this);
        //  CustomUtil.ShowDialog(MyGardenActivity.this, true);


        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, WebUrl.get_preview_news, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                previewImageDetailsList.clear();
//                mArrListImg.clear();
                pd.dismiss();
                Log.d("get_preview_news_re", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("preview_images");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                        PreviewImageDetails previewImageDetails = new PreviewImageDetails();


                        previewImageDetails.setId(jsonObject1.getString("id"));
                        previewImageDetails.setUser_id(jsonObject1.getString("user_id"));

                        previewImageDetails.setStory_image(jsonObject1.getString("news_image"));
                        previewImageDetails.setCreated_date(jsonObject1.getString("created_date"));

                        previewImageDetails.setImage(jsonObject1.getString("image"));


                        previewImageDetailsList.add(previewImageDetails);
                        Log.d("getlistsize", String.valueOf(previewImageDetailsList.size()));
                    }
                    newsPreviewAdapter = new NewsPreviewAdapter(mArrListImg,previewImageDetailsList, NewsPreview.this);
                    recyclerView.setHasFixedSize(true);
                    GridLayoutManager manager = new GridLayoutManager(NewsPreview.this, 2, GridLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(manager);

                    recyclerView.setAdapter(newsPreviewAdapter);



                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("dfddf", volleyError.toString());
                pd.dismiss();
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                if (message != null) {
                    Toast.makeText(NewsPreview.this, message, Toast.LENGTH_SHORT).show();

                } else {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", PrefManager.getUserId(NewsPreview.this));
                return param;
            }
        };
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(stringRequest1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                    Pix.start(NewsPreview.this, Options.init().setRequestCode(100));
                } else {
                    Toast.makeText(NewsPreview.this, "Approve permissions to open gallery", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public byte[] convertVideoToBytes(File file) {
        byte[] videoBytes = null;
        try {//  w  w w  . j ava 2s . c  o m
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);

            videoBytes = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoBytes;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        lengthbmp = imageInByte.length;
        return byteArrayOutputStream.toByteArray();
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
