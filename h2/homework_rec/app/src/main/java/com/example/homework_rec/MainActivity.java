package com.example.homework_rec;

import android.content.Context;
import android.os.IBinder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /**
     * 搜索框
     */
    private EditText mEdtSearch;
    /**
     * 删除按钮
     */
    private ImageView mImgvDelete;
    /**
     * recyclerview
     */
    private RecyclerView mRcSearch;
    /**
     * 全部匹配的适配器
     */
    private RcAdapterWholeChange adapter;
    /**
     * 所有数据 可以是联网获取 如果有需要可以将其储存在数据库中 我们用简单的String做演示
     */
    private List<String> wholeList;
    /**
     * 此list用来保存符合我们规则的数据
     */
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            initView();
            initData();
            refreshUI();
            setListener();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置监听
     */
    private void setListener() {
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    mImgvDelete.setVisibility(View.GONE);
                } else {
                    mImgvDelete.setVisibility(View.VISIBLE);
                }
                doChangeColor(editable.toString().trim());
            }
        });

        adapter.setOnItemClickListener(new RcAdapterWholeChange.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Toast.makeText(MainActivity.this, "点击了第" + pos+"个数据", Toast.LENGTH_SHORT).show();
            }
        });

        mImgvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEdtSearch.setText("");
            }
        });
    }

    /**
     * 字体匹配方法
     */
    private void doChangeColor(String text) {

        list.clear();

        if (text.equals("")) {
            list.addAll(wholeList);

            adapter.setText(null);
            refreshUI();
        } else {

            for (String i : wholeList) {
                if (i.contains(text)) {
                    list.add(i);
                }
            }

            adapter.setText(text);
            refreshUI();
        }
    }

    private void initData() {
        //假数据
        wholeList = new ArrayList<>();
        list = new ArrayList<>();
        wholeList.add("第一天一天");
        wholeList.add("第二天一天");
        wholeList.add("第三天一天");
        wholeList.add("第四天一天");
        wholeList.add("第五天五天");
        wholeList.add("第六天一天");
        wholeList.add("第七天七天");
        wholeList.add("第一天八天");
        wholeList.add("第一天九天");
        wholeList.add("第一天十天");
        wholeList.add("第一天十一天");

        list.addAll(wholeList);
    }

    /**
     * 刷新UI
     */
    private void refreshUI() {
        if (adapter == null) {
            adapter = new RcAdapterWholeChange(this, list);
            mRcSearch.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        mEdtSearch = (EditText) findViewById(R.id.edt_search);
        mImgvDelete = (ImageView) findViewById(R.id.imgv_delete);
        mRcSearch = (RecyclerView) findViewById(R.id.rc_search);
        mRcSearch.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
