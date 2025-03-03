package com.ltthuc.feature.domain.repository

import com.ltthuc.feature.domain.entity.NotificationModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiVnMobileService {

    @GET("/AirQuality/api.php?method=notifications")
    suspend fun getNotifications(@Query("countryCode") countryCode: String): List<NotificationModel>
}
