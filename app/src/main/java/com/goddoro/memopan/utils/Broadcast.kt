package com.goddoro.memopan.utils

import com.goddoro.memopan.room.Memo
import io.reactivex.subjects.PublishSubject


/**
 * created By DORO 5/19/21
 */

object Broadcast {

    val saveCompleteBroadcast : PublishSubject<Memo> = PublishSubject.create()

    val moveToDetailBroadcast : PublishSubject<String> = PublishSubject.create()


}