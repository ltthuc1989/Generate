package com.ltthuc.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.appcompat.content.res.AppCompatResources
import kotlinx.parcelize.Parcelize

@Parcelize
class MoreAppRowData (val icon: Int, val appName: String, val packageName: String): Parcelable {
    fun getAppIcon(context: Context): Drawable? {
        return AppCompatResources.getDrawable(context, icon)
    }
}