package com.example.tagihin.view.bill.pending

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.data.remote.model.PendingBill
import com.example.tagihin.databinding.ActivityBillBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.bill.pending.fragments.PendingFragment
import com.example.tagihin.view.bill.pending.fragments.PendingSearchFragment
import com.example.tagihin.view.bill.unpaid.fragments.UnpaidFragment
import com.example.tagihin.view.bill.unpaid.fragments.UnpaidSearchFragment
import com.example.tagihin.view.detail.DetailBillActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.Disposable
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class PendingActivity : BaseActivity<PendingViewModel, ActivityBillBinding>(PendingViewModel::class) {
    var woList: ArrayList<Int> = ArrayList()
    val LIST_FRAGMENT = "list_fragment"
    val SEARCH_LIST_FRAGMENT = "search_list_fragment"
    private var textChangeListener: Disposable? = null
    override fun getLayoutRes(): Int = R.layout.activity_bill

    override fun showMessage(message: String) {
        Snackbar.make(dataBinding.root,message,Snackbar.LENGTH_SHORT).show()
    }

    override fun initView(savedInstanceState: Bundle?) {
        supportFragmentManager.beginTransaction()
            .add(R.id.bill_viewpager, PendingFragment(), LIST_FRAGMENT)
            .add(R.id.bill_viewpager, PendingSearchFragment(), SEARCH_LIST_FRAGMENT)
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
        dataBinding.cancelWoFab.setOnClickListener {
            woList.clear()
            viewModel.woListSize.value = woList.size
            viewModel.resetSelection.value = true
        }
        dataBinding.addWoFab.setOnClickListener {
            showDialog()
            viewModel.moveToWO(woList)
        }
        dataBinding.addWoFab.hide()
        dataBinding.cancelWoFab.hide()
        viewModel.updateWo.observe(this, Observer {
            hideDialog()
            if (it) {
                woList.clear()
                viewModel.woListSize.value = woList.size
                viewModel.resetSelection.value = true
                viewModel.woList.value = woList
                showMessage("Data berhasil ditambah ke WO")
                viewModel.refresh()
            }else{
                showMessage("Data sudah ada di WO")
            }
        })
        viewModel.woListSize.observe(this, Observer {
            viewModel.woList.value = woList
            if (it <= 0) {
                viewModel.multiSelectMode.value = false
                dataBinding.addWoFab.hide()
                dataBinding.cancelWoFab.hide()
                //dataBinding.moveToWoContainer.visibility = View.GONE
            } else {
                viewModel.multiSelectMode.value = true
                dataBinding.addWoFab.show()
                dataBinding.cancelWoFab.show()
                dataBinding.addWoFab.text = String.format("Pindah ke WO (%d)", it)
//                dataBinding.moveToWoBtn.text = String.format("Pindahkan ke WO (%d)", it)
//                dataBinding.moveToWoContainer.visibility = View.VISIBLE
            }
        })
    }

    override fun setupToolbar() {
        setSupportActionBar(dataBinding.toolbarMain)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        dataBinding.appbarTitle.text = "Data Tagihan Tertunda"
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
                        .add(R.id.bill_viewpager, PendingFragment(), LIST_FRAGMENT)
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
                        .add(R.id.bill_viewpager, PendingSearchFragment(), SEARCH_LIST_FRAGMENT)
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