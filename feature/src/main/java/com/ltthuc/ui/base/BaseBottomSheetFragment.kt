package com.ltthuc.ui.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ltthuc.ui.extension.alertDialog
import com.ltthuc.ui.extension.observe
import com.ltthuc.utils.AppException
import com.ltthuc.utils.getErrorMessage

abstract class BaseBottomSheetFragment <DB : ViewDataBinding, VM : BaseViewModel>(private val inflate: Inflate<DB>) : BottomSheetDialogFragment() {
    private lateinit var viewModel: VM
    private var _binding: DB? = null
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    val binding get() = _binding!!

    abstract fun getVM(): VM

    abstract fun bindVM(binding: DB, vm: VM)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getVM()
    }

    open fun onCreatedDialogCustom(dialog: Dialog) {
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.WHITE)
        )
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindVM(binding, viewModel)
        with(viewModel) {
            observe(progressLiveEvent) { show ->
                if (show) (activity as BaseActivity<*, *>).showProgress()
                else (activity as BaseActivity<*, *>).hideProgress()
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


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            execute()
        }
    }

    private val mBottomSheetBehaviorCallback: BottomSheetBehavior.BottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (BottomSheetBehavior.STATE_EXPANDED == newState) {
//                showView(mBinding!!.appBarLayout, getActionBarSize());
//                hideAppBar(mBinding!!.ivServiceTitle);
//                hideAppBar(mBinding!!.viewLine);

            }
            if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
//                hideAppBar(mBinding!!.appBarLayout);
//                showView(mBinding!!.ivServiceTitle, resources.getDimension(R.dimen._20sdp).toInt())
//                showView(mBinding!!.viewLine, resources.getDimension(R.dimen._4sdp).toInt());
            }

            if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                dismiss();
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    fun setUpAdapter(adapter: RecyclerView.Adapter<*>?, recyclerView: RecyclerView, showDivider: Boolean = true) {
        with(adapter) {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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


}