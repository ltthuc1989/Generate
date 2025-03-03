package com.ltthuc.feature.presentation.page.notification

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ltthuc.ads.AdsManager
import com.ltthuc.ads.AdsSettings
import com.ltthuc.ads.BannerAd
import com.ltthuc.ads.BannerType
import com.ltthuc.feature.R
import com.ltthuc.feature.databinding.FragmentNotiBinding
import com.ltthuc.feature.presentation.adapter.NotificationsAdapter
import com.ltthuc.ui.adapter.section.CustomItemDecoration

import com.ltthuc.ui.base.BaseFragment
import com.ltthuc.ui.base.toolbar.IToolbar
import com.ltthuc.ui.base.toolbar.ToolbarManager
import com.ltthuc.ui.extension.transparentStatusBar
import com.ltthuc.utils.ISecretAdsKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotiBinding, NotificationViewModel>(FragmentNotiBinding::inflate) {
    private val viewModel: NotificationViewModel by viewModels()
    override fun getVM(): NotificationViewModel = viewModel
    private var adapter: NotificationsAdapter? = null

    @Inject
    lateinit var iSecretAdsKey: ISecretAdsKey
    @Inject
    lateinit var adsManager: AdsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBottomBar(false)
    }

    override fun bindVM(binding: FragmentNotiBinding, vm: NotificationViewModel) {
        adapter = NotificationsAdapter( onViewClicked = { position, item -> }
        )

        setUpAdapter(adapter, binding.recyclerView, CustomItemDecoration(requireContext(), false))

        viewModel.notifications.observe(viewLifecycleOwner) {
            adapter?.swapItems(it)
        }
    }

    override fun setUpToolbar() {
        super.setUpToolbar()
        showBottomBar(true)
        val config = ToolbarManager.ToolbarConfig(
            title = "Notifications",
            titleSize = 20f,
            navigationIcon = com.ltthuc.ui.R.drawable.baseline_arrow_back_ios_24,
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

        setUpAds(iSecretAdsKey, BannerType.COLLAPSIBLE_BOTTOM, adsManager)
    }

    override fun rightClickToolbar() {
        super.rightClickToolbar()
    }


}