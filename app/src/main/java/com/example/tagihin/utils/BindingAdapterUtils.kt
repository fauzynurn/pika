package com.example.tagihin.utils

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tagihin.R

@BindingAdapter("imageUrl")
fun loadImage(view : ImageView, url : String){
    Glide.with(view.context)
        .load(url)
        .apply(
            RequestOptions()
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

@BindingAdapter("visibility")
fun View.setVisibility(value : String?){
    this.visibility = if(value == "TTT" || value == null) {
        View.GONE
    } else {
        View.VISIBLE
    }
}