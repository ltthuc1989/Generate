package com.ltthuc.ui.base.toolbar

import android.util.TypedValue
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import com.ltthuc.ui.R

class ToolbarManager(private val activity: AppCompatActivity) {
    private var toolbar: Toolbar? = null

    // Base configuration for toolbar
    data class ToolbarConfig(
        val title: String? = null,
        val titleColorResId: Int? = null, // Now using resource ID instead of direct color
        val titleSize: Float = 18f,
        val isTitleCentered: Boolean = true,  // Default to centered
        val backgroundColorResId: Int? = null, // Now using resource ID
        val navigationIcon: Int? = null,
        val navigationIconTintResId: Int? = R.color.default_text_color, // Now using resource ID
        val menuRes: Int? = null,
        val menuIconTintResId: Int? = null, // Now using resource ID
        val elevation: Float? = null,
        val onNavigationClick: (() -> Unit)? = null,
        val onMenuItemClick: ((MenuItem) -> Boolean)? = null
    )

    // Visibility methods
    fun hideToolbar() {
        toolbar?.visibility = View.GONE
    }

    fun toggleToolbarVisibility() {
        if (isToolbarVisible()) {
            hideToolbar()
        } else {
            showToolbar()
        }
    }

    fun showToolbar() {
        toolbar?.visibility = View.VISIBLE
    }

    fun isToolbarVisible(): Boolean {
        return toolbar?.visibility == View.VISIBLE
    }

    fun getToolbar(): Toolbar? = toolbar

    fun initToolbar(toolbar: Toolbar) {
        this.toolbar = toolbar
        activity.setSupportActionBar(toolbar)
    }

    fun configureToolbar(config: ToolbarConfig) {
        toolbar?.let { applyBaseConfig(it, config) }
    }

    private fun applyBaseConfig(toolbar: Toolbar, config: ToolbarConfig) {
        // Clear previous content
        toolbar.title = null
        toolbar.navigationIcon = null
        toolbar.menu.clear()
        showToolbar()

        // Remove any previous custom title views
        for (i in 0 until toolbar.childCount) {
            val view = toolbar.getChildAt(i)
            if (view is TextView && view.tag == "centered_title") {
                toolbar.removeView(view)
            }
        }

        // Set title
        if (config.isTitleCentered) {
            // For centered title, we need a custom view
            val titleTextView = TextView(activity).apply {
                text = config.title
                textSize = config.titleSize
                gravity = Gravity.CENTER
                tag = "centered_title" // Add a tag to identify our custom view
                layoutParams = Toolbar.LayoutParams(
                    Toolbar.LayoutParams.WRAP_CONTENT,
                    Toolbar.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER
                }

                // Apply theme-aware title color
                config.titleColorResId?.let {
                    setTextColor(ContextCompat.getColor(activity, it))
                } ?: run {
                    // If no specific color is provided, use default text color from theme
                    val typedValue = TypedValue()
                    activity.theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
                    setTextColor(typedValue.data)
                }
            }
            toolbar.addView(titleTextView)
        } else {
            toolbar.title = config.title
            config.titleColorResId?.let {
                toolbar.setTitleTextColor(ContextCompat.getColor(activity, it))
            } ?: run {
                // If no specific color is provided, use default text color from theme
                val typedValue = TypedValue()
                activity.theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
                toolbar.setTitleTextColor(typedValue.data)
            }
        }

        // Background color
        config.backgroundColorResId?.let {
            toolbar.setBackgroundColor(ContextCompat.getColor(activity, it))
        }

        // Elevation
        config.elevation?.let {
            toolbar.elevation = it
        }

        // Navigation icon
        config.navigationIcon?.let { icon ->
            toolbar.setNavigationIcon(icon)
            config.navigationIconTintResId?.let { tintResId ->
                toolbar.navigationIcon?.setTint(ContextCompat.getColor(activity, tintResId))
            }
            config.onNavigationClick?.let { listener ->
                toolbar.setNavigationOnClickListener { listener() }
            }
        }

        // Menu
        config.menuRes?.let { menuRes ->
            toolbar.inflateMenu(menuRes)
            config.menuIconTintResId?.let { tintResId ->
                val tintColor = ContextCompat.getColor(activity, tintResId)
                toolbar.menu.forEach { menuItem ->
                    menuItem.icon?.setTint(tintColor)
                }
            }
            config.onMenuItemClick?.let { listener ->
                toolbar.setOnMenuItemClickListener { menuItem ->
                    listener(menuItem)
                }
            }
        }
    }
}