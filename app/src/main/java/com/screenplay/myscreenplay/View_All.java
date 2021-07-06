package com.screenplay.myscreenplay;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class View_All extends AppCompatActivity {

    private TextView view_all;
    ProgressBar progressBar;
    Toolbar toolbar;
    Button button1,button2;
    CheckBox checkBox1, checkBox2, checkBox3;
     LinearLayout layoutpdf;
     Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__all);

        view_all = findViewById(R.id.textView_all);
          toolbar=findViewById(R.id.toolbar2);
         setSupportActionBar(toolbar);
        checkBox1 = findViewById(R.id.checkbox_bold);
        checkBox2 = findViewById(R.id.checkbox_italic);
        checkBox3 = findViewById(R.id.checkbox_bold_italic);
        button1 = findViewById(R.id.font_size);
        button2=findViewById(R.id.style_font);
        progressBar=findViewById(R.id.progressBar_viewall);
        layoutpdf=findViewById(R.id.linearpdf);

        registerForContextMenu(button1);
        registerForContextMenu(button2);








        dataScene();
        LoadShared();
        LoadSize();

    }


    public void check_box(View view) {
        boolean check = ((CheckBox) view).isChecked();

        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int id = ((CheckBox) view).getId();
        switch (id) {
            case R.id.checkbox_bold:
                if (check == true) {

                    view_all.setTypeface(null,Typeface.BOLD);
                    editor.putInt("number",1);
                    editor.apply();
                } else {
                    view_all.setTypeface(null, Typeface.NORMAL);
                    editor.putInt("number",0);
                    editor.apply();
                }
                break;

            case R.id.checkbox_italic:
                if (check == true) {
                    Typeface t=Typeface.createFromAsset(getAssets(),"italicy.ttf");
                    view_all.setTypeface(t);
                  editor.putInt("number",2);
                    editor.apply();
                } else {
                    view_all.setTypeface(null, Typeface.NORMAL);
                    editor.putInt("number",0);
                    editor.apply();
                }
                break;
            case R.id.checkbox_bold_italic:
                if (check == true) {
                    Typeface ty=Typeface.createFromAsset(getAssets(),"bold-italic.ttf");
                    view_all.setTypeface(ty);
                    editor.putInt("number",3);
                    editor.apply();
                } else {
                    view_all.setTypeface(null, Typeface.NORMAL);
                    editor.putInt("number",0);
                    editor.apply();
                    break;
                }
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.font_size_menu, menu);
        int id = v.getId();
        if (id == R.id.style_font) {
            MenuItem m1 = menu.findItem(R.id.fifteen);
            MenuItem m2 = menu.findItem(R.id.twenty);
            MenuItem m3 = menu.findItem(R.id.twenty_five);
            MenuItem m4 = menu.findItem(R.id.thirty);
            MenuItem m5 = menu.findItem(R.id.thirty_five);
            m1.setVisible(false);
            m2.setVisible(false);
            m3.setVisible(false);
            m4.setVisible(false);
            m5.setVisible(false);
        }else {
            MenuItem def=menu.findItem(R.id.defaul);
            MenuItem m1 = menu.findItem(R.id.font1);
            MenuItem m2 = menu.findItem(R.id.font2);
            MenuItem m3 = menu.findItem(R.id.font3);
            MenuItem m4 = menu.findItem(R.id.font4);
            MenuItem m5 = menu.findItem(R.id.font5);
            def.setVisible(false);
            m1.setVisible(false);
            m2.setVisible(false);
            m3.setVisible(false);
            m4.setVisible(false);
            m5.setVisible(false);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (item.getItemId()) {
            case R.id.fifteen:
                view_all.setTextSize(15);
                item.setChecked(true);

                editor.putInt("size",15);
                editor.apply();
                break;
            case R.id.twenty:
                view_all.setTextSize(20);
                item.setChecked(true);
                editor.putInt("size",20);
                editor.apply();
                break;
            case R.id.twenty_five:
                view_all.setTextSize(25);
                item.setChecked(true);
                editor.putInt("size",25);
                editor.apply();
                break;
            case R.id.thirty:
                view_all.setTextSize(30);
                item.setChecked(true);
                editor.putInt("size",30);
                editor.apply();
                break;
            case R.id.thirty_five:
                view_all.setTextSize(35);
                item.setChecked(true);
                editor.putInt("size",35);
                editor.apply();
                break;

            case R.id.font1:
                Typeface typeface1=Typeface.createFromAsset(getAssets(),"dongol-regular.otf");
                view_all.setTypeface(typeface1);
                editor.putInt("number",5);
                editor.apply();
                break;
            case R.id.font2:
                Typeface typeface2=Typeface.createFromAsset(getAssets(),"tajawal-medium.ttf");
                view_all.setTypeface(typeface2);
                editor.putInt("number",6);
                editor.apply();
                break;
            case R.id.font3:
                Typeface typeface3=Typeface.createFromAsset(getAssets(),"tajawal-extraLight.ttf");
                view_all.setTypeface(typeface3);
                editor.putInt("number",7);
                editor.apply();
                break;
            case R.id.font4:
                Typeface typeface4=Typeface.createFromAsset(getAssets(),"zekra-serif.otf");
                view_all.setTypeface(typeface4);
                editor.putInt("number",8);
                editor.apply();
                break;
            case R.id.font5:
                Typeface typeface5=Typeface.createFromAsset(getAssets(),"massir-ballpoint.ttf");
                view_all.setTypeface(typeface5);
                editor.putInt("number",9);
                editor.apply();
                break;

            case R.id.defaul:
                view_all.setTypeface(null,Typeface.NORMAL);
                editor.putInt("number",10);
                editor.apply();
                break;
        }
        return false;
    }







    private void LoadShared(){
        SharedPreferences sharedPreferences=getSharedPreferences("save",MODE_PRIVATE);
        int yy=sharedPreferences.getInt("number", -1);


        if (yy==5){
            Typeface typeface1=Typeface.createFromAsset(getAssets(),"dongol-regular.otf");
            view_all.setTypeface(typeface1);
        }else if (yy==6){
            Typeface typeface2=Typeface.createFromAsset(getAssets(),"tajawal-medium.ttf");
            view_all.setTypeface(typeface2);
        }else if (yy==7){
            Typeface typeface3=Typeface.createFromAsset(getAssets(),"tajawal-extraLight.ttf");
            view_all.setTypeface(typeface3);
        }else if (yy==8){
            Typeface typeface4=Typeface.createFromAsset(getAssets(),"zekra-serif.otf");
            view_all.setTypeface(typeface4);
        }else if (yy==9){
            Typeface typeface5=Typeface.createFromAsset(getAssets(),"massir-ballpoint.ttf");
            view_all.setTypeface(typeface5);
        }else if (yy==10){
            view_all.setTypeface(null,Typeface.NORMAL);
        }
        else if (yy==1){
            view_all.setTypeface(null,Typeface.BOLD);
            checkBox1.setChecked(true);
        } else if(yy==2){
            view_all.setTypeface(Typeface.MONOSPACE,Typeface.ITALIC);
            checkBox2.setChecked(true);
        }else if(yy==3){
            view_all.setTypeface(Typeface.MONOSPACE,Typeface.BOLD_ITALIC);
            checkBox3.setChecked(true);
    }else{
            view_all.setTypeface(null,Typeface.NORMAL);
        }

    }
    private void LoadSize(){
        SharedPreferences sharedPreferences=getSharedPreferences("save",MODE_PRIVATE);
        int s=sharedPreferences.getInt("size",-1 );
        if (s==15){
            view_all.setTextSize(15);
        }else if(s==20){
            view_all.setTextSize(20);
        }else if(s==25){
            view_all.setTextSize(25);
        }else if (s==30){
            view_all.setTextSize(30);
        }else if (s==35){
            view_all.setTextSize(35);
        }
    }
    private void LoadStyle(){
        SharedPreferences sharedPreferences=getSharedPreferences("save",MODE_PRIVATE);
        int ss=sharedPreferences.getInt("form",-1 );

         if (ss==5){
            Typeface typeface1=Typeface.createFromAsset(getAssets(),"dongol-regular.otf");
            view_all.setTypeface(typeface1);
        }else if (ss==6){
            Typeface typeface2=Typeface.createFromAsset(getAssets(),"tajawal-medium.ttf");
            view_all.setTypeface(typeface2);
        }else if (ss==7){
            Typeface typeface3=Typeface.createFromAsset(getAssets(),"tajawal-extraLight.ttf");
            view_all.setTypeface(typeface3);
        }else if (ss==8){
            Typeface typeface4=Typeface.createFromAsset(getAssets(),"zekra-serif.otf");
            view_all.setTypeface(typeface4);
        }else if (ss==9){
            Typeface typeface5=Typeface.createFromAsset(getAssets(),"massir-ballpoint.ttf");
            view_all.setTypeface(typeface5);
        }else if (ss==10){
            view_all.setTypeface(null,Typeface.NORMAL);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.pdf_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        View view=getLayoutInflater().inflate(R.layout.main_alert,null);
        final EditText editText=view.findViewById(R.id.edit_alerttext);
        final EditText editText1=view.findViewById(R.id.edit_alertnum);
        switch (item.getItemId()){

            case R.id.pdf_document:


                final AlertDialog.Builder builder=new AlertDialog.Builder(this);
                   builder.setTitle("ادخل اسم الملف");

                builder.setView(view);
                   builder.setCancelable(false);
                   builder.setPositiveButton("انشاء", new DialogInterface.OnClickListener() {
                               @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   String mv = editText.getText().toString().trim();
                                   String numtext = editText1.getText().toString().trim();
                                   if (mv.equals("") && numtext.equals("") || mv.equals("") || numtext.equals("")) {


                                       Toast.makeText(getBaseContext(), "ادخل المطلوب", Toast.LENGTH_SHORT).show();
                                   } else {


                                       String no = view_all.getText().toString().trim();


                                       DisplayMetrics displayMetrics = new DisplayMetrics();
                                       getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                       float wi = displayMetrics.widthPixels;
                                       float hi = displayMetrics.heightPixels;
                                       int cowi = (int) wi;
                                       int cohi = (int) hi;
                                       // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                                       int textlength = no.length();

                                       int total = 0;
                                       int numpage = 1;
                                       int index1 = 0, index2 = 200, hight = Integer.parseInt(numtext);

                                       PdfDocument mypdf = new PdfDocument();
                                       PdfDocument.Page page = null;


                                       for (int ii = 0; ii < textlength; ii++) {

                                           if (total > textlength) {
                                               total = 0;
                                               total = textlength;
                                           }

                                           if (ii == total) {

                                               total = total + 200;


                                               PdfDocument.PageInfo mypage = new PdfDocument.PageInfo.Builder(cowi, hight, numpage).create();
                                               numpage++;

                                               page = mypdf.startPage(mypage);


                                               Paint paint = new Paint();


                                               paint.setTextAlign(Paint.Align.CENTER);

                                               SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
                                               int s = sharedPreferences.getInt("size", -1);
                                               if (s == 15) {
                                                   paint.setTextSize(15);
                                               } else if (s == 20) {
                                                   paint.setTextSize(20);
                                               } else if (s == 25) {
                                                   paint.setTextSize(25);
                                               } else if (s == 30) {
                                                   paint.setTextSize(30);
                                               } else if (s == 35) {
                                                   paint.setTextSize(35);
                                               }

                                               Fontpdf(paint);


                                               int x = 250, y = 50;

                                               int opo = no.length();

                                               if (index2 > opo) {

                                                   index2 = 0;
                                                   index2 = opo;
                                               }

                                               String mvc = no.substring(index1, index2);

                                               for (String line : mvc.split("\n")) {
                                                   page.getCanvas().drawText(line, x, y, paint);

                                                   y += paint.descent() - paint.ascent();


                                               }

                                               index1 += 200;
                                               index2 += 200;


                                               bitmap = bitmap.createBitmap(layoutpdf.getWidth(), hight, Config.ARGB_8888);
                                               Canvas c = new Canvas(bitmap);
                                               layoutpdf.draw(c);
                                               bitmap = bitmap.createScaledBitmap(bitmap, cowi, hight, true);

                                               mypdf.finishPage(page);

                                           }

                                       }


                                       FileOutputStream fileOutputStream = null;


                                       String sd = editText.getText().toString().trim();
                                       File nm = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                       File file = new File(nm, sd + ".pdf");


                                       try {
                                           fileOutputStream = new FileOutputStream(file);

                                           mypdf.writeTo(fileOutputStream);
                                           fileOutputStream.write(no.getBytes());
                                           Toast.makeText(getBaseContext(), "save", Toast.LENGTH_LONG).show();
                                       } catch (FileNotFoundException e) {
                                           e.printStackTrace();
                                       } catch (IOException e) {
                                           e.printStackTrace();


                                       } finally {
                                           try {
                                               if (fileOutputStream != null) {
                                                   fileOutputStream.close();
                                               }
                                           } catch (IOException e) {
                                               e.printStackTrace();
                                           }
                                       }
                                   }
                               }
                           });


                           Toast.makeText(getBaseContext(), "not null", Toast.LENGTH_SHORT).show();
                           builder.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   dialogInterface.dismiss();
                               }

                           }).show();


                   }


        return false;
    }
    private void Fontpdf(Paint paint){

        SharedPreferences sharedPreferences=getSharedPreferences("save",MODE_PRIVATE);

        int yy=sharedPreferences.getInt("number", -1);

        if (yy==1){
            Typeface t=Typeface.createFromAsset(getAssets(),"bold.ttf");

            paint.setTypeface(t);

        } else if(yy==2){
            Typeface tt=Typeface.createFromAsset(getAssets(),"italicy.ttf");

            paint.setTypeface(tt);
        }else if(yy==3) {
            Typeface ty=Typeface.createFromAsset(getAssets(),"bold-italic.ttf");

            paint.setTypeface(ty);

        }else if (yy==5){
            Typeface typeface1=Typeface.createFromAsset(getAssets(),"dongol-regular.otf");
            paint.setTypeface(typeface1);
        }else if (yy==6){
            Typeface typeface2=Typeface.createFromAsset(getAssets(),"tajawal-medium.ttf");
            paint.setTypeface(typeface2);
        }else if (yy==7){
            Typeface typeface3=Typeface.createFromAsset(getAssets(),"tajawal-extraLight.ttf");
            paint.setTypeface(typeface3);
        }else if (yy==8){
            Typeface typeface4=Typeface.createFromAsset(getAssets(),"zekra-serif.otf");
            paint.setTypeface(typeface4);
        }else if (yy==9){
            Typeface typeface5=Typeface.createFromAsset(getAssets(),"massir-ballpoint.ttf");
           paint.setTypeface(typeface5);
        }else if (yy==10){
            paint.setTypeface(Typeface.DEFAULT);
        }

    }









    private void dataScene() {
        final String token= VollySing.getInstanse(getBaseContext()).getToken().getToken();


        progressBar.setVisibility(View.VISIBLE);
        view_all.setVisibility(View.GONE);


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.GET_SCENE, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {


                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                       screen.getInt("id");

                     String num=screen.getString("numScene");
                        Toast.makeText(getBaseContext(),num,Toast.LENGTH_SHORT).show();
                        String name =screen.getString("nameScene");
                        String  content=screen.getString("contentScene");
                        String dialog=screen.getString("dialogueScene");

                       view_all.setText(view_all.getText().toString()+"\n"+num+"\n"+name+"\n"+content+"\n"+dialog+"\n");
                      // view_all.setText(view_all.getText().toString()+"\n"+num+"\n"+name+"\n"+content+"\n"+dialog+"\n");
                    }
                    progressBar.setVisibility(View.GONE);
                    view_all.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                view_all.setVisibility(View.VISIBLE);
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

