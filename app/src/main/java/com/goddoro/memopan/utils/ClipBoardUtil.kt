package com.goddoro.memopan.utils

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService


/**
 * created By DORO 5/19/21
 */

class ClipBoardUtil (private val context : Application){


    val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager


    fun copyToClipboard ( title : String, body : String){
        val clip = ClipData.newPlainText(title,body)
        clipboard.setPrimaryClip(clip)
    }

    fun isClipboardAvailable() = clipboard.hasPrimaryClip()

    fun getClipBoard() = clipboard.primaryClip?.getItemAt(0)?.coerceToText(context).toString()

}