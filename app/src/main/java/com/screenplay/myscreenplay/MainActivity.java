package com.screenplay.myscreenplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.screenplay.myscreenplay.Model.Item_screen;
import com.screenplay.myscreenplay.Model.Iten_new;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;
import com.screenplay.myscreenplay.regANDlog.Login_Activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.me.hardill.volley.multipart.MultipartRequest;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private static RecyclerAdapter recyclerAdapter;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private String token;
    private RequestQueue requestQueue;
    static ArrayList<Item_screen> arrayList;
    String namee;
    private ProgressBar progressBar;
     Intent intent;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        requestQueue = Volley.newRequestQueue(this);
        if (VollySing.getInstanse(this).is_Login()) {

            if (VollySing.getInstanse(this).getToken() != null) {
                token = VollySing.getInstanse(this).getToken().getToken();

            }
        } else {
            finish();
            Toast.makeText(getBaseContext(), "No save token", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, Login_Activity.class));
            return;
        }






















        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recycler1);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar=findViewById(R.id.progress_main);
        bottomNavigationView = findViewById(R.id.navigation);
        Intent intent = getIntent();
        namee = intent.getStringExtra("name");
        //Toast.makeText(this, namee + "ooo", Toast.LENGTH_SHORT).show();
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

                    case R.id.users:
                        Intent iii = new Intent(getBaseContext(), All_Users.class);
                        iii.putExtra("name1", namee);
                        startActivity(iii);
                        finish();
                        break;


                    case R.id.my_page:
                        Intent iiii = new Intent(getBaseContext(),MyProfile.class);
                        startActivity(iiii);
                        finish();
                        break;
                }

                return false;
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {



                arrayList = data();
                recyclerAdapter = new RecyclerAdapter(arrayList, getBaseContext(), new Conection() {
                    @Override
                    public void Con(int i) {
                        Intent intent = new Intent(getBaseContext(),ElementsRecycler.class);
                        intent.putExtra("add", i);
                        finish();
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(recyclerAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });






        arrayList = data();
        recyclerAdapter = new RecyclerAdapter(arrayList, this, new Conection() {
            @Override
            public void Con(int i) {
                Intent intent = new Intent(getBaseContext(), ElementsRecycler.class);
                intent.putExtra("add", i);
                finish();
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(recyclerAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter.notifyDataSetChanged();


        // showAlert();
        dataMessage();



        Intent intent1=getIntent();

    }


    public static void noti() {

        recyclerAdapter.notifyDataSetChanged();

    }


    private ArrayList<Item_screen> data() {
        final ArrayList<Item_screen> arrayList1 = new ArrayList<>();
        arrayList1.clear();
        progressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.GET_DATA_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);
                        Item_screen b = new Item_screen();

                        b.setId(screen.getInt("id"));
                        b.setTitle(screen.getString("title"));
                        String im = screen.getString("image");
                        b.setImage(im);
                        b.setCreated_at(screen.getString("created_at"));
                        arrayList1.add(b);
                        recyclerAdapter.notifyDataSetChanged();

                    }
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getBaseContext(), getResources().getString(R.string.nointrnet), Toast.LENGTH_SHORT).show();


            }

        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer  " + token);
                return map;


            }

        };

        VollyLib.getInstance(this).

            addRequest(jsonObjectRequest);

        return arrayList1;
        }


    private void dataMessage() {

        final String token = VollySing.getInstanse(getApplicationContext()).getToken().getToken();

//        Item_profile profile=new Item_profile();
//        int id=profile.getUser_id();
        ArrayList<Iten_new> arrayList1 =new ArrayList<>();




            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.MESSAGE_ACSEPT_Url, null, new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {

                    try {


                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject screen = jsonArray.getJSONObject(i);
                            Iten_new item_profile=new Iten_new();

//
//                            item_profile.setNamE(screen.getString("name"));
//                            item_profile.setId(screen.getInt("id"));

                            showAlert(screen.getString("name"),screen.getInt("id"));
//                        if (!im.) {
//                            b.setImage(im);

//
//                        }else {
                        //   Toast.makeText(getBaseContext(),screen.getString("name"),Toast.LENGTH_SHORT).show();
//                            b.setImage(null);
//                        }

                          //  arrayList1.add(item_profile);


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
                    map.put("Authorization", "Bearer  " + token);
                    return map;
                }

            };
            VollyLib.getInstance(this).addRequest(jsonObjectRequest);

          //  return arrayList1;

    }



    private void showAlert(String name, final int ii) {
//
//        ArrayList<Iten_new> arrayList = dataMessage();
//        for (final Iten_new it : arrayList) {

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("طلب");
            builder.setMessage("يريد" + name + "رؤية محتوياتك هل تسمخ له");
            builder.setCancelable(false);
            builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    upDate("yes",ii);
                }
            });

            builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    deleteItemMessage(ii);
                }
            });
            builder.show();

        }


    private void upDate(String answer,int ty){


        final String token=VollySing.getInstanse(this).getToken().getToken();
//        Item_profile item_profile=new Item_profile();
//
//          int id=item_profile.getId();

        Toast.makeText(getBaseContext(),ty+"",Toast.LENGTH_SHORT).show();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("visibl",answer);

        }catch (Exception e){
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT, ServerApi.MESSAGE_Data_Url+"/"+ty,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    if (response.getBoolean("success")){
                        Toast.makeText(getBaseContext(),response.getString("message"),Toast.LENGTH_SHORT).show();

                        //progressDialog.dismiss();
                        finish();
                        startActivity(new Intent(getBaseContext(),MainActivity.class));
                    }else {
                        finish();
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
                map.put("Authorization","Bearer "+ token);
                return map;
            }

        };
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);







    }




    public void deleteItemMessage(int id){

        final String token=VollySing.getInstanse(getBaseContext()).getToken().getToken();



        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.DELETE, ServerApi.MESSAGE_Data_Url+"/"+id,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean("success")){
                        Toast.makeText(getBaseContext(),response.getString("message"),Toast.LENGTH_SHORT).show();

                        //progressDialog.dismiss();

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
                map.put("Authorization","Bearer "+ token);
                return map;
            }

        };
        VollyLib.getInstance(getBaseContext()).addRequest(jsonObjectRequest);


    }


















































