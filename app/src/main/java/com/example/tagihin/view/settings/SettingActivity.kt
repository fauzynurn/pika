package com.example.tagihin.view.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.base.BaseActivityNoViewModel
import com.example.tagihin.data.remote.model.UnpaidBill
import com.example.tagihin.databinding.ActivityBillBinding
import com.example.tagihin.databinding.ActivitySettingsBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.bill.unpaid.fragments.UnpaidFragment
import com.example.tagihin.view.bill.unpaid.fragments.UnpaidSearchFragment
import com.example.tagihin.view.detail.DetailBillActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class SettingActivity :
    BaseActivity<SettingViewModel, ActivitySettingsBinding>(SettingViewModel::class) {
    override fun getLayoutRes(): Int = R.layout.activity_settings

    override fun showMessage(message: String) {
        Snackbar.make(dataBinding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun setupToolbar() {
        setSupportActionBar(dataBinding.toolbarMain)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        dataBinding.appbarTitle.text = "Pengaturan"
        dataBinding.resetWoContainer.setOnClickListener {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialog.setTitle("Apakah anda yakin?")
            alertDialog.setMessage("Data yang sudah dihapus tidak dapat dikembalikan")
            alertDialog.setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
            alertDialog.setPositiveButton("Ya") { _, _ ->
                showDialog()
                viewModel.resetWo()
            }
            alertDialog.show()
        }
        dataBinding.resetBillContainer.setOnClickListener {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialog.setTitle("Apakah anda yakin?")
            alertDialog.setMessage("Data yang sudah dihapus tidak dapat dikembalikan")
            alertDialog.setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
            alertDialog.setPositiveButton("Ya") { _, _ ->
                showDialog()
                viewModel.resetBill()
            }
            alertDialog.show()
        }

        viewModel.reset.observe(this, Observer {
            hideDialog()
            if (it) {
                showMessage("Data berhasil direset")
            } else {
                showMessage("Terdapat kesalahan saat mereset data")
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}