package me.martichou.unswayed.ui.folder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.martichou.unswayed.R
import me.martichou.unswayed.database.AppDatabase
import me.martichou.unswayed.database.BackedFolderDao
import me.martichou.unswayed.databinding.FolderRvItemBinding
import me.martichou.unswayed.models.BackedFolder
import me.martichou.unswayed.models.FolderItem
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class FolderAdapter(private val db: AppDatabase) : ListAdapter<FolderItem, FolderAdapter.ViewHolder>(FolderDiff()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), db.backedFolderDao())
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
        fun bind(item: FolderItem, dao: BackedFolderDao) {
            binding.apply {
                folderName.setTextFuture(
                    PrecomputedTextCompat.getTextFuture(
                        item.folderName,
                        TextViewCompat.getTextMetricsParams(binding.folderName), null
                    )
                )
                Glide.with(this.folderImage)
                    .load(item.lastImgUri)
                    .override(binding.folderImage.width, binding.folderImage.height)
                    .thumbnail(0.1f)
                    .error(R.drawable.placeholder)
                    .into(binding.folderImage)
                binding.addToBackup.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        dao.insertBackedFolder(BackedFolder(item.folderName, item.folderId))
                    }
                }
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