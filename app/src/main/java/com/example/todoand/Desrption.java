package com.example.todoand;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.todoand.controllers.DataBaseHelper;
import com.example.todoand.moduls.Task;

import java.text.SimpleDateFormat;

public class Desrption extends AppCompatActivity {
private TextView title,des,compl,timedate,starttime,endtime,reapet;
private CardView cardView;
private ImageButton button;
private SimpleDateFormat DMY=new SimpleDateFormat("dd-MMM-YYYY"),HM=new SimpleDateFormat("HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desrption);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        title=findViewById(R.id.Tasktitle);
        des=findViewById(R.id.Task_Des);
        compl=findViewById(R.id.Taskcompltxt);
        timedate=findViewById(R.id.Task_Date);
        starttime=findViewById(R.id.Task_starttime);
        endtime=findViewById(R.id.Task_endtime);
        reapet=findViewById(R.id.Task_repeat);
        cardView=findViewById(R.id.Taskcard);
        button=findViewById(R.id.backbuttonD);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent=getIntent();
        int id=intent.getIntExtra("id",-1);
        DataBaseHelper dataBaseHelper= DataBaseHelper.getInstance(this);
       Task task= dataBaseHelper.get_by_id(id);
       if(task!=null&&id!=-1)
       {
           title.setText(task.getTitle());
           des.setText(task.getNote());
           reapet.setText("repeat type ="+task.getRepeat());
           timedate.setText(DMY.format(task.getStartTime().getTime()));
           starttime.setText("start at"+ HM.format(task.getStartTime().getTime()));
           endtime.setText("end at"+HM.format(task.getEndTime().getTime()));
           if(task.isCompleted()==0)
           {
               compl.setText("still need to do");
           }
           else
           {
               compl.setText("completed");
           }
           if(task.getColor()==0)
               cardView.setCardBackgroundColor(Color.parseColor("#0000FF"));
           else if(task.getColor()==1)
               cardView.setCardBackgroundColor(Color.parseColor("#FFC0CB"));
           else
               cardView.setCardBackgroundColor(Color.parseColor("#ee7600"));
       }
       else {
           title.setText("error");
       }
    }
}