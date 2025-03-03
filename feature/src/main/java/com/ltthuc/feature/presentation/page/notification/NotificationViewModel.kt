package com.ltthuc.feature.presentation.page.notification


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.ltthuc.billing.helper.BillingManager

import com.ltthuc.feature.domain.entity.NotificationModel
import com.ltthuc.feature.domain.repository.ApiService
import com.ltthuc.navigation.api.Navigator
import com.ltthuc.ui.base.BaseViewModel

import com.ltthuc.utils.CommonUtils
import com.ltthuc.utils.DeviceUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val apiService: ApiService,
    val navigator: Navigator,
) :
    BaseViewModel() {
    @Inject
    lateinit var billingManager: BillingManager

    val notifications = MutableLiveData<List<NotificationModel>>()

    private val sharedPrefs by lazy { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }

    init {
        getNotifications()
    }

    private fun getNotifications() {
        progressLiveEvent.value = true
        viewModelScope.launch(handlerException) {

           notifications.value = apiService.getNotifications(getCountryCode(context = context))
            progressLiveEvent.postValue(false)
        }
    }




    override fun onCleared() {
        super.onCleared()
        billingManager.close()
    }



   private fun getCountryCode(context: Context): String {
           return CommonUtils.getCountryCode(context)
    }



}