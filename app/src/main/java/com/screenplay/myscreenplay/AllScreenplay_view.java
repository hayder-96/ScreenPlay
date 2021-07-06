package com.screenplay.myscreenplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.screenplay.myscreenplay.Model.Item_AllScreen;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllScreenplay_view extends AppCompatActivity {


    private Intent intent;
    private int id;
    TextView textView, size;
    LikeButton likeButton;
    Button comment;
    ScrollView scrollView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_screen_play);

          comment=findViewById(R.id.but_comment);
        textView = findViewById(R.id.id_text_screen);
        size = findViewById(R.id.text_size);
        likeButton = findViewById(R.id.heart_button);
         scrollView=findViewById(R.id.scrool);
         progressBar=findViewById(R.id.progressBar_allscreen);
        intent = getIntent();

        id = intent.getIntExtra("id", -1);





        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getBaseContext(),Comment_Activity.class);
                intent.putExtra("main_screen_id",id);
                startActivity(intent);
            }
        });










        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                dataa(id);


                int sz=Integer.parseInt(size.getText().toString().trim());
                size.setText(sz+1+"");
            }

            @Override
            public void unLiked(LikeButton likeButton) {

                getLike(id);
                int sz=Integer.parseInt(size.getText().toString().trim());
                size.setText(sz-1+"");


            }
        });


        data(id);

        likeAll(id);


        getLikeTest(id);
    }



    private void data(int idi) {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();
        final ArrayList<Item_AllScreen> arrayList1 = new ArrayList<>();
        final StringBuffer stringBuffer = new StringBuffer();
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        arrayList1.clear();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.INDEX_Data_Url + idi, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);
                        Item_AllScreen item_allScreen = new Item_AllScreen();

                        String num = screen.getString("numScene");
                        String nam = screen.getString("nameScene");
                        String con = screen.getString("contentScene");
                        String dia = screen.getString("dialogueScene");
                        Toast.makeText(getBaseContext(), nam, Toast.LENGTH_SHORT).show();

                        stringBuffer.append(num + "\n" + nam + "\n" + con + "\n" + dia+"\n");


                    }
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    textView.setText(stringBuffer);


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getBaseContext(),getResources().getString(R.string.nointrnet),Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
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


    private void dataSave(int i, String name, String country, String image) {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();

        JSONObject js = new JSONObject();
        try {
            js.put("main_screen_id", i);
            js.put("name", name);
            js.put("country", country);
            js.put("image", image);
             js.put("boolean","true");
        } catch (Exception e) {
            e.printStackTrace();
        }


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ServerApi.LIKE_ALL, js, new Response.Listener<JSONObject>() {
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


    private void dataa(final int ii) {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.USERS_Data_Url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);



                        String name = screen.getString("name");
                        String image = screen.getString("image");
                        String country = screen.getString("country");


                        dataSave(ii, name, country, image);


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


    public void deleteItem(int id) {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, ServerApi.LIKE_ALL + "/" + id, null, new Response.Listener<JSONObject>() {
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
        VollyLib.getInstance(getBaseContext()).addRequest(jsonObjectRequest);


    }


    private void getLike(int idi) {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.LIKE_ITEM_USER+idi, null, new Response.Listener<JSONObject>() {


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
                map.put("Authorization", "Bearer  " + token);
                return map;
            }

        };
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);


    }


    private void likeAll(int idi) {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();
        final ArrayList<Integer> arrayList1 = new ArrayList<>();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.LIKE_ITEM + idi, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        int id = screen.getInt("id");


                        Toast.makeText(getBaseContext(), id + "ididi", Toast.LENGTH_SHORT).show();
                        arrayList1.add(id);

                    }

                    size.setText(arrayList1.size() + "");


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







    private void getLikeTest(int id) {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.LIKE_ITEM_USER+id, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);


                        String bol=screen.getString("boolean");

                        if (bol.contains("true")){

                            likeButton.setLiked(true);
                        }else {
                            likeButton.setLiked(false);
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
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);


    }


}

