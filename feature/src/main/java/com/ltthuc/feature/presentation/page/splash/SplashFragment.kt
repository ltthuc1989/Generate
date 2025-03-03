package com.ltthuc.feature.presentation.page.splash


import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.ltthuc.ads.AdsSettings
// import com.ltthuc.ads.AppOpen
import com.ltthuc.ads.AppOpenAdsManager
import com.ltthuc.ads.GoogleMobileAdsConsentManager

import com.ltthuc.feature.databinding.FragmentSplashBinding

import com.ltthuc.ui.base.BaseFragment
import com.ltthuc.ui.helper.rate.RateViewModel


import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>(FragmentSplashBinding::inflate) {
    private val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager
    @Inject
    lateinit var appOpenAdsManager: AppOpenAdsManager

    val rateHelper: RateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rateHelper.increaseCount()
    }

    override fun onStart() {
        super.onStart()
        AdsSettings.isSplashScreen = true
    }

    override fun onStop() {
        super.onStop()
        AdsSettings.isSplashScreen = false
    }

//    override fun restoreAds() {
//
//    }
//
//    override fun closeAds() {
//
//    }

    override fun getVM(): SplashViewModel = viewModel


    override fun bindVM(binding: FragmentSplashBinding, vm: SplashViewModel)  = Unit
}