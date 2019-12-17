//package com.example.tagihin.view.search
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.tagihin.R
//import com.example.tagihin.base.BaseActivity
//import com.example.tagihin.data.remote.model.Bill
//import com.example.tagihin.databinding.ActivitySearchBinding
//import com.example.tagihin.utils.Consts
//import com.example.tagihin.view.detail.DetailBillActivity
//import com.example.tagihin.view.home.BillAdapter
//import com.example.tagihin.view.bill.unpaid.BillOnClickListener
//import com.jakewharton.rxbinding2.widget.RxTextView
//import io.reactivex.disposables.Disposable
//import java.util.concurrent.TimeUnit
//
//class SearchActivity : BaseActivity<SearchViewModel, ActivitySearchBinding>(SearchViewModel::class) {
//    private var billAdapter: BillAdapter? = null
//    private var list: MutableList<Bill> = mutableListOf()
//    private var textChangeListener : Disposable? = null
//    override fun getLayoutRes(): Int = R.layout.activity_search
//
//    override fun showMessage(message: String) {
//        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
//    }
//
//    override fun initView(savedInstanceState: Bundle?) {
//        dataBinding.backIcon.setOnClickListener {
//            finish()
//            textChangeListener?.dispose()
//        }
//        billAdapter = BillAdapter(
//            false,
//            this,
//            list,
//            object : BillOnClickListener {
//                override fun onClick(bill: Bill) {
//                    startActivity(
//                        Intent(this@SearchActivity, DetailBillActivity::class.java).let {
//                            it.putExtra("bill",bill)
//                            it.putExtra("status", bill.keterangan)
//                        }
//                    )
//                }
//
//                override fun addToWOList(id: Int) {
//
//                }
//
//                override fun removeFromWOList(id: Int) {
//
//                }
//
//            }
//        )
//
//        textChangeListener = RxTextView.textChanges(dataBinding.searchBox)
//            .debounce(550, TimeUnit.MILLISECONDS)
//            .map {
//                it.toString()
//            }
//            .subscribe {
//                if (it.isNotEmpty()) {
//                    runOnUiThread {
//                    dataBinding.searchRecycler.showShimmerAdapter()
//                    }
//                    viewModel.search(it)
//                }
//            }
//        dataBinding.searchRecycler.apply {
//            layoutManager = LinearLayoutManager(this@SearchActivity)
//            adapter = billAdapter
//        }
//        viewModel.searchResult.observe(this, Observer {
//            dataBinding.searchRecycler.hideShimmerAdapter()
//            billAdapter?.dataSource?.clear()
//            billAdapter?.dataSource?.addAll(it)
//            billAdapter?.notifyDataSetChanged()
//        })
//    }
//
//    override fun setupToolbar() {}
//
//}