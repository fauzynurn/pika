package com.example.tagihin.view.home.fragments.regularbill

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tagihin.R
import com.example.tagihin.base.BaseFragment
import com.example.tagihin.databinding.FragmentRegularBillBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.home.HomeActivity
import com.example.tagihin.view.home.HomeViewModel
import com.example.tagihin.view.home.fragments.PaidFragment
import com.example.tagihin.view.home.fragments.PendingFragment
import com.example.tagihin.view.home.fragments.UnpaidFragment
import androidx.lifecycle.Observer
import com.example.tagihin.view.login.LoginActivity
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import java.util.*

class RegularBillFragment  : BaseFragment<HomeViewModel,FragmentRegularBillBinding>(HomeViewModel::class){
    override fun getLayoutRes(): Int = R.layout.fragment_regular_bill

    override fun initView(view: View) {
        val adapter = FragmentPagerItemAdapter(
            activity?.supportFragmentManager,
            FragmentPagerItems.with(activity)
                .add(Consts.PAID, PaidFragment::class.java)
                .add(Consts.PENDING, PendingFragment::class.java)
                .add(Consts.UNPAID, UnpaidFragment::class.java)
                .create()
        )


        dataBinding.workOrderViewPager.adapter = adapter
        dataBinding.tabs.setViewPager(dataBinding.workOrderViewPager)
        sharedviewModel.getBillStat()
        dataBinding.refresh.setOnClickListener {
            startAllLoader()
            viewModel.getBillStat()
            viewModel.reloadLiveData.value = true
        }
    }

    override fun onViewReady() {

    }

    override fun setupToolbar(activity: AppCompatActivity) {}

}