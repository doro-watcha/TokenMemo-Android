package com.goddoro.memopan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goddoro.memopan.room.Memo
import com.goddoro.memopan.room.MemoDao
import com.goddoro.memopan.utils.Once
import com.goddoro.memopan.utils.addSchedulers
import com.goddoro.memopan.utils.disposedBy
import io.reactivex.disposables.CompositeDisposable


/**
 * created By DORO 5/18/21
 */

class MainViewModel ( private val memoDao: MemoDao) : ViewModel() {


    val memoList : MutableLiveData<List<Memo>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    val onLoadCompleted : MutableLiveData<Once<Unit>> = MutableLiveData()
    val clickAddMemo : MutableLiveData<Once<Unit>> = MutableLiveData()

    val errorInvoked : MutableLiveData<Once<Throwable>> = MutableLiveData()


    init {


        getMemoList()

    }

    private fun getMemoList() {

        memoDao.getMemoList()
            .addSchedulers()
            .subscribe({
                memoList.value = it
            },{
                errorInvoked.value = Once(it)
            }).disposedBy(compositeDisposable)
    }

    fun refresh() {

        getMemoList()
    }

    fun onClickAddMemo() {

        clickAddMemo.value = Once(Unit)
    }
}


