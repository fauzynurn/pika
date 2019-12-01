package com.example.tagihin.view.login

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.databinding.ActivityLoginBinding
import com.example.tagihin.utils.rule.NotEmptyRule
import com.example.tagihin.view.home.HomeActivity
import org.koin.android.ext.android.inject
import ru.whalemare.rxvalidator.RxCombineValidator
import ru.whalemare.rxvalidator.RxValidator
import timber.log.Timber

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>(LoginViewModel::class){
    override fun getLayoutRes(): Int = R.layout.activity_login
    val notEmptyRule : NotEmptyRule by inject()
    override fun showMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun initView(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel

        viewModel.login.observe(this, Observer {
            hideDialog()
            if(it.status){
                pref.setLoginStatus(true)
                pref.setName(it.data?.nama!!)
                pref.setUsername(viewModel.username)
                if(it.data?.foto != null) pref.setProfilePicUrl(it.data?.foto!!)
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }else {
                showMessage(it.message)
            }
        })

        val usernameDisposable = RxValidator(dataBinding.inputNamaLayout).apply {
            add(notEmptyRule)
        }.asObservable()

        val passwordDisposable = RxValidator(dataBinding.inputPassLayout).apply{
            add(notEmptyRule)
        }.asObservable()

        val combine = RxCombineValidator(
            usernameDisposable,passwordDisposable
        ).asObservable()
            .distinctUntilChanged()
            .subscribe {
                dataBinding.loginBtn.isEnabled = it
            }

        viewModel.message.observe(this, Observer {
            showMessage(it)
        })

        val spannable = SpannableString("Silahkan masuk untuk dapat menggunakan aplikasi Pika")
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this,R.color.colorPrimary)),
            48, spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        dataBinding.loginSubtitle.text = spannable
        dataBinding.loginBtn.setOnClickListener {
            showDialog()
            viewModel.login()
        }
    }

    override fun setupToolbar() {

    }

}