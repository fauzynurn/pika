package com.example.tagihin.view.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.databinding.ActivityMainBinding
import com.example.tagihin.databinding.ActivityWorkOrderBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.home.fragments.workorder.fragments.PendingWorkOrderFragment
import com.example.tagihin.view.home.fragments.workorder.fragments.UnpaidWorkOrderFragment
import com.example.tagihin.view.login.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import java.util.*

class WorkOrderActivity : BaseActivity<HomeViewModel, ActivityWorkOrderBinding>(HomeViewModel::class){
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

        dataBinding.viewPager.adapter = adapter
        dataBinding.tabs.setupWithViewPager(dataBinding.viewPager)
        dataBinding.refresh.setOnClickListener {
            viewModel.reloadWorkOrderData.value = true
        }
    }

    override fun setupToolbar() {}
}