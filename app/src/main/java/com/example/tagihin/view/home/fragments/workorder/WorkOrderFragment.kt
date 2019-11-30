package com.example.tagihin.view.home.fragments.workorder

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tagihin.R
import com.example.tagihin.base.BaseFragment
import com.example.tagihin.databinding.FragmentRegularBillBinding
import com.example.tagihin.databinding.FragmentWorkOrderBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.home.HomeViewModel
import com.example.tagihin.view.home.fragments.regularbill.fragments.PaidFragment
import com.example.tagihin.view.home.fragments.regularbill.fragments.PendingFragment
import com.example.tagihin.view.home.fragments.regularbill.fragments.UnpaidFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class WorkOrderFragment  : BaseFragment<HomeViewModel, FragmentWorkOrderBinding>(HomeViewModel::class){
    override fun getLayoutRes(): Int = R.layout.fragment_work_order

    override fun initView(view: View) {
//        val adapter = FragmentPagerItemAdapter(
//            activity?.supportFragmentManager,
//            FragmentPagerItems.with(activity)
//                .add(Consts.PENDING, PendingFragment::class.java)
//                .add(Consts.UNPAID, UnpaidFragment::class.java)
//                .create()
//        )
//
//
//        dataBinding.viewPager.adapter = adapter
//        dataBinding.tabs.setViewPager(dataBinding.viewPager)
//        sharedviewModel.getBillStat()
//        dataBinding.refresh.setOnClickListener {
//            viewModel.getBillStat()
//            viewModel.reloadLiveData.value = true
//        }
    }

    override fun onViewReady() {

    }

    override fun setupToolbar(activity: AppCompatActivity) {}

}