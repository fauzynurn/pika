package com.example.tagihin.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.databinding.ActivityMainBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.home.fragments.PaidFragment
import com.example.tagihin.view.home.fragments.PendingFragment
import com.example.tagihin.view.home.fragments.UnpaidFragment
import com.example.tagihin.view.home.fragments.regularbill.RegularBillFragment
import com.example.tagihin.view.login.LoginActivity
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import java.util.*

class HomeActivity : BaseActivity<HomeViewModel,ActivityMainBinding>(HomeViewModel::class) {
    val listOfFragments = listOf<Fragment>()
    val activeFragment : Fragment? = null
    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun showMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun initView(savedInstanceState: Bundle?) {
        initFragments()
        dataBinding.drawerContainer.bringToFront()
        dataBinding.drawer.name.text = pref.getName()?.toLowerCase(Locale.ENGLISH)?.capitalize()
        dataBinding.drawer.logoutContainer.setOnClickListener {
            pref.setLoginStatus(false)
            startActivity(
                Intent(this@HomeActivity, LoginActivity::class.java)
            )
            finish()
        }
    }

    fun initFragments(){
        listOfFragments[0] = RegularBillFragment()
    }

    override fun setupToolbar() {}

    override fun onResume() {
        super.onResume()
        //startAllLoader()
//        viewModel.getBillStat()
//        viewModel.reloadLiveData.value = true
    }
}
