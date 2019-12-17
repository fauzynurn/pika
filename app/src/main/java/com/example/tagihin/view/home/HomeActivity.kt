package com.example.tagihin.view.home

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.databinding.ActivityMainBinding
import com.example.tagihin.view.bill.paid.PaidActivity
import com.example.tagihin.view.bill.pending.PendingActivity
import com.example.tagihin.view.bill.unpaid.UnpaidActivity
import com.example.tagihin.view.login.LoginActivity
import com.example.tagihin.view.settings.SettingActivity
import com.example.tagihin.view.workorder.WorkOrderActivity
import java.util.*
import kotlin.math.roundToInt


class HomeActivity : BaseActivity<HomeViewModel, ActivityMainBinding>(HomeViewModel::class) {
    val DETAIL = 66
    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun initView(savedInstanceState: Bundle?) {
        with(dataBinding) {
            drawer.name.text = pref.getName()?.toLowerCase(Locale.ENGLISH)?.capitalize()
            Glide.with(this@HomeActivity).load(pref.getProfilePicUrl()).into(dataBinding.drawer.profilePic)

            woListBtn.setOnClickListener {
                startActivity(Intent(this@HomeActivity, WorkOrderActivity::class.java))
            }
            hamburgerIcon.setOnClickListener {
                drawerContainer.openDrawer(drawer.root)
            }
            swipeRefresh.setOnRefreshListener {
                chartContainer.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                viewModel.getBillStat()
            }
            drawer.logoutContainer.setOnClickListener {
                pref.setLoginStatus(false)
                startActivity(
                    Intent(this@HomeActivity, LoginActivity::class.java)
                )
                finish()
            }

            drawer.settingsContainer.setOnClickListener {
                startActivity(
                    Intent(this@HomeActivity, SettingActivity::class.java)
                )
                drawerContainer.closeDrawers()
            }

            paidListBtn.setOnClickListener {
                startActivity(
                    Intent(this@HomeActivity,PaidActivity::class.java)
                )
            }

            pendingListBtn.setOnClickListener {
                startActivity(
                    Intent(this@HomeActivity, PendingActivity::class.java)
                )
            }

            unpaidListBtn.setOnClickListener {
                startActivity(
                    Intent(this@HomeActivity, UnpaidActivity::class.java)
                )
            }
        }

        viewModel.getBillStat()
        viewModel.billStat.observe(this, androidx.lifecycle.Observer { billStat ->
            with(dataBinding) {
                //swipeLayout.isRefreshing = false
                swipeRefresh.isRefreshing = false
                setChart(true)
                var colorValuePair = listOf(
                    Pair(
                        Color.rgb(60, 216, 141),
                        if(billStat.seluruh != 0) (billStat.lunas * 100f).div(billStat.seluruh).roundToInt().toFloat() else 0.toFloat()
                    ),
                    Pair(
                        Color.rgb(255, 218, 68),
                        if(billStat.seluruh != 0) (billStat.pending * 100f).div(billStat.seluruh).roundToInt().toFloat() else 0.toFloat()
                    ),
                    Pair(
                        Color.rgb(255, 120, 120),
                        if(billStat.seluruh != 0) (billStat.belum * 100f).div(billStat.seluruh).roundToInt().toFloat() else 0.toFloat()
                    )
                )
                colorValuePair = colorValuePair.sortedByDescending {
                    it.second
                }
                with(slimChart) {
                    this.stats = colorValuePair.map {
                        it.second
                    }.toFloatArray()
                    this.stats[0] = 100.toFloat()
                    this.colors = colorValuePair.map {
                        it.first
                    }.toIntArray()
                    this.setStartAnimationDuration(1500)
                    playStartAnimation()
                }
                with(billStat) {
                    slimChart.text = seluruh.toString()
                    paidText.text = lunas.toString()
                    unpaidText.text = belum.toString()
                    pendingText.text = pending.toString()
                }
            }
        })
    }

    override fun setupToolbar() {

    }

    fun setChart(isVisible: Boolean) {
        dataBinding.chartContainer.visibility = if (isVisible) View.VISIBLE else View.GONE
        dataBinding.progressBar.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DETAIL) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.getBillStat()
                viewModel.reloadLiveData.value = true
            }
        }
    }
}
