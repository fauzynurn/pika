package com.example.tagihin.view.home.fragments.workorder.fragments

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tagihin.R
import com.example.tagihin.base.BaseFragment
import com.example.tagihin.data.remote.model.Bill
import com.example.tagihin.databinding.FragmentPendingBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.detail.DetailBillActivity
import com.example.tagihin.view.home.BillAdapter
import com.example.tagihin.view.home.BillOnClickListener
import com.example.tagihin.view.home.HomeActivity
import com.example.tagihin.view.home.HomeViewModel

class PendingWorkOrderFragment : BaseFragment<HomeViewModel, FragmentPendingBinding>(HomeViewModel::class){
    private var billAdapter : BillAdapter? = null
    private var list : List<Bill> = listOf()

    override fun getLayoutRes(): Int = R.layout.fragment_pending

    override fun initView(view: View) {}

    override fun onViewReady() {
        billAdapter = BillAdapter(
            list,
            object : BillOnClickListener{
                override fun onClick(bill: Bill) {
                    startActivity(
                    Intent(activity, DetailBillActivity::class.java).let {
                        it.putExtra("bill",bill)
                        it.putExtra("status", Consts.PENDING)
                    }
                    )
                }

            }
        )
        dataBinding.pendingRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = billAdapter
            showShimmerAdapter()
        }
        sharedviewModel.getPendingBill(0,10)
        sharedviewModel.pendingBill.observe(this, Observer {
            dataBinding.pendingRecycler.hideShimmerAdapter()
            billAdapter?.dataSource = it
            billAdapter?.notifyDataSetChanged()

        })
        sharedviewModel.reloadLiveData.observe(activity as HomeActivity, Observer {
            dataBinding.pendingRecycler.showShimmerAdapter()
            sharedviewModel.getPendingBill(0, 10)
        })
    }

    override fun setupToolbar(activity: AppCompatActivity) {
    }


}