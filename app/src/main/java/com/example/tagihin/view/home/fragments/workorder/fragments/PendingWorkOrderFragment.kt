package com.example.tagihin.view.home.fragments.workorder.fragments

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagihin.R
import com.example.tagihin.base.BaseFragment
import com.example.tagihin.data.remote.model.Bill
import com.example.tagihin.databinding.FragmentPendingBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.detail.DetailBillActivity
import com.example.tagihin.view.home.*

class PendingWorkOrderFragment : BaseFragment<HomeViewModel, FragmentPendingBinding>(HomeViewModel::class){
    var billAdapter: BillAdapter? = null
    private var list: MutableList<Bill> = mutableListOf()
    val DETAIL = 66
    val layoutManager = LinearLayoutManager(activity)
    var PAGE = 0
    var mAllowLoadMore = true
    //    private var woList: MutableList<Int> = mutableListOf()
//    var snackBar: Snackbar? = null
    override fun getLayoutRes(): Int = R.layout.fragment_pending

    override fun initView(view: View) {}

    override fun onViewReady() {
        billAdapter = BillAdapter(
            false,
            context!!,
            list,
            object : BillOnClickListener {
                override fun onClick(bill: Bill) {
                    activity?.startActivityForResult(
                        Intent(activity, DetailBillActivity::class.java).let {
                            it.putExtra("bill", bill)
                            it.putExtra("status", Consts.PENDING)
                        }, DETAIL
                    )
                }

                override fun addToWOList(id: Int) {

                }

                override fun removeFromWOList(id: Int) {
                }

            }
        )
        val onScroll = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisiblesItems = layoutManager.findLastVisibleItemPosition()
                    if (mAllowLoadMore) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            PAGE += 6
                            sharedviewModel.getPendingBill(PAGE, 6)
                            mAllowLoadMore = false
                        }
                    }
                }
            }
        }
        dataBinding.pendingRecycler.apply {
            layoutManager = this@PendingWorkOrderFragment.layoutManager
            addOnScrollListener(onScroll)
            adapter = billAdapter
            showShimmerAdapter()
        }

        sharedviewModel.getPendingWorkOrderBill(PAGE, 6)
        sharedviewModel.pendingWorkOrder.observe(this, Observer {
            dataBinding.pendingRecycler.hideShimmerAdapter()
            mAllowLoadMore = if (it.isNotEmpty()) {
                if(PAGE == 0){
                    billAdapter?.dataSource?.clear()
                }
                billAdapter?.dataSource?.addAll(it)
                billAdapter?.notifyDataSetChanged()
                true
            } else {
                false

            }

        })

        sharedviewModel.reloadWorkOrderData.observe(activity as WorkOrderActivity, Observer {
            PAGE = 0
            dataBinding.pendingRecycler.showShimmerAdapter()
            sharedviewModel.getPendingWorkOrderBill(PAGE, 6)
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