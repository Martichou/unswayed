package me.martichou.unswayed.ui.folder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import me.martichou.unswayed.R
import me.martichou.unswayed.databinding.FolderAlbumRvItemBinding
import me.martichou.unswayed.models.FolderItem

class AlbumAdapter : ListAdapter<FolderItem, AlbumAdapter.ViewHolder>(AlbumDiff()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    @ExperimentalStdlibApi
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FolderAlbumRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class ViewHolder(private val binding: FolderAlbumRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FolderItem) {
            binding.apply {
                albumName.setTextFuture(
                    PrecomputedTextCompat.getTextFuture(
                        item.folderName,
                        TextViewCompat.getTextMetricsParams(binding.albumName), null
                    )
                )

                Glide.with(this.albumImage)
                    .load(item.lastImgUri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(binding.albumImage.measuredWidth, binding.albumImage.measuredHeight)
                    .transform(
                        MultiTransformation(CenterCrop(), RoundedCorners(16))
                    )
                    .thumbnail(0.1f)
                    .error(R.drawable.placeholder)
                    .into(binding.albumImage)
                executePendingBindings()
            }
        }
    }
}

class AlbumDiff : DiffUtil.ItemCallback<FolderItem>() {
    override fun areItemsTheSame(oldItem: FolderItem, newItem: FolderItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: FolderItem, newItem: FolderItem): Boolean {
        return oldItem.lastImgUri == newItem.lastImgUri
    }
}