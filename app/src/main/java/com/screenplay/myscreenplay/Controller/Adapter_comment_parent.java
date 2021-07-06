package com.screenplay.myscreenplay.Controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.screenplay.myscreenplay.Comment_Activity;
import com.screenplay.myscreenplay.Model.Item_comment;
import com.screenplay.myscreenplay.Model.Item_comment_part;
import com.screenplay.myscreenplay.R;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Adapter_comment_parent extends RecyclerView.Adapter<Adapter_comment_parent.MyHolder> {


    ArrayList<Item_comment> arrayList;
    Context context;
    static Adapter_comment_part adapter_comment_part;
    android.os.Handler handler=new Handler();
    public Adapter_comment_parent(ArrayList<Item_comment> arrayList,Context context) {
        this.arrayList = arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public Adapter_comment_parent.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_split, parent, false);


        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_comment_parent.MyHolder holder, final int position) {





        final Item_comment item_comment = arrayList.get(position);
         final int parent_id=item_comment.getId();
         final int main_screen=item_comment.getMain_screen();
         int user_id=item_comment.getUser_id();
        holder.name.setText(item_comment.getName());
        holder.time.setText(formatdate(item_comment.getCreate_at()));
        holder.comment.setText(item_comment.getDescription());
        if (item_comment.getImage().equals("no")) {

            holder.imageView.setImageResource(R.drawable.profileuser);
            Toast.makeText(context,item_comment.getImage()+"nullnull",Toast.LENGTH_SHORT).show();

        }else {
            Picasso.get().load(Uri.parse(item_comment.getImage())).resize(200, 200).into(holder.imageView);
        }












        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                holder.repl.setVisibility(View.VISIBLE);
                if (charSequence.toString().trim().equals("")){
                    holder.repl.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        holder.repl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataa(main_screen, holder.editText.getText().toString(), parent_id);



                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {



                                ArrayList<Item_comment_part> arrayList22=dataComment(parent_id,holder.recyclerView);
                                adapter_comment_part =new Adapter_comment_part(arrayList22,context);

                                holder.recyclerView.setAdapter(adapter_comment_part);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context);
                                holder.recyclerView.setLayoutManager(layoutManager);
                                holder.recyclerView.setHasFixedSize(true);
                                adapter_comment_part.notifyDataSetChanged();






                            }
                        });
                    }
                });

                thread.start();




            }


        });

        getComauth(user_id,holder.deleted,holder.edit);

        holder.deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(parent_id,position);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                show(parent_id,holder.comment);
                 Toast.makeText(context,parent_id+"",Toast.LENGTH_SHORT).show();

            }
        });



     ArrayList<Item_comment_part> arrayList22=dataComment(parent_id,holder.recyclerView);
         adapter_comment_part =new Adapter_comment_part(arrayList22,context);

        holder.recyclerView.setAdapter(adapter_comment_part);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setHasFixedSize(true);
        adapter_comment_part.notifyDataSetChanged();
    }

    public static void notii(){

        adapter_comment_part.notifyDataSetChanged();
    }



    private String formatdate(String newDate){
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        Date date= null;
        try {
            date = format.parse(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        return sm.format(date);

    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder  {

        TextView name,time,comment,pop;
        ImageView imageView;
        EditText editText;
        RecyclerView recyclerView;
        Button repl,deleted,edit;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name_comment);
            time=itemView.findViewById(R.id.date_comment);
            imageView=itemView.findViewById(R.id.image_comment);
             comment=itemView.findViewById(R.id.id_comment);
             editText=itemView.findViewById(R.id.edit_replied);
             recyclerView=itemView.findViewById(R.id.recycler_replied);
              repl=itemView.findViewById(R.id.but_repl);
              deleted=itemView.findViewById(R.id.but_delete_come);
            edit=itemView.findViewById(R.id.but_edit_come);












        }

    }
    private void dataSave(int parent_id,int main_screen_id,String name,String description,String image) {

        final String token = VollySing.getInstanse(context).getToken().getToken();

        JSONObject js = new JSONObject();
        try {
            js.put("main_screen_id",main_screen_id);
            js.put("parent_id",parent_id);
            js.put("descreption",description);
            js.put("image",image);
            js.put("name", name);


        } catch (Exception e) {
            e.printStackTrace();
        }


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ServerApi.ALL_COMMENT, js, new Response.Listener<JSONObject>() {
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

    private void dataa(final int ii, final String desc, final int parent) {

        final String token = VollySing.getInstanse(context).getToken().getToken();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.USERS_Data_Url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {



                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);


                        String name = screen.getString("name");
                        String image=screen.getString("image");
                        dataSave(parent,ii,name,desc,image);

                        Toast.makeText(context,name+"popopo",Toast.LENGTH_SHORT).show();

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


    private ArrayList<Item_comment_part>  dataComment(final int idi, final RecyclerView recyclerView) {

        final String token = VollySing.getInstanse(context).getToken().getToken();

        final ArrayList<Item_comment_part> arrayList = new ArrayList<>();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.COMMENT_PART+idi, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {



                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        Item_comment_part item_comment_part=new Item_comment_part();

                        item_comment_part.setId(screen.getInt("id"));
                        item_comment_part.setUser_id(screen.getInt("user_id"));
                        item_comment_part.setName(screen.getString("name"));
                        item_comment_part.setImage(screen.getString("image"));
                        item_comment_part.setMain_screen(screen.getInt("main_screen_id"));
                        item_comment_part.setDescription(screen.getString("descreption"));
                        item_comment_part.setCreate_at(screen.getString("created_at"));

                        Toast.makeText(context,screen.getString("name")+"ttt",Toast.LENGTH_SHORT).show();
                        arrayList.add(item_comment_part);
                          adapter_comment_part.notifyDataSetChanged();

                    }
//
//
//                   Adapter_comment_part adapter_comment_part=new Adapter_comment_part(arrayList,context);
//        recyclerView.setAdapter(adapter_comment_part);
//        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context);
//       recyclerView.setLayoutManager(layoutManager);






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

        return arrayList;

    }






    public void deleteItem(int id,int pos){

        final String token=VollySing.getInstanse(context).getToken().getToken();



        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.DELETE, ServerApi.ALL_COMMENT+"/"+id,null, new Response.Listener<JSONObject>() {
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
        Comment_Activity.notifi();
    }



    private void getComauth(final int iu, final Button b, final Button bb) {

        final String token = VollySing.getInstanse(context).getToken().getToken();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.ALL_COMMENT, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {



                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);


                       int user_id= screen.getInt("user_id");

                        if (user_id==iu){

                            Toast.makeText(context,user_id+"qqxxx",Toast.LENGTH_SHORT).show();
                            b.setVisibility(View.VISIBLE);
                            bb.setVisibility(View.VISIBLE);
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




    private void show(final int id, final TextView textView){


        final String token= VollySing.getInstanse(context).getToken().getToken();



        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, ServerApi.ALL_COMMENT+"/"+id,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.getBoolean("success")) {
                        JSONObject jsonObject = response.getJSONObject("data");


                       String dse=jsonObject.getString("descreption");
                       Toast.makeText(context,jsonObject.getString("descreption"),Toast.LENGTH_SHORT).show();
                       showAlert(dse,id,textView);

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





    private void showAlert(String desc, final int ii, final TextView textView) {

        final EditText editText=new EditText(context);
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("تعديل");
        builder.setCancelable(false);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(editText);
        editText.setText(desc);
        Toast.makeText(context,desc+"desc",Toast.LENGTH_SHORT).show();
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                String desc = editText.getText().toString().trim();

                if (TextUtils.isEmpty(desc)) {
                    editText.setError("لايمكن تركه فارغ");
                    editText.requestFocus();
                    return;
                }else {


                    upDate(ii, editText.getText().toString());
                    textView.setText(editText.getText().toString());
                }
            }
        });

        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

              dialogInterface.dismiss();
            }
        });
        builder.show();

    }





    private void upDate(int id,String de){


        final String token=VollySing.getInstanse(context).getToken().getToken();


        JSONObject js=new JSONObject();
        try {
            js.put("descreption",de);

        }catch (Exception e){
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT, ServerApi.ALL_COMMENT+"/"+id,js, new Response.Listener<JSONObject>() {
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




