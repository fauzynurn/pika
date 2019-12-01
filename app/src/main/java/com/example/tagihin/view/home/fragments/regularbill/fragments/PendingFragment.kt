package com.example.tagihin.view.home.fragments.regularbill.fragments

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagihin.R
import com.example.tagihin.base.BaseFragment
import com.example.tagihin.data.remote.model.Bill
import com.example.tagihin.databinding.FragmentPendingBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.utils.interfaces.EndlessOnScrollListener
import com.example.tagihin.view.detail.DetailBillActivity
import com.example.tagihin.view.home.BillAdapter
import com.example.tagihin.view.home.BillOnClickListener
import com.example.tagihin.view.home.HomeActivity
import com.example.tagihin.view.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class PendingFragment : BaseFragment<HomeViewModel, FragmentPendingBinding>(HomeViewModel::class) {
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
            (activity as HomeActivity).multiSelectMode,
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
                    (activity as HomeActivity).woList.add(id)
                    //notify all adapter that the multiselect is on
                    (activity as HomeActivity).viewModel.woListData.value =
                        (activity as HomeActivity).woList.size
                    //assign global list to list inside adapter so that adapter
                    //get the latest list update
                    billAdapter?.selectedList = (activity as HomeActivity).woList
//                    woList.add(id)
//                    (activity as HomeActivity).viewModel.woListData.value = woList.size
                }

                override fun removeFromWOList(id: Int) {
                    (activity as HomeActivity).woList.remove(id)
                    (activity as HomeActivity).viewModel.woListData.value =
                        (activity as HomeActivity).woList.size
                    billAdapter?.selectedList = (activity as HomeActivity).woList
//                    woList.remove(id)
//                    (activity as HomeActivity).viewModel.woListData.value = woList.size
                }

            }, true
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
            layoutManager = this@PendingFragment.layoutManager
            addOnScrollListener(onScroll)
            adapter = billAdapter
            showShimmerAdapter()
        }
        sharedviewModel.multiSelectState.observe(activity as HomeActivity, Observer {
//            billAdapter?.setMultiSelect(false)
//            dataBinding.pendingRecycler.showShimmerAdapter()
//            sharedviewModel.getPendingBill(PAGE, 6)
//            //billAdapter?.notifyDataSetChanged()
            billAdapter?.multiSelectMode = it
        })

        sharedviewModel.woListLiveData.observe(activity as HomeActivity, Observer {
            billAdapter?.selectedList = it.toMutableList()
        })

        sharedviewModel.getPendingBill(PAGE, 6)
        sharedviewModel.pendingBill.observe(this, Observer {
            mAllowLoadMore = if (it.isNotEmpty()) {
                dataBinding.pendingRecycler.hideShimmerAdapter()
                billAdapter?.dataSource?.addAll(it)
                billAdapter?.notifyDataSetChanged()
                true
            } else {
                false

            }

        })

        sharedviewModel.reloadLiveData.observe(activity as HomeActivity, Observer {
            dataBinding.pendingRecycler.showShimmerAdapter()
            sharedviewModel.getPendingBill(PAGE, 6)
        })

        sharedviewModel.resetSelection.observe(activity as HomeActivity, Observer {
            billAdapter?.notifyDataSetChanged()
        })
    }

    override fun setupToolbar(activity: AppCompatActivity) {
    }


//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//        Toast.makeText(context!!,"I AM HIDING", Toast.LENGTH_SHORT).show()
//    }
}