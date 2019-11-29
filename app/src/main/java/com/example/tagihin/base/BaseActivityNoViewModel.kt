package com.example.tagihin.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.example.tagihin.utils.PreferencesHelper
import com.example.tagihin.widget.CustomLoading
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

abstract class BaseActivityNoViewModel<D : ViewDataBinding> :
    AppCompatActivity(), LifecycleOwner, IDefaultActivity {
    val pref: PreferencesHelper by inject()

    lateinit var customLoad: CustomLoading

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    lateinit var dataBinding: D


    private var snackbar: Snackbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes())

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