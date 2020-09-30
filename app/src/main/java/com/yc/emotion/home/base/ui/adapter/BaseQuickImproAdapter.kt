package com.yc.emotion.home.base.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.utils.clickWithTrigger

/**
 * Created by suns  on 2020/6/19 11:57.
 */
abstract class BaseQuickImproAdapter<T, K : BaseViewHolder?> : BaseQuickAdapter<T, K?> {
    constructor(layoutResId: Int, data: List<T>?) : super(layoutResId, data)
    constructor(data: List<T>?) : super(data)
    constructor(layoutResId: Int) : super(layoutResId)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): K {

        mContext = parent.context
        mLayoutInflater = LayoutInflater.from(mContext)
        return if (viewType == LOADING_VIEW || viewType == HEADER_VIEW || viewType == EMPTY_VIEW || viewType == FOOTER_VIEW) {
            super.onCreateViewHolder(parent, viewType)
        } else {
            val baseViewHolder = super.onCreateViewHolder(parent, viewType)
            bindViewClickListener(baseViewHolder)
            baseViewHolder
        }

    }


    private fun bindViewClickListener(baseViewHolder: BaseViewHolder?) {
        if (baseViewHolder == null) {
            return
        }
        val view = baseViewHolder.itemView
        if (onItemClickListener != null) {
            view.clickWithTrigger { v ->
                run {
                    var position = baseViewHolder.adapterPosition
                    if (position == RecyclerView.NO_POSITION) {
                        return@run
                    }
                    position -= headerLayoutCount
                    setOnItemClick(v, position)
                }
            }
        }
        if (onItemLongClickListener != null) {
            view.setOnLongClickListener { v ->
               run {
                   var position = baseViewHolder.adapterPosition
                   if (position == RecyclerView.NO_POSITION) {
                       return@setOnLongClickListener false
                   }
                   position -= headerLayoutCount
                   setOnItemLongClick(v, position) }
               }

        }
    }
}