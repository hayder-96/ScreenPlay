package com.screenplay.myscreenplay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.screenplay.myscreenplay.Model.Itemtow;

import java.util.ArrayList;

public class RecyclerAdabtertow  extends RecyclerView.Adapter<RecyclerAdabtertow.MyHolder>{

    ArrayList<Itemtow> arrayList;

    Onclickitem onclickitem;
    public RecyclerAdabtertow(ArrayList<Itemtow> arrayList,Onclickitem onclickitem) {
        this.arrayList = arrayList;
        this.onclickitem=onclickitem;
    }

    public ArrayList<Itemtow> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Itemtow> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerAdabtertow.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.split_recyclertow,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdabtertow.MyHolder holder, int position) {

        Itemtow itemtow=arrayList.get(position);

        holder.textView.setText(itemtow.getNumber());
        holder.imageView.setTag(itemtow.getId());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);


            textView=itemView.findViewById(R.id.textphoto);
            imageView=itemView.findViewById(R.id.view_data);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int o= (int) imageView.getTag();

                     onclickitem.Onclick(o);
                }
            });
        }
    }
}
