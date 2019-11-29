package com.example.tagihin.widget

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import com.example.tagihin.R
import hari.bounceview.BounceView

class CustomLoading constructor(var activity: Activity) {
    var dialog: Dialog? = null

    fun showDialog() {
        if (!activity.isDestroyed) {
            dialog = Dialog(activity)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setCancelable(false)
            dialog?.window?.apply {
                setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            dialog?.setContentView(R.layout.dialog_loading)
            BounceView.addAnimTo(dialog)
            dialog?.show()
        }
    }

    fun hideDialog() {
        if (!activity.isDestroyed) {
            if (dialog != null) {
                dialog?.dismiss()
            }
        }
    }

}