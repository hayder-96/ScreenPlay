package com.screenplay.myscreenplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.screenplay.myscreenplay.Controller.AdapterUserYes;
import com.screenplay.myscreenplay.Controller.Adapter_profile;

import com.screenplay.myscreenplay.Controller.NextFilm;
import com.screenplay.myscreenplay.Model.ItemAccept;
import com.screenplay.myscreenplay.Model.Item_profile;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class All_Users extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    private ArrayList<Item_profile> arrayList =new ArrayList<>();
     private ArrayList<ItemAccept> accepts;
    private static   Adapter_profile adapter_profile;
    private static AdapterUserYes adapterUserYes;
    private RecyclerView recyclerView;
    private LinearLayout layout_users;
    private ProgressBar bar;

    private Button all,friend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__users);


        all=findViewById(R.id.but_all);
        friend=findViewById(R.id.but_friend);
        recyclerView=findViewById(R.id.rec);
        layout_users=findViewById(R.id.layout_users);
        bar=findViewById(R.id.progressBar_users);

      arrayList=data();







        adapter_profile = new Adapter_profile(arrayList, this, new NextFilm() {
            @Override
            public void next(int id) {
                Intent intent=new Intent(getBaseContext(), Allfilm_view.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

                    recyclerView.setAdapter(adapter_profile);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    adapter_profile.notifyDataSetChanged();


        bottomNavigationView = findViewById(R.id.navigation_users);
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
                        Intent iii = new Intent(getBaseContext(),MainActivity.class);
                        finish();
                        startActivity(iii);
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


        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               arrayList=data();

                adapter_profile = new Adapter_profile(arrayList, getBaseContext(), new NextFilm() {
                    @Override
                    public void next(int id) {
                        Intent intent=new Intent(getBaseContext(), Allfilm_view.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                    }
                });

                recyclerView.setAdapter(adapter_profile);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                adapter_profile.notifyDataSetChanged();

            }
        });

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        accepts=dataYou();


                adapterUserYes = new AdapterUserYes(accepts, getBaseContext(), new NextFilm() {
                    @Override
                    public void next(int id) {
                        Intent intent=new Intent(getBaseContext(),Allfilm_view.class);
                        intent.putExtra("id",id);
                       startActivity(intent);
                    }
                });

                recyclerView.setAdapter(adapterUserYes);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                adapterUserYes.notifyDataSetChanged();






            }
        });



    }




    public static void noti() {


        adapterUserYes.notifyDataSetChanged();

    }




    private ArrayList<Item_profile> data() {

        final String token= VollySing.getInstanse(getApplicationContext()).getToken().getToken();
        final ArrayList<Item_profile> arrayList1 = new ArrayList<>();
      bar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.PROFILE_ONE_Url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);
                        Item_profile item_profile = new Item_profile();

                        item_profile.setId(screen.getInt("id"));
                        item_profile.setName(screen.getString("name"));
                        item_profile.setCountry(screen.getString("country"));
                        item_profile.setImage(screen.getString("image"));
                        item_profile.setUser_id(screen.getInt("user_id"));


                        arrayList1.add(item_profile);
                        adapter_profile.notifyDataSetChanged();

                    }
                    bar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
//                    adapter_profile = new Adapter_profile(arrayList1,getBaseContext());
//
//                    recyclerView.setAdapter(adapter_profile);
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
//                    recyclerView.setLayoutManager(layoutManager);
//                    recyclerView.setHasFixedSize(true);
//                    adapter_profile.notifyDataSetChanged();


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),getResources().getString(R.string.nointrnet),Toast.LENGTH_SHORT).show();

                bar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
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


        return arrayList1;

    }













    private ArrayList<ItemAccept> dataMessges() {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();

        final ArrayList<Integer> arrayList=new ArrayList<>();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.MESSAGE_Data_Url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);


                        String nn = screen.getString("visibl");
                        int name_id = screen.getInt("name_id");
                        int user_id = screen.getInt("user_id");



                        if (nn.equals("yes")) {


                            Toast.makeText(getBaseContext(), "تمت الموافقة" + name_id + "/" + user_id, Toast.LENGTH_SHORT).show();

                            accepts=dataMe(name_id);

                            // dataYou(user_id);


                        } else {
                            Toast.makeText(getBaseContext(), "لم تتم الموافقة", Toast.LENGTH_SHORT).show();

                        }


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


        return accepts;

    }










    private ArrayList<ItemAccept> dataMe(int id) {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();

        final ArrayList<ItemAccept> accepts=new ArrayList<>();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.MESSAGE_ACSEPT_MY_YOU+id, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        ItemAccept itemAccept = new ItemAccept();
                        itemAccept.setId(screen.getInt("id"));
                        itemAccept.setName(screen.getString("name"));
                        itemAccept.setCountry(screen.getString("country"));
                        itemAccept.setImage(screen.getString("image"));
                        itemAccept.setUser_id(screen.getInt("user_id"));

                        Toast.makeText(getBaseContext(),screen.getString("name")+"name_id",Toast.LENGTH_SHORT).show();
                        accepts.add(itemAccept);

                    }

//
//
//                    AdapterUserYes adapterUserYes=new AdapterUserYes(accepts,getBaseContext());
//                    recyclerView.setAdapter(adapterUserYes);
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
//                    recyclerView.setLayoutManager(layoutManager);
//                    recyclerView.setHasFixedSize(true);
//                    adapter_profile.notifyDataSetChanged();







//                    arrayListf=new ArrayList<>();
//
//
//                    arrayListf.add(new ItemFrag(FragmentUsers.newInstancee(accepts),"الاصدقاء"));
//                    AdapterTap adapterTap=new AdapterTap(getSupportFragmentManager(),arrayListf);
//                    viewPager.setAdapter(adapterTap);
//                    tabLayout.setupWithViewPager(viewPager);
//
//
//                    adapter_profile.notifyDataSetChanged();
//
//

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



        return accepts;
    }




    private ArrayList<ItemAccept> dataYou() {

        bar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);


        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();

        final ArrayList<ItemAccept> accepts=new ArrayList<>();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.MESSAGE_FRIEND_MY_YOU, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        ItemAccept itemAccept = new ItemAccept();
                        itemAccept.setId(screen.getInt("id"));
                        itemAccept.setName(screen.getString("name"));
                        itemAccept.setCountry(screen.getString("country"));
                        itemAccept.setImage(screen.getString("image"));
                        itemAccept.setUser_id(screen.getInt("name_id"));

                        accepts.add(itemAccept);

                        adapterUserYes.notifyDataSetChanged();

                    }

                    bar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);


//                    AdapterUserYes adapterUserYes=new AdapterUserYes(accepts,getBaseContext());
//                    recyclerView.setAdapter(adapterUserYes);
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
//                    recyclerView.setLayoutManager(layoutManager);
//                    recyclerView.setHasFixedSize(true);
//                    adapter_profile.notifyDataSetChanged();







//                    arrayListf=new ArrayList<>();
//
//
//                    arrayListf.add(new ItemFrag(FragmentUsers.newInstancee(accepts),"الاصدقاء"));
//                    AdapterTap adapterTap=new AdapterTap(getSupportFragmentManager(),arrayListf);
//                    viewPager.setAdapter(adapterTap);
//                    tabLayout.setupWithViewPager(viewPager);
//
//
//                    adapter_profile.notifyDataSetChanged();
//
//

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),getResources().getString(R.string.nointrnet),Toast.LENGTH_SHORT).show();

                bar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
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


        return accepts;

    }

        }






