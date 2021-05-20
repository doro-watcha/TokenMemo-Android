package com.goddoro.memopan.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


/**
 * created By DORO 5/19/21
 */

class AppPreference(context: Application) {

    private val TAG = AppPreference::class.java.simpleName
    private val preference: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    enum class KEY(val key: String) {

        KEY_APP_CLIP_BOARD("KEY_APP_CLIP_BOARD"),
        KEY_NEED_TO_CHECK_CLIP_BOARD("KEY_NEED_TO_CHECK_CLIP_BOARD")

    }


    @SuppressLint("ApplySharedPref")
    private operator fun set(key: KEY, value: Any?) {
        value?.let { value ->
            when (value) {
                is String -> preference.edit().putString(key.key, value).commit()
                is Int -> preference.edit().putInt(key.key, value).commit()
                is Boolean -> preference.edit().putBoolean(key.key, value).commit()
                is Float -> preference.edit().putFloat(key.key, value).commit()
                is Long -> preference.edit().putLong(key.key, value).commit()
                else -> throw UnsupportedOperationException("Not yet implemented")
            }
        } ?: kotlin.run {
            preference.edit().remove(key.key).commit()
        }
    }

    private inline operator fun <reified T : Any> get(key: KEY, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> preference.getString(key.key, defaultValue as? String) as T?
            Int::class -> preference.getInt(key.key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> preference.getBoolean(key.key, defaultValue as? Boolean ?: false) as T?
            Float::class -> preference.getFloat(key.key, defaultValue as? Float ?: -1f) as T?
            Long::class -> preference.getLong(key.key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }


    var curAppClipBoard: String
        get() = get(KEY.KEY_APP_CLIP_BOARD) ?: ""
        set(value) = set(KEY.KEY_APP_CLIP_BOARD, value)

    var needToCheckClipBoard : Boolean
        get() = get(KEY.KEY_NEED_TO_CHECK_CLIP_BOARD) ?: true
        set(value) = set(KEY.KEY_NEED_TO_CHECK_CLIP_BOARD, value)



}
