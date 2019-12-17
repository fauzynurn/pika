package com.example.tagihin.view.bill.savedbill

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tagihin.R
import com.example.tagihin.data.remote.NetworkState
import com.example.tagihin.data.remote.model.PendingWorkOrder
import com.example.tagihin.data.remote.model.TempBill
import com.example.tagihin.databinding.BillItemLayoutBinding
import com.example.tagihin.databinding.SavedBillItemLayoutBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.home.viewholder.NetworkStateViewHolder
import com.example.tagihin.view.workorder.pending.PendingWorkOrderOnClickListener

class SavedBillAdapter(
    var context: Context,
    diffCallback: DiffUtil.ItemCallback<TempBill>
) : PagedListAdapter<TempBill, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = SavedBillItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SavedBillViewHolder(view)
    }

    inner class SavedBillViewHolder(
        val binding: SavedBillItemLayoutBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun onBind(bill: TempBill) {
            with(binding) {
                orderCode.text = bill.orderCode
                name.text = bill.name
                addedDate.text = bill.dateAdded
                statusAfter.text = bill.status1
                when(bill.status1){
                    Consts.PAID -> statusAfterCv.setCardBackgroundColor(ContextCompat.getColor(context,R.color.greenTagihin))
                    Consts.PENDING_ENG -> statusAfterCv.setCardBackgroundColor(ContextCompat.getColor(context,R.color.yellowTagihin))
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            (holder as SavedBillViewHolder).onBind(
                it
            )
        }
    }
}