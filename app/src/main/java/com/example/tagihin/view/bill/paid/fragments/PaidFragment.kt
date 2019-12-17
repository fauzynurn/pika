package com.example.tagihin.view.bill.paid.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagihin.R
import com.example.tagihin.base.BaseFragment
import com.example.tagihin.data.remote.NetworkState
import com.example.tagihin.data.remote.model.PaidBill
import com.example.tagihin.data.remote.model.toBase
import com.example.tagihin.databinding.FragmentBillBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.bill.paid.PaidActivity
import com.example.tagihin.view.bill.paid.PaidBillAdapter
import com.example.tagihin.view.bill.paid.PaidBillOnClickListener
import com.example.tagihin.view.bill.paid.PaidViewModel
import com.example.tagihin.view.bill.pending.PendingBillOnClickListener
import com.example.tagihin.view.bill.unpaid.BillOnClickListener
import com.example.tagihin.view.detail.DetailBillActivity

class PaidFragment : BaseFragment<PaidViewModel, FragmentBillBinding>(PaidViewModel::class) {
    var mActivity: PaidActivity? = null
    private var billAdapter: PaidBillAdapter? = null
    val UPDATEDATA = 66
    override fun getLayoutRes(): Int = R.layout.fragment_bill

    override fun initView(view: View) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATEDATA) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.refresh()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
        viewModel.refreshState.observe(mActivity!!, Observer {
            dataBinding.swipeRefresh.isRefreshing = it == NetworkState.LOADING
        })
        viewModel.networkState.observe(this, Observer {
            billAdapter?.setNetworkState(it)
        })
    }

    override fun onViewReady() {
        mActivity = activity as PaidActivity
        billAdapter = PaidBillAdapter(
            context!!,
            object :
                PaidBillOnClickListener {
                override fun onClick(bill: PaidBill) {
                    startActivityForResult(
                        Intent(mActivity, DetailBillActivity::class.java).let {
                            it.putExtra("bill", bill.toBase())
                            it.putExtra("status", Consts.PAID)
                        }, UPDATEDATA
                    )
                }
            },
            {
                viewModel.retry()
            },
            object : DiffUtil.ItemCallback<PaidBill>() {
                override fun areItemsTheSame(oldItem: PaidBill, newItem: PaidBill): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: PaidBill,
                    newItem: PaidBill
                ): Boolean = oldItem == newItem
            }

        )
        dataBinding.billRecycler.apply {
            layoutManager = LinearLayoutManager(context!!)
            adapter = billAdapter
        }
        viewModel.shouldTriggerSomething.value = "changed"
        viewModel.posts.observe(this, Observer {
            billAdapter?.submitList(it) {
                // Workaround for an issue where RecyclerView incorrectly uses the loading / spinner
                // item added to the end of the list as an anchor during initial load.
                val layoutManager = (dataBinding.billRecycler.layoutManager as LinearLayoutManager)
                val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (position != RecyclerView.NO_POSITION) {
                    dataBinding.billRecycler.scrollToPosition(position)
                }
            }
        })
    }

    override fun setupToolbar(activity: AppCompatActivity) {

    }

}