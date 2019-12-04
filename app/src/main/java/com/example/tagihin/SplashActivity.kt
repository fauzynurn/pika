package com.example.tagihin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.tagihin.base.BaseActivityNoViewModel
import com.example.tagihin.databinding.ActivitySplashBinding
import com.example.tagihin.view.home.HomeActivity
import com.example.tagihin.view.login.LoginActivity
import timber.log.Timber

class SplashActivity : BaseActivityNoViewModel<ActivitySplashBinding>() {
    override fun getLayoutRes(): Int = R.layout.activity_splash

    override fun showMessage(message: String) {
        Timber.i(message)
    }

    override fun initView(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        Handler().postDelayed({
            if (pref.getLoginStatus()) {
                startActivity(
                    Intent(this, HomeActivity::class.java)
                )
            } else {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
            }
            finish()
        }, 1000)
    }

    override fun setupToolbar() {
    }

}