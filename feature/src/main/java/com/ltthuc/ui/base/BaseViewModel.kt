package com.ltthuc.ui.base



import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ltthuc.utils.AppException

import com.ltthuc.ui.utils.livedata.SingleLiveEvent
import dagger.hilt.android.internal.managers.ViewComponentManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


abstract class BaseViewModel: ViewModel() {
    protected var tag = this::class.java.simpleName


    var progressLiveEvent = SingleLiveEvent<Boolean>()
    var errorMessage = SingleLiveEvent<Any>()
    protected val _state = MutableLiveData<UIState>()

    val isDarkMode = MutableLiveData(false)

    val state: LiveData<UIState>
        get() = _state


    val handlerException = CoroutineExceptionHandler { _, exception ->
        manageException(exception)
    }

    open fun manageException(exception: Any){
        viewModelScope.launch {
            if (exception is AppException) {

                errorMessage.value = exception

            } else if (exception is Throwable){
                errorMessage.value = exception.localizedMessage
                logD("manageException", "${errorMessage.value}")
            }
            progressLiveEvent.postValue(false)
        }

    }

    inline fun  launchAsync(
        crossinline execute: suspend () -> Unit,
        crossinline onError:(Exception) -> Unit = {},
        showProgress: Boolean = true
    ) {
        viewModelScope.launch(handlerException) {
            if (showProgress)
                progressLiveEvent.value = true
            try {
                execute()

            } catch (ex: Exception) {
                onError(ex)
            } finally {
                progressLiveEvent.postValue(false)
            }
        }
    }

    inline fun <T> launchPagingAsync(
        crossinline execute: suspend () -> Flow<T>,
        crossinline onSuccess: suspend (Flow<T>) -> Unit,
        showProgress: Boolean = true
    ) {
        viewModelScope.launch {
            try {
                if (showProgress) {
                    progressLiveEvent.value = true
                }
                val result = execute()
                onSuccess(result)
                progressLiveEvent.postValue(false)
            } catch (ex: Exception) {
                errorMessage.value = ex.message
            } finally {
                progressLiveEvent.postValue(false)
            }
        }
    }

    fun activityContext(context: Context): Context? {

        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        } else context
    }


    fun logD(methodName: String, message: String?) {
        Log.d(tag, "$methodName: $message")
    }

    fun logI(methodName: String, message: String?) {
        Log.i(tag, "$methodName: $message")
    }

    fun logE(methodName: String, message: String?) {
        Log.e(tag, "$methodName: $message")
    }

    fun showToast(context: Context, message: String?, type:Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, type).show()
    }
}