package com.example.tagihin.view.home.fragments.regularbill.fragments

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    var PAGE = 0
    var mAllowLoadMore = true
    val layoutManager = LinearLayoutManager(activity)
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
                            sharedviewModel.getPaidBill(PAGE, 6)
                            mAllowLoadMore = false
                        }
                    }
                }
            }
        }
        dataBinding.paidRecycler.apply {
            layoutManager = this@PaidFragment.layoutManager
            adapter = billAdapter
            addOnScrollListener(onScroll)
            showShimmerAdapter()
        }
        sharedviewModel.getPaidBill(PAGE, 6)
        sharedviewModel.paidBill.observe(this, Observer {
            dataBinding.paidRecycler.hideShimmerAdapter()
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
        sharedviewModel.reloadLiveData.observe(activity as HomeActivity, Observer {
            PAGE = 0
            dataBinding.paidRecycler.showShimmerAdapter()
            sharedviewModel.getPaidBill(PAGE, 6)
        })
    }

    override fun setupToolbar(activity: AppCompatActivity) {
    }


}