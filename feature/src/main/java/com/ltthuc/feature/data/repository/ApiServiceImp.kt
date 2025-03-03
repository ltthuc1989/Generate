package com.ltthuc.feature.data.repository

import com.ltthuc.feature.domain.entity.NotificationModel
import com.ltthuc.feature.domain.repository.ApiExternalService
import com.ltthuc.feature.domain.repository.ApiService
import com.ltthuc.feature.domain.repository.ApiVnMobileService
import okhttp3.ResponseBody
import javax.inject.Inject

class ApiServiceImp @Inject constructor(
    private val apiVnMobileService: ApiVnMobileService,
    private  val apiExternalService: ApiExternalService
) : ApiService {
    override suspend fun getNotifications(countryCode: String): List<NotificationModel> {

        return  apiVnMobileService.getNotifications(countryCode)
    }
}