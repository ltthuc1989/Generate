package com.ltthuc.feature.presentation.page.splash

import androidx.lifecycle.viewModelScope
import com.ltthuc.ui.base.BaseViewModel
import com.ltthuc.navigation.api.Navigator
import com.ltthuc.navigation.api.model.Destination
import com.ltthuc.preferences.api.DatastoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val navigator: Navigator,
    datastoreRepository: DatastoreRepository
) : BaseViewModel() {


    init {
        viewModelScope.launch(Dispatchers.Main) {
            navigator.goTo(Destination.Home)
        }
    }
}