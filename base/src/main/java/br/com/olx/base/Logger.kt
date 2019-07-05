package br.com.olx.base

import android.util.Log

fun ologx(msg: String) {
    if (BuildConfig.DEBUG) Log.d("ologx", msg)
}