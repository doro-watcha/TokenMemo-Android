package com.goddoro.memopan.room

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single


/**
 * created By DORO 5/18/21
 */

@Dao
interface MemoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemo(item: Memo): Single<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMemo(item: Memo): Completable

    @Delete
    fun deleteMemo(vararg item: Memo): Completable

    @Query("SELECT * FROM Memo ORDER BY updateTimeMs DESC")
    fun getMemoList(): Single<List<Memo>>

}