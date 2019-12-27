package com.example.tagihin.data.remote.repository

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Environment
import com.example.tagihin.TagihinApp
import com.example.tagihin.data.local.TagihinDb
import com.example.tagihin.data.remote.ApiService
import com.example.tagihin.data.remote.model.DilItemValidationRequest
import com.example.tagihin.data.remote.model.DilResponse
import com.example.tagihin.data.remote.model.GeneralResponse
import com.example.tagihin.utils.PreferencesHelper
import io.reactivex.Maybe
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executor

class SearchDilRepository(
    val executor: Executor,
    val db: TagihinDb,
    val pref: PreferencesHelper,
    val apiService: ApiService
) {
    @SuppressLint("CheckResult")
    fun searchDil(query: String): Maybe<Response<DilResponse>> {
        return apiService.searchDil(
            pref.getUsername()!!, query, 3
        )
    }

    fun validateDil(
        dilValidate: DilItemValidationRequest
    ) : Maybe<Response<GeneralResponse>> {
        return apiService.validateDil(
            pref.getUsername()!!,pref.getPrivilege(),
            dilValidate.id,
            dilValidate.tanggal_validasi,
            dilValidate.cabut_siaga,
            dilValidate.jumlah_kwh,
            dilValidate.tagihan,
            dilValidate.tarif
        )
    }

    private fun createTempFile(bitmap: Bitmap): File {
        val file = File(
            TagihinApp.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + "_image.jpg"
        )
        val bos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapdata = bos.toByteArray()

        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
    }

    fun uploadConfirmation(
        id: String,
        idpel: String,
        nama: String,
        alamat: String,
        tarif: String,
        daya: String,
        tanggal: String,
        meterRusak: String,
        meterSiaga: String,
        pasangSiaga: String,
        noHp: String,
        korX: String,
        korY: String,
        fotoSiaga: Bitmap,
        fotoRusak: Bitmap,
        fotoBangunan: Bitmap
    ): Maybe<Response<GeneralResponse>> {
        val siagaFile = createTempFile(fotoSiaga)
        val rusakFile = createTempFile(fotoRusak)
        val bangunanFile = createTempFile(fotoBangunan)
        var reqFile = RequestBody.create(MediaType.parse("image/*"), siagaFile)
        val finalSiaga = MultipartBody.Part.createFormData("foto_siaga", siagaFile.name, reqFile)

        reqFile = RequestBody.create(MediaType.parse("image/*"), rusakFile)
        val finalRusak = MultipartBody.Part.createFormData("foto_rusak", rusakFile.name, reqFile)

        reqFile = RequestBody.create(MediaType.parse("image/*"), bangunanFile)
        val finalBangunan =
            MultipartBody.Part.createFormData("foto_bangunan", bangunanFile.name, reqFile)
        return apiService.uploadConfirmation(
            RequestBody.create(MediaType.parse("text/plain"), pref.getUsername()!!),
            RequestBody.create(MediaType.parse("text/plain"), 3.toString()),
            RequestBody.create(MediaType.parse("text/plain"), id),
            RequestBody.create(MediaType.parse("text/plain"), idpel),
            RequestBody.create(MediaType.parse("text/plain"), nama),
            RequestBody.create(MediaType.parse("text/plain"), alamat),
            RequestBody.create(MediaType.parse("text/plain"), tarif),
            RequestBody.create(MediaType.parse("text/plain"), daya),
            RequestBody.create(MediaType.parse("text/plain"), tanggal),
            RequestBody.create(MediaType.parse("text/plain"), meterRusak),
            RequestBody.create(MediaType.parse("text/plain"), meterSiaga),
            RequestBody.create(MediaType.parse("text/plain"), pasangSiaga),
            RequestBody.create(MediaType.parse("text/plain"), noHp),
            RequestBody.create(MediaType.parse("text/plain"), korX),
            RequestBody.create(MediaType.parse("text/plain"), korY),
            finalSiaga,
            finalRusak,
            finalBangunan
        )
    }
}
