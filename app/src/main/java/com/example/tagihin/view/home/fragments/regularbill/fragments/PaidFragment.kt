package com.example.tagihin.view.home.fragments.regularbill.fragments

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tagihin.R
import com.example.tagihin.base.BaseFragment
import com.example.tagihin.data.remote.model.Bill
import com.example.tagihin.databinding.FragmentPaidBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.detail.DetailBillActivity
import com.example.tagihin.view.home.BillAdapter
import com.example.tagihin.view.home.BillOnClickListener
import com.example.tagihin.view.home.HomeActivity
import com.example.tagihin.view.home.HomeViewModel

class PaidFragment : BaseFragment<HomeViewModel, FragmentPaidBinding>(HomeViewModel::class) {
    private var billAdapter: BillAdapter? = null
    private var list: MutableList<Bill> = mutableListOf()
    override fun getLayoutRes(): Int = R.layout.fragment_paid

    override fun initView(view: View) {}

    override fun onViewReady() {
        billAdapter = BillAdapter(
            (activity as HomeActivity).multiSelectMode,
            context!!,
            list,
            object : BillOnClickListener {
                override fun onClick(bill: Bill) {
                    startActivity(
                    Intent(activity, DetailBillActivity::class.java).let {
                        it.putExtra("bill",bill)
                        it.putExtra("status", Consts.PAID)
                    }
                    )
                }

                override fun addToWOList(id: Int) {

                }

                override fun removeFromWOList(id: Int) {

                }

            }
        )
        dataBinding.paidRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = billAdapter
            showShimmerAdapter()
        }
        sharedviewModel.getPaidBill(0, 10)
        sharedviewModel.paidBill.observe(this, Observer {
            dataBinding.paidRecycler.hideShimmerAdapter()
            billAdapter?.dataSource?.addAll(it)
            billAdapter?.notifyDataSetChanged()

        })
        sharedviewModel.reloadLiveData.observe(activity as HomeActivity, Observer {
            dataBinding.paidRecycler.showShimmerAdapter()
            sharedviewModel.getPaidBill(0, 10)
        })
    }

    override fun setupToolbar(activity: AppCompatActivity) {
    }


}