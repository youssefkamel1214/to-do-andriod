package com.example.todoand;
import com.example.todoand.controllers.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.todoand.databinding.ActivityMainBinding;
import com.example.todoand.databinding.BottomSheetDialogBinding;
import com.example.todoand.moduls.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements Datecontroll_interface {
    private ActivityMainBinding binding;
    private  MaterialTimePicker materalTimepicker;
    private PendingIntent pendingIntent;
    private RecyclerView.Adapter datetimadapter;
    private RecyclerView DatetimeR;
    private AlarmManager alarmManager;
    private ImageView imageButton;
    private boolean isDark;
    private Calendar selcted_date=Calendar.getInstance();
    private ArrayList<Task>tasks=new ArrayList<Task>();
    private    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.addtaskbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move_add_task_page();
            }
        });
        inalize_datetimeadapter();
        inialize_Databaseandrecycler();
        filterdates_give_adptares();
    }




    private void filterdates_give_adptares() {
        ArrayList<Task>filterd=new ArrayList<Task>();
        SimpleDateFormat DMY=new SimpleDateFormat("dd-MMM-YYYY");
        binding.Loading.setText("Loading...");
        binding.Loading.setVisibility(View.VISIBLE);
        binding.Taskrecler.setVisibility(View.GONE);
        selcted_date.set(Calendar.MINUTE,0);
        selcted_date.set(Calendar.HOUR,0);
        selcted_date.set(Calendar.SECOND,0);
        selcted_date.set(Calendar.MILLISECOND,0);
        for (Task task:tasks) {
            Calendar tmp=task.getStartTime();
            tmp.set(Calendar.MINUTE,0);
            tmp.set(Calendar.HOUR,0);
            tmp.set(Calendar.SECOND,0);
            tmp.set(Calendar.MILLISECOND,0);
            if(selcted_date.before(tmp))
                continue;
            if(task.getRepeat().equals("Daily"))
            {
                filterd.add(task);
            }
            else if(task.getRepeat().equals("Weekly") &&
                    (selcted_date.get(Calendar.DAY_OF_WEEK)==
                            tmp.get(Calendar.DAY_OF_WEEK)))
            {
                filterd.add(task);
            }
            else if(task.getRepeat().equals("Mounthly")&&
                    (selcted_date.get(Calendar.DAY_OF_MONTH)==
                            tmp.get(Calendar.DAY_OF_MONTH)))
            {
                filterd.add(task);
            }
            else if(DMY.format(selcted_date.getTime()).equals(DMY.format(task.getStartTime().getTime()))==true)
            {
                filterd.add(task);
            }
        }
        if(filterd.size()>0)
        {
            Task_adapter taskAdapter=new Task_adapter(filterd,this);
            binding.Taskrecler.setAdapter(taskAdapter);
            binding.Loading.setVisibility(View.GONE);
            binding.Taskrecler.setVisibility(View.VISIBLE);
        }
        else {
            binding.Loading.setText("there no Tasks");
            binding.Loading.setVisibility(View.VISIBLE);
            binding.Taskrecler.setVisibility(View.GONE);
        }
    }

    private void move_add_task_page() {
        Intent I=new Intent(getApplicationContext(),AddTask.class);
        startActivityForResult(I,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            inialize_Databaseandrecycler();
            filterdates_give_adptares();
        }

    }

    private void inialize_Databaseandrecycler() {
        try {
            dataBaseHelper=DataBaseHelper.getInstance(this);
            tasks= dataBaseHelper.get_all_tasks();
        }catch (Exception e){
            System.out.println(e);
        }
        Spaceitemdecoration datespace=new Spaceitemdecoration(5,10);
        binding.Taskrecler.addItemDecoration(datespace);
        binding.Taskrecler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }

    private void inalize_datetimeadapter(){

        ArrayList<String>days=new ArrayList<String>();
        Calendar c= Calendar.getInstance();
        SimpleDateFormat sdf=new  SimpleDateFormat("EEE dd MMM");
        binding.today.setText("Today\n"+sdf.format(c.getTime()));
        for (int i=0;i<31;i++)
        {
            days.add(sdf.format(c.getTime()));
            c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+1);
        }
        Spaceitemdecoration datespace=new Spaceitemdecoration(5,0);
        binding.datercycler.addItemDecoration(datespace);
        binding.datercycler.setHasFixedSize(true);
        binding.datercycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        datetimadapter=new recyler_adapter_dattime(days,this);
        binding.datercycler.setAdapter(datetimadapter);

    }



    @Override
    public void onDateselect(int days) {
        selcted_date=Calendar.getInstance();
        selcted_date.set(Calendar.DATE,
                selcted_date.get(Calendar.DATE)+days);
        filterdates_give_adptares();
    }

    @Override
    public void ontaskclick(Task task) {
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
        Button showtask=bottomSheetDialog.findViewById(R.id.showtaskbtn);
        Button deletetask=bottomSheetDialog.findViewById(R.id.deletetaskbtn);
        Button completetask=bottomSheetDialog.findViewById(R.id.completetaskbtn);

        showtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I=new Intent(getApplicationContext(),Desrption.class);
                I.putExtra("id",task.getId());
                startActivity(I);
                bottomSheetDialog.cancel();
            }
        });
        deletetask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.Delete_task(task);
                cancelnotifction(task);
                inialize_Databaseandrecycler();
                filterdates_give_adptares();
                bottomSheetDialog.cancel();
            }
        });
        if(task.isCompleted()!=0){
            completetask.setVisibility(View.GONE);
        }else {
            completetask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelnotifction(task);
                    dataBaseHelper.Update_task(task);
                    inialize_Databaseandrecycler();
                    filterdates_give_adptares();
                    bottomSheetDialog.cancel();
                }
            });
        }
        bottomSheetDialog.show();
    }

    private void cancelnotifction(Task task) {
        Intent intent=new Intent(this, AlarmReciver.class);
        intent.putExtra("id",task.getId());
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this
                ,task.getId(),intent,PendingIntent.FLAG_NO_CREATE);

        if(alarmManager==null)
        {
            alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        if(pendingIntent!=null)
            alarmManager.cancel(pendingIntent);
    }
}