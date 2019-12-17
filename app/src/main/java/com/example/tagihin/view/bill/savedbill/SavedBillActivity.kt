package com.example.tagihin.view.bill.savedbill

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.databinding.ActivitySavedBillBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.bill.savedbill.pending.PendingSavedBillFragment
import com.example.tagihin.view.bill.savedbill.unpaid.UnpaidSavedBillFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class SavedBillActivity : BaseActivity<SavedBillViewModel, ActivitySavedBillBinding>(
    SavedBillViewModel::class){
    override fun getLayoutRes(): Int = R.layout.activity_saved_bill

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun initView(savedInstanceState: Bundle?) {
        val adapter = FragmentPagerItemAdapter(
            supportFragmentManager,
            FragmentPagerItems.with(this)
                .add(Consts.PENDING, PendingSavedBillFragment::class.java)
                .add(Consts.UNPAID, UnpaidSavedBillFragment::class.java)
                .create()
        )
        dataBinding.backIcon.setOnClickListener {
            finish()
        }
        viewModel.onSendSuccess.observe(this, Observer {
            hideDialog()
            if(it) {
                showMessage("Data berhasil diperbaharui")
                viewModel.shouldTriggerSomething.value = "refresh"
            }else{
                showMessage("Terdapat masalah dalam memperbaharui data")
            }
        })

        viewModel.onSendFail.observe(this, Observer {
            hideDialog()
            showMessage("Terdapat masalah dalam memperbaharui data")
        })
        dataBinding.sendRequestBtn.setOnClickListener {
            showDialog()
            viewModel.sendRequest()
        }

        dataBinding.viewPager.adapter = adapter
        dataBinding.tabs.setupWithViewPager(dataBinding.viewPager)
    }

    override fun setupToolbar() {}
}