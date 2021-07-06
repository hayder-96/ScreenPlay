package com.screenplay.myscreenplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.screenplay.myscreenplay.Controller.Adapter_films;
import com.screenplay.myscreenplay.Controller.allScreenplay;
import com.screenplay.myscreenplay.Model.Iten_new;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Allfilm_view extends AppCompatActivity {


    private RecyclerView recyclerView;

    private ArrayList<Iten_new> arrayList;
    private Adapter_films adapter_films;
    private Intent intent;
    private int id;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allfilm_view);


        recyclerView = findViewById(R.id.recycler_film);
          progressBar=findViewById(R.id.progressBar_myfilm);

        intent = getIntent();

        id = intent.getIntExtra("id", -1);

        data(id);


    }




        private void data(int idi) {

        final String token=VollySing.getInstanse(getBaseContext()).getToken().getToken();
            final ArrayList<Iten_new> arrayList1 = new ArrayList<>();
            arrayList1.clear();
     progressBar.setVisibility(View.VISIBLE);
     recyclerView.setVisibility(View.GONE);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.INDEX_PROFILE_Url+idi, null, new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {

                    try {


                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject screen = jsonArray.getJSONObject(i);
                          Iten_new b=new Iten_new();

                            b.setId(screen.getInt("id"));
                            b.setTitle(screen.getString("title"));
                            String im = screen.getString("image");
                            b.setImage(im);


                            b.setCreated_at(screen.getString("created_at"));
                            arrayList1.add(b);

                        }

                        adapter_films=new Adapter_films(arrayList1, getBaseContext(), new allScreenplay() {
                            @Override
                            public void screen(int id) {
                                Intent intent=new Intent(getApplicationContext(),AllScreenplay_view.class);
                                intent.putExtra("id",id);
                                startActivity(intent);
                            }
                        });

                        recyclerView.setAdapter(adapter_films);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setHasFixedSize(true);
                        adapter_films.notifyDataSetChanged();
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




}












