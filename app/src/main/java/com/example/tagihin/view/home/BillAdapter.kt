package com.example.tagihin.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tagihin.R
import com.example.tagihin.data.remote.model.Bill
import com.example.tagihin.databinding.BillItemLayoutBinding

class BillAdapter(
    var multiSelectMode: Boolean = false,
    var context: Context,
    var dataSource: MutableList<Bill>,
    val onClickListener: BillOnClickListener,
    val multiSelectSupport: Boolean = false
) : RecyclerView.Adapter<BillAdapter.BillViewHolder>() {
    var selectedList: MutableList<Int> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BillItemLayoutBinding.inflate(inflater, parent, false)
        return BillViewHolder(binding, onClickListener)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        holder.onBind(dataSource[position])
    }

    inner class BillViewHolder(
        val binding: BillItemLayoutBinding, val onClickListener: BillOnClickListener
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun onBind(bill: Bill) {
            with(binding) {
                billItemContainer.setOnLongClickListener {
                    if (selectedList.isEmpty()) {
                        if (multiSelectMode) {
                            onClickListener.removeFromWOList(bill.id)
                            setItemState(false, binding)
                        } else {
                            onClickListener.addToWOList(bill.id)
                            setItemState(true, binding)
                        }
                        notifyDataSetChanged()
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
                        if (selectedList.contains(bill.id)) {
                            onClickListener.removeFromWOList(bill.id)
                            setItemState(false, binding)
                        } else {
                            onClickListener.addToWOList(bill.id)
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
                if(multiSelectMode){
                    setItemState(selectedList.contains(bill.id), binding)
                }else{
                    setItemState(false, binding)
                }
                orderCode.text = bill.idpel
                name.text = bill.nama
                address.text = bill.alamat
                billDetailBtn.setOnClickListener {
                    onClickListener.onClick(bill)
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
}

interface BillOnClickListener {
    fun onClick(bill: Bill)
    fun addToWOList(id: Int)
    fun removeFromWOList(id: Int)
}