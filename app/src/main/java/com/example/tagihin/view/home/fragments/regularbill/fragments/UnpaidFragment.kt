package com.example.tagihin.view.home.fragments.regularbill.fragments

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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

class UnpaidFragment : BaseFragment<HomeViewModel, FragmentUnpaidBinding>(HomeViewModel::class){
    var billAdapter : BillAdapter? = null
    private var list : List<Bill> = listOf()
    val DETAIL = 66
    override fun getLayoutRes(): Int = R.layout.fragment_unpaid

    override fun initView(view: View) {}

    override fun onViewReady() {
        billAdapter = BillAdapter(
            (activity as HomeActivity).multiSelectMode,
            context!!,
            list,
            object : BillOnClickListener{
                override fun onClick(bill: Bill) {
                    activity?.startActivityForResult(
                        Intent(activity, DetailBillActivity::class.java).let {
                            it.putExtra("bill",bill)
                            it.putExtra("status", Consts.UNPAID)
                        }
                        ,DETAIL)
                }

                override fun addToWOList(id: Int) {
                    (activity as HomeActivity).woList.add(id.toString())
                    (activity as HomeActivity).viewModel.woListData.value = (activity as HomeActivity).woList.size
                }

                override fun removeFromWOList(id: Int) {
                    (activity as HomeActivity).woList.remove(id.toString())
                    (activity as HomeActivity).viewModel.woListData.value = (activity as HomeActivity).woList.size
                }

            }, true
        )
        dataBinding.unpaidRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = billAdapter
            showShimmerAdapter()
        }
        sharedviewModel.getUnpaidBill(0,10)
        sharedviewModel.unpaidBill.observe(this, Observer {
            dataBinding.unpaidRecycler.hideShimmerAdapter()
            billAdapter?.dataSource = it
            billAdapter?.notifyDataSetChanged()

        })
sharedviewModel.multiSelectState.observe(activity as HomeActivity, Observer {
    billAdapter?.setMultiSelect(false)
})
        sharedviewModel.reloadLiveData.observe(activity as HomeActivity, Observer {
            dataBinding.unpaidRecycler.showShimmerAdapter()
            sharedviewModel.getUnpaidBill(0, 10)
        })
    }

    override fun setupToolbar(activity: AppCompatActivity) {
    }


}