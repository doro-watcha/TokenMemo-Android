package com.goddoro.memopan.di

import com.goddoro.memopan.MainViewModel
import com.goddoro.memopan.presentation.detail.DetailViewModel
import com.goddoro.memopan.presentation.dialog.ClipBoardDialogViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * created By DORO 5/18/21
 */

val viewModelModule = module {

    /**
     * Activity
     */

    viewModel{ DetailViewModel(get()) }
    viewModel { MainViewModel(get()) }

    /**
     * Dialog
     */
    viewModel { ClipBoardDialogViewModel()}
}