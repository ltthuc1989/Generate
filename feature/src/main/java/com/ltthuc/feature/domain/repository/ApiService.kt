package com.ltthuc.feature.domain.repository

import com.ltthuc.feature.domain.entity.NotificationModel
import okhttp3.ResponseBody

interface ApiService {
    suspend fun getNotifications(countryCode: String): List<NotificationModel>
}


