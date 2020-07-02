package me.martichou.unswayedphotos.ui.home.diffutil

import androidx.recyclerview.widget.DiffUtil
import me.martichou.unswayedphotos.data.model.recycler.HomeDate
import me.martichou.unswayedphotos.data.model.recycler.HomeGeneral
import me.martichou.unswayedphotos.data.model.room.ImageLocal

class HomeDiff : DiffUtil.ItemCallback<HomeGeneral>() {

    override fun areItemsTheSame(oldItem: HomeGeneral, newItem: HomeGeneral): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: HomeGeneral, newItem: HomeGeneral): Boolean {
        return if (oldItem.type != newItem.type || ((oldItem is HomeDate && newItem is ImageLocal) || (oldItem is ImageLocal && newItem is HomeDate)))
            false
        else if (oldItem is ImageLocal && newItem is ImageLocal)
            oldItem.imgUri == newItem.imgUri
        else if (oldItem is HomeDate && newItem is HomeDate)
            oldItem.date == newItem.date
        else
            false
    }

}