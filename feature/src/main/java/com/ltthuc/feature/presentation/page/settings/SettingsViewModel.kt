package com.ltthuc.feature.presentation.page.settings

import com.ltthuc.navigation.api.Navigator
import com.ltthuc.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class SettingViewModel @Inject constructor(
     private val navigator: Navigator
) : BaseViewModel() {

    fun onBackClick() {
        navigator.back()

    }



}