package me.martichou.unswayedphotos.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.martichou.unswayedphotos.models.rv.HomeDate
import me.martichou.unswayedphotos.models.rv.HomeGeneral
import me.martichou.unswayedphotos.models.Zimage
import me.martichou.unswayedphotos.databinding.RvHomeDateBinding
import me.martichou.unswayedphotos.databinding.RvHomeImageBinding
import me.martichou.unswayedphotos.ui.home.viewholder.DateVH
import me.martichou.unswayedphotos.ui.home.viewholder.ImageVH

class HomeAdapter : ListAdapter<HomeGeneral, RecyclerView.ViewHolder>(HomeDiff()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> ImageVH(
                RvHomeImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> DateVH(
                RvHomeDateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageVH) {
            holder.bind(getItem(position) as Zimage)
        } else if (holder is DateVH) {
            holder.bind(getItem(position) as HomeDate)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun getItemId(position: Int): Long = position.toLong()

    class HomeDiff : DiffUtil.ItemCallback<HomeGeneral>() {

        override fun areItemsTheSame(oldItem: HomeGeneral, newItem: HomeGeneral): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HomeGeneral, newItem: HomeGeneral): Boolean {
            return if (oldItem.type != newItem.type || ((oldItem is HomeDate && newItem is Zimage) || (oldItem is Zimage && newItem is HomeDate)))
                false
            else if (oldItem is Zimage && newItem is Zimage)
                oldItem.imgUri == newItem.imgUri
            else if (oldItem is HomeDate && newItem is HomeDate)
                oldItem.date == newItem.date
            else
                false
        }
    }
}