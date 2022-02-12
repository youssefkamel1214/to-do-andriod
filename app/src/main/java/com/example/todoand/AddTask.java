package com.example.todoand;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todoand.controllers.DataBaseHelper;
import com.example.todoand.moduls.Task;

import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTask extends AppCompatActivity {
    private String []repeat_items={"None","Daily","Mounthly","Weekly"},colors={"blue","pink","orange"};
    private  AutoCompleteTextView repeatA,remindA,ColorA;
    private int []remind_items={5,15,30};
    private int slect_rem=-1,selct_rep=-1,color=-1;
    private Calendar start_time=Calendar.getInstance(),end_time=Calendar.getInstance();
    private ArrayAdapter<String>adaptertiems1,adpteritems2,adpteritems3;
    private EditText title,note,date,starttime,endtime;
    private ImageButton imageButton;
    private  Button create;
    SimpleDateFormat DMY=new SimpleDateFormat("dd-MMM-YYYY"),HM=new SimpleDateFormat("HH: mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        inalaize_adapters();
        inalaize_calnd();
        title=findViewById(R.id.title_text);
        note=findViewById(R.id.Note_text);
        date=findViewById(R.id.Date_text);
        date.setText(DMY.format(start_time.getTime()));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickUpDate();
            }
        });
        starttime=findViewById(R.id.starttime);
        starttime.setText(HM.format(start_time.getTime()));
        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickupStartTime();
            }
        });
        endtime=findViewById(R.id.endtime);
        endtime.setText(HM.format(end_time.getTime()));
        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickUpEndTime();
            }
        });
       create=findViewById(R.id.Creattaskbutton);
       create.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               create_task();
           }
       });
       imageButton=findViewById(R.id.backbutton);
       imageButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               setResult(RESULT_CANCELED);
               finish();
           }
       });
    }

    private void inalaize_calnd() {
        start_time.set(Calendar.HOUR,end_time.get(Calendar.HOUR)+1);
        end_time.set(Calendar.HOUR,end_time.get(Calendar.HOUR)+1);
        start_time.set(Calendar.SECOND,0);
        end_time.set(Calendar.SECOND,0);
        if(end_time.get(Calendar.MINUTE)>44){
            end_time.set(Calendar.HOUR,end_time.get(Calendar.HOUR)+1);
            end_time.set(Calendar.MINUTE,0);
        }
        else
            end_time.set(Calendar.MINUTE,end_time.get(Calendar.MINUTE)+15);
    }

    private void create_task() {
        String tittxt=title.getText().toString(),notetxt=note.getText().toString();
        if(tittxt.isEmpty()==true||notetxt.isEmpty()==true||color==-1||selct_rep==-1||slect_rem==-1) {
          Toast a=  Toast.makeText(this,"compleate Data",Toast.LENGTH_LONG);
          a.show();
            return;
        }
        Calendar calendar=Calendar.getInstance();
        if(calendar.after(start_time))
        {
            Toast a=  Toast.makeText(this,"ast5fr allah al3zem ya 3m anta bt7otly tare5 3da",Toast.LENGTH_LONG);
            a.show();
            return;
        }
        if(start_time.after(end_time)==true){
            Toast a=  Toast.makeText(this,"ya Allah y3ny task 5lst 2bl ma tbd2 ",Toast.LENGTH_LONG);
            a.show();
            return;
        }
        DataBaseHelper dataBaseHelper=new DataBaseHelper(this);
        try {
            Task task=new Task(0,remind_items[slect_rem],color,tittxt,notetxt,
                    repeat_items[selct_rep],start_time,end_time,0);
           long i= dataBaseHelper.addOne(task);
           if(i!=-1){
               setResult(RESULT_OK);
               finish();
           }
           else {
               Toast a=  Toast.makeText(this,"cant insert Data",Toast.LENGTH_LONG);
               a.show();
           }
        }catch (Exception e){
            System.out.println("error when we create one task");
            Toast a=  Toast.makeText(this,"cant a7a Data",Toast.LENGTH_LONG);
            a.show();
            System.out.println(e);
        }

    }

    private void PickUpEndTime() {
        TimePickerDialog timePickerDialog=new TimePickerDialog(AddTask.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minite) {
                end_time.set(Calendar.HOUR,hour);
                end_time.set(Calendar.MINUTE,minite);
                endtime.setText(HM.format( end_time.getTime()));
            }
        },end_time.get(Calendar.HOUR),end_time.get(Calendar.MINUTE),false);
        timePickerDialog.setTitle("pick UP time");
        timePickerDialog.show();
    }

    private void PickupStartTime() {
        TimePickerDialog timePickerDialog=new TimePickerDialog(AddTask.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minite) {
                start_time.set(Calendar.HOUR,hour);
                start_time.set(Calendar.MINUTE,minite);
                starttime.setText(HM.format( start_time.getTime()));
            }
        },start_time.get(Calendar.HOUR),start_time.get(Calendar.MINUTE),false);
        timePickerDialog.setTitle("pick UP time");
        timePickerDialog.show();
    }

    private void PickUpDate() {
        DatePickerDialog datePickerDialog=new DatePickerDialog(AddTask.this, android.R.style.Theme_Holo_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mounth, int day) {
                start_time.set(Calendar.YEAR,year);
                start_time.set(Calendar.MONTH,mounth);
                start_time.set(Calendar.DAY_OF_MONTH,day);
                end_time.set(Calendar.YEAR,year);
                end_time.set(Calendar.MONTH,mounth);
                end_time.set(Calendar.DAY_OF_MONTH,day);
                date.setText(DMY.format(start_time.getTime()));
            }
        },start_time.get(Calendar.YEAR),start_time.get(Calendar.MONTH),start_time.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setTitle("Select date");
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private void inalaize_adapters() {
        ArrayList<String>strings=new ArrayList<String>();
        for (int i: remind_items)
            strings.add(new String( String.valueOf(i)+" mintes early"));
        remindA=findViewById(R.id.remind_txt);
        adpteritems2=new ArrayAdapter<String>(this,R.layout.list_items,strings );
        remindA.setAdapter(adpteritems2);
        remindA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                slect_rem=i;
            }
        });
        repeatA=findViewById(R.id.repeat_txt);
        adaptertiems1=new ArrayAdapter<String>(this,R.layout.list_items,repeat_items);
        repeatA.setAdapter(adaptertiems1);
        repeatA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selct_rep=i;
            }
        });
        ColorA=findViewById(R.id.color_txt);
        adpteritems3=new ArrayAdapter<String>(this,R.layout.list_items,colors);
        ColorA.setAdapter(adpteritems3);
        ColorA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                color=i;
            }
        });
    }
}