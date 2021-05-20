package com.goddoro.memopan.room

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.goddoro.memopan.utils.Broadcast
import com.google.gson.annotations.Expose
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent
import org.koin.core.get


/**
 * created By DORO 5/18/21
 */

@Entity(tableName = "Memo")
data class Memo (

    @PrimaryKey(autoGenerate = true)
    @Expose
    var id: Int? = 0,

    @Expose
    var updateTimeMs: Long? = System.currentTimeMillis(),

    @Expose
    var title : String? = "",

    @Expose
    var body : String? = ""


) : KoinComponent {

    companion object {

        val TAG = Memo::class.java.simpleName
    }

}