package com.example.todoand.controllers;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoand.Datecontroll_interface;
import com.example.todoand.R;
import com.example.todoand.moduls.Task;

import java.util.ArrayList;

public class Task_adapter extends RecyclerView.Adapter<Task_adapter.Viewholder> {
    ArrayList<Task>Task_list;
    Datecontroll_interface datecontroll_interface;
    public Task_adapter(ArrayList<Task>Task_List,Datecontroll_interface datecontroll_interface){
        this.Task_list=Task_List;
        this.datecontroll_interface=datecontroll_interface;
    }
    public static class  Viewholder extends RecyclerView.ViewHolder{
       public TextView title,note,starttime;
       public CardView cardView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById( R.id.line0);
            starttime=itemView.findViewById( R.id.line2);
            note=itemView.findViewById( R.id.line3);
            cardView=itemView.findViewById( R.id.Taskrecler_card);
        }
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  v= LayoutInflater.from(parent.getContext()).inflate(R.layout.taskrecleritem,parent,false);
        Viewholder viewholder=new Viewholder(v);
        return viewholder;
    }

    @Override

    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
      Task current=Task_list.get(position);
      holder.title.setText(current.getTitle());
      holder.starttime.setText(current.getStartTime().getTime().toString());
        if(current.getNote().length()>15){
            holder.note.setText(current.getNote().substring(0,14)+"....");
        }else
            holder.note.setText(current.getNote());
      if(current.getColor()==0)
          holder.cardView.setCardBackgroundColor(Color.parseColor("#0000FF"));
      else if(current.getColor()==1)
          holder.cardView.setCardBackgroundColor(Color.parseColor("#FFC0CB"));
      else
          holder.cardView.setCardBackgroundColor(Color.parseColor("#ee7600"));
      holder.cardView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              datecontroll_interface.ontaskclick(current);
          }
      });
    }

    @Override
    public int getItemCount() {
        return Task_list.size();
    }
}
