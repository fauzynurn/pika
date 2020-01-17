package com.example.tagihin.view.bill.unpaid.fragments

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
import com.example.tagihin.data.remote.model.PendingBill
import com.example.tagihin.data.remote.model.UnpaidBill
import com.example.tagihin.data.remote.model.toBase
import com.example.tagihin.databinding.FragmentBillBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.bill.pending.PendingActivity
import com.example.tagihin.view.bill.pending.PendingBillAdapter
import com.example.tagihin.view.bill.pending.PendingBillOnClickListener
import com.example.tagihin.view.bill.pending.PendingViewModel
import com.example.tagihin.view.bill.unpaid.BillOnClickListener
import com.example.tagihin.view.bill.unpaid.UnpaidActivity
import com.example.tagihin.view.bill.unpaid.UnpaidBillAdapter
import com.example.tagihin.view.bill.unpaid.UnpaidViewModel
import com.example.tagihin.view.detail.DetailBillActivity
import timber.log.Timber

class UnpaidFragment : BaseFragment<UnpaidViewModel, FragmentBillBinding>(UnpaidViewModel::class) {
    var mActivity: UnpaidActivity? = null
    private var billAdapter: UnpaidBillAdapter? = null
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
        mActivity?.viewModel?.multiSelectMode?.observe(mActivity!!, Observer {
            billAdapter?.multiSelectMode = it
        })
        dataBinding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
        viewModel.refreshState.observe(mActivity!!, Observer {
            dataBinding.swipeRefresh.isRefreshing = it == NetworkState.LOADING
        })
        mActivity?.viewModel?.resetSelection?.observe(mActivity!!, Observer {
            if(it) {
                billAdapter?.notifyDataSetChanged()
            }
        })
        viewModel.networkState.observe(this, Observer {
            billAdapter?.setNetworkState(it)
        })
        mActivity?.viewModel?.woList?.observe(mActivity!!, Observer {
            billAdapter?.selectedList = it
        })
    }

    override fun onViewReady() {
        mActivity = activity as UnpaidActivity
        billAdapter = UnpaidBillAdapter(
            false,
            context!!,
            object :
                BillOnClickListener {
                override fun onClick(bill: UnpaidBill) {
                    startActivityForResult(
                        Intent(mActivity, DetailBillActivity::class.java).let {
                            it.putExtra("bill", bill.toBase())
                            it.putExtra("status", Consts.UNPAID)
                        }, UPDATEDATA
                    )
                }

                override fun addToWOList(id: Int) {
                    mActivity?.woList?.add(id)
                    mActivity?.viewModel?.woListSize?.value = mActivity?.woList!!.size
                }

                override fun removeFromWOList(id: Int) {
                    mActivity?.woList?.remove(id)
                    mActivity?.viewModel?.woListSize?.value = mActivity?.woList!!.size
                }
            },
            {
                viewModel.retry()
            }, true,
            object : DiffUtil.ItemCallback<UnpaidBill>() {
                override fun areItemsTheSame(oldItem: UnpaidBill, newItem: UnpaidBill): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: UnpaidBill,
                    newItem: UnpaidBill
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Timber.e("HIDDDDDDEEENN")
    }

    override fun setupToolbar(activity: AppCompatActivity) {

    }

}