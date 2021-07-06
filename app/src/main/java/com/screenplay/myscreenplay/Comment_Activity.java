package com.screenplay.myscreenplay;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.screenplay.myscreenplay.Controller.Adapter_comment_parent;
import com.screenplay.myscreenplay.Model.ItemAccept;
import com.screenplay.myscreenplay.Model.Item_comment;
import com.screenplay.myscreenplay.Server.ServerApi;
import com.screenplay.myscreenplay.VollyLibary.VollyLib;
import com.screenplay.myscreenplay.VollySinglistion.VollySing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class Comment_Activity extends AppCompatActivity {


    private Intent intent;
    int id;
    private ArrayList<Item_comment> arrayList;
    private RecyclerView recyclerView;
    private EditText editComment;
    static Adapter_comment_parent parent;
    private Button done;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);

        recyclerView = findViewById(R.id.recycler_comment);
        editComment = findViewById(R.id.edit_comment);
        done = findViewById(R.id.but_done_com);
        progressBar = findViewById(R.id.progressBar_comment);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);


        intent = getIntent();
        id = intent.getIntExtra("main_screen_id", -1);


        Toast.makeText(this, id + "id", Toast.LENGTH_SHORT).show();














        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataa(id, editComment.getText().toString());


                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                arrayList = dataComment(id);

                                parent = new Adapter_comment_parent(arrayList, getBaseContext());

                                recyclerView.setAdapter(parent);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setHasFixedSize(true);
                                parent.notifyDataSetChanged();
                            }
                        });
                    }
                });


                thread.start();

            }
        });


        editComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                done.setVisibility(View.VISIBLE);

                if (charSequence.toString().trim().equals("")) {
                    done.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                arrayList = dataComment(id);

                parent = new Adapter_comment_parent(arrayList, getBaseContext());

                recyclerView.setAdapter(parent);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                parent.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        arrayList = dataComment(id);

        parent = new Adapter_comment_parent(arrayList, this);

        recyclerView.setAdapter(parent);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        parent.notifyDataSetChanged();


    }

    @Override
    protected void onStop () {
        super .onStop() ;
        startService( new Intent( this, NotificationService. class )) ;
    }
















    public static void notifi() {

        parent.notifyDataSetChanged();

    }


    private void dataSave(int main_screen_id, String name, String description, String image) {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();

        JSONObject js = new JSONObject();
        try {
            js.put("main_screen_id", main_screen_id);
            js.put("descreption", description);
            js.put("image", image);
            js.put("name", name);
            js.put("enable", "true");

        } catch (Exception e) {
            e.printStackTrace();
        }


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ServerApi.ALL_COMMENT, js, new Response.Listener<JSONObject>() {
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


    private void dataa(final int ii, final String desc) {

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
                        dataSave(ii, name, desc, image);


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


    private ArrayList<Item_comment> dataComment(final int idi) {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();
//        progressBar.setVisibility(View.VISIBLE);
        final ArrayList<Item_comment> arrayList = new ArrayList<>();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.COMMENT_PARENT + idi, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject screen = jsonArray.getJSONObject(i);

                        Item_comment item_comment = new Item_comment();

                        item_comment.setId(screen.getInt("id"));
                        item_comment.setUser_id(screen.getInt("user_id"));
                        item_comment.setName(screen.getString("name"));
                        item_comment.setImage(screen.getString("image"));
                        item_comment.setMain_screen(screen.getInt("main_screen_id"));
                        item_comment.setDescription(screen.getString("descreption"));
                        item_comment.setCreate_at(screen.getString("created_at"));

                        Toast.makeText(getBaseContext(), screen.getString("name"), Toast.LENGTH_SHORT).show();
                        String enable = screen.getString("enable");
                        if (enable.equals("true")) {
                            dataMe(screen.getString("name"), screen.getInt("user_id"), screen.getInt("id"));
                            progressBar.setVisibility(View.GONE);
                        }
                        arrayList.add(item_comment);
                        parent.notifyDataSetChanged();


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
                Toast.makeText(getBaseContext(), getResources().getString(R.string.nointrnet), Toast.LENGTH_SHORT).show();

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


        return arrayList;

    }


    private void noty(String name) {


        int i = 1;
        Notification.Builder builder = new Notification.Builder(getBaseContext())
                .setContentTitle("قام " + name + "بالتعليق على اليناريو الخاص بك")
                .setSmallIcon(R.drawable.braveheart)
                .setVibrate(new long[]{500, 1000, 500, 1000, 500})
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(getBaseContext(), Comment_Activity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getBaseContext());
        taskStackBuilder.addNextIntent(intent);
        taskStackBuilder.addParentStack(Comment_Activity.class);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.addAction(R.drawable.com_facebook_button_icon, "read", pendingIntent);
        manager.notify(i, builder.build());
        i++;


    }


    private void applyNoty() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationChannel notificationChannel = new NotificationChannel("my notification", "my notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }


    }


    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void dataMe(final String name, int user_id, final int id) {

        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ServerApi.COMMENT_NOTIFICTION + user_id, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    String message = response.getString("message");

                    if (message.equals("yes")) {
                        noty(name);
                        upDate(id);
                    }


                    progressBar.setVisibility(View.GONE);

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


    private void upDate(int id) {


        final String token = VollySing.getInstanse(getBaseContext()).getToken().getToken();


        JSONObject js = new JSONObject();
        try {
            js.put("enable", "false");

        } catch (Exception e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, ServerApi.COMMENT_UP + id, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    if (response.getBoolean("success")) {
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

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


    public class NotificationService extends Service {
        public static final String NOTIFICATION_CHANNEL_ID = "10001";
        private final static String default_notification_channel_id = "default";
        Timer timer;
        TimerTask timerTask;
        String TAG = "Timers";
        int Your_X_SECS = 5;

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);
            startTimer();
            return START_STICKY;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDestroy() {
            stopTimerTask();
            super.onDestroy();
        }

        //we are going to use a handler to be able to run in our TimerTask



        public void startTimer() {
            timer = new Timer();
            initializeTimerTask();
            timer.schedule(timerTask, 5000, Your_X_SECS * 1000); //
        }

        public void stopTimerTask() {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }

        }

        public void initializeTimerTask() {
            timerTask = new TimerTask() {


                @Override
                public void run() {

                    dataComment(id);
                }
            };
        }


    }
}




















