package com.screenplay.myscreenplay.VollyLibary;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VollyLib {



private static VollyLib vollyLib_instance;
private static Context context;
private RequestQueue requestQueue;


    public VollyLib(Context context) {
        this.context=context;
        this.requestQueue = getRequestQueue();
    }
    public static synchronized VollyLib getInstance(Context context){

        if (vollyLib_instance==null){
            vollyLib_instance=new VollyLib(context);
        }
        return vollyLib_instance;
    }
    public RequestQueue getRequestQueue(){

        if (requestQueue==null){
            requestQueue=Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T>void addRequest(Request<T> addReq){

        getRequestQueue().add(addReq);

    }
}
