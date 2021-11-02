package com.example.homework_rec;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author txs
 * @date 2018/01/16
 */

public class RcAdapterWholeChange extends RecyclerView.Adapter<RcAdapterWholeChange.MyViewHolder> {
    private Context context;
    /**
     * adapter传递过来的数据集合
     */
    private List<String> list = new ArrayList<>();
    /**
     * 需要改变颜色的text
     */
    private String text;
    /**
     * 属性动画
     */
    private Animator animator;

    /**
     * 在MainActivity中设置text
     */
    public void setText(String text) {
        this.text = text;
    }

    public RcAdapterWholeChange(Context context, List<String> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (text != null) {
            SpannableString string = matcherSearchText(Color.rgb(255, 0, 0), list.get(position), text);
            holder.mTvText.setText(string);
        } else {
            holder.mTvText.setText(list.get(position));
        }

        animator = AnimatorInflater.loadAnimator(context, R.animator.anim_set);
        animator.setTarget(holder.mLlItem);
        animator.start();
        holder.mLlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Recyclerview的点击监听接口
     */
    public interface onItemClickListener {
        void onClick(View view, int pos);
    }

    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(RcAdapterWholeChange.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlItem;
        private TextView mTvText;

        public MyViewHolder(View itemView) {
            super(itemView);
            mLlItem = (LinearLayout) itemView.findViewById(R.id.ll_item);
            mTvText = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }

    /**
     * 正则匹配 返回值是一个SpannableString 即经过变色处理的数据
     */
    private SpannableString matcherSearchText(int color, String text, String keyword) {
        SpannableString spannableString = new SpannableString(text);
        Pattern pattern = Pattern.compile(keyword);
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

}
