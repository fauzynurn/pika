package com.example.tagihin.view.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.base.BaseActivityNoViewModel
import com.example.tagihin.data.remote.model.Bill
import com.example.tagihin.databinding.ActivityDetailBillBinding
import com.example.tagihin.databinding.ChangeStatusLayoutBinding
import com.example.tagihin.utils.Consts

class DetailBillActivity : BaseActivity<BillViewModel,ActivityDetailBillBinding>(BillViewModel::class){
    override fun getLayoutRes(): Int = R.layout.activity_detail_bill
    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun initView(savedInstanceState: Bundle?) {
        val bill = intent.getSerializableExtra("bill") as Bill
        val statusString = intent.getStringExtra("status")
        with(dataBinding){
            customerId.text = bill.idpel
            status.text = statusString
            amountMoney.text = String.format("Rp%s",bill.total)
            name.text = bill.nama
            address.text = bill.alamat
            rbm.text = bill.rbm
            date.text = bill.tanggal
            note.text = if(bill.catatan == null) "Tidak ada catatan" else bill.catatan
        }

        doSpecificStyling(statusString)
        dataBinding.changeStatusBtn.setOnClickListener {
            val btmSheet = ChangeStatusBottomSheet(bill.id, statusString)
            btmSheet.show(supportFragmentManager, btmSheet.tag)
        }

    }

    fun setViewModel(binding : ChangeStatusLayoutBinding){
        binding.viewModel = viewModel
    }

    fun doSpecificStyling(statusString: String){
        when(statusString){
            Consts.PAID -> {
                dataBinding.amountMoneyLabel.text = "Jumlah lunas"
                dataBinding.dateLabel.text = "Tanggal lunas"
                dataBinding.status.setBackgroundColor(ContextCompat.getColor(this, R.color.greenTagihin))
                dataBinding.changeStatusBtn.visibility = View.GONE
            }

            Consts.PENDING -> {
                dataBinding.amountMoneyLabel.text = "Jumlah harus dibayar"
                dataBinding.dateLabel.text = "Tanggal harus bayar"
                dataBinding.status.setBackgroundColor(ContextCompat.getColor(this, R.color.yellowTagihin))
            }

            Consts.UNPAID -> {
                dataBinding.amountMoneyLabel.text = "Jumlah harus dibayar"
                dataBinding.dateContainer.visibility = View.GONE
                dataBinding.status.setBackgroundColor(ContextCompat.getColor(this, R.color.redTagihin))
            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
    }

    override fun setupToolbar() {
        setSupportActionBar(dataBinding.toolbarMain)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        dataBinding.appbarTitle.text = "Detail Tagihan"

    }

}