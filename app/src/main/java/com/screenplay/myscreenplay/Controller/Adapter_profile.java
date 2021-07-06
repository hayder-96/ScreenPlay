package com.screenplay.myscreenplay.Controller;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.screenplay.myscreenplay.Model.Item_profile;
import com.screenplay.myscreenplay.R;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adapter_profile extends RecyclerView.Adapter<Adapter_profile.MyHolder> {

    ArrayList<Item_profile> arrayList;
    Context context;
    NextFilm nextFilm;
    public Adapter_profile( ArrayList<Item_profile> arrayList, Context context,NextFilm nextFilm) {
        this.arrayList = arrayList;
        this.context = context;
         this.nextFilm=nextFilm;
    }


    @NonNull
    @Override
    public Adapter_profile.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.split_profile, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_profile.MyHolder holder, int position) {

        final Item_profile item_profile = arrayList.get(position);

       final int id=item_profile.getId();
       final String name=item_profile.getName();
       final String cou=item_profile.getCountry();
       final String ima=item_profile.getImage();

        holder.name.setText(item_profile.getName());
        if (item_profile.getImage().equals("no")) {

            holder.imageView.setImageResource(R.drawable.screenback);


        }else {
            Picasso.get().load(Uri.parse(item_profile.getImage())).resize(200, 200).into(holder.imageView);
        }
        holder.country.setText(item_profile.getCountry());
         final int i = item_profile.getUser_id();



         holder.view.setTag(i);






        holder.send_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                holder.send_req.setText("تم الارسال");
                dataa(i);
                 dataSaveFriend(id,i,name,cou,ima);


            }


        });




        dataEnable(i,holder.send_req,holder.progressBar);

        dataMessges(holder.view, holder.send_req,i,id,holder.progressBar);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        Button view, send_req;
        TextView name, country;
        ImageView imageView;
        ProgressBar progressBar;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.inage_user);
            name = itemView.findViewById(R.id.name_user);
            country = itemView.findViewById(R.id.country_user);
            view = itemView.findViewById(R.id.but_view);
            send_req = itemView.findViewById(R.id.but_sendreq);
            progressBar = itemView.findViewById(R.id.progressusers2);
            view.setEnabled(false);

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int i= (int) view.getTag();
                    nextFilm.next(i);

                }
            });
        }
    }

    private void dataSave(int name_id,String name) {

        final String token = VollySing.getInstanse(context).getToken().getToken();

        JSONObject js = new JSONObject();
        try {
            js.put("name_id", name_id);
            js.put("name", name);
            js.put("enable","false");

        } catch (Exception e) {
            e.printStackTrace();
        }


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ServerApi.MESSAGE_Data_Url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean("success")) {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();

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
        VollyLib.getInstance(context).addRequest(jsonObjectRequest);


    }


    private void dataMessges(final Button button, final Button send, final int ii, final int id, final ProgressBar progressBar) {

        progressBar.setVisibility(View.VISIBLE);
        final String token = VollySing.getInstanse(context).getToken().getToken();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.MESSAGE_Data_Url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    progressBar.setVisibility(View.GONE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        String nn = screen.getString("visibl");
                        int name_id = screen.getInt("name_id");

                        if (name_id == ii) {
                            if (nn.equals("yes")) {

                                upDateFriend("yes", id);

                                button.setEnabled(true);
                                Toast.makeText(context, "تمت الموافقة" + "YOU", Toast.LENGTH_SHORT).show();

                                send.setVisibility(View.INVISIBLE);

                            } else {
                                Toast.makeText(context, "لم تتم الموافقة", Toast.LENGTH_SHORT).show();

                            }
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
        VollyLib.getInstance(context).addRequest(jsonObjectRequest);


    }


    private void dataa(final int ii) {

        final String token = VollySing.getInstanse(context).getToken().getToken();

        final ArrayList<String> arrayList = new ArrayList<>();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.USERS_Data_Url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {



                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);


                        String pp = screen.getString("name");
                        String image=screen.getString("image");

                        dataSave(ii, pp);


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
        VollyLib.getInstance(context).addRequest(jsonObjectRequest);


    }











    private void dataSaveFriend(int id, int name_id, String name, String country,String image) {

        final String token = VollySing.getInstanse(context).getToken().getToken();

        JSONObject js = new JSONObject();
        try {
            js.put("id",id);
            js.put("name_id",name_id);
            js.put("country",country);
            js.put("name", name);
            js.put("image",image);

        } catch (Exception e) {
            e.printStackTrace();
        }


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ServerApi.MESSAGE_FRIEND_MY_YOU, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean("success")) {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();


                        Toast.makeText(context, "input new", Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();

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
        VollyLib.getInstance(context).addRequest(jsonObjectRequest);


    }







    private void upDateFriend(String answer,int id){


        final String token=VollySing.getInstanse(context).getToken().getToken();
//        Item_profile item_profile=new Item_profile();
//
//          int id=item_profile.getId();


        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("visibl",answer);

        }catch (Exception e){
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT, ServerApi.MESSAGE_FRIEND_MY_YOU+"/"+id,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    if (response.getBoolean("success")){
                        Toast.makeText(context,response.getString("message"),Toast.LENGTH_SHORT).show();

                        //progressDialog.dismiss();

                    }else {

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
        VollyLib.getInstance(context).addRequest(jsonObjectRequest);



    }






    private void dataEnable(final int ii, final Button button, final ProgressBar progressBar) {

//        progressBar.setVisibility(View.VISIBLE);
        final String token = VollySing.getInstanse(context).getToken().getToken();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.MESSAGE_Data_Url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");
                  //  progressBar.setVisibility(View.GONE);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        int name_id = screen.getInt("name_id");
                        String b = screen.getString("enable");
                        if (name_id == ii) {

                            if(b.equals("false")){
                                Toast.makeText(context,b+"enable",Toast.LENGTH_SHORT).show();

                                button.setEnabled(false);


                            }




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
        VollyLib.getInstance(context).addRequest(jsonObjectRequest);



    }
    }


