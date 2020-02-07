package com.example.tagihin.view.workorder.pending

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tagihin.R
import com.example.tagihin.base.BaseFragment
import com.example.tagihin.data.remote.NetworkState
import com.example.tagihin.data.remote.model.PendingWorkOrder
import com.example.tagihin.data.remote.model.toBase
import com.example.tagihin.databinding.FragmentPendingBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.bill.savedbill.SavedBillViewModel
import com.example.tagihin.view.detail.DetailBillActivity
import com.example.tagihin.view.workorder.WorkOrderActivity
import com.example.tagihin.view.workorder.WorkOrderViewModel

class PendingWorkOrderFragment : BaseFragment<WorkOrderViewModel, FragmentPendingBinding>(
    WorkOrderViewModel::class){
    var billAdapter: PendingWorkOrderAdapter? = null
    val layoutManager = LinearLayoutManager(activity)
    lateinit var mActivity : WorkOrderActivity
    private var woList: MutableList<Int> = mutableListOf()
//    var snackBar: Snackbar? = null
    override fun getLayoutRes(): Int = R.layout.fragment_pending

    override fun initView(view: View) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshPending()
        }
        viewModel.pendingRefreshState.observe(mActivity, Observer {
            dataBinding.swipeRefresh.isRefreshing = it == NetworkState.LOADING
        })
    }
    override fun onViewReady() {
        mActivity = activity as WorkOrderActivity
        billAdapter = PendingWorkOrderAdapter(
            false,
            activity as WorkOrderActivity,
            object : PendingWorkOrderOnClickListener {
                override fun onClick(bill: PendingWorkOrder) {
                    startActivity(
                        Intent(activity as WorkOrderActivity, DetailBillActivity::class.java).let {
                            it.putExtra("bill", bill.toBase())
                            it.putExtra("status", Consts.PENDING)
                        }
                    )
                }

                override fun addToWOList(id: Int) {
                    woList.add(id)
                    //notify all adapter that the multiselect is on
//                    (activity as HomeActivity).viewModel.woListData.value =
//                        (activity as HomeActivity).woList.size
                    //assign global list to list inside adapter so that adapter
                    //get the latest list update
                    //billAdapter?.selectedList = (activity as HomeActivity).woList
//                    woList.add(id)
//                    (activity as HomeActivity).viewModel.woListData.value = woList.size
                }

                override fun removeFromWOList(id: Int) {
                    woList.remove(id)
//                    (activity as HomeActivity).viewModel.woListData.value =
//                        (activity as HomeActivity).woList.size
//                    billAdapter?.selectedList = (activity as HomeActivity).woList
//                    woList.remove(id)
//                    (activity as HomeActivity).viewModel.woListData.value = woList.size
                }

            },
            {
                viewModel.retryPending()
            }, false,
            object : DiffUtil.ItemCallback<PendingWorkOrder>() {
                override fun areItemsTheSame(
                    oldItem: PendingWorkOrder,
                    newItem: PendingWorkOrder
                ): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: PendingWorkOrder,
                    newItem: PendingWorkOrder
                ): Boolean =
                    oldItem == newItem
            }

        )
        dataBinding.pendingRecycler.apply {
            layoutManager = LinearLayoutManager(activity as WorkOrderActivity)
            adapter = billAdapter
        }
        viewModel.pendingNetworkState.observe(this, Observer {
            billAdapter?.setNetworkState(it)
        })
        viewModel.shouldTriggerSomethingPending.value = "changed"
        viewModel.pendingPosts.observe(this, Observer {
            billAdapter?.submitList(it)
        })
    }

    override fun setupToolbar(activity: AppCompatActivity) {
    }

//    private var billAdapter : BillAdapter? = null
//    private var list : MutableList<Bill> = mutableListOf()
//
//    override fun getLayoutRes(): Int = R.layout.fragment_pending
//
//    override fun initView(view: View) {}
//
//    override fun onViewReady() {
//        billAdapter = BillAdapter(
//            false,
//            context!!,
//            list,
//            object : BillOnClickListener{
//                override fun onClick(bill: Bill) {
//                    startActivity(
//                    Intent(activity, DetailBillActivity::class.java).let {
//                        it.putExtra("bill",bill)
//                        it.putExtra("status", Consts.PENDING)
//                    }
//                    )
//                }
//
//                override fun addToWOList(id: Int) {
//
//                }
//
//                override fun removeFromWOList(id: Int) {
//
//                }
//
//            }
//        )
//        dataBinding.pendingRecycler.apply {
//            layoutManager = LinearLayoutManager(activity)
//            adapter = billAdapter
//            showShimmerAdapter()
//        }
//        sharedviewModel.getPendingWorkOrderBill(0,10)
//        sharedviewModel.pendingWorkOrder.observe(this, Observer {
//            dataBinding.pendingRecycler.hideShimmerAdapter()
//            billAdapter?.dataSource?.addAll(it)
//            billAdapter?.notifyDataSetChanged()
//
//        })
//        sharedviewModel.reloadWorkOrderData.observe(activity as WorkOrderActivity, Observer {
//            dataBinding.pendingRecycler.showShimmerAdapter()
//            sharedviewModel.getPendingWorkOrderBill(0, 10)
//        })
//    }
//
//    override fun setupToolbar(activity: AppCompatActivity) {
//    }


}