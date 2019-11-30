package com.example.tagihin.view.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.databinding.ActivityMainBinding
import com.example.tagihin.view.home.fragments.regularbill.RegularBillFragment
import com.example.tagihin.view.home.fragments.workorder.WorkOrderFragment
import com.example.tagihin.view.login.LoginActivity
import com.google.android.material.navigation.NavigationView
import java.util.*


class HomeActivity : BaseActivity<HomeViewModel, ActivityMainBinding>(HomeViewModel::class), NavigationView.OnNavigationItemSelectedListener {
    val listOfFragments = mutableListOf<Fragment>()
    var activeFragment: Fragment? = null
    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun initView(savedInstanceState: Bundle?) {
        initFragments()
        dataBinding.drawer.name.text = pref.getName()?.toLowerCase(Locale.ENGLISH)?.capitalize()
        dataBinding.drawer.logoutContainer.setOnClickListener {
            pref.setLoginStatus(false)
            startActivity(
                Intent(this@HomeActivity, LoginActivity::class.java)
            )
            finish()
        }
        dataBinding.drawer.nvView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        dataBinding.drawerContainer.closeDrawer(GravityCompat.START)
        return when (item.itemId) {
            R.id.regular_bill_item -> {
                supportFragmentManager.beginTransaction().hide(activeFragment!!).show(listOfFragments[0]).commit()
                activeFragment = listOfFragments[0]
                true
            }
            R.id.work_order_item->{
                supportFragmentManager.beginTransaction().hide(activeFragment!!).show(listOfFragments[1]).commit()
                activeFragment = listOfFragments[1]
                true
            }
            else -> false
        }
    }

    fun initFragments() {
        val regularBillFragment = RegularBillFragment()
        val workOrderFragment = WorkOrderFragment()
        listOfFragments.add(regularBillFragment)
        listOfFragments.add(workOrderFragment)
        activeFragment = regularBillFragment
        with(supportFragmentManager) {
            beginTransaction().add(R.id.frame_layout, workOrderFragment, "2").hide(workOrderFragment).commit()
            beginTransaction().add(R.id.frame_layout, regularBillFragment, "1").commit()
        }
    }

    override fun setupToolbar() {}

    override fun onResume() {
        super.onResume()
        //startAllLoader()
//        viewModel.getBillStat()
//        viewModel.reloadLiveData.value = true
    }
}
