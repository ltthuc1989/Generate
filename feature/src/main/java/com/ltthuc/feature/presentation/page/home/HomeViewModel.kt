package com.ltthuc.feature.presentation.page.home

import android.content.Context
import android.text.InputType
import com.ltthuc.navigation.api.Navigator
import com.ltthuc.navigation.api.model.Destination
import com.ltthuc.preferences.api.DatastoreRepository
import com.ltthuc.preferences.api.PreferencesRepository
import com.ltthuc.ui.base.BaseViewModel
import com.ltthuc.ui.extension.alertDialog
import com.ltthuc.ui.extension.getActivity
import com.ltthuc.ui.extension.logD
import com.ltthuc.ui.extension.message
import com.ltthuc.ui.extension.title
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: Navigator,
    private val datastoreRepository: PreferencesRepository
) : BaseViewModel() {

    var inflationValue by datastoreRepository.getPreferences("inflationValue", 2.9f)

    init {

    }
    fun setData(context: Context) {
        context.getActivity().alertDialog("bcd"){
//            OnPositiveClicked {
//               logD("abc")
//            }
//            OnNegativeClicked {
//                logD("abcd")
//            }
            setEditText(true, true, hintText = "abc", com.ltthuc.ui.components.dialog.InputType.TEXT_SINGLE_LINE)
            setPositiveBtnText("Yes")
        }.show()
    }

    fun gotoSetting() {
        navigator.goTo(Destination.Setting)
    }

}