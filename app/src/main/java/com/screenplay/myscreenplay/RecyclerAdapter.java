package com.screenplay.myscreenplay;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.screenplay.myscreenplay.Model.Item_screen;
import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.viewHolder> {

    Conection conection;
    ArrayList<Item_screen> arrayList;
    Context context;
    Item_screen itemRecycler;
    public RecyclerAdapter(ArrayList<Item_screen> arrayList, Context context, Conection conection) {
        this.arrayList = arrayList;
        this.context = context;
        this.conection = conection;

    }



    public ArrayList<Item_screen> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Item_screen> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.split_recycler1, null, false);
        viewHolder viewHolder = new viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter.viewHolder holder, int position) {

        itemRecycler = arrayList.get(position);

        holder.textView.setText(itemRecycler.getTitle());
       if (itemRecycler.getImage().equals("no")) {

           holder.button.setImageResource(R.drawable.screenback);


       }else{
           Picasso.get().load(Uri.parse(itemRecycler.getImage())).resize(200,200).into(holder.button);


       }


           holder.date_time.setText(formatdate(itemRecycler.getCreated_at()));

        holder.button.setTag(itemRecycler.getId());
        holder.pop.setTag(itemRecycler.getId());


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

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

       private TextView textView;
        private ImageView button;
        private TextView pop;
        private TextView date_time;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            date_time=itemView.findViewById(R.id.date_film2);
            textView = itemView.findViewById(R.id.text_film2);
            button = itemView.findViewById(R.id.image_film23);
            pop = itemView.findViewById(R.id.textpop);
            pop.setOnClickListener(this);


           button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  int titl = (int) button.getTag();
                  Intent intent=new Intent(context,listscreen.class);
                  intent.putExtra("title",titl);
                  context.startActivity(intent);

               }
           });

        }

        @Override
        public void onClick(View v) {

            showpop(v);
        }

        private void showpop(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.activity_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.edit:

                    int yy= (int) pop.getTag();


                    conection.Con(yy);

                    return true;
            }
                    return false;
            }


    }




}










