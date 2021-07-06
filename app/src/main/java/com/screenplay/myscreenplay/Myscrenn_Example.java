package com.screenplay.myscreenplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Myscrenn_Example extends AppCompatActivity {

   private ListView listView;
   private TextView textView;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myscrenn__example);

        listView=findViewById(R.id.list_example);
        textView=findViewById(R.id.textView_example);


        bottomNavigationView=findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.help:
                        Intent i=new Intent(getBaseContext(),HomeActivity.class);
                        startActivity(i);
                        break;

                    case R.id.home:
                        Intent ii=new Intent(getBaseContext(),MainActivity.class);
                        startActivity(ii);
                        break;

                    case R.id.my_page:
                        Intent iii=new Intent(getBaseContext(),MyProfile.class);
                        startActivity(iii);
                        finish();
                        break;

                    case R.id.users:
                        Intent iiii = new Intent(getBaseContext(), All_Users.class);
                        startActivity(iiii);
                        finish();
                        break;
                }
                return false;
            }
        });






        ArrayList<Itemexample> arrayList=new ArrayList<>();
        arrayList.add(new Itemexample(getResources().getString(R.string.joker1),R.drawable.photo1));
        arrayList.add(new Itemexample(getResources().getString(R.string.joker2),R.drawable.photo2));
        arrayList.add(new Itemexample(getResources().getString(R.string.joker3),R.drawable.photo3));
        arrayList.add(new Itemexample(getResources().getString(R.string.joker4),R.drawable.photo4));
        arrayList.add(new Itemexample(getResources().getString(R.string.joker5),R.drawable.photo5));
        arrayList.add(new Itemexample(getResources().getString(R.string.joker6),R.drawable.photo6));
        arrayList.add(new Itemexample(getResources().getString(R.string.joker7),R.drawable.photo7));
        arrayList.add(new Itemexample(getResources().getString(R.string.joker8),R.drawable.photo8));
        arrayList.add(new Itemexample(getResources().getString(R.string.joker9),R.drawable.photo9));
        arrayList.add(new Itemexample(getResources().getString(R.string.joker10),R.drawable.photo10));
        arrayList.add(new Itemexample(getResources().getString(R.string.joker11),R.drawable.photo11));
        arrayList.add(new Itemexample(getResources().getString(R.string.joker12),R.drawable.photo12));
        arrayList.add(new Itemexample(getResources().getString(R.string.joker13),R.drawable.photo13));
        arrayList.add(new Itemexample(getResources().getString(R.string.joker14),R.drawable.photo14));


        AdapterExample adapterExample=new AdapterExample(arrayList,this);

        listView.setAdapter(adapterExample);



    }
}