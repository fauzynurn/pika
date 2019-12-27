package com.example.tagihin.utils

import android.Manifest
import android.provider.OpenableColumns
import android.content.ContentResolver
import androidx.core.content.FileProvider.getUriForFile
import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import android.app.AlertDialog
import android.content.Context
import android.provider.MediaStore
import android.widget.Toast
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tagihin.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.yalantis.ucrop.UCrop
import timber.log.Timber
import java.io.File


class ImagePickerActivity : AppCompatActivity() {

    private var lockAspectRatio = false
    private var setBitmapMaxWidthHeight = false
    private var ASPECT_RATIO_X = 16
    private var ASPECT_RATIO_Y = 9
    private var bitmapMaxWidth = 1000
    private var bitmapMaxHeight = 1000
    private var IMAGE_COMPRESSION = 100
    private var type = ""

    interface PickerOptionListener {
        fun onTakeCameraSelected()

        fun onChooseGallerySelected()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)

        val intent = intent
        if (intent == null) {
            Toast.makeText(applicationContext, "Silakan Pilih Gambar", Toast.LENGTH_LONG)
                .show()
            return
        }
        type = intent.getStringExtra("type")!!
        ASPECT_RATIO_X = intent.getIntExtra(INTENT_ASPECT_RATIO_X, ASPECT_RATIO_X)
        ASPECT_RATIO_Y = intent.getIntExtra(INTENT_ASPECT_RATIO_Y, ASPECT_RATIO_Y)
        IMAGE_COMPRESSION = intent.getIntExtra(INTENT_IMAGE_COMPRESSION_QUALITY, IMAGE_COMPRESSION)
        lockAspectRatio = intent.getBooleanExtra(INTENT_LOCK_ASPECT_RATIO, false)
        setBitmapMaxWidthHeight = intent.getBooleanExtra(INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false)
        bitmapMaxWidth = intent.getIntExtra(INTENT_BITMAP_MAX_WIDTH, bitmapMaxWidth)
        bitmapMaxHeight = intent.getIntExtra(INTENT_BITMAP_MAX_HEIGHT, bitmapMaxHeight)

        val requestCode = intent.getIntExtra(INTENT_IMAGE_PICKER_OPTION, -1)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            takeCameraImage()
        } else {
            chooseImageFromGallery()
        }
    }

    private fun takeCameraImage() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        fileName = System.currentTimeMillis().toString() + ".jpg"
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT, getCacheImagePath(
                                fileName
                            )
                        )
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                            var resInfoList =
                                applicationContext.packageManager.queryIntentActivities(
                                    takePictureIntent,
                                    PackageManager.MATCH_DEFAULT_ONLY
                                )
                            for (resolveInfo in resInfoList) {
                                var packageName = resolveInfo.activityInfo.packageName;
                                applicationContext.grantUriPermission(
                                    packageName,
                                    getCacheImagePath(fileName),
                                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                                )
                            }
//                            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        if (takePictureIntent.resolveActivity(packageManager) != null) {
                            startActivityForResult(
                                takePictureIntent,
                                REQUEST_IMAGE_CAPTURE
                            )
                        }
                    }
                }
            }).check()
    }

    private fun chooseImageFromGallery() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val pickPhoto = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(
                            pickPhoto,
                            REQUEST_GALLERY_IMAGE
                        )
                    }
                }
            }).check()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.e("RequestCode $requestCode : $resultCode")
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> if (resultCode == Activity.RESULT_OK) {
                cropImage(getCacheImagePath(fileName))
            } else {
                setResultCancelled()
            }
            REQUEST_GALLERY_IMAGE -> if (resultCode == Activity.RESULT_OK) {
                val imageUri = data!!.data
                cropImage(imageUri)
            } else {
                setResultCancelled()
            }
            UCrop.REQUEST_CROP -> if (resultCode == Activity.RESULT_OK) {
                handleUCropResult(data)
            } else {
                setResultCancelled()
            }
            UCrop.RESULT_ERROR -> {
                data?.let {
                    val cropError = UCrop.getError(data)
                    Timber.e("Crop error: $cropError")
                    setResultCancelled()
                }
            }
            else -> setResultCancelled()
        }
    }

    private fun cropImage(sourceUri: Uri?) {
        val destinationUri = Uri.fromFile(
            File(
                cacheDir,
                queryName(
                    contentResolver,
                    sourceUri
                )
            )
        )
        val options = UCrop.Options()
//        options.setCompressionQuality(IMAGE_COMPRESSION)
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary))

//        if (lockAspectRatio)
        options.withAspectRatio(ASPECT_RATIO_X.toFloat(), ASPECT_RATIO_Y.toFloat())
//
//        if (setBitmapMaxWidthHeight)
//            options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight)

        sourceUri?.let {
            UCrop.of(it, destinationUri)
                .withOptions(options)
                .start(this)
        }
    }

    private fun handleUCropResult(data: Intent?) {
        if (data == null) {
            setResultCancelled()
            return
        }
        val resultUri = UCrop.getOutput(data)
        resultUri?.let { setResultOk(it) }
    }

    private fun setResultOk(imagePath: Uri) {
        val intent = Intent()
        intent.putExtra("type", type)
        intent.putExtra("path", imagePath)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setResultCancelled() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    private fun getCacheImagePath(fileName: String): Uri {
        val path = File(externalCacheDir, "camera")
        if (!path.exists()) path.mkdirs()
        val image = File(path, fileName)
        return getUriForFile(this@ImagePickerActivity, "${packageName}.provider", image)
    }

    companion object {
        private val TAG = ImagePickerActivity::class.java.simpleName
        val INTENT_IMAGE_PICKER_OPTION = "image_picker_option"
        val INTENT_ASPECT_RATIO_X = "aspect_ratio_x"
        val INTENT_ASPECT_RATIO_Y = "aspect_ratio_Y"
        val INTENT_LOCK_ASPECT_RATIO = "lock_aspect_ratio"
        val INTENT_IMAGE_COMPRESSION_QUALITY = "compression_quality"
        val INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT = "set_bitmap_max_width_height"
        val INTENT_BITMAP_MAX_WIDTH = "max_width"
        val INTENT_BITMAP_MAX_HEIGHT = "max_height"


        val REQUEST_IMAGE_CAPTURE = 0
        val REQUEST_GALLERY_IMAGE = 1
        lateinit var fileName: String

        fun showImagePickerOptions(
            context: Context,
            title: String,
            listener: PickerOptionListener
        ) {
            // setup the alert builder
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)

            // add a list

            val animals = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                arrayOf<String>(
                    "Galeri"
                )
            } else {
                arrayOf(
                    "Kamera",
                    "Galeri"
                )
            }
            builder.setItems(animals) { dialog, which ->

                if (animals.size == 1) {
                    when (which) {
                        0 -> listener.onChooseGallerySelected()
                    }
                } else {
                    when (which) {
                        0 -> listener.onTakeCameraSelected()
                        1 -> listener.onChooseGallerySelected()
                    }
                }
            }

            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }

        private fun queryName(resolver: ContentResolver, uri: Uri?): String {
            val returnCursor = resolver.query(uri!!, null, null, null, null)!!
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            val name = returnCursor.getString(nameIndex)
            returnCursor.close()
            return name
        }

        /**
         * Calling this will delete the images from cache directory
         * useful to clear some memory
         */
        fun clearCache(context: Context) {
            val path = File(context.externalCacheDir, "camera")
            if (path.exists() && path.isDirectory) {
                for (child in path.listFiles()) {
                    child.delete()
                }
            }
        }
    }
}