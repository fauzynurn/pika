package com.example.tagihin.view.bill.unpaid

import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.databinding.ActivityBillBinding
import com.example.tagihin.view.bill.unpaid.fragments.UnpaidFragment
import com.example.tagihin.view.bill.unpaid.fragments.UnpaidSearchFragment
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class UnpaidActivity : BaseActivity<UnpaidViewModel, ActivityBillBinding>(UnpaidViewModel::class) {
    var woList: ArrayList<Int> = ArrayList()
    val LIST_FRAGMENT = "list_fragment"
    val SEARCH_LIST_FRAGMENT = "search_list_fragment"
    private var textChangeListener: Disposable? = null
    private var mActionMode: ActionMode? = null
    override fun getLayoutRes(): Int = R.layout.activity_bill

    override fun showMessage(message: String) {
        Snackbar.make(dataBinding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun initView(savedInstanceState: Bundle?) {
        val mActionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
            override fun onCreateActionMode(
                mode: ActionMode,
                menu: Menu
            ): Boolean { // Inflate a menu resource providing context menu items
                val inflater: MenuInflater = mode.menuInflater
                inflater.inflate(R.menu.bill_menu, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false // Return false if nothing is done
            }

            override fun onActionItemClicked(
                mode: ActionMode?,
                item: MenuItem?
            ): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {}
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.bill_viewpager, UnpaidFragment(), LIST_FRAGMENT)
            .add(R.id.bill_viewpager, UnpaidSearchFragment(), SEARCH_LIST_FRAGMENT)
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
            } else {
                showMessage("Data sudah ada di WO")
            }
        })
        viewModel.woListSize.observe(this, Observer {
            viewModel.woList.value = woList
            if (it <= 0) {
                viewModel.multiSelectMode.value = false
//                dataBinding.addWoFab.hide()
//                dataBinding.cancelWoFab.hide()
                //dataBinding.moveToWoContainer.visibility = View.GONE
            } else {
                viewModel.multiSelectMode.value = true
                if (mActionMode == null) {
                    mActionMode = startActionMode(mActionModeCallback)
                }
                dataBinding.toolbarMain.startActionMode(mActionModeCallback)
                mActionMode?.title = String.format("%d item dipilih", it)
//                dataBinding.moveToWoBtn.text = String.format("Pindahkan ke WO (%d)", it)
//                dataBinding.moveToWoContainer.visibility = View.VISIBLE
            }
        })

    }

    override fun setupToolbar() {
        setSupportActionBar(dataBinding.toolbarMain)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        dataBinding.appbarTitle.text = "Data Tagihan Belum Bayar"
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
                        .add(R.id.bill_viewpager, UnpaidFragment(), LIST_FRAGMENT)
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
                        .add(R.id.bill_viewpager, UnpaidSearchFragment(), SEARCH_LIST_FRAGMENT)
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