package com.example.todoand.moduls;

import java.util.Calendar;

public class Task implements Comparable {
    private int id,remind,color;
    private  String title,note,repeat;
    private Calendar StartTime,EndTime;
    private  int IsCompleted;

    public Task(int id, int remind, int color, String title, String note, String repeat, Calendar startTime, Calendar endTime,int isCompleted) {
             this.id=id;
             this.remind=remind;
             this.color=color;
             this.title=title;
             this.note=note;
             this.repeat=repeat;
             this.StartTime=startTime;
             this.EndTime=endTime;
             this.IsCompleted=isCompleted;
    }
    @Override
    public int compareTo(Object o) {
        Task other=(Task) o;
        if(StartTime.before(other.StartTime))
        return 1;
        else
            return  0;

    }
    public void  setId(int id){this.id=id;}
    public void compleateTask(){
        IsCompleted=1;
    }
    public int getId() {
        return id;
    }

    public int getRemind() {
        return remind;
    }

    public String getTitle() {
        return title;
    }
    public int getColor(){return color;}

    public String getNote() {
        return note;
    }

    public String getRepeat() {
        return repeat;
    }

    public Calendar getStartTime() {
        return StartTime;
    }

    public Calendar getEndTime() {
        return EndTime;
    }

    public int isCompleted() {
       return IsCompleted;
    }
}
