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
import com.example.tagihin.view.home.*

class PendingWorkOrderFragment : BaseFragment<HomeViewModel, FragmentPendingBinding>(HomeViewModel::class){
    private var billAdapter : BillAdapter? = null
    private var list : List<Bill> = listOf()

    override fun getLayoutRes(): Int = R.layout.fragment_pending

    override fun initView(view: View) {}

    override fun onViewReady() {
        billAdapter = BillAdapter(
            false,
            context!!,
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

                override fun addToWOList(id: Int) {

                }

                override fun removeFromWOList(id: Int) {

                }

            }
        )
        dataBinding.pendingRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = billAdapter
            showShimmerAdapter()
        }
        sharedviewModel.getPendingWorkOrderBill(0,10)
        sharedviewModel.pendingWorkOrder.observe(this, Observer {
            dataBinding.pendingRecycler.hideShimmerAdapter()
            billAdapter?.dataSource = it
            billAdapter?.notifyDataSetChanged()

        })
        sharedviewModel.reloadWorkOrderData.observe(activity as WorkOrderActivity, Observer {
            dataBinding.pendingRecycler.showShimmerAdapter()
            sharedviewModel.getPendingWorkOrderBill(0, 10)
        })
    }

    override fun setupToolbar(activity: AppCompatActivity) {
    }


}