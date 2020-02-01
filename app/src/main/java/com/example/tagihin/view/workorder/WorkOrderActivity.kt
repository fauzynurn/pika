package com.example.tagihin.view.workorder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.databinding.ActivityWorkOrderBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.bill.savedbill.SavedBillActivity
import com.example.tagihin.view.home.HomeViewModel
import com.example.tagihin.view.bill.savedbill.pending.PendingSavedBillFragment
import com.example.tagihin.view.bill.savedbill.unpaid.UnpaidSavedBillFragment
import com.example.tagihin.view.workorder.pending.PendingWorkOrderFragment
import com.example.tagihin.view.workorder.unpaid.UnpaidWorkOrderFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class WorkOrderActivity : BaseActivity<WorkOrderViewModel, ActivityWorkOrderBinding>(
    WorkOrderViewModel::class
) {
    override fun getLayoutRes(): Int = R.layout.activity_work_order

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun initView(savedInstanceState: Bundle?) {
        val adapter = FragmentPagerItemAdapter(
            supportFragmentManager,
            FragmentPagerItems.with(this)
                .add(Consts.PENDING, PendingWorkOrderFragment::class.java)
                .add(Consts.UNPAID, UnpaidWorkOrderFragment::class.java)
                .create()
        )
        dataBinding.backIcon.setOnClickListener {
            finish()
        }
        dataBinding.refreshBtn.setOnClickListener {
            viewModel.refreshAll()
        }
        dataBinding.savedBillBtn.setOnClickListener {
            startActivity(
                Intent(this, SavedBillActivity::class.java)
            )
        }
        dataBinding.viewPager.adapter = adapter
        dataBinding.tabs.setupWithViewPager(dataBinding.viewPager)
    }

    override fun setupToolbar() {}
}