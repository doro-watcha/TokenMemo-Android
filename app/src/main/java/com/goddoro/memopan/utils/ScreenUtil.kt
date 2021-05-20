package com.goddoro.memopan.utils

import android.app.Application


/**
 * created By DORO 5/19/21
 */


class ScreenUtil(private val context: Application) {
    val screenWidth: Int
        get() {
            return context.resources.displayMetrics.widthPixels
        }

    val screenHeight: Int
        get() = context.resources.displayMetrics.heightPixels

    fun toPixel(dp: Int): Float = context.resources.displayMetrics.density * dp
}