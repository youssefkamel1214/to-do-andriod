package com.example.todoand.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.example.todoand.moduls.Task;

import java.util.ArrayList;
import java.util.Calendar;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String Task_table = "Task_table";
    public static final String Task_title = "title";
    public static final String Task_note = "note";
    public static final String Task_repeat = "repeat";
    public static final String Task_remind = "remind";
    public static final String Task_color = "color";
    public static final String Task_start_time = "starttime";
    public static final String Task_endtime = "endtime";
    public static final String Task_iscompleted = "isCompleted";


    public DataBaseHelper(@Nullable Context context) {
        super(context, "TaskDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE "+Task_table+" (id INTEGER PRIMARY KEY AUTOINCREMENT"+
        ",title text,note text,isCompleted intger ,startTime integer,"+
                "endTime BIGINT ,color integer,remind BIGINT ,repeat  text )";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long addOne(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(task.getStartTime().getTime());
        System.out.println(task.getEndTime().getTime());
        System.out.println(task.getRepeat());
        ContentValues cv = new ContentValues();
        try {
            cv.put(Task_remind,task.getRemind());
            cv.put(Task_color,task.getColor());
            cv.put(Task_title,task.getTitle());
            cv.put(Task_iscompleted,task.isCompleted());
            cv.put(Task_note,task.getNote());
            cv.put(Task_repeat,task.getRepeat());
            cv.put(Task_start_time,task.getStartTime().getTimeInMillis());
            cv.put(Task_endtime,task.getEndTime().getTimeInMillis());
            long ins = db.insert(Task_table,null,cv);
            db.close();
            return ins;
        }catch (Exception e){
            System.out.println("some thing wrong happend:");
            System.out.println(e);
            return -1;
        }


    }

    public ArrayList<Task> get_all_tasks()
    {
         ArrayList<Task>tasks=new ArrayList<Task>();
        String query = "SELECT * FROM " + Task_table  ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        Task task;
        if(cursor.moveToFirst()){
            do {
                try {


                    Calendar starttime = Calendar.getInstance(), endtime = Calendar.getInstance();
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    String note = cursor.getString(2);
                    int iscompleated = cursor.getInt(3);
                    starttime.setTimeInMillis(cursor.getLong(4));
                    endtime.setTimeInMillis(cursor.getLong(5));
                    int color = cursor.getInt(6);
                    int remind = cursor.getInt(7);
                    String repeat = cursor.getString(8);
                    tasks.add(new Task(id, remind, color, title, note, repeat, starttime, endtime, iscompleated));
                }
                catch (Exception e)
                {
                    System.out.println("some thing wrong happend:");
                    System.out.println(e);
                }
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tasks;
    }
    public boolean Update_task(Task task){
        String query="UPDATE "+Task_table+" Set "+Task_iscompleted+" = '"+'1'+"' Where id = '"+task.getId()+"'";
        SQLiteDatabase db=this.getWritableDatabase();
      try {
          db.execSQL(query);
          return true;
      }catch (Exception e){
          return false;
      }

    }
    public boolean Delete_task(Task task){
        String query="DELETE FROM " + Task_table + " WHERE id ='" + task.getId()+"'" ;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        if (cursor.moveToFirst())
            return true;
        else
            return false;
    }
    public Task get_by_id(int id)
    {
        Task task;
        try {
            String query ="SELECT * FROM " + Task_table + " WHERE id"  + " = '" + id+"'" ;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query,null);
             if (cursor.moveToFirst()){
                 Calendar starttime = Calendar.getInstance(), endtime = Calendar.getInstance();
                 id = cursor.getInt(0);
                 String title = cursor.getString(1);
                 String note = cursor.getString(2);
                 int iscompleated = cursor.getInt(3);
                 starttime.setTimeInMillis(cursor.getLong(4));
                 endtime.setTimeInMillis(cursor.getLong(5));
                 int color = cursor.getInt(6);
                 int remind = cursor.getInt(7);
                 String repeat = cursor.getString(8);
                 System.out.println(starttime.getTime());
                 return new Task(id, remind, color, title, note, repeat, starttime, endtime, iscompleated);

             }
                 else{
                     return null;
             }

        }catch (Exception e){
            System.out.println(e);
            return null;
        }

    }
}