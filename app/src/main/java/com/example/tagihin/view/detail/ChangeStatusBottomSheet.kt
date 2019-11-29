package com.example.tagihin.view.detail

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.tagihin.databinding.ChangeStatusLayoutBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.utils.Consts.Companion.OPTIONS
import com.example.tagihin.widget.BottomSheet
import java.util.*


class ChangeStatusBottomSheet(val billId: Int, var statusString: String) : BottomSheet(),
    OnDateSetListener {
    var binding: ChangeStatusLayoutBinding? = null
    lateinit var datePickerDialog: DatePickerDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChangeStatusLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(
            context!!,
            this,
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.datePicker.minDate = Date().time
        binding?.datePickerBtn?.setOnClickListener {
            datePickerDialog.show()
        }
        (activity as DetailBillActivity).setViewModel(binding!!)
        if (statusString == Consts.PENDING) {
            binding?.paidOffText?.visibility = View.VISIBLE
            binding?.statusDropdown?.visibility = View.GONE
            binding?.dateContainer?.visibility = View.GONE
        }
        val adapter = ArrayAdapter<String>(
            context!!, android.R.layout.simple_dropdown_item_1line, OPTIONS
        )
        binding?.statusDropdown?.setAdapter(adapter)
        binding?.confirmBtn?.setOnClickListener {
            (activity as DetailBillActivity).let {
                it.showDialog()
                it.viewModel.updateBill(billId)
            }
        }
        (activity as DetailBillActivity).viewModel.let {
            it.status.observe(
                this, Observer {
                    if (it == "LUNAS") {
                        binding?.dateContainer?.visibility = View.GONE
                    } else {
                        binding?.dateContainer?.visibility = View.VISIBLE
                    }
                }
            )
            it.updateBill.observe(activity as DetailBillActivity, Observer {
                (activity as DetailBillActivity).apply {
                    this.hideDialog()
                    this.finish()
                    Toast.makeText(this, "Data tagihan berhasil diubah", Toast.LENGTH_SHORT).show()
                }
            })
            it.dateChange.observe(activity as DetailBillActivity, Observer {
                binding?.date?.text = it
            }
            )
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        binding?.confirmBtn?.isEnabled = true
        (activity as DetailBillActivity).viewModel.dateChange.value =
            String.format("%d/%d/%d", day, month, year)
    }
}