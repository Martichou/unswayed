package me.martichou.unswayed.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import me.martichou.unswayed.R
import me.martichou.unswayed.databinding.MainRvPhotoBinding
import me.martichou.unswayed.databinding.MainRvSeparatorBinding
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
                MainRvPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> ViewHolderSeparator(
                MainRvSeparatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    class ViewHolder(private val binding: MainRvPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageItem) {
            binding.apply {
                Glide.with(binding.imageView)
                    .load(item.imgUri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(binding.imageView.measuredWidth, binding.imageView.measuredHeight)
                    .thumbnail(0.1f)
                    .error(R.drawable.placeholder)
                    .into(binding.imageView)
                executePendingBindings()
            }
        }
    }

    @ExperimentalStdlibApi
    class ViewHolderSeparator(private val binding: MainRvSeparatorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DateItem) {
            binding.apply {
                separatorDate.setTextFuture(
                    PrecomputedTextCompat.getTextFuture(
                        SimpleDateFormat("EE d MMM y", Locale.getDefault()).format(item.date)
                            .capitalize(Locale.getDefault()),
                        TextViewCompat.getTextMetricsParams(binding.separatorDate), null
                    )
                )
                executePendingBindings()
            }
        }
    }
}

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