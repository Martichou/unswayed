package me.martichou.unswayed.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.martichou.unswayed.R
import me.martichou.unswayed.databinding.RvItemsBinding
import me.martichou.unswayed.databinding.RvSeparatorBinding
import me.martichou.unswayed.models.ImageObject
import java.text.SimpleDateFormat
import java.util.*

class ImagesAdapter : ListAdapter<ImageObject, RecyclerView.ViewHolder>(ImagesDiff()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder)
            holder.bind(getItem(position))
        else if (holder is ViewHolderSep)
            holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> ViewHolder(
                RvItemsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> ViewHolderSep(
                RvSeparatorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isSep) 2 else 1
    }

    class ViewHolder(private val binding: RvItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageObject) {
            binding.apply {
                Glide.with(binding.imageView)
                    .load(item.imgUri)
                    .override(binding.imageView.width, binding.imageView.height)
                    .thumbnail(0.1f)
                    .error(R.drawable.placeholder)
                    .into(binding.imageView)
                executePendingBindings()
            }
        }
    }

    class ViewHolderSep(private val binding: RvSeparatorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageObject) {
            binding.apply {
                separatorDate.text = SimpleDateFormat("EE d MMM y", Locale.getDefault()).format(item.imgDate)
                executePendingBindings()
            }
        }
    }
}