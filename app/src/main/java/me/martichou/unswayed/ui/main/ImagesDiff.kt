package me.martichou.unswayed.ui.main

import androidx.recyclerview.widget.DiffUtil
import me.martichou.unswayed.models.*
import java.util.*

class ImagesDiff : DiffUtil.ItemCallback<GeneralItem>() {

    override fun areItemsTheSame(oldItem: GeneralItem, newItem: GeneralItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: GeneralItem, newItem: GeneralItem): Boolean {
        return if (oldItem.type != newItem.type ||
            ((oldItem is DateItem && newItem is ImageItem) ||
                    (oldItem is ImageItem && newItem is DateItem))
        )
            false
        else if (oldItem is ImageItem && newItem is ImageItem)
            oldItem.imgUri == newItem.imgUri
        else if (oldItem is DateItem && newItem is DateItem)
            oldItem.date == newItem.date
        else
            false
    }
}