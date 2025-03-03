package com.ltthuc.feature.domain.entity

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date

data class NotificationModel(
    val name: String,
    @SerializedName("name_vi")
    val nameVI: String,
    val channel: String,
    val uid: String,
    @SerializedName("uid_prefix")
    val uidPrefix: String,
    val duration: Double,
    val last_noti: String,
    val isFree: Int,
    val purchaseKey: String,
    @SerializedName("latest_aqi")
    val aqi: Int,
    var isPurchased: Boolean? = false
) {
    fun getLastNoti(dateFormat: SimpleDateFormat): Date {
        return try {

            dateFormat.parse(last_noti) ?: Date()
        } catch (ex: Exception) {
            Date()
        }
    }

    fun isFree(): Boolean {
        return isFree == 1
    }
}
