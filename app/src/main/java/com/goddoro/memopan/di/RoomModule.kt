package com.goddoro.memopan.di

import androidx.room.Room
import com.goddoro.memopan.room.MemoDatabase
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module


/**
 * created By DORO 5/18/21
 */

val roomModule = module {

    single() {
        Room.databaseBuilder(get(), MemoDatabase::class.java, "m02")
            .allowMainThreadQueries()
            .build()
    } bind MemoDatabase::class

    single {
        get<MemoDatabase>().memoDao()
    }


}