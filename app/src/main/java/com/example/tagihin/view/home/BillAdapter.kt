package com.example.tagihin.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tagihin.data.remote.model.Bill
import com.example.tagihin.databinding.BillItemLayoutBinding

class BillAdapter(
    var dataSource: List<Bill>,
    val onClickListener: BillOnClickListener
) : RecyclerView.Adapter<BillViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BillItemLayoutBinding.inflate(inflater, parent, false)
        return BillViewHolder(binding, onClickListener)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        holder.onBind(dataSource[position])
    }

}

interface BillOnClickListener {
    fun onClick(bill: Bill)
}

class BillViewHolder(
    val binding: BillItemLayoutBinding, val onClickListener: BillOnClickListener
) : RecyclerView.ViewHolder(
    binding.root
) {
    fun onBind(bill: Bill) {
        with(binding) {
            orderCode.text = bill.idpel
            name.text = bill.nama
            address.text = bill.alamat
            billDetailBtn.setOnClickListener {
                onClickListener.onClick(bill)
            }
        }
    }
}