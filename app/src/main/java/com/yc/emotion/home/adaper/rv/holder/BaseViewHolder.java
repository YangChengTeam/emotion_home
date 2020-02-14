package com.yc.emotion.home.adaper.rv.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.emotion.home.base.listener.RecyclerViewItemListener;

/**
 * Created by Administrator on 2017/9/12.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    private RecyclerViewItemListener myAdapterListener;

    public BaseViewHolder(Context context, ViewGroup root, int layoutRes, RecyclerViewItemListener listener) {
        super(LayoutInflater.from(context).inflate(layoutRes, root, false));//super(View view)
        this.myAdapterListener =listener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }


    /*public BaseViewHolder( ViewGroup root, int layoutRes) {
        super(LayoutInflater.from(root.getContext()).inflate(layoutRes, root, false));//super(View view)
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }*/

    /**绑定数据入口*/
    public abstract void bindData(T t);

    @Override
    public void onClick(View v) {
        if(myAdapterListener !=null){
            myAdapterListener.onItemClick(getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(myAdapterListener !=null){
            myAdapterListener.onItemLongClick(getAdapterPosition());
        }
        return true;
    }
}