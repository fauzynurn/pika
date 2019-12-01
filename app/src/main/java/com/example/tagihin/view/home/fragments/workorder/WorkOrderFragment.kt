package com.example.tagihin.view.home.fragments.workorder

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tagihin.R
import com.example.tagihin.base.BaseFragment
import com.example.tagihin.databinding.FragmentRegularBillBinding
import com.example.tagihin.databinding.FragmentWorkOrderBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.home.HomeActivity
import com.example.tagihin.view.home.HomeViewModel
import com.example.tagihin.view.home.fragments.regularbill.fragments.PaidFragment
import com.example.tagihin.view.home.fragments.regularbill.fragments.PendingFragment
import com.example.tagihin.view.home.fragments.regularbill.fragments.UnpaidFragment
import com.example.tagihin.view.home.fragments.workorder.fragments.PendingWorkOrderFragment
import com.example.tagihin.view.home.fragments.workorder.fragments.UnpaidWorkOrderFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class WorkOrderFragment  : BaseFragment<HomeViewModel,FragmentWorkOrderBinding>(HomeViewModel::class){
    override fun getLayoutRes(): Int = R.layout.fragment_work_order

    override fun initView(view: View) {
        val adapter = FragmentPagerItemAdapter(
            activity?.supportFragmentManager,
            FragmentPagerItems.with(activity)
                .add(Consts.PENDING, PendingWorkOrderFragment::class.java)
                .add(Consts.UNPAID, UnpaidWorkOrderFragment::class.java)
                .create()
        )

        dataBinding.hamburgerIcon.setOnClickListener{
            (activity as HomeActivity).let{
                it.dataBinding.drawerContainer.openDrawer(it.dataBinding.drawer.root)
            }
        }
        dataBinding.viewPager.adapter = adapter
        dataBinding.tabs.setupWithViewPager(dataBinding.viewPager)
        dataBinding.refresh.setOnClickListener {
            sharedviewModel.reloadLiveData.value = true
        }

    }

    override fun onViewReady() {

    }

    override fun setupToolbar(activity: AppCompatActivity) {}

}