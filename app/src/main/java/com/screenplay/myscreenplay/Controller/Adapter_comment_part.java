package com.screenplay.myscreenplay.Controller;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.InputType;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class Adapter_comment_part extends RecyclerView.Adapter<Adapter_comment_part.MyHolder> {


    ArrayList<Item_comment_part> arrayList;
    Context context;
    public Adapter_comment_part(ArrayList<Item_comment_part> arrayList,Context context) {
        this.context=context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Adapter_comment_part.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_part, parent, false);


        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_comment_part.MyHolder holder, final int position) {

        Item_comment_part item_comment_part = arrayList.get(position);
         int user_id=item_comment_part.getUser_id();
         final int id=item_comment_part.getId();
        holder.name.setText(item_comment_part .getName());
        holder.time.setText(formatdate(item_comment_part .getCreate_at()));
        holder.comment.setText(item_comment_part .getDescription());
        if (item_comment_part.getImage().equals("no")) {

            holder.imageView.setImageResource(R.drawable.screenback);


        }else {
            Picasso.get().load(Uri.parse(item_comment_part.getImage())).resize(200, 200).into(holder.imageView);
        }


        holder.deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteItem(id,position);
            }
        });

        holder.editing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                show(id,holder.comment);
            }
        });

        getComauth(user_id,holder.deleted,holder.editing);
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

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView name,time,comment;
        ImageView imageView;
        Button deleted,editing;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name_comment2);
            time=itemView.findViewById(R.id.date_comment2);
            imageView=itemView.findViewById(R.id.image_comment2);
             comment=itemView.findViewById(R.id.id_comment2);
              deleted=itemView.findViewById(R.id.but_delete_coco);
              editing=itemView.findViewById(R.id.but_edit_coco);

        }
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

        Adapter_comment_parent.notii();
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
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String desc = editText.getText().toString();

                if (TextUtils.isEmpty(desc)) {
                    editText.setError("لايمكن تركه فارغ");
                    editText.requestFocus();
                    return;
                }




                upDate(ii,editText.getText().toString());
                textView.setText(editText.getText().toString());
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
