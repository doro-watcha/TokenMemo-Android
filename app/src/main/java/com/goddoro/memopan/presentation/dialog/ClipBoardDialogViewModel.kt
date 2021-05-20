package com.goddoro.memopan.presentation.dialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goddoro.memopan.utils.Once


/**
 * created By DORO 5/19/21
 */

class ClipBoardDialogViewModel : ViewModel() {


    val body : MutableLiveData<String> = MutableLiveData()

    val clickSaveButton : MutableLiveData<Once<Unit>> = MutableLiveData()
    val clickDismiss : MutableLiveData<Once<Unit>> = MutableLiveData()


    fun onClickSave() {
        clickSaveButton.value = Once(Unit)
    }

    fun onClickDismiss() {
        clickDismiss.value = Once(Unit)
    }
}