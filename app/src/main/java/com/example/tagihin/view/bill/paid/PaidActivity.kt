package com.example.tagihin.view.bill.paid

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.data.remote.model.PaidBill
import com.example.tagihin.databinding.ActivityBillBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.bill.paid.fragments.PaidFragment
import com.example.tagihin.view.bill.paid.fragments.PaidSearchFragment
import com.example.tagihin.view.detail.DetailBillActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class PaidActivity : BaseActivity<PaidViewModel, ActivityBillBinding>(PaidViewModel::class) {
    val LIST_FRAGMENT = "list_fragment"
    val SEARCH_LIST_FRAGMENT = "search_list_fragment"
    private var textChangeListener: Disposable? = null
    override fun getLayoutRes(): Int = R.layout.activity_bill

//        sharedviewModel.reloadLiveData.observe(activity as HomeActivity, Observer {
//            PAGE = 0
//            dataBinding.paidRecycler.showShimmerAdapter()
//            sharedviewModel.getPaidBill(PAGE, 6)
//        })

    override fun showMessage(message: String) {
        Snackbar.make(dataBinding.root,message,Snackbar.LENGTH_SHORT).show()
    }

    override fun initView(savedInstanceState: Bundle?) {
        dataBinding.multiSelectNav.visibility = View.GONE
        dataBinding.refreshBtn.setOnClickListener {
            viewModel.refresh()
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.bill_viewpager, PaidFragment(), LIST_FRAGMENT)
            .add(R.id.bill_viewpager, PaidSearchFragment(), SEARCH_LIST_FRAGMENT)
            .commitAllowingStateLoss()
        textChangeListener = RxTextView.textChanges(dataBinding.searchBox)
            .debounce(550, TimeUnit.MILLISECONDS)
            .map {
                it.toString()
            }
            .subscribe {
                if (it.isNotEmpty()) {
                    manageFragmentTransaction(SEARCH_LIST_FRAGMENT)
                    viewModel.query.postValue(it)
                } else {
                    manageFragmentTransaction(LIST_FRAGMENT)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun setupToolbar() {
        setSupportActionBar(dataBinding.toolbarMain)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        dataBinding.appbarTitle.text = "Data Tagihan Lunas"
    }

    fun manageFragmentTransaction(selectedFrag: String) {
        when (selectedFrag) {
            LIST_FRAGMENT -> {
                if (supportFragmentManager.findFragmentByTag(LIST_FRAGMENT) != null) {
                    //if the fragment exists, show it.
                    supportFragmentManager.beginTransaction()
                        .show(supportFragmentManager.findFragmentByTag(LIST_FRAGMENT)!!)
                        .commitAllowingStateLoss()
                } else {
                    //if the fragment does not exist, add it to fragment manager.
                    supportFragmentManager.beginTransaction()
                        .add(R.id.bill_viewpager, PaidFragment(), LIST_FRAGMENT)
                        .commitAllowingStateLoss()
                }
                if (supportFragmentManager.findFragmentByTag(SEARCH_LIST_FRAGMENT) != null) {
                    //if the other fragment is visible, hide it.
                    supportFragmentManager.beginTransaction()
                        .hide(supportFragmentManager.findFragmentByTag(SEARCH_LIST_FRAGMENT)!!)
                        .commitAllowingStateLoss()
                }
            }
            SEARCH_LIST_FRAGMENT -> {
                if (supportFragmentManager.findFragmentByTag(SEARCH_LIST_FRAGMENT) != null) {
                    //if the fragment exists, show it.
                    supportFragmentManager.beginTransaction()
                        .show(supportFragmentManager.findFragmentByTag(SEARCH_LIST_FRAGMENT)!!)
                        .commitAllowingStateLoss()
                } else {
                    //if the fragment does not exist, add it to fragment manager.
                    supportFragmentManager.beginTransaction()
                        .add(R.id.bill_viewpager, PaidSearchFragment(), SEARCH_LIST_FRAGMENT)
                        .commitAllowingStateLoss()
                }
                if (supportFragmentManager.findFragmentByTag(LIST_FRAGMENT) != null) {
                    //if the other fragment is visible, hide it.
                    supportFragmentManager.beginTransaction()
                        .hide(supportFragmentManager.findFragmentByTag(LIST_FRAGMENT)!!)
                        .commitAllowingStateLoss()
                }
            }
        }
    }
}