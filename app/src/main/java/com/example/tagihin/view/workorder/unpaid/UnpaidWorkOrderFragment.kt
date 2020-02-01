package com.example.tagihin.view.workorder.unpaid

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
import com.example.tagihin.data.remote.model.UnpaidBill
import com.example.tagihin.data.remote.model.UnpaidWorkOrder
import com.example.tagihin.data.remote.model.toBase
import com.example.tagihin.databinding.FragmentUnpaidBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.bill.unpaid.UnpaidViewModel
import com.example.tagihin.view.detail.DetailBillActivity
import com.example.tagihin.view.home.*
import com.example.tagihin.view.workorder.WorkOrderActivity
import com.example.tagihin.view.workorder.WorkOrderViewModel

class UnpaidWorkOrderFragment : BaseFragment<WorkOrderViewModel, FragmentUnpaidBinding>(WorkOrderViewModel::class){
    var billAdapter: UnpaidWorkOrderAdapter? = null
    val layoutManager = LinearLayoutManager(activity)
    private var woList: MutableList<Int> = mutableListOf()
    lateinit var mActivity : WorkOrderActivity
    //    var snackBar: Snackbar? = null
    override fun getLayoutRes(): Int = R.layout.fragment_unpaid

    override fun initView(view: View) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshUnpaid()
        }
        viewModel.unpaidRefreshState.observe(mActivity, Observer {
            dataBinding.swipeRefresh.isRefreshing = it == NetworkState.LOADING
        })
    }

    override fun onViewReady() {
        mActivity = activity as WorkOrderActivity
        billAdapter = UnpaidWorkOrderAdapter(
            false,
            activity as WorkOrderActivity,
            object : UnpaidWorkOrderOnClickListener {
                override fun onClick(bill: UnpaidWorkOrder) {
                    startActivity(
                        Intent(activity as WorkOrderActivity, DetailBillActivity::class.java).let {
                            it.putExtra("bill", bill.toBase())
                            it.putExtra("status", Consts.UNPAID)
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
                viewModel.retryUnpaid()
            }, false,
            object : DiffUtil.ItemCallback<UnpaidWorkOrder>() {
                override fun areItemsTheSame(oldItem: UnpaidWorkOrder, newItem: UnpaidWorkOrder): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: UnpaidWorkOrder, newItem: UnpaidWorkOrder): Boolean =
                    oldItem == newItem
            }

        )
        dataBinding.unpaidRecycler.apply {
            layoutManager = LinearLayoutManager(activity as WorkOrderActivity)
            adapter = billAdapter
        }
        viewModel.unpaidNetworkState.observe(this, Observer {
            billAdapter?.setNetworkState(it)
        })
        viewModel.shouldTriggerSomethingUnpaid.value = "changed"
        viewModel.unpaidPosts.observe(this, Observer {
            billAdapter?.submitList(it)
        })
    }

    override fun setupToolbar(activity: AppCompatActivity) {
    }

//    var billAdapter: UnpaidWorkOrderAdapter? = null
//    private var list: MutableList<UnpaidBill> = mutableListOf()
//    val DETAIL = 66
//    val layoutManager = LinearLayoutManager(activity)
//    var mAllowLoadMore = true
//    //    private var woList: MutableList<Int> = mutableListOf()
////    var snackBar: Snackbar? = null
//    override fun getLayoutRes(): Int = R.layout.fragment_unpaid
//
//    override fun initView(view: View) {}
//
//    override fun onViewReady() {
//        billAdapter = UnpaidWorkOrderAdapter(
//            false,
//            context!!,
//            object : BillOnClickListener {
//                override fun onClick(bill: UnpaidBill) {
//                    activity?.startActivityForResult(
//                        Intent(activity, DetailBillActivity::class.java).let {
//                            it.putExtra("bill", bill)
//                            it.putExtra("status", Consts.PENDING)
//                        }, DETAIL
//                    )
//                }
//
//                override fun addToWOList(id: Int) {
//
//                }
//
//                override fun removeFromWOList(id: Int) {
//                }
//
//            }
//        )
//        dataBinding.unpaidRecycler.apply {
//            layoutManager = this@UnpaidWorkOrderFragment.layoutManager
//            adapter = billAdapter
//        }
//
//        viewModel.shouldTriggerSomething.value = "changed"
//        viewModel.unpaidWorkOrder.observe(this, Observer {
//            mAllowLoadMore = if (it.isNotEmpty()) {
//                if(PAGE == 0){
//                    billAdapter?.dataSource?.clear()
//                }
//                billAdapter?.dataSource?.addAll(it)
//                billAdapter?.notifyDataSetChanged()
//                true
//            } else {
//                false
//
//            }
//
//        })
//
//        viewModel.reloadWorkOrderData.observe(activity as WorkOrderActivity, Observer {
//            PAGE = 0
//            dataBinding.unpaidRecycler.showShimmerAdapter()
//            viewModel.getUnpaidWorkOrderBill(PAGE, 6)
//        })
//    }
//
//    override fun setupToolbar(activity: AppCompatActivity) {
//    }


}