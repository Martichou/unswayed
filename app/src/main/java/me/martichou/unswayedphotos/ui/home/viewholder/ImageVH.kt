package me.martichou.unswayedphotos.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import me.martichou.unswayedphotos.databinding.RvHomeImageBinding
import me.martichou.unswayedphotos.models.Zimage

class ImageVH(private val binding: RvHomeImageBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Zimage) {
        Glide.with(binding.root)
            .load(item.imgUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(binding.imageView.measuredWidth, binding.imageView.measuredHeight)
            .thumbnail(0.1f)
            // TODO - Add error placeholder
            .into(binding.imageView)
        binding.executePendingBindings()
    }
}