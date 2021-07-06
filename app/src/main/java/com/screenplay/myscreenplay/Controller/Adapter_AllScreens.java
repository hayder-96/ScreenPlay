package com.screenplay.myscreenplay.Controller;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.screenplay.myscreenplay.Model.Item_AllScreen;

import java.util.ArrayList;

public class Adapter_AllScreens extends RecyclerView.Adapter<Adapter_AllScreens.MyHolder> {


    ArrayList<Item_AllScreen> allScreens;


    @NonNull
    @Override
    public Adapter_AllScreens.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_AllScreens.MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
