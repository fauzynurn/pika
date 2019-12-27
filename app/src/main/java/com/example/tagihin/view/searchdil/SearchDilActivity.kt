package com.example.tagihin.view.searchdil

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.databinding.ActivitySearchDilBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.utils.Consts.Companion.REQUEST_IMAGE
import com.example.tagihin.utils.PreferencesHelper
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit


class SearchDilActivity :
    BaseActivity<SearchDilViewModel, ActivitySearchDilBinding>(SearchDilViewModel::class) {
    private var textChangeListener: Disposable? = null
    override fun getLayoutRes(): Int = R.layout.activity_search_dil

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun initView(savedInstanceState: Bundle?) {
        dataBinding.backIcon.setOnClickListener {
            finish()
            textChangeListener?.dispose()
        }
        dataBinding.lifecycleOwner = this
        dataBinding.pref = pref
        dataBinding.viewModel = viewModel
        textChangeListener = RxTextView.textChanges(dataBinding.searchBox)
            .debounce(550, TimeUnit.MILLISECONDS)
            .map {
                it.toString()
            }
            .subscribe {
                viewModel.query.postValue(it)
                if (it.isNotEmpty()) {
                    viewModel.loadingState.postValue(true)
                    viewModel.searchDil(it)
                }
            }
        viewModel.dilItemRequest.observe(this, Observer {
            Timber.i("DIL item request observed!!")
        })
        viewModel.error.observe(this, Observer {
            showMessage(it)
        })

        dataBinding.dilDetailBtn.setOnClickListener {
            val btmSheet = ConfirmBottomSheet(viewModel.dilItem.value!!)
            btmSheet.show(supportFragmentManager, btmSheet.tag)
        }

        dataBinding.validateBtn.setOnClickListener {
            val btmSheet = ValidateBottomSheet()
            btmSheet.show(supportFragmentManager, btmSheet.tag)
        }
    }

    override fun setupToolbar() {}

}