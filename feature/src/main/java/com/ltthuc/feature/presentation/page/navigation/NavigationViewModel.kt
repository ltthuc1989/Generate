package com.ltthuc.feature.presentation.page.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.ltthuc.ads.AdsManager
import com.ltthuc.feature.MainGraphDirections
import com.ltthuc.feature.R
import com.ltthuc.navigation.api.Navigator
import com.ltthuc.navigation.api.model.Destination
import com.ltthuc.preferences.api.DatastoreRepository
import com.ltthuc.ui.base.BaseViewModel
import com.ltthuc.ui.utils.livedata.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
 class NavigationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val datastoreRepository: DatastoreRepository,
    private val navigator: Navigator,
     private val adsManager: AdsManager
) : BaseViewModel() {

    var navController: NavController? = null

    val tabOneSelected = SingleLiveEvent<Boolean>()
    val tabSettingSelected = SingleLiveEvent<Boolean>()
    val tabTwoSelected = SingleLiveEvent<Boolean>()
    val tabThreeSelected = SingleLiveEvent<Boolean>()
    private var _tabTitles = MutableLiveData<List<String>>()
    var tabTitles: LiveData<List<String>> = _tabTitles

    private val currentDestination
        get() = navController?.currentBackStackEntry?.destination?.id

    private var targetDestination: Destination?
        get() = savedStateHandle[TARGET_DESTINATION]
        set(value) = savedStateHandle.set(TARGET_DESTINATION, value)

    init {
        navigator.destination
            .onEach { destination ->
                val navController = navController ?: return@onEach
                navigateTo(
                    navController = navController,
                    destination = destination
                )

            }
            .launchIn(viewModelScope)
        tabOneSelected.value = true

    }

    fun setTabTitles(titles: List<String>) {
        _tabTitles.value = titles
    }

    private fun navigateTo(
        navController: NavController,
        destination: Destination,
        navOptions: NavOptions? = null
    ) {

        when (destination) {

            is Destination.Back -> {
                navController.navigateUp()
            }

            is Destination.Home -> {
                if (currentDestination == R.id.splashFragment) {
                    navController.popBackStack()
                }
                if (!navController.popBackStack(R.id.homeFragment, false)) {
                    navController.navigate(
                        MainGraphDirections.toHome(),
                        navOptions
                    )
                }
            }

            is Destination.Notification -> {
                if (currentDestination == R.id.splashFragment) {
                    navController.popBackStack()
                }
                if (!navController.popBackStack(R.id.notification, false)) {
                    navController.navigate(
                        MainGraphDirections.toNotification(),
                        navOptions
                    )
                }
            }


            is Destination.Setting -> {
                if (currentDestination == R.id.splashFragment) {
                    navController.popBackStack()
                }
                if (!navController.popBackStack(R.id.settingsFragment, false)) {
                    navController.navigate(
                        MainGraphDirections.toSettings(),
                        navOptions
                    )
                }
            }

            else -> {}
        }


    }

    fun homeClick() {
        if (tabOneSelected.value == true) return
        //adsManager.removeAd()
        tabOneSelected.value = true
        tabTwoSelected.value = false
        tabThreeSelected.value = false
        tabSettingSelected.value = false
        navigator.goTo(Destination.Home)

    }

    fun tabTwoClick() {
        if (tabTwoSelected.value == true) return
        tabTwoSelected.value = true
        tabOneSelected.value = false
        tabThreeSelected.value = false
        tabSettingSelected.value = false
        navigator.goTo(Destination.Notification)
    }

    fun tabThreeClick() {
        if (tabThreeSelected.value == true) return
        tabThreeSelected.value = true
        tabOneSelected.value = false
        tabTwoSelected.value = false
        tabSettingSelected.value = false
        // navigator.goTo(Destination.)
    }

    fun settingsClick() {
        if (tabSettingSelected.value == true) return
        tabSettingSelected.postValue(true)
        tabTwoSelected.value = false
        tabOneSelected.value = false
        tabThreeSelected.value = false
        navigator.goTo(Destination.Setting)
    }

    companion object {
        private const val TARGET_DESTINATION = "targetDestination"
    }
}

