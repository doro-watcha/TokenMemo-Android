package com.goddoro.memopan.di

import com.goddoro.memopan.utils.AppPreference
import com.goddoro.memopan.utils.ClipBoardUtil
import com.goddoro.memopan.utils.ScreenUtil
import com.goddoro.memopan.utils.ToastUtil
import io.reactivex.schedulers.Schedulers.single
import org.koin.dsl.module


/**
 * created By DORO 5/19/21
 */

val utilModule = module {


    single{ ClipBoardUtil(get()) }
    single { AppPreference(get()) }
    single { ScreenUtil(get())}
    single { ToastUtil(get()) }
}