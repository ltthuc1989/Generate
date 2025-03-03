package com.ltthuc.navigation.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Destination : Parcelable {

    @Parcelize
    object Back : Destination()

    @Parcelize
    object Home : Destination()

    @Parcelize
    object Notification : Destination()


    @Parcelize
    object Setting : Destination()

}

