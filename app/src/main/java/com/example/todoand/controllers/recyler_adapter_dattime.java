package com.example.todoand.controllers;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoand.Datecontroll_interface;
import com.example.todoand.R;

import java.util.ArrayList;

public class recyler_adapter_dattime extends RecyclerView.Adapter<recyler_adapter_dattime.Viewholder> {
    ArrayList<String>rceyler_items_list;
    private Datecontroll_interface datecontroll_interface;
    int selct_pos=0;
    public  static class  Viewholder extends RecyclerView.ViewHolder {
        public TextView mounthN;
        public TextView DayM;
        public TextView DayW;
        public CardView cardView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mounthN=itemView.findViewById(R.id.mounthName);
            DayM=itemView.findViewById(R.id.day_mounth);
            DayW=itemView.findViewById(R.id.day_week);
            cardView=itemView.findViewById(R.id.cardview);
        }
    }
public recyler_adapter_dattime(ArrayList<String> rceyler_items_list,Datecontroll_interface datecontroll_interface){
        this.datecontroll_interface=datecontroll_interface;
        this.rceyler_items_list=rceyler_items_list;
}
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View  v= LayoutInflater.from(parent.getContext()).inflate(R.layout.datetimerecle,parent,false);
       Viewholder viewholder=new Viewholder(v);
       return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
      String current=rceyler_items_list.get(position);
      holder.mounthN.setText(current.substring(7,10));
      holder.DayM.setText(current.substring(4,6));
      holder.DayW.setText(current.substring(0,3));
      holder.cardView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              selct_pos=position;
              datecontroll_interface.onDateselect(selct_pos);
              notifyDataSetChanged();
          }
      });
      if(position==selct_pos)
          holder.cardView.setCardBackgroundColor(Color.parseColor("#FF4e5ae8"));
      else
          holder.cardView.setCardBackgroundColor(null);
    }

    @Override
    public int getItemCount() {return rceyler_items_list.size(); }
}
