package com.example.tagihin.view.home.fragments.regularbill.fragments

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tagihin.R
import com.example.tagihin.base.BaseFragment
import com.example.tagihin.data.remote.model.Bill
import com.example.tagihin.databinding.FragmentPendingBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.detail.DetailBillActivity
import com.example.tagihin.view.home.BillAdapter
import com.example.tagihin.view.home.BillOnClickListener
import com.example.tagihin.view.home.HomeActivity
import com.example.tagihin.view.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class PendingFragment : BaseFragment<HomeViewModel, FragmentPendingBinding>(HomeViewModel::class) {
    var billAdapter: BillAdapter? = null
    private var list: List<Bill> = listOf()
    val DETAIL = 66
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
                        },DETAIL
                    )
                }

                override fun addToWOList(id: Int) {
                    (activity as HomeActivity).woList.add(id.toString())
                    (activity as HomeActivity).viewModel.woListData.value = (activity as HomeActivity).woList.size
//                    woList.add(id)
//                    (activity as HomeActivity).viewModel.woListData.value = woList.size
                }

                override fun removeFromWOList(id: Int) {
                    (activity as HomeActivity).woList.remove(id.toString())
                    (activity as HomeActivity).viewModel.woListData.value = (activity as HomeActivity).woList.size
//                    woList.remove(id)
//                    (activity as HomeActivity).viewModel.woListData.value = woList.size
                }

            }, true
        )
        dataBinding.pendingRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = billAdapter
            showShimmerAdapter()
        }
        sharedviewModel.multiSelectState.observe(activity as HomeActivity, Observer {
            billAdapter?.setMultiSelect(false)
        })

        sharedviewModel.getPendingBill(0, 10)
        sharedviewModel.pendingBill.observe(this, Observer {
            dataBinding.pendingRecycler.hideShimmerAdapter()
            billAdapter?.dataSource = it
            billAdapter?.notifyDataSetChanged()

        })

        sharedviewModel.reloadLiveData.observe(activity as HomeActivity, Observer {
            dataBinding.pendingRecycler.showShimmerAdapter()
            sharedviewModel.getPendingBill(0, 10)
        })
    }

    override fun setupToolbar(activity: AppCompatActivity) {
    }


//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//        Toast.makeText(context!!,"I AM HIDING", Toast.LENGTH_SHORT).show()
//    }
}