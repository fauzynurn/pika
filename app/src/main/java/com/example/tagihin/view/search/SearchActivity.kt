package com.example.tagihin.view.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.databinding.ActivityMainBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.home.HomeViewModel
import com.example.tagihin.view.home.fragments.PaidFragment
import com.example.tagihin.view.home.fragments.PendingFragment
import com.example.tagihin.view.home.fragments.UnpaidFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

//class SearchActivity : BaseActivity<SearchViewModel,ActivityMainBinding>(SearchViewModel::class) {
//
//    override fun getLayoutRes(): Int = R.layout.activity_search
//
//    override fun showMessage(message: String) {
//        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
//    }
//
//    override fun initView(savedInstanceState: Bundle?) {
//        val adapter = FragmentPagerItemAdapter(
//            supportFragmentManager,
//            FragmentPagerItems.with(this)
//                .add(Consts.PAID,PaidFragment::class.java)
//                .add(Consts.PENDING,PendingFragment::class.java)
//                .add(Consts.UNPAID,UnpaidFragment::class.java)
//                .create()
//        )
//        dataBinding.viewPager.adapter = adapter
//        dataBinding.tabs.setViewPager(dataBinding.viewPager)
//        viewModel.getBillStat()
//        viewModel.billStat.observe(this, Observer {
//            with(dataBinding){
//                totalText.text = it.seluruh.toString()
//                paidText.text = it.lunas.toString()
//                unpaidText.text = it.belum.toString()
//                pendingText.text = it.pending.toString()
//            }
//            stopAllLoader()
//        })
//        dataBinding.hamburgerIcon.setOnClickListener{
//            dataBinding.drawerContainer.openDrawer(dataBinding.drawer)
//        }
//    }
//
//    fun stopAllLoader(){
//        with(dataBinding){
//            totalLoading.visibility = View.GONE
//            paidLoading.visibility = View.GONE
//            pendingLoading.visibility = View.GONE
//            unpaidLoading.visibility = View.GONE
//
//            totalText.visibility = View.VISIBLE
//            paidText.visibility = View.VISIBLE
//            pendingText.visibility = View.VISIBLE
//            unpaidText.visibility = View.VISIBLE
//
//        }
//    }
//    override fun setupToolbar() {}
//
//}