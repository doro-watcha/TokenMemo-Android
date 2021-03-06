package com.goddoro.memopan.presentation.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goddoro.memopan.room.Memo
import com.goddoro.memopan.room.MemoDao
import com.goddoro.memopan.utils.Broadcast
import com.goddoro.memopan.utils.Once
import com.goddoro.memopan.utils.addSchedulers
import com.goddoro.memopan.utils.disposedBy
import io.reactivex.disposables.CompositeDisposable


/**
 * created By DORO 5/18/21
 */


class DetailViewModel(private val memoDao: MemoDao) : ViewModel() {

    private val TAG = DetailViewModel::class.java.simpleName

    private val compositeDisposable = CompositeDisposable()


    val title: MutableLiveData<String> = MutableLiveData()
    val body: MutableLiveData<String> = MutableLiveData()

    val onSaveComplete: MutableLiveData<Once<String>> = MutableLiveData()

    val clickFinish : MutableLiveData<Once<Unit>> = MutableLiveData()

    val needToFillBody: MutableLiveData<Once<Unit>> = MutableLiveData()
    val errorInvoked: MutableLiveData<Once<Throwable>> = MutableLiveData()


    init {


    }


    fun onClickSave() {

        if (body.value?.length == 0 || body.value == null) {
            needToFillBody.value = Once(Unit)
            Log.d(TAG, "가즈아")
            return
        }


        val memo = Memo(
            id = System.currentTimeMillis().toInt(),
            title = title.value ?: "",
            body = body.value ?: ""
        )

        memoDao.insertMemo(memo)
            .addSchedulers()
            .subscribe({
                Broadcast.saveCompleteBroadcast.onNext(memo)
                onSaveComplete.value = Once(title.value ?: "")
            }, {
                errorInvoked.value = Once(it)
            }).disposedBy(compositeDisposable)

    }

    fun onClickFinish(){
        clickFinish.value = Once(Unit)
    }

    fun onClickRemoveAll() {
        body.value = ""
    }

}