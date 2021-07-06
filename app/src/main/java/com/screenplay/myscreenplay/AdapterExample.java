package com.screenplay.myscreenplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterExample extends BaseAdapter {
    ArrayList<Itemexample> arrayList;
    Context context;
    public AdapterExample(ArrayList<Itemexample> arrayList,Context context) {
        this.arrayList = arrayList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       View view1= LayoutInflater.from(context).inflate(R.layout.split_screen_help,null);

       Button button = view1.findViewById(R.id.but_example_help);
       TextView textView = view1.findViewById(R.id.text_example_help);
       Itemexample itemexample=arrayList.get(i);
       button.setBackgroundResource(itemexample.getPhoto_example());
       textView.setText(itemexample.getExample_text());


        return view1;
    }
}
