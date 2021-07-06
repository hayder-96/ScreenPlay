package com.screenplay.myscreenplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.screenplay.myscreenplay.Model.Itemtow;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public  class activity_myscreen extends AppCompatActivity {

    private EditText location, description, Dialogue, number_id;


    int id;
    int idi;
    private TextView text_number, text_location, text_description, text_dialogue;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myscreen);

        progressBar=findViewById(R.id.progressBar_myscreen);
        location = findViewById(R.id.Scene_location);
        description = findViewById(R.id.Scene_description);
        Dialogue = findViewById(R.id.Dialogue);
        number_id = findViewById(R.id.number_screen);
        text_number = findViewById(R.id.text_number);
        text_location = findViewById(R.id.text_location);
        text_description = findViewById(R.id.text_descreption);
        text_dialogue = findViewById(R.id.text_Dialogue);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Intent intent = getIntent();
        idi=intent.getIntExtra("idi",-1);
        Toast.makeText(getApplicationContext(),idi+"idi up",Toast.LENGTH_SHORT).show();

        id = intent.getIntExtra("id", -1);
        if (id == -1) {

            number_id.setVisibility(View.VISIBLE);
            location.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            Dialogue.setVisibility(View.VISIBLE);


        } else {

            text_number.setVisibility(View.VISIBLE);
            text_location.setVisibility(View.VISIBLE);
            text_description.setVisibility(View.VISIBLE);
            text_dialogue.setVisibility(View.VISIBLE);

         show(id);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_delete, menu);
        MenuItem save = menu.findItem(R.id.save_screen);
        MenuItem delete = menu.findItem(R.id.delete_screen);
        MenuItem edit = menu.findItem(R.id.edit_screen);
        if (id == -1) {
            edit.setVisible(false);
            delete.setVisible(false);
            save.setVisible(true);
        } else {
            edit.setVisible(true);
            delete.setVisible(true);
            save.setVisible(false);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_screen:

                if (id == -1) {

                    dataSave();

                    return true;
                } else {


                  upDate(id);

                    break;
                }


            case R.id.delete_screen:
                RemoveItem();

                return true;
            case R.id.edit_screen:
                MenuItem save = toolbar.getMenu().findItem(R.id.save_screen);
                MenuItem edit = toolbar.getMenu().findItem(R.id.edit_screen);
                MenuItem delete = toolbar.getMenu().findItem(R.id.delete_screen);
                save.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);

                text_number.setVisibility(View.GONE);
                text_location.setVisibility(View.GONE);
                text_description.setVisibility(View.GONE);
                text_dialogue.setVisibility(View.GONE);



              showItem(id);
                      break;

        }

        return false;
    }

    private void RemoveItem() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.delete));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                deleteItem(id);

                Intent intent=new Intent();
                intent.putExtra("back",idi);
                setResult(RESULT_OK,intent);
                finish();



            }

        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }



    private void dataSave() {

        final String token = VollySing.getInstanse(this).getToken().getToken();
        String loci = location.getText().toString();
        String desc = description.getText().toString();
        String dialo = Dialogue.getText().toString();
        String number_i = number_id.getText().toString();

        if (TextUtils.isEmpty(loci)) {
            location.setError("Enter title");
            location.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(desc)) {
            description.setError("Enter title");
            description.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(dialo)) {
            Dialogue.setError("Enter title");
            Dialogue.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(number_i)) {
            number_id.setError("Enter title");
            number_id.requestFocus();
            return;
        }


        JSONObject js=new JSONObject();
        try{
            js.put("numScene",number_i);
            js.put("nameScene",loci);
            js.put("contentScene",desc);
            js.put("dialogueScene",dialo);
            js.put("main_screen_id",idi);
        }catch (Exception e){
            e.printStackTrace();
        }




        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, ServerApi.GET_SCENE,js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean("success")){
                        Toast.makeText(getBaseContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                        Toast.makeText(getBaseContext(),"add scene",Toast.LENGTH_SHORT).show();



                        Intent intent=new Intent();
                        intent.putExtra("back",idi);
                        setResult(RESULT_OK,intent);
                        finish();
                    }else {
                        finish();
                        Toast.makeText(getBaseContext(),"error",Toast.LENGTH_SHORT).show();

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





    private void show(int id){



        final String token= VollySing.getInstanse(this).getToken().getToken();


        progressBar.setVisibility(View.VISIBLE);

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, ServerApi.GET_SCENE+"/"+id,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.getBoolean("success")) {
                        JSONObject jsonObject = response.getJSONObject("data");

                        text_number.setText(jsonObject.getString("numScene"));
                        text_location.setText(jsonObject.getString("nameScene"));
                        text_description.setText(jsonObject.getString("contentScene"));
                        text_dialogue.setText(jsonObject.getString("dialogueScene"));

                    }
                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(),getResources().getString(R.string.nointrnet),Toast.LENGTH_SHORT).show();

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





    private void showItem(int id){


        final String token= VollySing.getInstanse(this).getToken().getToken();

        progressBar.setVisibility(View.VISIBLE);

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, ServerApi.GET_SCENE+"/"+id,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.getBoolean("success")) {
                        JSONObject jsonObject = response.getJSONObject("data");

                        number_id.setText(jsonObject.getString("numScene"));
                        location.setText(jsonObject.getString("nameScene"));
                        description.setText(jsonObject.getString("contentScene"));
                        Dialogue.setText(jsonObject.getString("dialogueScene"));

                    }
                    progressBar.setVisibility(View.GONE);
                    number_id.setVisibility(View.VISIBLE);
                    location.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    Dialogue.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(),getResources().getString(R.string.nointrnet),Toast.LENGTH_SHORT).show();
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





    private void upDate(int id){


        final String token=VollySing.getInstanse(this).getToken().getToken();
        String loci = location.getText().toString();
        String desc = description.getText().toString();
        String dialo = Dialogue.getText().toString();
        String number_i = number_id.getText().toString();
            Toast.makeText(getApplicationContext(),token,Toast.LENGTH_SHORT).show();
        if (TextUtils.isEmpty(loci)) {
            location.setError("Enter title");
            location.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(desc)) {
            description.setError("Enter title");
            description.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(dialo)) {
            Dialogue.setError("Enter title");
            Dialogue.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(number_i)) {
            number_id.setError("Enter title");
            number_id.requestFocus();
            return;
        }


        JSONObject js=new JSONObject();
        try {
            js.put("numScene",number_i);
            js.put("nameScene",loci);
            js.put("contentScene",desc);
            js.put("dialogueScene",dialo);
            js.put("main_screen_id",idi);
        }catch (Exception e){
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT, ServerApi.GET_SCENE+"/"+id,js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    if (response.getBoolean("success")){
                        Toast.makeText(getBaseContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                        Toast.makeText(getBaseContext(),"upsuccess",Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent();
                        intent.putExtra("back",idi);
                        setResult(RESULT_OK,intent);
                        finish();
                       // startActivity(intent);
                    }else {
                        finish();
                        Toast.makeText(getBaseContext(),"error",Toast.LENGTH_SHORT).show();

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







    public void deleteItem(int id){

        final String token=VollySing.getInstanse(this).getToken().getToken();



        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.DELETE, ServerApi.GET_SCENE+"/"+id,null, new Response.Listener<JSONObject>() {
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
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);

        MainActivity.noti();




    }



}




