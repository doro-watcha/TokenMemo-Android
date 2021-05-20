package com.goddoro.memopan.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose


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


)