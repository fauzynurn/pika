package com.example.tagihin.utils

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseMethod
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat

object Converters {

    @InverseMethod("convertFromMoney")
    @JvmStatic
    fun convertToMoney(formatter : DecimalFormat,value : Int) : String = formatter.format(value)

    @JvmStatic
    fun convertFromMoney(formatter: DecimalFormat, value : String) : Int = if(value.isNotEmpty()) value.replace(",","").toInt() else 0
}