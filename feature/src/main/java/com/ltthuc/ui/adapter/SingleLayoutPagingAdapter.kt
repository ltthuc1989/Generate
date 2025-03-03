package com.ltthuc.ui.adapter

import androidx.databinding.ViewDataBinding
import com.ltthuc.ui.utils.paging.ItemComparable

open class SingleLayoutPagingAdapter<T : ItemComparable, B : ViewDataBinding>(
    private val layoutId: Int,
    onItemClicked: ((Int, (T)) -> Unit)? = null,
    onBind: B.(Int) -> Unit = {}
) : BasePagingAdapter<T, B>(onItemClicked = onItemClicked, onBind = onBind) {

    override fun getLayoutId(position: Int): Int = layoutId
}