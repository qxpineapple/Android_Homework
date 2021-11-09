package com.example.todolist.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.bean.DayStatus;
import com.example.todolist.db.DayStatusDao;
import com.example.todolist.db.ListItemDao;
import com.example.todolist.receiver.MyService;
import com.example.todolist.utils.DateUtil;
import com.example.todolist.utils.LogUtil;
import com.example.todolist.utils.ToastUtil;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private ListFragment homeFragment;
    private static final String[] FRAGMENT_TAGS=
            new String[]{"homeFragment","calendarFragment","graphFragment","alarmFragment"};
    private int savedIndex=0;
    private long preTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager=getSupportFragmentManager();
        initLastData();
        initViews();
        initToolbar();
        if(savedInstanceState!=null){
            savedIndex=savedInstanceState.getInt("savedIndex");
        }
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        hideFragments(transaction);
        savedIndex=0;
        toolbar.setTitle(getResources().getString(R.string.home));
        if(homeFragment==null){
            homeFragment=new ListFragment();
            transaction.add(R.id.main_content,homeFragment,FRAGMENT_TAGS[0]);
        }else{
            transaction.show(homeFragment);
        }
        transaction.commit();
    }
    private void initLastData(){
        SharedPreferences preferences=getSharedPreferences("list",MODE_PRIVATE);
        String lastVisitTime=preferences.getString("lastVisitTime","");
        Calendar tempCalendar=Calendar.getInstance();
        String todayTime= DateUtil.getYearMonthDayNumberic(tempCalendar.getTime());
        if(!lastVisitTime.equals("")){
            if(!lastVisitTime.equals(todayTime)){
                DayStatus dayStatus=ListItemDao.updateNoRecord(lastVisitTime);
                if(dayStatus!=null){
                    DayStatusDao.insertDayStatus(dayStatus);
                }
            }
        }
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("lastVisitTime",todayTime);
        editor.apply();
    }
    private void initViews(){
        toolbar=findViewById(R.id.main_toolbar);
    }
    private void initToolbar(){
        setSupportActionBar(toolbar);
    }



    private void hideFragments(FragmentTransaction transaction){
        if(homeFragment!=null){
            transaction.hide(homeFragment);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("savedIndex",savedIndex);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long curTime=System.currentTimeMillis();
            if ((curTime - preTime) > 1000 * 2) {
                ToastUtil.showToast("再按一次退出程序");
                preTime = curTime;
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
