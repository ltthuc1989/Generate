package com.ltthuc.feature.presentation.page.settings

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ltthuc.ads.AdsManager
import com.ltthuc.feature.R
import com.ltthuc.feature.databinding.FragmentSettingsBinding
import com.ltthuc.ui.base.BaseFragment
import com.ltthuc.ui.base.settings.CommonSettingFragment
import com.ltthuc.ui.base.toolbar.IToolbar
import com.ltthuc.ui.base.toolbar.ToolbarItem
import com.ltthuc.ui.base.toolbar.ToolbarManager
import com.ltthuc.ui.base.toolbar.ToolbarViewModel
import com.ltthuc.ui.extension.addFragment
import com.ltthuc.ui.extension.alertDialog
import com.ltthuc.ui.extension.removeFragment
import com.ltthuc.ui.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
internal class SettingsFragment :
    BaseFragment<FragmentSettingsBinding, SettingViewModel>(FragmentSettingsBinding::inflate) {
    private val viewModel: SettingViewModel by viewModels()
    override fun getVM(): SettingViewModel = viewModel
    @Inject
    lateinit var adsManager: AdsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isDarkMode.value =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES;
    }

    override fun bindVM(binding: FragmentSettingsBinding, vm: SettingViewModel) {
        binding.viewModel = viewModel
        showBottomBar(true)
        showActivityBannerAds(false)
        childFragmentManager.addFragment(R.id.linSetting, CommonSettingFragment.newInstance(true))
    }

    override fun setUpToolbar() {
        super.setUpToolbar()
        val config = ToolbarManager.ToolbarConfig(
            title = "Setting3",
            titleSize = 20f,
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.isDarkMode.value =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                resources.configuration.isNightModeActive
            } else {
                newConfig.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK ==
                        Configuration.UI_MODE_NIGHT_YES
            }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun rightClickToolbar() {
        super.rightClickToolbar()
        showToast("setting")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeFragments()
    }

    private fun removeFragments() {
        childFragmentManager.removeFragment(CommonSettingFragment::class)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().alertDialog {  }
        //adsManager.removeAd()
    }

}