package com.screenplay.myscreenplay.regANDlog;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.screenplay.myscreenplay.MainActivity;
import com.screenplay.myscreenplay.Model.User;
import com.screenplay.myscreenplay.Profile_Activity;
import com.screenplay.myscreenplay.R;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.me.hardill.volley.multipart.MultipartRequest;

public class Register extends AppCompatActivity {

    private EditText name,email,password,edit_code;
    private Button Register,but_cod,return_code;




      LinearLayout layout_desian,layout_code;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        email=findViewById(R.id.reg_email);
        password=findViewById(R.id.reg_password);
        Register=findViewById(R.id.but_reg);
        name=findViewById(R.id.editText_name_reg);
        but_cod=findViewById(R.id.but_code);
        edit_code=findViewById(R.id.editText_code);
        layout_code=findViewById(R.id.layout_code);
        layout_desian=findViewById(R.id.layout_desain);
         return_code=findViewById(R.id.but_return_code);




        but_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (TextUtils.isEmpty(edit_code.getText().toString().trim())){
                    edit_code.setError("Enter code");
                    edit_code.requestFocus();
                    return;
                }
                dataMess();



            }
        });





        return_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                returnCode();
            }
        });
















        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int num_password=password.getText().toString().trim().length();

                if (num_password<8) {

                    Toast.makeText(getBaseContext(),getResources().getString(R.string.codesmall), Toast.LENGTH_LONG).show();
                    return;
                }


                dataSave();

            }
        });
    }

    private void saveRegi(){

        final String name1=name.getText().toString().trim();
        final String user_email=email.getText().toString();
        final String user_password=password.getText().toString();

        if (TextUtils.isEmpty(name1)){
            name.setError("Enter your name");
            name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(user_email)){
            email.setError("Enter your email");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(user_password)){
            password.setError("Enter your password");
            password.requestFocus();
            return;
        }

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ServerApi.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {

                                JSONObject object = jsonObject.getJSONObject("data");
                                VollySing.getInstanse(getBaseContext()).saveUser(new User(object.getString("token")));





                            } else {
                                Toast.makeText(getBaseContext(), "message", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        }
        )

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map=new HashMap<>();
                map.put("Content-Type","application/json");
                map.put("name",name1);
                map.put("email",user_email);
                map.put("password",user_password);
                map.put("c_password",user_password);

                return map ;
            }
        };
        VollyLib.getInstance(getBaseContext()).addRequest(stringRequest);

    }










    private void dataSave() {


        final ArrayList<String> arrayList=new ArrayList<>();
        JSONObject js = new JSONObject();
        try {
            js.put("email",email.getText().toString().trim());

        } catch (Exception e) {
            e.printStackTrace();
        }


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ServerApi.GET_CODE_RES, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean("success")) {

                        if (response.getString("message").equals("yes")){


                            arrayList.add(response.getString("message"));

                            email.setError(getResources().getString(R.string.account_register));
                            Toast.makeText(getBaseContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            return;
                        }else {

                            layout_desian.setVisibility(View.GONE);
                            layout_code.setVisibility(View.VISIBLE);

                        }

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

                return map;
            }

        };
        VollyLib.getInstance(getBaseContext()).addRequest(jsonObjectRequest);




    }











    private void dataMessage() {


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.GET_EMAIL+email.getText().toString().trim(), null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        int id=screen.getInt("id");

                             saveRegi();

                             deleteItem(id);


                        Intent intent=new Intent(getBaseContext(), Profile_Activity.class);
                        startActivity(intent);
                        finish();


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

                return map;
            }

        };
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);

        //  return arrayList1;

    }





    public void deleteItem(int id) {




        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, ServerApi.GET_CODE_RES + "/" + id, null, new Response.Listener<JSONObject>() {
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

                return map;
            }

        };
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);




    }












    private void returnCode() {


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.GET_EMAIL+email.getText().toString().trim(), null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        int id=screen.getInt("id");

                        upDate(id);



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

                return map;
            }

        };
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);



    }










    private void upDate(int id){

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("email",email.getText().toString().trim());

        }catch (Exception e){
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT, ServerApi.GET_CODE_RES+"/"+id,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    if (response.getBoolean("success")){

                        Toast.makeText(getBaseContext(),"تم الارسال",Toast.LENGTH_SHORT).show();


                    }else {

                        Toast.makeText(getBaseContext(),"error",Toast.LENGTH_SHORT).show();
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


        })
        {
            public Map<String,String> getHeaders(){
                Map<String, String> map=new HashMap<>();
                map.put("Accept","application/json");

                return map;
            }

        };
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);







    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dataMessageback();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        dataMessageback();

    }

    private void dataMessageback() {


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.GET_EMAIL+email.getText().toString().trim(), null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        int id=screen.getInt("id");


                        deleteItem(id);



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

                return map;
            }

        };
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);



    }





























    private void dataMess() {


        JSONObject js = new JSONObject();
        try {
            js.put("email", email.getText().toString().trim());
            js.put("code", edit_code.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ServerApi.GET_CODE_RESGISTER, js, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {



                        if (response.getString("message").equals("done")) {

                            dataMessage();


                            Toast.makeText(getBaseContext(), response.getString("message")+"ddd", Toast.LENGTH_SHORT).show();
//                            String name1=namee.getText().toString().trim();
//                            String birthdayy=birthday.getText().toString().trim();
//                            String coun=country.getText().toString().trim();











                    } else {
                        Toast.makeText(getBaseContext(), "ادخل الرمز الصحيح", Toast.LENGTH_SHORT).show();
                        return;
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

                return map;
            }

        };
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);



    }
}
