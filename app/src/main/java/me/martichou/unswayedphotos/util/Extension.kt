package me.martichou.unswayedphotos.util

import android.content.res.Resources
import android.util.TypedValue
import java.util.regex.Pattern

fun String.isaValidEmail(): Boolean {
    return Pattern.compile("^([a-z0-9_+]([a-z0-9_+.]*[a-z0-9_+])?)@([a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,6})")
        .matcher(this).matches()
}

fun Float.toDp(resources: Resources): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)
}

