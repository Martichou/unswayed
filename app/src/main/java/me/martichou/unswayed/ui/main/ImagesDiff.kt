package me.martichou.unswayed.ui.main

import androidx.recyclerview.widget.DiffUtil
import me.martichou.unswayed.models.ImageObject

class ImagesDiff : DiffUtil.ItemCallback<ImageObject>() {

    override fun areItemsTheSame(oldItem: ImageObject, newItem: ImageObject): Boolean {
        return oldItem.imgUri == newItem.imgUri
    }

    override fun areContentsTheSame(oldItem: ImageObject, newItem: ImageObject): Boolean {
        return oldItem == newItem
    }
}