package com.ccube9.gochat.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.ccube9.gochat.R;
import com.ccube9.gochat.Util.PrefManager;
import com.ccube9.gochat.Util.Validation;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PersonalDetailActivity extends AppCompatActivity {

    private EditText dobname_editText,firstname_editText,lastname_editText,schoolname_editText;
    private String gender="";
    private Button male_button,female_button,pd_continue_button;
    private TextInputLayout txtInputFname,txtInputlastname,txtInputdob,txtInputschoolname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detail);

        dobname_editText=findViewById(R.id.dobname_editText);
        txtInputFname=findViewById(R.id.txtInputFname);
        txtInputschoolname=findViewById(R.id.txtInputschoolname);
        schoolname_editText=findViewById(R.id.schoolname_editText);
        txtInputlastname=findViewById(R.id.txtInputlastname);
        txtInputdob=findViewById(R.id.txtInputdob);
        firstname_editText=findViewById(R.id.firstname_editText);
        lastname_editText=findViewById(R.id.lastname_editText);
        male_button=findViewById(R.id.male_button);
        female_button=findViewById(R.id.female_button);
        pd_continue_button=findViewById(R.id.pd_continue_button);


        firstname_editText.setText(PrefManager.getFirstName(PersonalDetailActivity.this));
        lastname_editText.setText(PrefManager.getLastName(PersonalDetailActivity.this));

        male_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_button.setBackground(getDrawable(R.drawable.backgroundfilled_round));
                female_button.setBackground(getDrawable(R.drawable.background_round));

                male_button.setTextColor(getResources().getColor(R.color.colorWhite));
                female_button.setTextColor(getResources().getColor(R.color.colorblack));
                gender="Male";
            }
        });


        female_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_button.setBackground(getDrawable(R.drawable.background_round));
                female_button.setBackground(getDrawable(R.drawable.backgroundfilled_round));

                male_button.setTextColor(getResources().getColor(R.color.colorblack));
                female_button.setTextColor(getResources().getColor(R.color.colorWhite));
                gender="Female";

            }
        });

        pd_continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation validation=new Validation();


                 if (gender.equals("")){
                     Toast.makeText(PersonalDetailActivity.this, getResources().getString(R.string.pleaseselectgender), Toast.LENGTH_SHORT).show();

                }

                else if (!validation.edttxtvalidation(firstname_editText,txtInputFname,PersonalDetailActivity.this)){

                }
                else if (!validation.edttxtvalidation(lastname_editText,txtInputlastname,PersonalDetailActivity.this)){

                }
                else if (!validation.edttxtvalidation(dobname_editText,txtInputdob,PersonalDetailActivity.this)){

                }
//                 else if (!validation.edttxtvalidation(schoolname_editText,txtInputschoolname,PersonalDetailActivity.this)){
//
//                 }

                    else{
                    Intent intent = new Intent(PersonalDetailActivity.this, PersonalDetailSettingActivity.class);


                     Bundle bundle=new Bundle();
                     bundle.putString("gender",gender);
                     bundle.putString("firstname",firstname_editText.getText().toString());
                     bundle.putString("lastname",lastname_editText.getText().toString());
                     bundle.putString("dob",dobname_editText.getText().toString());
                     bundle.putString("school",schoolname_editText.getText().toString());
                     intent.putExtra("bundlepersonaldata",bundle);

                     startActivity(intent);
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

                DatePickerDialog dp = new DatePickerDialog(PersonalDetailActivity.this,android.R.style.Theme_Holo_Light_Dialog,new DatePickerDialog.OnDateSetListener() {
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
        finish();
    }
}
