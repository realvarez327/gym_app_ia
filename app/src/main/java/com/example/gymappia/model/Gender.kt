package com.example.gymappia.model

import androidx.annotation.StringRes
import com.example.gymappia.R

enum class Gender(@StringRes val stringId :Int) {
    Female(stringId = R.string.female),
    Male(stringId = R.string.male)
}