package com.example.tagihin.utils

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tagihin.R

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