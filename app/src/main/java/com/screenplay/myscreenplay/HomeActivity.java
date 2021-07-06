package com.screenplay.myscreenplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {

    private Button imageView1,imageView2,imageView3;
    int[] image,image1,image2;

     int i;
     int ii;
     int iii;
    Thread t,tt,ttt;
     boolean running=true;
      Handler handler=new Handler();
    Button b;
   private BottomNavigationView bottomNavigationView;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    imageView1 = findViewById(R.id.image_film23);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);


        bottomNavigationView = findViewById(R.id.navigation);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        running=false;
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                        break;

                    case R.id.example_screen:
                        running=false;
                        Intent ii=new Intent(getBaseContext(),Myscrenn_Example.class);
                        startActivity(ii);
                        finish();
                        break;


                    case R.id.my_page:
                        running=false;
                        Intent iii=new Intent(getBaseContext(),MyProfile.class);
                        startActivity(iii);
                        finish();
                        break;

                    case R.id.users:
                        running=false;
                        Intent iiii = new Intent(getBaseContext(), All_Users.class);
                        startActivity(iiii);
                        finish();
                        break;
                }

                return false;
            }
        });






        image = new int[]{R.drawable.harrypotter, R.drawable.braveheart, R.drawable.titanic, R.drawable.avatar, R.drawable.lacasa, R.drawable.matrix};
        image1 = new int[]{R.drawable.fastandfuires, R.drawable.assassins, R.drawable.avengers, R.drawable.caribbean, R.drawable.joker, R.drawable.rush_hour};
        image2 = new int[]{R.drawable.hardtarget, R.drawable.shawshank, R.drawable.terminator, R.drawable.mission_impossible, R.drawable.others, R.drawable.pursuithappyness};

        timeImage(imageView1, image);
        timeImage1(imageView2, image1);
        timeImage2(imageView3, image2);


    }



    private void timeImage(final Button button, final int[] ima) {



         t = new Thread(new Runnable() {
            @Override
            public void run() {
                i=0;
               while (i<6){

                   if (!running)return;



                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button.setBackgroundResource(ima[i]);

                            i++;
                        }
                    });

                   if (i >5)
                       i = 0;
                }
            }
        });
        t.start();


    }
    private void timeImage1(final Button button, final int[] ima) {



         tt = new Thread(new Runnable() {
            @Override
            public void run() {
                ii=0;
                while (ii<6){

                    if (!running)return;



                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button.setBackgroundResource(ima[ii]);
                            ii++;
                        }
                    });

                    if (ii >5)
                        ii = 0;
                }
            }
        });
        tt.start();

    }
    private void timeImage2(final Button button, final int[] ima) {



        ttt = new Thread(new Runnable() {
            @Override
            public void run() {
                iii=0;
                while (iii<6){

                    if (!running)return;


                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button.setBackgroundResource(ima[iii]);

                            iii++;
                        }
                    });

                    if (iii >5)
                        iii = 0;
                }
            }
        });
        ttt.start();

    }





    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(t);
        handler.removeCallbacks(tt);
        handler.removeCallbacks(ttt);
    }
}
