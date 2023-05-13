package com.yasir.myfamily.sharedPrefrences

import android.app.Application

class MyfamilyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPref.init(this)
    }
}