package com.goddoro.memopan

import android.util.Log
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

    private val TAG = MainViewModel::class.java.simpleName


    val memoList : MutableLiveData<List<Memo>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    val onRemoveCompleted : MutableLiveData<Once<Int>> = MutableLiveData()
    val onLoadCompleted : MutableLiveData<Boolean> = MutableLiveData(false)
    val clickAddMemo : MutableLiveData<Once<Unit>> = MutableLiveData()

    val errorInvoked : MutableLiveData<Once<Throwable>> = MutableLiveData()

    private fun getMemoList() {

        memoDao.getMemoList()
            .addSchedulers()
            .subscribe({
                memoList.value = it
                Log.d(TAG,it?.size.toString())
                onLoadCompleted.value = true
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

    fun removeMemo( position : Int ) {

        val memo = (memoList.value ?: listOf())[position]

        memoDao.deleteMemo((memoList.value ?: listOf())[position])
            .addSchedulers()
            .subscribe({
               refresh()
            },{
                errorInvoked.value = Once(it)
            }).disposedBy(compositeDisposable)

    }
}


