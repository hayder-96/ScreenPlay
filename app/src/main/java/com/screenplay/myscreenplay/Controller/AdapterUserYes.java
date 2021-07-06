package com.screenplay.myscreenplay.Controller;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.screenplay.myscreenplay.All_Users;
import com.screenplay.myscreenplay.Model.ItemAccept;
import com.screenplay.myscreenplay.R;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterUserYes extends RecyclerView.Adapter<AdapterUserYes.MyHolder> {

    ArrayList<ItemAccept> arrayList;
     NextFilm nextFilm;
    Context context;
    public AdapterUserYes(ArrayList<ItemAccept> arrayList,Context context,NextFilm nextFilm) {
        this.arrayList = arrayList;
        this.context=context;
        this.nextFilm=nextFilm;
    }

    @NonNull
    @Override
    public AdapterUserYes.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.split_profile,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUserYes.MyHolder holder, final int position) {

       ItemAccept item_yes=arrayList.get(position);
       final int id=item_yes.getId();
       final int user_id=item_yes.getUser_id();
        holder.name.setText(item_yes.getName());
        holder.country.setText(item_yes.getCountry());
        if (item_yes.getImage()!=null && !item_yes.getImage().isEmpty()){
            holder.imageView.setImageURI(Uri.parse(item_yes.getImage()));
        }

        holder.view.setTag(user_id);
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteItem(id,position);
                 dataMessges(user_id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        Button view, cancel;
        TextView name, country;
        ImageView imageView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.inage_user);
            name = itemView.findViewById(R.id.name_user);
            country = itemView.findViewById(R.id.country_user);
            view = itemView.findViewById(R.id.but_view);
            cancel = itemView.findViewById(R.id.but_sendreq);
            cancel.setText("الغاء");

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int id= (int) view.getTag();
                    nextFilm.next(id);

                }
            });
        }
    }




    public void deleteItem(int id,int pos){

        final String token=VollySing.getInstanse(context).getToken().getToken();



        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.DELETE, ServerApi.MESSAGE_FRIEND_MY_YOU+"/"+id,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean("success")){
                        Toast.makeText(context,response.getString("message"),Toast.LENGTH_SHORT).show();

                        //progressDialog.dismiss();

                    }else {

                        Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
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
        VollyLib.getInstance(context).addRequest(jsonObjectRequest);

        arrayList.remove(pos);
        All_Users.noti();

    }




    private void dataMessges(final int ii) {

        final String token = VollySing.getInstanse(context).getToken().getToken();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.MESSAGE_Data_Url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);
                        int id = screen.getInt("id");
                        int name_id = screen.getInt("name_id");

                        if (name_id == ii) {

                            deleteItemMessage(id);

                        } else {
                            Toast.makeText(context, "لم تتم الموافقة", Toast.LENGTH_SHORT).show();

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
    public void deleteItemMessage(int id){

        final String token=VollySing.getInstanse(context).getToken().getToken();



        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.DELETE, ServerApi.MESSAGE_Data_Url+"/"+id,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean("success")){
                        Toast.makeText(context,response.getString("message"),Toast.LENGTH_SHORT).show();

                        //progressDialog.dismiss();

                    }else {

                        Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
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
        VollyLib.getInstance(context).addRequest(jsonObjectRequest);


    }


}
