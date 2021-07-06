package com.screenplay.myscreenplay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


import uk.me.hardill.volley.multipart.MultipartRequest;

public  class ElementsRecycler extends AppCompatActivity {

    private ImageView image;
    private TextInputEditText title;
    private Uri uri;
    private Intent intent;
    private int i;
    int ii;
    String encodedImage;
    private Toolbar toolbar;
    Map<String, String> map;
    ProgressBar progressBar;
    LinearLayout layout;
    AppBarLayout appBarLayout;
    private static final int PERMTION_REQUSET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements_recycler);

        progressBar=findViewById(R.id.progressBar_film);
        image = findViewById(R.id.butimage);
        title = findViewById(R.id.edittitle);
        toolbar = findViewById(R.id.toolbar);

        layout=findViewById(R.id.linear_nasted);
        appBarLayout=findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
//
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMTION_REQUSET);
//        } else {
//
//
//        }


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_PICK);
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 2);
            }
        });


        intent = getIntent();
        i = intent.getIntExtra("add", -1);
        ii = intent.getIntExtra("pos", -1);
        Toast.makeText(getBaseContext(),i+"",Toast.LENGTH_SHORT).show();
        if (i == -1) {

        } else {


            show(i);


        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {

                uri = data.getData();
//


                Picasso.get().load(uri.toString()).resize(200,200).into(image);


            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu2, menu);
        MenuItem save = menu.findItem(R.id.save);
        MenuItem delete = menu.findItem(R.id.delete);
        if (i == -1) {
            save.setVisible(true);
            delete.setVisible(false);

        } else {
            save.setVisible(true);
            delete.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.save:

                String imag = null;

                if (imag == null && i == -1) {


                    mulityy(uri);

                    finish();
                    startActivity(new Intent(getBaseContext(), MainActivity.class));

                    return true;

                } else {

                    mulityImage(String.valueOf(i),uri);


                    progressBar.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);
                    appBarLayout.setVisibility(View.GONE);
                    Thread thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    data(i);
                                    progressBar.setVisibility(View.GONE);
                                    layout.setVisibility(View.VISIBLE);
                                    appBarLayout.setVisibility(View.VISIBLE);
                                    finish();
                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                }
                            });
                        }
                    });

                    thread.start();

                    if (uri==null) {
                        upDatetitle(i);
                    }
                }
                return true;
            case R.id.delete:
                RemoveItem();
                return true;

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

                deleteItem(i);
                finish();
                startActivity(new Intent(getBaseContext(), MainActivity.class));

            }


        });
        builder.setNegativeButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMTION_REQUSET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                }
        }
    }


    private void dataSave() {

        final String token = VollySing.getInstanse(this).getToken().getToken();
        String tit = title.getText().toString().trim();
        String imagee = null;

        if (TextUtils.isEmpty(tit)) {
            title.setError("Enter title");
            title.requestFocus();
            return;
        }


//        if (uri != null) {
//            imagee = uri.toString();
//            Toast.makeText(getBaseContext(),imagee,Toast.LENGTH_SHORT).show();
//        }
//        }else{
//            String im="https://cdn.pixabay.com/photo/2015/12/22/04/00/photo-1103597_960_720.png";
//             Uri uri=Uri.parse(im);
//            imagee=uri.toString();
//        }


//
//        final ProgressDialog progressDialog=new ProgressDialog(getBaseContext());
//        progressDialog.setMessage("Loading .....");
//        progressDialog.show();
//
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//
//
//


        JSONObject js = new JSONObject();
        try {
            js.put("title", tit);

            //  js.put("image",encodedImage);
        } catch (Exception e) {
            e.printStackTrace();
        }


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ServerApi.GET_DATA_URL, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {


                    if (response.getBoolean("success")) {
                        Toast.makeText(getBaseContext(), response.getString("message"), Toast.LENGTH_SHORT).show();


                        //progressDialog.dismiss();

                        finish();
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                    } else {
                        finish();
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
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new Hashtable<>();
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer " + token);
                map.put("image", encodedImage);
                return map;
            }


        };


        VollyLib.getInstance(this).addRequest(jsonObjectRequest);
    }


    private void show(int id) {


        final String token = VollySing.getInstanse(this).getToken().getToken();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.GET_DATA_URL + "/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.getBoolean("success")) {
                        JSONObject jsonObject = response.getJSONObject("data");

                        title.setText(jsonObject.getString("title"));
                       String u=jsonObject.getString("image");
                       if(u.equals("no")){
                               image.setImageResource(R.drawable.screenback);
                       }else {
                           Picasso.get().load(u).resize(200, 200).into(image);
                           Toast.makeText(getBaseContext(), jsonObject.getString("image") + "  image", Toast.LENGTH_SHORT).show();
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
                map.put("Authorization", "Bearer " + token);
                return map;
            }

        };
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);


    }


    private void upDate(int id,String image) {


        final String token = VollySing.getInstanse(this).getToken().getToken();
        String tit = title.getText().toString().trim();


        if (TextUtils.isEmpty(tit)) {
            title.setError("Enter title");
            title.requestFocus();
            return;
        }



        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", tit);
            jsonObject.put("image", image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, ServerApi.GET_DATA_URL + "/" + id, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    if (response.getBoolean("success")) {
                        Toast.makeText(getBaseContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                        //progressDialog.dismiss();

                    } else {
                        finish();
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
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);


    }

    public void deleteItem(int id) {

        final String token = VollySing.getInstanse(this).getToken().getToken();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, ServerApi.GET_DATA_URL + "/" + id, null, new Response.Listener<JSONObject>() {
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
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);

        MainActivity.noti();


    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap1) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

    }



    private void mulityy(Uri urii) {


        String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();
        Map<String, String> map1 = new HashMap<>();
        map1.put("Accept", "application/json");
        map1.put("Authorization", "Bearer " + token);


        MultipartRequest request = new MultipartRequest(ServerApi.GET_DATA_URL, map1,
                new Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });


        request.addPart(new MultipartRequest.FormPart("title", title.getText().toString()));


        if (urii != null) {
            try {

                Bitmap  bitmapp;
                 bitmapp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), urii);




                Bitmap bitmap = Bitmap.createScaledBitmap(bitmapp,200,200, false);






                request.addPart(new MultipartRequest.FilePart("image", "*/*", urii.toString(), getFileDataFromDrawable(bitmap)));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        VollyLib.getInstance(getBaseContext()).addRequest(request);


    }






    private void mulityImage(String film_id,Uri urii) {


        String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();
        Map<String, String> map1 = new HashMap<>();
        map1.put("Accept", "application/json");
        map1.put("Authorization", "Bearer " + token);


        MultipartRequest request = new MultipartRequest(ServerApi.UPIMAGE, map1,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });



        request.addPart(new MultipartRequest.FormPart("film_id",film_id));
        request.addPart(new MultipartRequest.FormPart("title",title.getText().toString().trim()));


        if (urii != null) {
            try {
              Bitmap  bitmapp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), urii);
                Bitmap bitmap = Bitmap.createScaledBitmap(bitmapp,200,200, false);
                request.addPart(new MultipartRequest.FilePart("image", "*/*", urii.toString(), getFileDataFromDrawable(bitmap)));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        VollyLib.getInstance(getBaseContext()).addRequest(request);


    }





    public void deleteImage(int id) {

        final String token = VollySing.getInstanse(this).getToken().getToken();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, ServerApi.UPIMAGE + "/" + id, null, new Response.Listener<JSONObject>() {
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
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);

        MainActivity.noti();


    }



    private void data(final int idi) {


        final String token = VollySing.getInstanse(this).getToken().getToken();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.GET_UPIMAGE+idi, null, new Response.Listener<JSONObject>() {



            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                         int id=screen.getInt("id");
                         String im = screen.getString("image");




                                        upDate(idi,im);
                                        deleteImage(id);

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










    private void upDatetitle(int id) {


        final String token = VollySing.getInstanse(this).getToken().getToken();
        String tit = title.getText().toString().trim();


        if (TextUtils.isEmpty(tit)) {
            title.setError("Enter title");
            title.requestFocus();
            return;
        }



        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", tit);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, ServerApi.GET_DATA_URL + "/" + id, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    if (response.getBoolean("success")) {
                        Toast.makeText(getBaseContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                        //progressDialog.dismiss();

                    } else {
                        finish();
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
        VollyLib.getInstance(this).addRequest(jsonObjectRequest);


    }








}
































