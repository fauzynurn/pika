package com.example.tagihin.view.bill.unpaid

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
import com.example.tagihin.data.remote.model.UnpaidBill
import com.example.tagihin.databinding.BillItemLayoutBinding
import com.example.tagihin.view.home.viewholder.NetworkStateViewHolder

class UnpaidBillAdapter(
    var multiSelectMode: Boolean = false,
    var context: Context,
    val onClickListener: BillOnClickListener,
    private val retryCallback: () -> Unit,
    val multiSelectSupport: Boolean = false,
    diffCallback: DiffUtil.ItemCallback<UnpaidBill>
) : PagedListAdapter<UnpaidBill, RecyclerView.ViewHolder>(diffCallback) {
    var selectedList: ArrayList<Int> = arrayListOf()
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
        val binding: BillItemLayoutBinding, val onClickListener: BillOnClickListener
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun onBind(bill: UnpaidBill) {
            with(binding) {
                billItemContainer.setOnLongClickListener {
                    if (selectedList.isEmpty()) {
                        if (multiSelectMode) {
                            onClickListener.removeFromWOList(bill.id.toInt())
                            setItemState(false, binding)
                        } else {
                            onClickListener.addToWOList(bill.id.toInt())
                            setItemState(true, binding)
                        }
                        //notifyDataSetChanged()
//                        setItemState(bill, binding)
//                        if(bill.isSelected){
//                            onClickListener.addToWOList(bill.id)
//                        }else{
//                            onClickListener.removeFromWOList(bill.id)
//                        }
                    }
                    true
                }
                billItemContainer.setOnClickListener {
                    if (selectedList.isNotEmpty()) {
                        if (selectedList.contains(bill.id.toInt())) {
                            onClickListener.removeFromWOList(bill.id.toInt())
                            setItemState(false, binding)
                        } else {
                            onClickListener.addToWOList(bill.id.toInt())
                            setItemState(true, binding)
                        }
//                            setItemState(bill, binding)
//                            if(bill.isSelected){
//                                onClickListener.addToWOList(bill.id)
//                            }else{
//                                onClickListener.removeFromWOList(bill.id)
//                            }
                    }
                }
                woLabel.visibility = if(bill.status == 1) View.VISIBLE else View.GONE
                orderCode.text = bill.idpel
                name.text = bill.nama
                address.text = bill.alamat
                billDetailBtn.setOnClickListener {
                    onClickListener.onClick(bill)
                }
                if (multiSelectMode) {
                    setItemState(selectedList.contains(bill.id.toInt()), binding)
                } else {
                    setItemState(false, binding)
                }
            }
        }
    }

    fun setMultiSelect(isEnabled: Boolean) {
        multiSelectMode = isEnabled
    }

    fun setItemState(selected: Boolean, binding: BillItemLayoutBinding) {
        with(binding) {
            if (selected) {
                billItemContainer.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
                orderCode.setTextColor(ContextCompat.getColor(context, R.color.white))
                name.setTextColor(ContextCompat.getColor(context, R.color.white))
                address.setTextColor(ContextCompat.getColor(context, R.color.white))
                billDetailBtn.visibility = View.GONE
            } else {
                billItemContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                orderCode.setTextColor(ContextCompat.getColor(context, R.color.black))
                name.setTextColor(ContextCompat.getColor(context, R.color.black))
                address.setTextColor(ContextCompat.getColor(context, R.color.greyTagihin))
                billDetailBtn.visibility = View.VISIBLE
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

interface BillOnClickListener{
    fun onClick(bill: UnpaidBill)
    fun addToWOList(id: Int)
    fun removeFromWOList(id: Int)
}