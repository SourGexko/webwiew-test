package com.example.webviewtest

import android.content.Context
import android.content.Context.MODE_PRIVATE

class Prefs(context: Context) {
    private val prefName="mPref"
    private val prefs = context.getSharedPreferences(prefName, MODE_PRIVATE)

    var token: String?
        get
}