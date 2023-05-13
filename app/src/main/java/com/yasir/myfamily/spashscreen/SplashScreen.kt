package com.yasir.myfamily.spashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yasir.myfamily.constants.Constants
import com.yasir.myfamily.sharedPrefrences.SharedPref
import com.yasir.myfamily.ui.MainActivity
import com.yasir.myfamily.ui.logIn.LoginActivity


@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val IsUserLogedIn = SharedPref.getBoolean(Constants.IS_USER_LOGED_IN)

        if(IsUserLogedIn){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
}