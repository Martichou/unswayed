package me.martichou.unswayed.ui.folder

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
import me.martichou.unswayed.databinding.FolderRvItemBinding
import me.martichou.unswayed.models.FolderItem

class FolderAdapter : ListAdapter<FolderItem, FolderAdapter.ViewHolder>(FolderDiff()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    @ExperimentalStdlibApi
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FolderRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class ViewHolder(private val binding: FolderRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FolderItem) {
            binding.apply {
                folderName.setTextFuture(
                    PrecomputedTextCompat.getTextFuture(
                        item.folderName,
                        TextViewCompat.getTextMetricsParams(binding.folderName), null
                    )
                )
                Glide.with(this.folderImage)
                    .load(item.lastImgUri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(binding.folderImage.measuredWidth, binding.folderImage.measuredHeight)
                    .thumbnail(0.1f)
                    .error(R.drawable.placeholder)
                    .into(binding.folderImage)
                executePendingBindings()
            }
        }
    }
}

class FolderDiff : DiffUtil.ItemCallback<FolderItem>() {
    override fun areItemsTheSame(oldItem: FolderItem, newItem: FolderItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: FolderItem, newItem: FolderItem): Boolean {
        return oldItem.lastImgUri == newItem.lastImgUri
    }
}