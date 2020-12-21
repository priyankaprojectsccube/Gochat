package com.ccube9.gochat.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.RequiresApi;
import com.ccube9.gochat.R;
import com.google.android.material.textfield.TextInputLayout;
public class Validation {



    public String email_pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public String no_pattern="^\\+?\\d+$";
    public String swiftcode_pattern="^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$";     //AABSDE31XXA

    private String iban_pattern="[A-Z]{2}\\d{2} ?\\d{4} ?\\d{4} ?\\d{4} ?\\d{4} ?[\\d]{0,2}";  //DE89 3704 0044 0532 0130 00




    public boolean edttxtvalidation(EditText editText, Context context) {
        if (editText.getText().toString().equals("")) {
            editText.setError(context.getResources().getString(R.string.fill_field));
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }


    public boolean conpassvalidation(EditText editText, EditText editTextcon, Context context) {

        if (!editText.getText().toString().equals(editTextcon.getText().toString())) {
            editTextcon.setError(context.getResources().getString(R.string.con_pass_error));
            return false;
        } else {
            return true;
        }
    }
    public boolean passvalidation(EditText editText, Context context) {

        if (editText.getText().length() < 6) {
            editText.setError(context.getResources().getString(R.string.pass_error));
            return false;
        } else {
            return true;
        }
    }


    public boolean edttxtvalidation(EditText editText, TextInputLayout textInputLayout, Context context) {
        if (editText.getText().toString().equals("") ) {
            textInputLayout.setError(context.getResources().getString(R.string.fill_field));
            editText.requestFocus();
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }






    public boolean emailmobilevalidation(EditText editText,TextInputLayout textInputLayout, Context context) {
        if (!editText.getText().toString().matches(email_pattern) && !editText.getText().toString().matches(no_pattern)  ) {
            textInputLayout.setError(context.getResources().getString(R.string.invalidemailmob));
            editText.requestFocus();
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }


    }

    public boolean ibanvalidation(EditText editText, Context context) {
        if (!editText.getText().toString().matches(iban_pattern)) {
            editText.setError(context.getResources().getString(R.string.invalideiban));
            editText.requestFocus();
            return false;
        } else {
            editText.setError(null);
            return true;
        }


    }

    public boolean swiftcodevalidation(EditText editText, Context context) {
        if (!editText.getText().toString().matches(swiftcode_pattern)) {
            editText.setError(context.getResources().getString(R.string.invalidswiftcode));
            editText.requestFocus();
            return false;
        } else {
            editText.setError(null);
            return true;
        }


    }


    public boolean emailvalidation(EditText editText,TextInputLayout textInputLayout, Context context) {

        if (!editText.getText().toString().matches(email_pattern)) {
            textInputLayout.setError(context.getResources().getString(R.string.email_error));
            editText.requestFocus();
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }

    public boolean mobnovalidation(EditText editText,TextInputLayout textInputLayout, Context context) {

        if (!editText.getText().toString().matches(no_pattern)  || editText.getText().length()<10) {
            textInputLayout.setError(context.getResources().getString(R.string.mob_no_error));
            editText.requestFocus();
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }



    public boolean passvalidation(EditText editText,TextInputLayout textInputLayout, Context context) {

        if (editText.getText().length() < 6) {
            textInputLayout.setError(context.getResources().getString(R.string.pass_error));
            editText.requestFocus();
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }

    public void launchGalleryIntent(int requestcode){

    }


    public boolean conpassvalidation(EditText editText, EditText editTextcon,TextInputLayout textInputLayout, Context context) {

        if (!editText.getText().toString().equals(editTextcon.getText().toString())) {
            textInputLayout.setError(context.getResources().getString(R.string.con_pass_error));
            editText.requestFocus();
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }



    public boolean  getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return true;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }






    public Bitmap setPic(String currentPhotoPath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW/12000, photoH/12000);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        return bitmap;
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean neverAskAgainSelected(Activity activity,String permission, String name) {
        final boolean prevShouldShowStatus = getRatinaleDisplayStatus(activity,permission,name);
        final boolean currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission);
        return prevShouldShowStatus != currShouldShowStatus;
    }


    public static boolean getRatinaleDisplayStatus(Context context,String permission,String name) {
        SharedPreferences genPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return genPrefs.getBoolean(permission, false);
    }


    public void displayNeverAskAgainDialog(String permission, final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(permission.equals("camera")) {
            builder.setMessage(context.getResources().getString(R.string.camera_permission));
        }
        if(permission.equals("storage")) {
            builder.setMessage(context.getResources().getString(R.string.storage_permission));
        }

        builder.setPositiveButton(context.getResources().getString(R.string.permitmanually), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });

        builder.show();
    }


    public static void setShouldShowStatus(final Context context, final String permission,String name) {
        SharedPreferences genPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = genPrefs.edit();
        editor.putBoolean(permission, true);
        editor.commit();
    }


}
