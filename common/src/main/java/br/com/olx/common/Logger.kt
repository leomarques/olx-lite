package br.com.olx.common

import android.util.Log

fun ologx(msg: String) {
    if (BuildConfig.DEBUG) Log.d("ologx", msg)
}