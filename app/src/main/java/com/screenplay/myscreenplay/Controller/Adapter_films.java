package com.screenplay.myscreenplay.Controller;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.screenplay.myscreenplay.Model.Iten_new;
import com.screenplay.myscreenplay.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_films extends RecyclerView.Adapter<Adapter_films.MyHolder> {

    ArrayList<Iten_new> arrayList;
    allScreenplay allScreenplay;
    Context context;
    public Adapter_films(ArrayList<Iten_new> arrayList,Context context,allScreenplay allScreenplay) {
        this.arrayList = arrayList;
         this.context=context;
         this.allScreenplay=allScreenplay;
    }

    @NonNull
    @Override
    public Adapter_films.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.split_filmes, parent, false);

        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_films.MyHolder holder, int position) {

        Iten_new iten_new=arrayList.get(position);



        holder.title.setText(iten_new.getTitle());

        if (iten_new.getImage().equals("no")) {

            holder.imageView.setImageResource(R.drawable.screenback);


        }else {
            Picasso.get().load(Uri.parse(iten_new.getImage())).resize(200, 200).into(holder.imageView);
        }

        holder.time.setText(formatdate(iten_new.getCreated_at()));

        holder.imageView.setTag(iten_new.getId());


        }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView time,title;
        private ImageView imageView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            time=itemView.findViewById(R.id.date_film22);
            title= itemView.findViewById(R.id.text_film22);
            imageView= itemView.findViewById(R.id.image_film2);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int id= (int) imageView.getTag();
                    allScreenplay.screen(id);
                }
            });
        }
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
}
