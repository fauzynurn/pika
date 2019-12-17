package com.example.tagihin.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.tagihin.utils.PreferencesHelper
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.reflect.KClass


/**
 * Base fragment for All Fragment with ViewModel
 * @param V : ViewModel
 * @param D : ViewDataBinding
 * @property viewModelFactory Factory?
 * @property viewModel V
 * @property dataBinding D
 * @property layoutRes Int
 */

abstract class BaseFragment<out V : ViewModel, D : ViewDataBinding>(clazz: KClass<V>) :
    Fragment(), IDefaultFragment, LifecycleOwner {

    private var parentActivity: AppCompatActivity? = null
    private var mView: View? = null

    protected lateinit var dataBinding: D
    val viewModel : V by sharedViewModel(clazz)
    val pref: PreferencesHelper by inject()

    var TAG = clazz::class.java.simpleName

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentActivity = activity as AppCompatActivity
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        initView(dataBinding.root)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (isAdded) {
            onViewReady()
        }

        setupToolbar(parentActivity!!)
        super.onViewCreated(view, savedInstanceState)
    }
}

interface IDefaultFragment {
    fun initView(view: View)
    fun onViewReady()
    fun setupToolbar(activity: AppCompatActivity)
}