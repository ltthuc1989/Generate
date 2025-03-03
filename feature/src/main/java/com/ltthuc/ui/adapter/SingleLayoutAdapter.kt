package com.ltthuc.ui.adapter


import androidx.databinding.ViewDataBinding

/**
 * Simplest implementation of [BaseAdapter] to use as initView single layout adapter.
 */
open class SingleLayoutAdapter<T : Any, B : ViewDataBinding>(
    private val layoutId: Int,
    items: List<T> = emptyList(),
    onItemClicked: ((Int, (T)) -> Unit)? = null,
    onBind: B.(Int) -> Unit = {},
    viewClickedTag: List<String>? = null,
    onViewClicked: ((Int, (T), String) -> Unit)? = null,
    onStartDragListener: OnStartDragListener? = null
) : BaseAdapter<T, B>(items = items, onItemClicked = onItemClicked, onBind = onBind, viewClickTag = viewClickedTag, onViewClicked = onViewClicked, onStartDragListener = onStartDragListener) {

    override fun getLayoutId(position: Int): Int = layoutId
}