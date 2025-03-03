package com.ltthuc.ui.adapter.section

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Layout.Alignment.ALIGN_CENTER
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getDimensionOrThrow
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.graphics.withTranslation
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import com.ltthuc.ui.R
import com.ltthuc.ui.extension.newStaticLayout
import kotlin.math.ceil

/**
 * A [RecyclerView.ItemDecoration] which draws sticky headers marking the header title in a given list of
 * [Block]s. It also inserts gaps between days.
 */
class SectionHeadersDecoration(
    val context: Context,
    style: Int,
) : ItemDecoration() {

    private val paint: TextPaint
    private val textWidth: Int
    private val decorHeight: Int
    private val verticalBias: Float
    private val divider: Drawable
    init {
        val attrs = context.obtainStyledAttributes(
            style,
            R.styleable.SectionHeader
        )
        paint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG).apply {
            color = attrs.getColorOrThrow(R.styleable.SectionHeader_android_textColor)
            textSize = attrs.getDimensionOrThrow(R.styleable.SectionHeader_android_textSize)
            divider = attrs.getDrawable(R.styleable.SectionHeader_android_drawable)?: getDefaultDivider(context)!!


            try {
                typeface = ResourcesCompat.getFont(
                    context,
                    attrs.getResourceIdOrThrow(R.styleable.SectionHeader_android_fontFamily)
                )
            } catch (_: Exception) {
                // ignore
            }
        }

        paint.bgColor = ContextCompat.getColor(context, R.color.color_orange)

        textWidth = attrs.getDimensionPixelSizeOrThrow(R.styleable.SectionHeader_android_width)
        val height = attrs.getDimensionPixelSizeOrThrow(R.styleable.SectionHeader_android_height)
        val minHeight = ceil(paint.textSize).toInt()
        decorHeight = Math.max(height, minHeight)

        verticalBias = attrs.getFloat(R.styleable.SectionHeader_verticalBias, 0.5f).coerceIn(0f, 1f)

        attrs.recycle()
    }

    // Get the header index and create header layouts for each
    private var _headerSlots: Map<Int, StaticLayout>  = emptyMap()

    fun setHeaderSlot(slot: Map<Int, StaticLayout>) {
        this._headerSlots = slot
    }
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        val position = parent.getChildAdapterPosition(view)
        outRect.top = if (_headerSlots.containsKey(position)) decorHeight else 0
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: State) {
        val layoutManager = parent.layoutManager ?: return
        val centerX = parent.width / 2f
        val dividerLeft: Int = 32

        // right margin for the divider with
        // reference to the parent width
        val dividerRight: Int = parent.width - 32
        parent.forEach { child ->
            if (child.top < parent.height && child.bottom > 0) {
                // Child is visible
                val layout = _headerSlots[parent.getChildAdapterPosition(child)]
                if (layout != null) {
                    val dx = centerX - (layout.width / 2)
                    val dy = layoutManager.getDecoratedTop(child) +
                            child.translationY +
                            // offset vertically within the space according to the bias
                            (decorHeight - layout.height) * verticalBias
                    canvas.withTranslation(x = 0f + dividerLeft, y = dy) {
                        layout.draw(this)
                    }


                    val params = child.layoutParams as RecyclerView.LayoutParams


                    val firstDividerTop: Int = child.top+ params.topMargin
                    val firstDividerBottom: Int = firstDividerTop + divider.intrinsicHeight
                    divider.setBounds(dividerLeft,  firstDividerTop, dividerRight, firstDividerBottom)
                    divider.draw(canvas)

                    val dividerTop: Int = child.bottom + params.bottomMargin
                    val dividerBottom: Int = dividerTop + divider.intrinsicHeight
                    divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                    divider.draw(canvas)

                } else {
                    val params = child.layoutParams as RecyclerView.LayoutParams

                    // calculating the distance of the
                    // divider to be drawn from the top
                    val dividerTop: Int = child.bottom + params.bottomMargin
                    val dividerBottom: Int = dividerTop + divider.intrinsicHeight

                    divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                    divider.draw(canvas)
                }
            }
        }
    }

    /**
     * Create a header layout for the given [time]
     */
    fun createHeader(
        title: String,
    ): StaticLayout {
        return newStaticLayout(title, paint, textWidth, ALIGN_CENTER, 1f, 0f, false)
    }

    private fun getDefaultDivider(context: Context): Drawable? {
        val attrsDivider = intArrayOf(android.R.attr.listDivider)
        val styledAttributes = context.obtainStyledAttributes(attrsDivider)
        val divider = styledAttributes.getDrawable(0)
        styledAttributes.recycle()
        return divider
    }

}
