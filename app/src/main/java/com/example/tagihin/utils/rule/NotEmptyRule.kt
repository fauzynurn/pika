package com.example.tagihin.utils.rule

import ru.whalemare.rxvalidator.ValidateRule

class NotEmptyRule : ValidateRule{
    override fun errorMessage(): String = "Wajib diisi"

    override fun validate(data: String?): Boolean = data != ""

}