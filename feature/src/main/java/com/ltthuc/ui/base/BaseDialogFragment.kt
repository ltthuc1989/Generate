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
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.ltthuc.ui.extension.alertDialog
import com.ltthuc.ui.extension.observe
import com.ltthuc.ui.utils.ScreenUtils.getScreenWidth
import com.ltthuc.utils.AppException
import com.ltthuc.utils.R
import com.ltthuc.utils.getErrorMessage
import com.ltthuc.ui.R as ui

abstract class BaseDialogFragment<DB : ViewDataBinding, VM : BaseViewModel>(private val inflate: Inflate<DB>) : DialogFragment() {
    private lateinit var viewModel: VM
    protected open var isFullScreen = true
    private var _binding: DB? = null
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

    override fun onStart() {
        super.onStart()
        onChildStart()
    }

    open fun onChildStart() {
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!
                .setLayout((getScreenWidth(requireContext()) * .9).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (isFullScreen) {
            setStyle(STYLE_NO_TITLE, ui.style.FullScreenDialogStyle)
        }
        isCancelable = true
        val dialog = super.onCreateDialog(savedInstanceState)
        onCreatedDialogCustom(dialog)
        return dialog
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
                        activity?.alertDialog(getString(R.string.alert_error_title),msg.getErrorMessage(requireContext()))
                    }
                    is String -> {
                        activity?.alertDialog(getString(R.string.alert_error_title),msg)?.show()

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

}