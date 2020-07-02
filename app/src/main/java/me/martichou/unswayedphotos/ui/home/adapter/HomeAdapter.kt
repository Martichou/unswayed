package me.martichou.unswayedphotos.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.martichou.unswayedphotos.data.model.recycler.HomeDate
import me.martichou.unswayedphotos.data.model.recycler.HomeGeneral
import me.martichou.unswayedphotos.data.model.room.ImageLocal
import me.martichou.unswayedphotos.databinding.RvHomeDateBinding
import me.martichou.unswayedphotos.databinding.RvHomeImageBinding
import me.martichou.unswayedphotos.ui.home.diffutil.HomeDiff
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
            holder.bind(getItem(position) as ImageLocal)
        } else if (holder is DateVH) {
            holder.bind(getItem(position) as HomeDate)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun getItemId(position: Int): Long = position.toLong()

}