package com.goddoro.memopan.room

import androidx.room.Database
import androidx.room.RoomDatabase


/**
 * created By DORO 5/18/21
 */

@Database(entities = [Memo::class], version = 1)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
}