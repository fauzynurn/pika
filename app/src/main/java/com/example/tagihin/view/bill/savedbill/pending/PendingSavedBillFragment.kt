package com.example.tagihin.view.bill.savedbill.pending

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tagihin.R
import com.example.tagihin.base.BaseFragment
import com.example.tagihin.data.remote.model.PendingWorkOrder
import com.example.tagihin.data.remote.model.TempBill
import com.example.tagihin.databinding.FragmentPendingBinding
import com.example.tagihin.view.bill.savedbill.SavedBillActivity
import com.example.tagihin.view.bill.savedbill.SavedBillAdapter
import com.example.tagihin.view.workorder.WorkOrderActivity
import com.example.tagihin.view.bill.savedbill.SavedBillViewModel

class PendingSavedBillFragment : BaseFragment<SavedBillViewModel, FragmentPendingBinding>(
    SavedBillViewModel::class){
    var billAdapter: SavedBillAdapter? = null
    val layoutManager = LinearLayoutManager(activity)
    override fun getLayoutRes(): Int = R.layout.fragment_pending

    override fun initView(view: View) {}

    override fun onViewReady() {
        billAdapter = SavedBillAdapter(
            activity as SavedBillActivity,
            object : DiffUtil.ItemCallback<TempBill>() {
                override fun areItemsTheSame(
                    oldItem: TempBill,
                    newItem: TempBill
                ): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: TempBill,
                    newItem: TempBill
                ): Boolean =
                    oldItem == newItem
            }

        )
        dataBinding.pendingRecycler.apply {
            layoutManager = LinearLayoutManager(activity as SavedBillActivity)
            adapter = billAdapter
        }
        viewModel.shouldTriggerSomething.value = "changed"
        viewModel._savedPending.observe(this, Observer {
            billAdapter?.submitList(it)
        })
    }

    override fun setupToolbar(activity: AppCompatActivity) {
    }
}