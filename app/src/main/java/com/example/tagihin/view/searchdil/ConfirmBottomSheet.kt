package com.example.tagihin.view.searchdil

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Build
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tagihin.R
import com.example.tagihin.data.remote.model.DilItemRequest
import com.example.tagihin.data.remote.model.DilItemResponse
import com.example.tagihin.databinding.ConfirmDilBtmSheetLayoutBinding
import com.example.tagihin.utils.Consts
import com.example.tagihin.utils.Consts.Companion.REQUEST_IMAGE
import com.example.tagihin.utils.ImagePickerActivity
import com.example.tagihin.widget.BottomSheet
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.io.IOException
import java.util.*


class ConfirmBottomSheet(val dilItem: DilItemResponse) : BottomSheet(),
    OnDateSetListener {
    var binding: ConfirmDilBtmSheetLayoutBinding? = null
    lateinit var datePickerDialog: DatePickerDialog
    lateinit var mActivity: SearchDilActivity
    var observer: Observer<Boolean>? = null
    val viewModel: SearchDilViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConfirmDilBtmSheetLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.getParcelableExtra<Uri>("path")
                val type = data?.getStringExtra("type")
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                    when (type) {
                        Consts.SIAGA -> {
                            viewModel.dilItemRequest.value?.foto_siaga = bitmap
                            Glide.with(this)
                                .load(uri.toString())
                                .apply(
                                    RequestOptions()
                                        .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .error(
                                            ColorDrawable(
                                                ContextCompat.getColor(
                                                    context!!,
                                                    R.color.greyTagihin
                                                )
                                            )
                                        )
                                )
                                .into(binding?.siagaImage!!)
                            binding?.siagaUploadPhotoCont?.visibility = View.GONE
                        }
                        Consts.RUSAK -> {
                            viewModel.dilItemRequest.value?.foto_rusak = bitmap
                            Glide.with(this)
                                .load(uri.toString())
                                .apply(
                                    RequestOptions()
                                        .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .error(
                                            ColorDrawable(
                                                ContextCompat.getColor(
                                                    context!!,
                                                    R.color.greyTagihin
                                                )
                                            )
                                        )
                                )
                                .into(binding?.rusakImage!!)
                            binding?.rusakUploadPhotoCont?.visibility = View.GONE
                        }
                        Consts.BANGUNAN -> {
                            viewModel.dilItemRequest.value?.foto_bangunan = bitmap
                            Glide.with(this)
                                .load(uri.toString())
                                .apply(
                                    RequestOptions()
                                        .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .error(
                                            ColorDrawable(
                                                ContextCompat.getColor(
                                                    context!!,
                                                    R.color.greyTagihin
                                                )
                                            )
                                        )
                                )
                                .into(binding?.bangunanImage!!)
                            binding?.bangunanUploadPhotoCont?.visibility = View.GONE
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as SearchDilActivity
        observer = Observer {
            mActivity.hideDialog()
            if (it) {
                mActivity.showMessage("Data berhasil diperbaharui")
                hideBtmSheet()
            } else {
                mActivity.showMessage("Data gagal diperbaharui")
            }
        }
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding?.confirmBtn?.setOnClickListener {
            viewModel.sendConfirmationForm()
            mActivity.showDialog()
        }
        viewModel.updateSuccess.observe(this, observer!!)
        viewModel.latLong.observe(this, Observer {
            if(it.first != 0.0 && it.second != 0.0 ){
                viewModel.latLongFinal.value = Pair<Double,Double>(it.first, it.second)
            }else{
                getLatLong()
            }
        })
        binding?.siagaPhoto?.setOnClickListener {
            checkPermission("Upload foto KwH meter siaga", Consts.SIAGA)
        }
        binding?.rusakPhoto?.setOnClickListener {
            checkPermission("Upload foto KwH meter rusak", Consts.RUSAK)
        }
        binding?.bangunanPhoto?.setOnClickListener {
            checkPermission("Upload foto bangunan", Consts.BANGUNAN)
        }
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
//        binding?.confirmBtn?.setOnClickListener {
//            mActivity.let {
//                it.showDialog()
//                it.saveResponse(billId)
//            }
//        }
//        mActivity.viewModel.let {
//            it.dateChange.observe(mActivity, Observer {
//                binding?.date?.text = it
//            }
//            )
//        }
    }

    fun launchCameraIntent(type: String) {
        var intent = Intent(context, ImagePickerActivity::class.java)
        var bundle = androidx.core.os.bundleOf(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION to ImagePickerActivity.REQUEST_IMAGE_CAPTURE,
            ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO to true,
            ImagePickerActivity.INTENT_ASPECT_RATIO_X to 16,
            ImagePickerActivity.INTENT_ASPECT_RATIO_Y to 9,
            "type" to type
        )
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    private fun launchGalleryIntent(type: String) {
        var intent = Intent(context, ImagePickerActivity::class.java)
        var bundle = androidx.core.os.bundleOf(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION to ImagePickerActivity.REQUEST_GALLERY_IMAGE,
            ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO to true,
            ImagePickerActivity.INTENT_ASPECT_RATIO_X to 16,
            ImagePickerActivity.INTENT_ASPECT_RATIO_Y to 9,
            "type" to type
        )
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    fun hideBtmSheet() {
        this.dismiss()
    }

    fun checkPermission(label: String, type: String) {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                @Override
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showImagePickerOptions(label, type)
                    }

                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

            }).check()
    }

    fun showSettingsDialog() {
        var builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
        builder.setTitle("Hak akses untuk merubah foto")
        builder.setMessage("Pika membutuhkan hak akses penyimpanan")
        builder.setPositiveButton("Buka Pengaturan") { dialog, which ->
            dialog?.cancel()
            openSettings()
        }
        builder.setNegativeButton("Batal") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    fun openSettings() {
        var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        var uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    fun showImagePickerOptions(label: String, type: String) {
        ImagePickerActivity.showImagePickerOptions(
            context!!,
            label,
            object : ImagePickerActivity.PickerOptionListener {
                @Override
                override fun onTakeCameraSelected() {
                    launchCameraIntent(type)
                }

                @Override
                override fun onChooseGallerySelected() {
                    launchGalleryIntent(type)
                }
            })
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        binding?.confirmBtn?.isEnabled = true
        viewModel.dilItemRequest.value?.tanggal =
            String.format("%d-%d-%d", year, month + 1, day)
        binding?.date?.text = viewModel.dilItemRequest.value?.tanggal
    }

    fun getLatLong(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    mActivity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    123
                )
            } else {
                val mFusedLocation = LocationServices.getFusedLocationProviderClient(mActivity)
                mFusedLocation.lastLocation.addOnSuccessListener(
                    mActivity
                ) { location ->
                    viewModel.latLongFinal.postValue(Pair<Double,Double>(location?.latitude!!,location.longitude))
                }
            }
        }
    }
}