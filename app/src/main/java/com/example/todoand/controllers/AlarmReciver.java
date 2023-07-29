package com.example.todoand.controllers;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.todoand.Desrption;
import com.example.todoand.R;
import com.example.todoand.moduls.Task;

import java.util.Calendar;

public class AlarmReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
          int id=intent.getIntExtra("id",-1);
          Log.d("AlarmReciver",Integer.toString(id));
          if(id==-1)
              return;
          DataBaseHelper dataBaseHelper= DataBaseHelper.getInstance(context);
        Task task=dataBaseHelper.get_by_id(id);
        Intent i=new Intent(context, Desrption.class);
        i.putExtra("id",id);
        if(task.getRepeat().equals("Mounthly"))
            create_new_noti_formounthly(context, task);
        PendingIntent pendingIntent;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S) {
            pendingIntent =PendingIntent.getActivity(context,id,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
        }
        else {
            pendingIntent = PendingIntent.getActivity(context,id,i,PendingIntent.FLAG_UPDATE_CURRENT);
        }
        NotificationCompat.Builder builder=new NotificationCompat.
                Builder(context,"todo")
                .setSmallIcon(R.drawable.ic_notifaction)
                .setContentTitle(task.getTitle())
                .setContentText(task.getNote())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat=  NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id,builder.build());
    }
    public  void create_new_noti_formounthly(Context context,Task task)
    {
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, AlarmReciver.class);
        intent.putExtra("id",task.getId());
        PendingIntent pendingIntent;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S) {
            pendingIntent =PendingIntent.getBroadcast(context,task.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
        }
        else {
            pendingIntent =PendingIntent.getBroadcast(context,task.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Calendar tmp=task.getStartTime();
        if(task.getStartTime().get(Calendar.MINUTE)<task.getRemind())
        {
            tmp.set(Calendar.HOUR,task.getStartTime().get(Calendar.HOUR)-1);
            tmp.set(Calendar.MINUTE,60-(task.getRemind()-task.getStartTime().get(Calendar.MINUTE)));
        }
        else
           tmp.set(Calendar.MINUTE, task.getStartTime().get(Calendar.MINUTE)-task.getRemind());
        while (tmp.before(Calendar.getInstance()))
        {
            if(tmp.get(Calendar.MONTH)<11)
              tmp.set(Calendar.MONTH,tmp.get(Calendar.MONTH)+1 );
            else {
                tmp.set(Calendar.MONTH,0 );
                tmp.set(Calendar.YEAR,tmp.get(Calendar.YEAR)+1 );

            }
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP,tmp.getTimeInMillis(),pendingIntent);

    }
}
