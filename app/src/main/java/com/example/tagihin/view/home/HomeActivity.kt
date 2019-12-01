package com.example.tagihin.view.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.databinding.ActivityMainBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.home.fragments.regularbill.RegularBillFragment
import com.example.tagihin.view.home.fragments.regularbill.fragments.PaidFragment
import com.example.tagihin.view.home.fragments.regularbill.fragments.PendingFragment
import com.example.tagihin.view.home.fragments.regularbill.fragments.UnpaidFragment
import com.example.tagihin.view.home.fragments.workorder.WorkOrderFragment
import com.example.tagihin.view.login.LoginActivity
import com.example.tagihin.view.search.SearchActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import java.util.*


class HomeActivity : BaseActivity<HomeViewModel, ActivityMainBinding>(HomeViewModel::class) {
    val listOfFragments = mutableListOf<Fragment>()
    var activeFragment: Fragment? = null
    var adapter: FragmentStatePagerAdapter? = null
    var woList: ArrayList<String> = ArrayList()
    var snackBar: Snackbar? = null
    var multiSelectMode: Boolean = false
    val DETAIL = 66
    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun initView(savedInstanceState: Bundle?) {
        dataBinding.drawer.name.text = pref.getName()?.toLowerCase(Locale.ENGLISH)?.capitalize()
        Glide.with(this).load(pref.getProfilePicUrl()).into(dataBinding.drawer.profilePic)
        adapter = MainNavigationAdapter(
            supportFragmentManager
        )

        dataBinding.viewPager.offscreenPageLimit = 3

        dataBinding.viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (snackBar != null) {
                    if (snackBar?.isShown!!) {
                        snackBar?.dismiss()
                        woList.clear()
                        multiSelectMode = false
                    }
                }
            }

        })
        dataBinding.workOrderBtn.setOnClickListener {
            startActivity(Intent(this, WorkOrderActivity::class.java))
        }

        viewModel.updateWo.observe(this, androidx.lifecycle.Observer {
            if (it) {
                customLoad.hideDialog()
                Toast.makeText(this, "Data berhasil ditambahkan ke WO", Toast.LENGTH_SHORT).show()
            }
        })

        dataBinding.hamburgerIcon.setOnClickListener {
            this.let {
                it.dataBinding.drawerContainer.openDrawer(it.dataBinding.drawer.root)
            }
        }

        dataBinding.searchIcon.setOnClickListener {
            startActivity(
                Intent(this, SearchActivity::class.java)
            )
        }
        dataBinding.viewPager.adapter = adapter
        dataBinding.tabs.setupWithViewPager(dataBinding.viewPager)
        viewModel.getBillStat()
        viewModel.billStat.observe(this, androidx.lifecycle.Observer {
            with(dataBinding) {
                //swipeLayout.isRefreshing = false
                totalText.text = it.seluruh.toString()
                paidText.text = it.lunas.toString()
                unpaidText.text = it.belum.toString()
                pendingText.text = it.pending.toString()
            }
            stopAllLoader()
        })
        dataBinding.refresh.setOnClickListener {
            viewModel.getBillStat()
            viewModel.reloadLiveData.value = true
            startAllLoader()
        }
        dataBinding.drawer.logoutContainer.setOnClickListener {
            pref.setLoginStatus(false)
            startActivity(
                Intent(this@HomeActivity, LoginActivity::class.java)
            )
            finish()
        }

        viewModel.woListData.observe(this, androidx.lifecycle.Observer {
            if (it == 0) {
                snackBar?.dismiss()
                viewModel.multiSelectState.value = false
//                val fragment1 = adapter?.getItem(1) as PendingFragment
//                val fragment2 = adapter?.getItem(2) as UnpaidFragment
//                fragment1.billAdapter?.setMultiSelect(false)
//                fragment2.billAdapter?.setMultiSelect(false)
            } else {
                snackBar = Snackbar.make(
                    dataBinding.root,
                    String.format("%d item dipilih", it),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(
                    "Tambah ke WO"
                ) {
                    Toast.makeText(this, woList.toString(), Toast.LENGTH_SHORT).show()
                    snackBar?.dismiss()
                    customLoad.showDialog()
                    viewModel.moveToWO(woList)
                }
                snackBar?.show()
            }
        })
    }

    override fun setupToolbar() {

    }

    fun stopAllLoader() {
        with(dataBinding) {
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

    fun startAllLoader() {
        with(dataBinding) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DETAIL) {
            if (resultCode == Activity.RESULT_OK) {
                startAllLoader()
                viewModel.getBillStat()
                viewModel.reloadLiveData.value = true
            }
        }
    }

//    override fun initView(savedInstanceState: Bundle?) {
//        initFragments()
//        dataBinding.drawer.name.text = pref.getName()?.toLowerCase(Locale.ENGLISH)?.capitalize()
//        Glide.with(this).load(pref.getProfilePicUrl()).into(dataBinding.drawer.profilePic)
//        dataBinding.drawer.logoutContainer.setOnClickListener {
//            pref.setLoginStatus(false)
//            startActivity(
//                Intent(this@HomeActivity, LoginActivity::class.java)
//            )
//            finish()
//        }
//        dataBinding.drawer.nvView.setNavigationItemSelectedListener(this)
//    }
//
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        dataBinding.drawerContainer.closeDrawer(GravityCompat.START)
//        return when (item.itemId) {
//            R.id.regular_bill_item -> {
//                dataBinding.frameLayout.currentItem = 0
//                true
//            }
//            R.id.work_order_item->{
//                dataBinding.frameLayout.currentItem = 1
//                true
//            }
//            else -> false
//        }
//    }
//
//    fun initFragments() {
//        mainAdapter = MainNavigationAdapter(
//            supportFragmentManager
//        )
//        dataBinding.frameLayout.setSwipeable(false)
//        dataBinding.frameLayout.adapter = mainAdapter
//    }
//
//    override fun setupToolbar() {}
//
//    override fun onResume() {
//        super.onResume()
//        //startAllLoader()
////        viewModel.getBillStat()
////        viewModel.reloadLiveData.value = true
//    }
}
