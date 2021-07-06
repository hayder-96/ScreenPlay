package com.screenplay.myscreenplay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import uk.me.hardill.volley.multipart.MultipartRequest;

public class Profile_Activity extends AppCompatActivity {


    EditText birthday,country;
    String dateBirthday;
    ImageView imageView;
    EditText name;
    RadioButton radioButton1, radioButton2;
    String male_female;
    Uri uri;
    Button done;
    RadioGroup radioGroup;
    DatePickerDialog.OnDateSetListener dateSetListener;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_);

        country=findViewById(R.id.edit_country);
        birthday = findViewById(R.id.birthday_id);
        imageView = findViewById(R.id.image_profile);
        name = findViewById(R.id.name_profile);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
          done=findViewById(R.id.but_done);
           radioGroup=findViewById(R.id.radio_groub);
          done.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  if (TextUtils.isEmpty(male_female)){
                      Toast.makeText(getBaseContext(),"enter your gender",Toast.LENGTH_SHORT).show();
                      return;
                  }
                  mulityy();


              }
          });
        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    male_female = radioButton1.getText().toString();
                    radioButton1.setChecked(true);
                }
            }
        });

        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    male_female = radioButton2.getText().toString();
                    radioButton1.setChecked(true);
                }
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });
//
//
//        birthday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar calendar = Calendar.getInstance();
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH);
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog dialog = new DatePickerDialog(getBaseContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth, dateSetListener
//                        , year, month, day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//
//                dateSetListener = new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//
//                        dateBirthday = year + "/" + month + "/" + day;
//                        birthday.setText(dateBirthday);
//                    }
//                };
//            }
//        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {

                uri = data.getData();
                Picasso.get().load(uri.toString()).resize(200,200).into(imageView);


            }
        }

    }





    private void mulityy() {


        String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();
        Map<String, String> map1 = new HashMap<>();
        map1.put("Accept", "application/json");
        map1.put("Authorization", "Bearer " + token);


        MultipartRequest request = new MultipartRequest(ServerApi.USERS_Data_Url, map1,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });


        final String name1=name.getText().toString();
        String birthdayy=birthday.getText().toString();
        String coun=country.getText().toString();


        if (TextUtils.isEmpty(name1)) {
            name.setError("Enter title");
            name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(birthdayy)) {
            birthday.setError("Enter birthday");
            birthday.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(coun)) {
            country.setError("Enter country");
            country.requestFocus();
            return;
        }



        request.addPart(new MultipartRequest.FormPart("name",name1));
        request.addPart(new MultipartRequest.FormPart("age",birthdayy));
        request.addPart(new MultipartRequest.FormPart("gender",male_female));
        request.addPart(new MultipartRequest.FormPart("country",coun));


        if (uri != null) {
            try {

                Bitmap  bitmapp;
                bitmapp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);




                Bitmap bitmap = Bitmap.createScaledBitmap(bitmapp,200,200, false);






                request.addPart(new MultipartRequest.FilePart("image", "*/*", uri.toString(), getFileDataFromDrawable(bitmap)));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        intent=new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
        finish();
        VollyLib.getInstance(getBaseContext()).addRequest(request);


    }



    public byte[] getFileDataFromDrawable(Bitmap bitmap1) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

    }






    }
