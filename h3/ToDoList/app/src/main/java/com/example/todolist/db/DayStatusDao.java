package com.example.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.todolist.activity.MyApplication;
import com.example.todolist.bean.DayStatus;
import com.example.todolist.utils.DataUtil;
import com.example.todolist.utils.DateUtil;
import com.example.todolist.utils.LogUtil;

import java.util.Calendar;
import java.util.Date;

public class DayStatusDao {
    private static MyOpenHelper dbHelper=new MyOpenHelper(MyApplication.getContext(),"list.db",null,4);
    private static Calendar calendar=Calendar.getInstance();
    public static long insertDayStatus(DayStatus dayStatus){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        ContentValues values= DataUtil.getDayStatusCV(dayStatus);
        return database.insert("DayStatus",null,values);
    }
    public static int queryStatus(String time){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("DayStatus",new String[]{"status"},"time=?", new String[]{time},null,null,null);
        int status=-1;
        while(cursor.moveToNext()){
            status=cursor.getInt(cursor.getColumnIndex("status"));
        }
        cursor.close();
        return status;
    }
}
