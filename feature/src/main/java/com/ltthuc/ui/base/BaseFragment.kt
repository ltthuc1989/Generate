package com.ltthuc.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ltthuc.ads.AdsManager
import com.ltthuc.ads.AdsSettings
import com.ltthuc.ads.BannerAd
import com.ltthuc.ads.BannerType
import com.ltthuc.ui.R
import com.ltthuc.ui.adapter.section.CustomItemDecoration
import com.ltthuc.ui.base.toolbar.IToolbar
import com.ltthuc.ui.base.toolbar.ToolbarManager
import com.ltthuc.ui.base.toolbar.ToolbarViewModel
import com.ltthuc.ui.extension.alertDialog
import com.ltthuc.ui.extension.observe
import com.ltthuc.ui.extension.transparentStatusBar
import com.ltthuc.ui.utils.CoreUIConstants.KEY_SHOW_BOTTOM_BANNER_ADS
import com.ltthuc.ui.utils.CoreUIConstants.KEY_SHOW_BOTTOM_BAR
import com.ltthuc.utils.AppException
import com.ltthuc.utils.ISecretAdsKey
import com.ltthuc.utils.getErrorMessage
import kotlinx.coroutines.launch
import javax.inject.Inject


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<DB : ViewDataBinding, VM : BaseViewModel>(private val inflate: Inflate<DB>) : Fragment() {
    private lateinit var viewModel: VM
    private val TAG = this::class.java.simpleName
    protected open var transparentStatusBar: Boolean = true
    private var isStopped = false
    private var reCreateView = false
    private var _reCallOnViewCreated = false
    protected val toolbar: ToolbarViewModel by activityViewModels()

    protected var bannerAd: BannerAd? = null

    protected lateinit var toolbarManager: ToolbarManager
    private var _binding: DB? = null

    val binding get() = _binding!!
    abstract fun getVM(): VM

    abstract fun bindVM(binding: DB, vm: VM)
    open fun bindView(){ requireActivity().transparentStatusBar(true) }


    open var fragmentResultCompletion: (Intent?) -> Unit = {}

    val registerForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result?.resultCode == Activity.RESULT_OK) {
            fragmentResultCompletion(result.data)
        } else {
            fragmentResultCompletion(null)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        bindView()
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getVM()
    }

    override fun onResume() {
        super.onResume()
        if (isStopped) {
            isStopped = false
            if (!reCreateView) {
                resumeFromBackGround()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        bindVM(binding, viewModel)
        binding.lifecycleOwner = viewLifecycleOwner
        with(viewModel) {
            observe(progressLiveEvent) { show ->
                if (show) (activity as BaseActivity<*, *>).showProgress()
                else {
                    (activity as BaseActivity<*, *>).hideProgress()
                }
            }

            observe(errorMessage) { msg ->
                when(msg){
                    is AppException -> {
                        activity?.alertDialog(getString(com.ltthuc.utils.R.string.alert_error_title),msg.getErrorMessage(requireContext()))
                    }
                    is String -> {
                        activity?.alertDialog(getString(com.ltthuc.utils.R.string.alert_error_title),msg)?.show()

                    }
                }
            }
        }
        setOnClickListener()
        reCreateView = true
        if (!_reCallOnViewCreated) {
            onViewCreatedCallOnce()
            _reCallOnViewCreated = true
        } else {
            reCallOnViewCreated()
        }

    }

    open fun onViewCreatedCallOnce() {}

    open fun reCallOnViewCreated() {}

    fun <C> startActivityForResult(clazz: Class<C>) {
        activity?.let {
            registerForResult.launch(Intent(it, clazz))
        }
    }

    fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            execute()
        }
    }

    protected open fun setUpAds(iSecretAdsKey: ISecretAdsKey, bannerType: BannerType, adManager: AdsManager?= null) {
        if (AdsSettings.disableAd) return
        if (bannerAd == null) {
            bannerAd = BannerAd(iSecretAdsKey)
        }
        if (bannerAd?.isExpanded == true) {
            viewLifecycleOwner.lifecycleScope.launch {
              binding.root.findViewById<FrameLayout>(R.id.bannerAdView)?.let {
                    adManager?.showAds(it)
                }

            }
        } else {
            binding.root.findViewById<FrameLayout>(R.id.bannerAdView)?.let {
                bannerAd?.loadAndShowBannerAd(true, it, bannerType)
                viewLifecycleOwner.lifecycleScope.launch {
                    adManager?.loadAd(it)
                }
            }

        }
    }


    fun showBottomBar(isShow: Boolean) {
        requireActivity().supportFragmentManager.setFragmentResult(KEY_SHOW_BOTTOM_BAR, bundleOf(
            KEY_SHOW_BOTTOM_BAR to isShow)
        )

    }

    fun showActivityBannerAds(isShow: Boolean) {
        requireActivity().supportFragmentManager.setFragmentResult(
            KEY_SHOW_BOTTOM_BANNER_ADS, bundleOf(
                KEY_SHOW_BOTTOM_BANNER_ADS to isShow
            )
        )

    }

  open  fun setUpAdapter(adapter: RecyclerView.Adapter<*>?, recyclerView: RecyclerView, itemDecoration: CustomItemDecoration? = null) {
        with(adapter) {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = this
            recyclerView.itemAnimator = null
            recyclerView.layoutManager = layoutManager
            itemDecoration?.let {
                recyclerView.addItemDecoration(it)
            }

        }
    }


    fun setUpGridAdapter(adapter: RecyclerView.Adapter<*>?, recyclerView: RecyclerView, showDivider: Boolean = true, column: Int, isVertical: Boolean = true) {
        with(adapter) {
            val layoutManager = GridLayoutManager(context, column, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = this
            recyclerView.itemAnimator = null
            recyclerView.layoutManager = layoutManager
            if (showDivider) {
                recyclerView.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        layoutManager.orientation
                    )
                )
            }

        }
    }

    protected open fun setUpToolbar() {
        if (this::class.simpleName != "SplashFragment") {
            toolbarManager = (requireActivity() as IToolbar).getToolbar()
        }
    }


    fun logD(methodName: String, message: String?) {
        Log.d(TAG, "$methodName: $message")
    }

    fun logI(methodName: String, message: String?) {
        Log.i(TAG, "$methodName: $message")
    }

    fun logE(methodName: String, message: String?) {
        Log.e(TAG, "$methodName: $message")
    }

    fun isTransparentStatusBar(): Boolean = transparentStatusBar

    open fun setOnClickListener() {}

    open fun resumeFromBackGround(){
        Log.d(TAG, "ResumeFromBackground")
    }

    open fun rightClickToolbar() {}
    open fun leftClickToolbar(): Boolean { return false}
    open fun endRightClickToolbar() {}
    open fun rightTitleToolbarClick() {}
    fun getRightTitleToolbar(): TextView {
        return (binding.root.parent.parent.parent as View).findViewById(R.id.right_toolbar_title)
    }
    fun getTitleToolbar(): TextView {
        return (binding.root.parent.parent.parent as View).findViewById(R.id.titleToolbar)
    }
    fun getToolbar(): View {
        return (binding.root.parent.parent.parent as View).findViewById(R.id.toolbar)
    }
    override fun onStop() {
        super.onStop()
        isStopped = true
        reCreateView = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerAd?.clear()
        _binding = null
    }


}