package com.example.tagihin.view.home.fragments.regularbill.fragments

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
import com.example.tagihin.databinding.FragmentUnpaidBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.detail.DetailBillActivity
import com.example.tagihin.view.home.BillAdapter
import com.example.tagihin.view.home.BillOnClickListener
import com.example.tagihin.view.home.HomeActivity
import com.example.tagihin.view.home.HomeViewModel

class UnpaidFragment : BaseFragment<HomeViewModel, FragmentUnpaidBinding>(HomeViewModel::class) {
    var billAdapter: BillAdapter? = null
    private var list: MutableList<Bill> = mutableListOf()
    val DETAIL = 66
    val layoutManager = LinearLayoutManager(activity)
    var PAGE = 0
    var mAllowLoadMore = true
    override fun getLayoutRes(): Int = R.layout.fragment_unpaid

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
                            it.putExtra("status", Consts.UNPAID)
                        }
                        , DETAIL)
                }

                override fun addToWOList(id: Int) {
                    (activity as HomeActivity).woList.add(id)
                    (activity as HomeActivity).viewModel.woListData.value =
                        (activity as HomeActivity).woList.size
                    billAdapter?.selectedList = (activity as HomeActivity).woList
                }

                override fun removeFromWOList(id: Int) {
                    (activity as HomeActivity).woList.remove(id)
                    (activity as HomeActivity).viewModel.woListData.value =
                        (activity as HomeActivity).woList.size
                    billAdapter?.selectedList = (activity as HomeActivity).woList
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
                            sharedviewModel.getUnpaidBill(PAGE, 6)
                            mAllowLoadMore = false
                        }
                    }
                }
            }
        }
        dataBinding.unpaidRecycler.apply {
            layoutManager = this@UnpaidFragment.layoutManager
            adapter = billAdapter
            addOnScrollListener(onScroll)
            showShimmerAdapter()
        }
        sharedviewModel.getUnpaidBill(PAGE, 6)
        sharedviewModel.unpaidBill.observe(this, Observer {
            dataBinding.unpaidRecycler.hideShimmerAdapter()
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
        sharedviewModel.multiSelectState.observe(activity as HomeActivity, Observer {
            //    billAdapter?.setMultiSelect(false)
//    dataBinding.unpaidRecycler.showShimmerAdapter()
//    sharedviewModel.getUnpaidBill(0,10)
            billAdapter?.multiSelectMode = it

            //billAdapter?.notifyDataSetChanged()
        })
        sharedviewModel.woListLiveData.observe(activity as HomeActivity, Observer {
            billAdapter?.selectedList = it.toMutableList()
        })
        sharedviewModel.reloadLiveData.observe(activity as HomeActivity, Observer {
            PAGE = 0
            dataBinding.unpaidRecycler.showShimmerAdapter()
            sharedviewModel.getUnpaidBill(PAGE, 6)
        })

        sharedviewModel.resetSelection.observe(activity as HomeActivity, Observer {
            billAdapter?.notifyDataSetChanged()
        })
    }

    override fun setupToolbar(activity: AppCompatActivity) {
    }


}