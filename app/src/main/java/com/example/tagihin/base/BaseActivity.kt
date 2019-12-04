package com.example.tagihin.base

import android.R
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.tagihin.utils.PreferencesHelper
import com.example.tagihin.widget.CustomLoading
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseActivity<out V : ViewModel, D : ViewDataBinding>(clazz: KClass<V>) :
    AppCompatActivity(), LifecycleOwner, IDefaultActivity{
    var TAG = clazz::class.java.simpleName

    val viewModel: V by viewModel(clazz)
    val pref: PreferencesHelper by inject()

    lateinit var customLoad: CustomLoading

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    lateinit var dataBinding: D


    private var snackbar: Snackbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes())
        dataBinding.lifecycleOwner = this
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            window.statusBarColor = Color.parseColor("#41BEFF")
        }else{
            window.statusBarColor = ContextCompat
                .getColor(this, R.color.white)
        }
        customLoad = CustomLoading(this)

        initView(savedInstanceState)
        setupToolbar()
    }


    abstract fun showMessage(message: String)

    fun showDialog() {
        customLoad.showDialog()
    }

    fun hideDialog() {
        customLoad.hideDialog()
    }
}

interface IDefaultActivity {
    fun initView(savedInstanceState: Bundle?)

    fun setupToolbar()
}