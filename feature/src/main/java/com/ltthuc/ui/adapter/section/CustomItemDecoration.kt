package com.ltthuc.ui.adapter.section

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecoration(
    context: Context,
    private val isShowLastDivider: Boolean = false,
    private val isShowFirstDivider: Boolean = true,
    private val isShowTopDivider: Boolean = false,
) : RecyclerView.ItemDecoration() {

    private val ATTRS = intArrayOf(android.R.attr.listDivider)
    private var mDivider: Drawable? = null

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = 0
        val right = parent.width

        for (i in 0 until parent.childCount) {
            val child: View = parent.getChildAt(i)
            val position = parent.getChildLayoutPosition(child)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top: Int = child.bottom + params.bottomMargin
            val bottom: Int = top + (mDivider?.intrinsicHeight ?: 0)

            if (position != state.itemCount - 1 || isShowLastDivider ) {
                mDivider?.setBounds(left, top, right, bottom)
                mDivider?.draw(c)
            }

            if (position == 0 && isShowTopDivider) {
                val bottomFirst = child.top - params.topMargin
                val topFirst = bottomFirst - (mDivider?.intrinsicHeight ?: 0)
                mDivider?.setBounds(left, topFirst, right, bottomFirst)
                mDivider?.draw(c)
            }


        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildLayoutPosition(view)

        // hide the divider for the last child
        if (!isShowLastDivider && position == state.itemCount - 1) {
            outRect.setEmpty()
        } else if (!isShowFirstDivider && position == 0) {
            outRect.setEmpty()
        } else if (isShowFirstDivider && isShowTopDivider && position == 0) {
            val height = mDivider?.intrinsicHeight ?: 0
            outRect.top = height
            outRect.bottom = (mDivider?.intrinsicHeight ?: 0)

        } else {
            outRect.bottom = (mDivider?.intrinsicHeight ?: 0)
            // super.getItemOffsets(outRect, view, parent, state)
        }
    }
}