//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode==1 && resultCode==RESULT_OK){
//            Splitdata splitdata=Splitdata.GetInstance(this);
//            splitdata.Open();
//
//            arrayList=splitdata.GetData();
//            Save(arrayList);
//            recyclerAdapter.setArrayList(arrayList);
//            if (arrayList==null){
//            }else {
//                splitdata.Close();
//                recyclerView.setAdapter(recyclerAdapter);
//                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
//                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.setHasFixedSize(true);
//
//                recyclerAdapter.notifyDataSetChanged();
//            }
//
//        }
//    }

//        private void Save(ArrayList<ItemRecycler> arrayList1){
//            SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
//            SharedPreferences.Editor editor=sharedPreferences.edit();
//            Gson gson=new Gson();
//           String arrayname=gson.toJson(arrayList1);
//                 editor.putString("key",arrayname);
//            editor.apply();
//        }
//
//        private void Loaded(){
//            SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
//            Gson gson = new Gson();
//            String name=sharedPreferences.getString("key",null);
//            Type type=new TypeToken<ArrayList<ItemRecycler>>(){}.getType();
//            arrayList=gson.fromJson(name,type);
//            if (arrayList==null){
//                arrayList=new ArrayList<>();
//            }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_menu,menu);




        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_item:

                Intent intent=new Intent(getBaseContext(),ElementsRecycler.class);
                intent.putExtra("add",-1);
                finish();
                startActivity(intent);
                return true;
            case R.id.out_log:
                VollySing.getInstanse(getBaseContext()).user_Logout();
                finish();
                startActivity(new Intent(getBaseContext(),Login_Activity.class));
        }

        return false;
    }














}












