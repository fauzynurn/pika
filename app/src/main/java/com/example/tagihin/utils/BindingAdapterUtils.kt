package com.example.tagihin.utils

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseMethod
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tagihin.R
import com.example.tagihin.data.remote.model.DilItemValidationRequest
import com.example.tagihin.utils.Converters.convertToMoney
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("imageUrl")
fun loadImage(view : ImageView, url : String?){
    if(url != null) {
        val circularProgressDrawable = CircularProgressDrawable(view.context)
        circularProgressDrawable.setColorFilter(ContextCompat.getColor(view.context, R.color.fadeBlue), PorterDuff.Mode.SRC_IN )
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 25f
        circularProgressDrawable.start()
        Glide.with(view.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(circularProgressDrawable)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(
                        ColorDrawable(
                            ContextCompat.getColor(
                                view.context,
                                R.color.greyTagihin
                            )
                        )
                    )
            )
            .into(view)
    }
}

@BindingAdapter("visibility")
fun View.setVisibility(value : String?){
    this.visibility = if(value == "TTT" || value == null) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

//@BindingAdapter("setText")
//fun TextInputEditText.applyText(value : String){
//    if(this.text.toString().isNotEmpty()) {
//        this.setSelection(this.text?.length!!)
//    }
//    this.setText(value)
//}
//
//@InverseBindingAdapter(attribute = "setText")
//fun TextInputEditText.getAppliedText() : String = this.text.toString()
//@BindingAdapter(value=["cabutSiaga","pasangSiaga"])
//fun TextInputEditText.calculateResult(cabutSiaga : String, pasangSiaga : String){
//    val convertedCabutSiaga = if(cabutSiaga.isEmpty()) 0 else cabutSiaga.toInt()
//    val convertedPasangSiaga = if(pasangSiaga.isEmpty()) 0 else pasangSiaga.toInt()
//    val result = (convertedCabutSiaga - convertedPasangSiaga).toString()
//    if(this.text.toString() != result) {
//        this.setText(result)
//    }
//}

