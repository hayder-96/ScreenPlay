package com.screenplay.myscreenplay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.screenplay.myscreenplay.Model.Itemtow;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class listscreen extends AppCompatActivity {


    private int nm;

    RecyclerView recyclerView;
    RecyclerAdabtertow recyclerAdabtertow;
    ArrayList<Itemtow> arrayList=new ArrayList<>();
    private Toolbar toolbar;
    ProgressBar progressBar;
      View view;
    int tt;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listscreen);

         view=getLayoutInflater().inflate(R.layout.split_recyclertow,(ViewGroup)findViewById(R.id.linear1));
        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recycler2);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      progressBar=findViewById(R.id.progressBar_listscreen);

        final Intent intent = getIntent();
        nm = intent.getIntExtra("title",-1);

        start(nm);


    }


    private void start(final int m){

        ArrayList<Itemtow> arrayList1=dataScene(m);
        recyclerAdabtertow = new RecyclerAdabtertow(arrayList1, new Onclickitem() {
            @Override
            public void Onclick(int id) {
                Intent intent1=new Intent(getBaseContext(),activity_myscreen.class);
                intent1.putExtra("id",id);
                intent1.putExtra("idi",m);
                startActivityForResult(intent1,1);
                finish();

            }
        });
        recyclerView.setAdapter(recyclerAdabtertow);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerAdabtertow.notifyDataSetChanged();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ArrayList<Itemtow> arrayList1=dataScene(m);
                recyclerAdabtertow = new RecyclerAdabtertow(arrayList1, new Onclickitem() {
                    @Override
                    public void Onclick(int id) {
                        Intent intent1=new Intent(getBaseContext(),activity_myscreen.class);
                        intent1.putExtra("id",id);
                        intent1.putExtra("idi",m);
                        startActivity(intent1);
                        finish();

                    }
                });
                recyclerView.setAdapter(recyclerAdabtertow);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerAdabtertow.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {




        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.activity_add_screen,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_screen:
                Intent intent = new Intent(this, activity_myscreen.class);
                intent.putExtra("id", -1);
                intent.putExtra("idi",nm);
                finish();
                startActivity(intent);
                break;
            case R.id.view_all:
              Intent intent1=new Intent(this,View_All.class);
              startActivity(intent1);
              break;

        }
        return false;
    }




    private ArrayList<Itemtow> dataScene(int id) {
        final String token= VollySing.getInstanse(getBaseContext()).getToken().getToken();
        final ArrayList<Itemtow> arrayList1 = new ArrayList<>();
        arrayList1.clear();

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.INDEX_Data_Url+id, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);
                        Itemtow itemtow=new Itemtow();

                        itemtow.setId(screen.getInt("id"));
                        itemtow.setNumber(screen.getString("numScene"));
                        itemtow.setScene_location(screen.getString("nameScene"));
                        itemtow.setScene_description(screen.getString("contentScene"));
                        itemtow.setDialogue(screen.getString("dialogueScene"));

                        arrayList1.add(itemtow);
                        recyclerAdabtertow.notifyDataSetChanged();


                    }
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(getBaseContext(),getResources().getString(R.string.nointrnet),Toast.LENGTH_SHORT).show();
            }


        })
        {
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
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);

        return arrayList1;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==1&&resultCode==RESULT_OK){


           int i=data.getIntExtra("back",-1);
           Toast.makeText(getApplicationContext(),i+"",Toast.LENGTH_SHORT).show();
            start(i);

        }
    }
}



