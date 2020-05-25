package me.martichou.unswayed.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.martichou.unswayed.R
import me.martichou.unswayed.databinding.RvItemsBinding
import me.martichou.unswayed.databinding.RvSeparatorBinding
import me.martichou.unswayed.models.DateItem
import me.martichou.unswayed.models.GeneralItem
import me.martichou.unswayed.models.ImageItem
import java.text.SimpleDateFormat
import java.util.*

class ImagesAdapter : ListAdapter<GeneralItem, RecyclerView.ViewHolder>(ImagesDiff()) {

    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder)
            holder.bind(getItem(position) as ImageItem)
        else if (holder is ViewHolderSeparator)
            holder.bind(getItem(position) as DateItem)
    }

    @ExperimentalStdlibApi
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> ViewHolder(
                RvItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> ViewHolderSeparator(
                RvSeparatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    class ViewHolder(private val binding: RvItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageItem) {
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

    @ExperimentalStdlibApi
    class ViewHolderSeparator(private val binding: RvSeparatorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DateItem) {
            binding.apply {
                separatorDate.text =
                    SimpleDateFormat("EE d MMM y", Locale.getDefault()).format(item.date)
                        .capitalize(Locale.getDefault())
                executePendingBindings()
            }
        }
    }
}