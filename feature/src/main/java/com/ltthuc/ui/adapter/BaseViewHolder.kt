package com.ltthuc.ui.adapter

import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.MotionEventCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


/**
 * A wrapper for [RecyclerView.ViewHolder] in order to use in [MvvmAdapter]
 * @param T type of data model that will be set in Binding class
 * @param B A [ViewDataBinding] extended class that representing Binding for item layout
 *
 * @param binding an instance of [B] to get root view for [RecyclerView.ViewHolder] constructor and
 * display data model
 */
open class BaseViewHolder<in T : Any, out B : ViewDataBinding>(
    open val binding: B,
    onItemClicked: ((Int, (T)) -> Unit)? = null,
    viewClickedTag: List<String>? = null,
    private val onViewClicked: ((Int, (T), String) -> Unit)? = null,
    onStartDragListener: OnStartDragListener? = null
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: T
    private var currentSelectedItem = -1

    init {
        if (!viewClickedTag.isNullOrEmpty()) {
            viewClickedTag?.forEach { tag ->
                val view: View = itemView.findViewWithTag(tag)
                if (tag == "drag") {
                    view.setOnTouchListener { v, event ->
                        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                            onStartDragListener?.onStartDrag(this)

                        }
                        return@setOnTouchListener false
                    }

                } else {
                    view.setOnClickListener {
                        onViewClicked?.invoke(bindingAdapterPosition, item, tag)
                    }
                }

            }
        } else {
            itemView.setOnClickListener {
                itemView.isSelected = true
                onItemClicked?.invoke(bindingAdapterPosition, item)
            }

        }
    }

    fun getItemData(): @UnsafeVariance T {
        return item
    }


    /**
     * binding method that bind data model to [ViewDataBinding] class
     *
     * @param itemBindingId Generated item binding id that will should be founded in BR class.
     * @param item an instance of [T] to be shown in layout
     */
    open fun bind(itemBindingId: Int, item: T) {
        this.item = item
        binding.setVariable(itemBindingId, item)
        binding.executePendingBindings()
    }

}