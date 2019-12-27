package com.example.tagihin.view.searchdil

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tagihin.R
import com.example.tagihin.data.remote.model.DilItemRequest
import com.example.tagihin.data.remote.model.DilItemResponse
import com.example.tagihin.databinding.ConfirmDilBtmSheetLayoutBinding
import com.example.tagihin.databinding.ValidateDilBtmSheetLayoutBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.utils.Consts.Companion.REQUEST_IMAGE
import com.example.tagihin.utils.ImagePickerActivity
import com.example.tagihin.widget.BottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.IOException
import java.util.*


class ValidateBottomSheet : BottomSheet(),
    OnDateSetListener {
    var binding: ValidateDilBtmSheetLayoutBinding? = null
    lateinit var datePickerDialog: DatePickerDialog
    lateinit var mActivity: SearchDilActivity
    var observer : Observer<Boolean>? = null
    val viewModel : SearchDilViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ValidateDilBtmSheetLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer = Observer {
            mActivity.hideDialog()
            if (it) {
                mActivity.showMessage("Data berhasil diperbaharui")
                hideBtmSheet()
            } else {
                mActivity.showMessage("Data gagal diperbaharui")
            }
        }
        mActivity = activity as SearchDilActivity
        binding?.viewModel = viewModel
        //viewModel.dilItem.value = dilItem
        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding?.validateBtn?.setOnClickListener {
            viewModel.sendValidationForm()
            mActivity.showDialog()
        }
        viewModel.updateSuccess.observe(this, observer!!)

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
    }

    fun hideBtmSheet() {
        this.dismiss()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        binding?.validateBtn?.isEnabled = true
        viewModel.dilValidate.tanggal_validasi =
            String.format("%d-%d-%d", year, month + 1, day)
        //binding?.date?.text = viewModel.dilItemRequest.value?.tanggal
    }
}