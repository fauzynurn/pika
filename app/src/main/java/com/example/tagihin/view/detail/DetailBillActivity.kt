package com.example.tagihin.view.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.base.BaseActivityNoViewModel
import com.example.tagihin.data.remote.model.BaseBill
import com.example.tagihin.data.remote.model.UnpaidBill
import com.example.tagihin.databinding.ActivityDetailBillBinding
import com.example.tagihin.databinding.ChangeStatusLayoutBinding
import com.example.tagihin.utils.Consts

class DetailBillActivity : BaseActivity<BillViewModel,ActivityDetailBillBinding>(BillViewModel::class){
    override fun getLayoutRes(): Int = R.layout.activity_detail_bill
    lateinit var bill : BaseBill
    var statusString = ""
    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun initView(savedInstanceState: Bundle?) {
        bill = intent.getSerializableExtra("bill") as BaseBill
        statusString = intent.getStringExtra("status")!!
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
        viewModel.updateBill.observe(this, Observer {
                hideDialog()
                setResult(Activity.RESULT_OK)
                finish()
                Toast.makeText(this, "Data tagihan berhasil diubah", Toast.LENGTH_SHORT).show()
        })
        viewModel.localUpdateBill.observe(this, Observer {
            hideDialog()
            //setResult(Activity.RESULT_OK)
            finish()
            showToast("Permintaan perubahan data berhasil disimpan")
        })
        doSpecificStyling(statusString)
        dataBinding.changeStatusBtn.setOnClickListener {
            val btmSheet = ChangeStatusBottomSheet(bill.id.toInt(), statusString)
            btmSheet.show(supportFragmentManager, btmSheet.tag)
        }

    }

    fun setViewModel(binding : ChangeStatusLayoutBinding){
        binding.viewModel = viewModel
    }

    fun showToast(message: String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    fun saveResponse(id : Int){
        viewModel.updateBill(id,bill.nama, bill.idpel, statusString)
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

            Consts.UNPAID_SHORT -> {
                dataBinding.amountMoneyLabel.text = "Jumlah harus dibayar"
                dataBinding.dateContainer.visibility = View.GONE
                dataBinding.status.text = Consts.UNPAID
                dataBinding.status.setBackgroundColor(ContextCompat.getColor(this, R.color.redTagihin))
            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                setResult(Activity.RESULT_CANCELED)
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