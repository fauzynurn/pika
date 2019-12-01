package com.example.tagihin.view.home.fragments.regularbill

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.tagihin.R
import com.example.tagihin.base.BaseFragment
import com.example.tagihin.databinding.FragmentRegularBillBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.home.HomeActivity
import com.example.tagihin.view.home.HomeViewModel
import com.example.tagihin.view.home.fragments.regularbill.fragments.PaidFragment
import com.example.tagihin.view.home.fragments.regularbill.fragments.PendingFragment
import com.example.tagihin.view.home.fragments.regularbill.fragments.UnpaidFragment
import com.example.tagihin.view.search.SearchActivity
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

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

        dataBinding.hamburgerIcon.setOnClickListener{
            (activity as HomeActivity).let{
                it.dataBinding.drawerContainer.openDrawer(it.dataBinding.drawer.root)
            }
        }

        dataBinding.searchIcon.setOnClickListener {
            startActivity(
                Intent(activity,SearchActivity::class.java)
            )
        }
        dataBinding.viewPager.adapter = adapter
        dataBinding.tabs.setupWithViewPager(dataBinding.viewPager)
        sharedviewModel.getBillStat()
        sharedviewModel.billStat.observe(this, Observer {
            with(dataBinding){
                //swipeLayout.isRefreshing = false
                totalText.text = it.seluruh.toString()
                paidText.text = it.lunas.toString()
                unpaidText.text = it.belum.toString()
                pendingText.text = it.pending.toString()
            }
            stopAllLoader()
        })
        dataBinding.refresh.setOnClickListener {
            sharedviewModel.getBillStat()
            sharedviewModel.reloadLiveData.value = true
            startAllLoader()
        }

    }

    fun stopAllLoader(){
        with(dataBinding){
            totalLoading.visibility = View.GONE
            paidLoading.visibility = View.GONE
            pendingLoading.visibility = View.GONE
            unpaidLoading.visibility = View.GONE

            totalText.visibility = View.VISIBLE
            paidText.visibility = View.VISIBLE
            pendingText.visibility = View.VISIBLE
            unpaidText.visibility = View.VISIBLE

        }
    }

    fun startAllLoader(){
        with(dataBinding){
            totalLoading.visibility = View.VISIBLE
            paidLoading.visibility = View.VISIBLE
            pendingLoading.visibility = View.VISIBLE
            unpaidLoading.visibility = View.VISIBLE

            totalText.visibility = View.GONE
            paidText.visibility = View.GONE
            pendingText.visibility = View.GONE
            unpaidText.visibility = View.GONE

        }
    }

    override fun onViewReady() {

    }

    override fun setupToolbar(activity: AppCompatActivity) {}

}