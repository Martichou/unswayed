package me.martichou.unswayed.utils

import android.content.res.Resources
import android.util.TypedValue

fun toDP(value: Float, resources: Resources): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value,
        resources.displayMetrics
    )
}