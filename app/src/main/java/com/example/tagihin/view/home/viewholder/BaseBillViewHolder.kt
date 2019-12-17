//package com.example.tagihin.view.home.viewholder
//
//import androidx.recyclerview.widget.RecyclerView
//import com.example.tagihin.databinding.BillItemLayoutBinding
//import com.example.tagihin.view.bill.unpaid.BillOnClickListener
//
//abstract class BaseBillViewHolder<T>(
//    val binding: BillItemLayoutBinding, val onClickListener: BillOnClickListener<T>
//) : RecyclerView.ViewHolder(
//    binding.root
//) {
//    fun onBinding(bill: T){
//        with(binding) {
//            orderCode.text = bill.idpel
//            name.text = bill.nama
//            address.text = bill.alamat
//            billDetailBtn.setOnClickListener {
//                onClickListener.onClick(bill)
//            }
//        }
//    }
//}