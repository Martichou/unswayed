package me.martichou.unswayed.models

import android.net.Uri
import java.util.*

data class ImageItem(val imgUri: Uri?, val imgDate: Date) : GeneralItem() {
    override val type: Int
        get() = TYPE_GENERAL
}