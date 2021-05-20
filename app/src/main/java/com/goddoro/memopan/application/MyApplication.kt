package com.goddoro.memopan.application

import android.app.Application
import com.goddoro.memopan.di.roomModule
import com.goddoro.memopan.di.utilModule
import com.goddoro.memopan.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


/**
 * created By DORO 5/18/21
 */

class MyApplication : Application(){


    override fun onCreate() {
        super.onCreate()

        inject()
    }

    private fun inject() {

        startKoin {
            androidContext(this@MyApplication)
            androidLogger(Level.INFO)
            modules(
                listOf(
                    viewModelModule,
                    roomModule,
                    utilModule
                )
            )
        }
    }
}