package com.example.tagihin.view.bill.paid

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
import com.example.tagihin.data.remote.model.PaidBill
import com.example.tagihin.databinding.BillItemLayoutBinding
import com.example.tagihin.view.home.viewholder.NetworkStateViewHolder

class PaidBillAdapter(
    var context: Context,
    val onClickListener: PaidBillOnClickListener,
    private val retryCallback: () -> Unit,
    diffCallback: DiffUtil.ItemCallback<PaidBill>
) : PagedListAdapter<PaidBill, RecyclerView.ViewHolder>(diffCallback) {
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.bill_item_layout -> {
                val view = BillItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return BillViewHolder(view, onClickListener)
            }
            R.layout.network_state_item -> {
                NetworkStateViewHolder.create(parent, retryCallback)
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + if (hasExtraRow()) 1 else 0

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.bill_item_layout
        }
    }

    inner class BillViewHolder(
        val binding: BillItemLayoutBinding, val onClickListener: PaidBillOnClickListener
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun onBind(bill: PaidBill) {
            with(binding) {
                woLabel.visibility = View.GONE
                orderCode.text = bill.idpel
                name.text = bill.nama
                address.text = bill.alamat
                billDetailBtn.setOnClickListener {
                    onClickListener.onClick(bill)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.bill_item_layout -> getItem(position)?.let {
                (holder as BillViewHolder).onBind(
                    it
                )
            }
            R.layout.network_state_item -> (holder as NetworkStateViewHolder).onBind(
                networkState
            )
        }
    }
}

interface PaidBillOnClickListener{
    fun onClick(bill: PaidBill)
}