package com.screenplay.myscreenplay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import uk.me.hardill.volley.multipart.MultipartRequest;

public class MyProfile extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;

    private ImageView imageView;
    private TextView myname,mybirthday,mycountry;
    private EditText mnam,mybarth,mycont;
    private Button edit_prof,goto_prof,done_edit;
    private  Uri uri;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_my);

        imageView=findViewById(R.id.my_image);
        myname=findViewById(R.id.mynameinprofile);
        mycountry=findViewById(R.id.mycountry);
        mybirthday=findViewById(R.id.mybirthday);
        progressBar=findViewById(R.id.progressBar_myprof);


        mnam=findViewById(R.id.mynameinprofile_edit);
        mybarth=findViewById(R.id.mybirthday_edit);
        mycont=findViewById(R.id.mycountry_edit);


        edit_prof=findViewById(R.id.but_edit_prof);
        goto_prof=findViewById(R.id.but_your_prfo);

        done_edit=findViewById(R.id.but_done_edit);




        goto_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataa();
            }
        });





        bottomNavigationView = findViewById(R.id.navigation_pro);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.help:
                        Intent i = new Intent(getBaseContext(), HomeActivity.class);
                        finish();
                        startActivity(i);
                        break;

                    case R.id.example_screen:
                        Intent ii = new Intent(getBaseContext(), Myscrenn_Example.class);
                        finish();
                        startActivity(ii);
                        break;

                    case R.id.home:
                        Intent iii = new Intent(getBaseContext(), MainActivity.class);
                        finish();
                        startActivity(iii);
                        break;
                    case R.id.users:
                        Intent iiii = new Intent(getBaseContext(), All_Users.class);
                        startActivity(iiii);
                        finish();
                        break;
                }
                return false;
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);

                edit_prof.setVisibility(View.GONE);

                   dataaa();
            }
        }
        );

        data();













        edit_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                myname.setVisibility(View.GONE);
                mybirthday.setVisibility(View.GONE);
                mycountry.setVisibility(View.GONE);

                edit_prof.setVisibility(View.GONE);
                done_edit.setVisibility(View.VISIBLE);

                dataaa();
                mnam.setVisibility(View.VISIBLE);
                mybarth.setVisibility(View.VISIBLE);
                mycont.setVisibility(View.VISIBLE);

            }
        });


        done_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                myname.setVisibility(View.VISIBLE);
                mybirthday.setVisibility(View.VISIBLE);
                mycountry.setVisibility(View.VISIBLE);



                edit_prof.setVisibility(View.VISIBLE);
                done_edit.setVisibility(View.GONE);





                mulityImage();

                mnam.setVisibility(View.GONE);
                mybarth.setVisibility(View.GONE);
                mycont.setVisibility(View.GONE);
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(7000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                dataer();
                                finish();
                                startActivity(new Intent(getBaseContext(),MainActivity.class));
                            }
                        });


                    }
                });

                thread.start();

                if(uri==null){
                    datagetnoimage();
                }
            }



        });



        mnam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().trim().equals("")){
                    done_edit.setEnabled(false);
                }else {
                    done_edit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        mycont.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().trim().equals("")){
                    done_edit.setEnabled(false);
                }else {
                    done_edit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        mybarth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().trim().equals("")){
                    done_edit.setEnabled(false);
                }else {
                    done_edit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }



    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {

                done_edit.setVisibility(View.VISIBLE);
                uri = data.getData();
                Picasso.get().load(uri.toString()).resize(200,200).into(imageView);

            }
        }
    }


















    private void data() {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();

        progressBar.setVisibility(View.VISIBLE);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.USERS_Data_Url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);


                        String name = screen.getString("name");
                       String urii=screen.getString("image");
                         String birthday=screen.getString("age");
                         String country=screen.getString("country");



                         Toast.makeText(getBaseContext(),urii,Toast.LENGTH_SHORT).show();

                         myname.setText(name);
                         mycountry.setText(country);
                         if (urii.equals("no")){
                             imageView.setImageResource(R.drawable.profileuser);
                         }else {
                             Picasso.get().load(urii).resize(200, 200).into(imageView);
                         }
                         mybirthday.setText(birthday);



                    }

                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),getResources().getString(R.string.nointrnet),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }


        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer  " + token);
                return map;
            }

        };
        VollyLib.getInstance(getBaseContext()).addRequest(jsonObjectRequest);


    }




    private void dataa() {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.USERS_Data_Url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);


                        int user_id=screen.getInt("user_id");

                        myProf(user_id);


                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer  " + token);
                return map;
            }

        };
        VollyLib.getInstance(getBaseContext()).addRequest(jsonObjectRequest);


    }
    private void myProf(int use){

        Intent intent=new Intent(getBaseContext(),Allfilm_view.class);
        intent.putExtra("id",use);
        startActivity(intent);
        Toast.makeText(getBaseContext(),use+"idd",Toast.LENGTH_SHORT).show();
    }





    private void dataaa() {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();

        progressBar.setVisibility(View.VISIBLE);


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.USERS_Data_Url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);


                        int id=screen.getInt("id");

                       show(id);


                    }


                    progressBar.setVisibility(View.GONE);


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(),getResources().getString(R.string.nointrnet),Toast.LENGTH_SHORT).show();

            }


        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer  " + token);
                return map;
            }

        };
        VollyLib.getInstance(getBaseContext()).addRequest(jsonObjectRequest);


    }









    private void upDate(int id,String image) {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();


        String nn=myname.getText().toString().trim();
        String cc=mycountry.getText().toString().trim();
        String aa=mybirthday.getText().toString().trim();


        JSONObject js = new JSONObject();
        try {
            js.put("name",nn);
            js.put("country",cc);
            js.put("age",aa);
            js.put("image",image);

        } catch (Exception e) {
            e.printStackTrace();
        }


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, ServerApi.USERS_Data_Url+"/"+id, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean("success")) {
                        Toast.makeText(getBaseContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                        Toast.makeText(getBaseContext(),"update",Toast.LENGTH_SHORT).show();



                    } else {

                        Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer " + token);
                return map;
            }

        };
        VollyLib.getInstance(getBaseContext()).addRequest(jsonObjectRequest);


    }







    private void show(final int id){


        final String token= VollySing.getInstanse(getBaseContext()).getToken().getToken();

            progressBar.setVisibility(View.VISIBLE);

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, ServerApi.USERS_Data_Url+"/"+id,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.getBoolean("success")) {
                        JSONObject screen = response.getJSONObject("data");


                        String name=screen.getString("name");
                        String age=screen.getString("age");
                        String country=screen.getString("country");
                        mnam.setText(name);
                        mybarth.setText(age);
                        mycont.setText(country);



                    }
                    progressBar.setVisibility(View.GONE);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(),getResources().getString(R.string.nointrnet),Toast.LENGTH_SHORT).show();
            }


        })
        {
            public Map<String,String> getHeaders(){
                Map<String, String> map=new HashMap<>();
                map.put("Accept","application/json");
                map.put("Authorization","Bearer "+ token);
                return map;
            }

        };
        VollyLib.getInstance(getBaseContext()).addRequest(jsonObjectRequest);



    }


    private void dataer() {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();


        progressBar.setVisibility(View.VISIBLE);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.USERS_Data_Url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);


                        int id = screen.getInt("id");
                        Toast.makeText(getBaseContext(),"dater"+id,Toast.LENGTH_SHORT).show();
                       dataimage(id);






                    }
                    progressBar.setVisibility(View.GONE);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),getResources().getString(R.string.nointrnet),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }


        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer  " + token);
                return map;
            }

        };
        VollyLib.getInstance(getBaseContext()).addRequest(jsonObjectRequest);


    }





    private void dataimage(final int idi) {


        final String token = VollySing.getInstanse(this).getToken().getToken();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.UPIMAGE_PROFILE, null, new Response.Listener<JSONObject>() {



            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        int id=screen.getInt("id");
                        String im = screen.getString("image");


                        Toast.makeText(getBaseContext(),"data_image",Toast.LENGTH_SHORT).show();


                        upDate(idi,im);
                        deleteImage(id);

                    }






                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer  " + token);
                return map;
            }

        };
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);


    }



    public void deleteImage(int id) {

        final String token = VollySing.getInstanse(this).getToken().getToken();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, ServerApi.UPIMAGE_PROFILE + "/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean("success")) {
                        Toast.makeText(getBaseContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                        //progressDialog.dismiss();

                    } else {

                        Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();
                        //  progressDialog.dismiss();
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer " + token);
                return map;
            }

        };
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);

        MainActivity.noti();


    }



    public byte[] getFileDataFromDrawable(Bitmap bitmap1) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

    }








    private void mulityImage() {


        String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();
        Map<String, String> map1 = new HashMap<>();
        map1.put("Accept", "application/json");
        map1.put("Authorization", "Bearer " + token);


        MultipartRequest request = new MultipartRequest(ServerApi.UPIMAGE_PROFILE, map1,
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







        if (uri != null) {
            try {
                Bitmap bitmapp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                Bitmap bitmap = Bitmap.createScaledBitmap(bitmapp,200,200, false);
                request.addPart(new MultipartRequest.FilePart("image", "*/*", uri.toString(), getFileDataFromDrawable(bitmap)));

                Toast.makeText(getBaseContext(),"mullity_image",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        VollyLib.getInstance(getBaseContext()).addRequest(request);


    }












    private void upDatenoImage(int id) {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();


        String nn=mnam.getText().toString().trim();
        String cc=mycont.getText().toString().trim();
        String aa=mybarth.getText().toString().trim();








        JSONObject js = new JSONObject();
        try {
            js.put("name",nn);
            js.put("country",cc);
            js.put("age",aa);


        } catch (Exception e) {
            e.printStackTrace();
        }


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, ServerApi.USERS_Data_Url+"/"+id, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean("success")) {
                        Toast.makeText(getBaseContext(), response.getString("message"), Toast.LENGTH_SHORT).show();




                    } else {

                        Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer " + token);
                return map;
            }

        };
        VollyLib.getInstance(getBaseContext()).addRequest(jsonObjectRequest);


    }





    private void datagetnoimage() {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.USERS_Data_Url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);


                        int id=screen.getInt("id");

                        upDatenoImage(id);


                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer  " + token);
                return map;
            }

        };
        VollyLib.getInstance(getBaseContext()).addRequest(jsonObjectRequest);


    }
}






