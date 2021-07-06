package com.screenplay.myscreenplay.regANDlog;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.screenplay.myscreenplay.MainActivity;
import com.screenplay.myscreenplay.Model.User;
import com.screenplay.myscreenplay.R;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    EditText email2, password2, email_done, forgot_password, edit_num;
    Button register2, login2, email_done_forgot, but_forgot_password, but_done_num, return_forgot;
    TextView text_forgot;

    LinearLayout layout_forgot, layout_login, layout_passowrd, layout_number;


    LoginButton signInButton;

    CallbackManager callbackManager;

      ProgressBar progressBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);







                email2 = findViewById(R.id.log_email);
        password2 = findViewById(R.id.log_password);
        register2 = findViewById(R.id.but_reg2);
        login2 = findViewById(R.id.but_log);

        layout_passowrd = findViewById(R.id.layout_password);
        layout_forgot = findViewById(R.id.layout_forgot);
        layout_login = findViewById(R.id.layout_login);
        layout_number = findViewById(R.id.layout_number);

       progressBar=findViewById(R.id.progressBar_login);
        email_done = findViewById(R.id.edit_email_login);
        email_done_forgot = findViewById(R.id.but_done_login);


        return_forgot = findViewById(R.id.but_return_forgot);

        return_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                returnCode();
            }
        });


        signInButton = findViewById(R.id.login_button);


        callbackManager=CallbackManager.Factory.create();

        signInButton.setReadPermissions(Arrays.asList("email","gaming_profile"));





              signInButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {




        signInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                Profile(loginResult.getAccessToken());






            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

                  }
              });










        forgot_password = findViewById(R.id.forgot_password);
        but_forgot_password = findViewById(R.id.but_forgot_password);


        edit_num = findViewById(R.id.edit_number_forgot);
        but_done_num = findViewById(R.id.but_done_numforgot);


        but_done_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (TextUtils.isEmpty(edit_num.getText().toString().trim())) {
                    edit_num.setError("Enter code");
                    edit_num.requestFocus();
                    return;
                }


                dataMess();
            }
        });


        but_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int num_password = forgot_password.getText().toString().trim().length();

                if (num_password < 8) {

                    Toast.makeText(getBaseContext(), getResources().getString(R.string.codesmall), Toast.LENGTH_LONG).show();
                    return;
                } else {

                    Savepassword();
                }


            }
        });


        email_done_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               login();

            }
        });


        text_forgot = findViewById(R.id.text_forgot);
        register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getBaseContext(), Register.class));
            }
        });
        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginIn();
            }
        });


        text_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_login.setVisibility(View.GONE);
                layout_forgot.setVisibility(View.VISIBLE);


            }
        });


    }

    public void loginIn() {


        final String user_email = email2.getText().toString();
        final String user_password = password2.getText().toString();


        if (TextUtils.isEmpty(user_email)) {
            email2.setError("Enter your email");
            email2.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(user_password)) {
            password2.setError("Enter your password");
            password2.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);


                    Toast.makeText(getBaseContext(), jsonObject.getString("message")+"poo",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    if (jsonObject.getBoolean("success")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        User user = new User(object.getString("token"));

                        VollySing.getInstanse(getBaseContext()).saveUser(user);
                        finish();
                        startActivity(new Intent(getBaseContext(), MainActivity.class));


                    } else {
                        Toast.makeText(getBaseContext(), "message", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getBaseContext(),"لا يوجد اتصال بالانترنت",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }


        }

        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json");
                map.put("email", user_email);
                map.put("password", user_password);


                return map;
            }


        };


        VollyLib.getInstance(getBaseContext()).addRequest(stringRequest);


    }




    private void dataMessage() {


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.GET_EMAIL+email_done.getText().toString().trim(),null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        int id = screen.getInt("id");

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

        //  return arrayList1;

    }


    public void deleteItem(int id) {


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, ServerApi.GET_CODE_RES + "/" + id, null, new Response.Listener<JSONObject>() {
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

                return map;
            }

        };
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);


    }


    private void Savepassword() {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();

        JSONObject js = new JSONObject();
        try {
            js.put("password", forgot_password.getText().toString().trim());
            js.put("email", email_done.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ServerApi.UPDATE_PASSWORD, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean("success")) {


                        layout_passowrd.setVisibility(View.GONE);
                        layout_login.setVisibility(View.VISIBLE);
                        Toast.makeText(getBaseContext(), "تم تغيير الرمز", Toast.LENGTH_SHORT).show();


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


    private void returnCode() {


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.GET_EMAIL+ email_done.getText().toString().trim(), null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        int id = screen.getInt("id");

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


    private void upDate(int id) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email_done.getText().toString().trim());

        } catch (Exception e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, ServerApi.GET_CODE_RES + "/" + id, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    if (response.getBoolean("success")) {

                        Toast.makeText(getBaseContext(), "تم الارسال", Toast.LENGTH_SHORT).show();


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


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.GET_EMAIL+ email_done.getText().toString().trim(), null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        int id = screen.getInt("id");


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





    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {



        Toast.makeText(getBaseContext(),connectionResult.toString()+"connection",Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);






    }


    private void Profile(final AccessToken accessToken){

        Bundle bundle=new Bundle();
        bundle.putString("fields","name");

        GraphRequest graphRequest=new GraphRequest(accessToken,"me",bundle,null, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {

                GraphRequest.GraphJSONObjectCallback callbackEmail = new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject me, GraphResponse response) {


                        try {

                            final JSONObject json = response.getJSONObject();
                            Toast.makeText(getApplicationContext(), json.toString()
                                    + "xxxxxxxxxxxx", Toast.LENGTH_LONG).show();


                            LogFcaebook(me.getString("name"),accessToken.getUserId());
                        } catch (Exception e) {

                        }

                    }
                };
                callbackEmail.onCompleted(response.getJSONObject(), response);
            }

        });


         graphRequest.executeAsync();















    }






    private void LogFcaebook(final String name_face, final String password_face){



        StringRequest stringRequest=new StringRequest(Request.Method.POST, ServerApi.GET_FACEBOOK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {

                                JSONObject object = jsonObject.getJSONObject("data");
                                VollySing.getInstanse(getBaseContext()).saveUser(new User(object.getString("token")));

                                Intent intent=new Intent(getBaseContext(),MainActivity.class);
                                startActivity(intent);
                                finish();

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
                map.put("name",name_face);
                map.put("password",password_face);
                map.put("c_password",password_face);

                return map ;
            }
        };
        VollyLib.getInstance(getBaseContext()).addRequest(stringRequest);

    }








    public void login() {



        StringRequest stringRequest = new StringRequest(Request.Method.POST,  ServerApi.GET_FORGOT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);



                        if (jsonObject.getString("message").equals("no")) {


                            Toast.makeText(getBaseContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            email_done.setError("هذا الحساب غير موجود");
                            return;

                        } else {


                            User user = new User(jsonObject.getString("message"));
                            Toast.makeText(getBaseContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                            VollySing.getInstanse(getBaseContext()).saveUser(user);
                            layout_forgot.setVisibility(View.GONE);
                            layout_number.setVisibility(View.VISIBLE);

                        }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getBaseContext(),"لا يوجد اتصال بالانترنت",Toast.LENGTH_SHORT).show();

            }


        }

        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json");
                map.put("email",email_done.getText().toString().trim());

                return map;
            }


        };


        VollyLib.getInstance(getBaseContext()).addRequest(stringRequest);


    }













    private void dataMess() {


        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();
        JSONObject js = new JSONObject();
        try {
            js.put("email", email_done.getText().toString().trim());
            js.put("code", edit_num.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ServerApi.GET_CODE, js, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {




                    if (response.getBoolean("success")) {

                        if (response.getString("message").equals("done")) {

                            layout_number.setVisibility(View.GONE);
                            layout_passowrd.setVisibility(View.VISIBLE);
                              dataMessage();

                        }

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
                map.put("Authorization", "Bearer " + token);
                return map;
            }

        };
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);



    }

















    }






