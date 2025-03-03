package com.ltthuc.feature.presentation.page.home

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ltthuc.ads.AdsManager
import com.ltthuc.ads.BannerAd
import com.ltthuc.ads.BannerType
import com.ltthuc.feature.R
import com.ltthuc.feature.databinding.FragmentHomeBinding
import com.ltthuc.ui.base.BaseFragment
import com.ltthuc.ui.base.toolbar.IToolbar
import com.ltthuc.ui.base.toolbar.ToolbarItem
import com.ltthuc.ui.base.toolbar.ToolbarManager
import com.ltthuc.ui.base.toolbar.ToolbarViewModel
import com.ltthuc.ui.extension.showToast
import com.ltthuc.ui.helper.rate.RateConfig
import com.ltthuc.ui.helper.rate.RateViewModel
import com.ltthuc.utils.ISecretAdsKey

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var iSecretAdsKey: ISecretAdsKey
    @Inject
    lateinit var adsManager: AdsManager

    override fun getVM(): HomeViewModel = viewModel

    private val rateHelper: RateViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun bindVM(binding: FragmentHomeBinding, vm: HomeViewModel) {
        binding.viewModel = viewModel
    }

    override fun setUpToolbar() {
        super.setUpToolbar()
        showBottomBar(true)
        val config = ToolbarManager.ToolbarConfig(
            title = "Home",
            titleSize = 20f,
            backgroundColorResId = com.ltthuc.ui.R.color.color_orange,
            menuRes = R.menu.home_menu,
            onMenuItemClick = { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_profile -> {

                        true
                    }
                    else -> false
                }
            }
        )

        (requireActivity() as IToolbar).getToolbar().configureToolbar(config)
        viewLifecycleOwner.lifecycleScope.launch {
            adsManager.showAds(binding.bannerAdView)
        }

    }

    override fun rightClickToolbar() {
        super.rightClickToolbar()
    }

    override fun onResume() {
        super.onResume()
        rateHelper.setRateConfig(RateConfig())
        rateHelper.showRateDialog(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerAd?.isExpanded = true
        logD("onDestroyView", "enter")
      //  adsManager.removeAd()
    }

}
