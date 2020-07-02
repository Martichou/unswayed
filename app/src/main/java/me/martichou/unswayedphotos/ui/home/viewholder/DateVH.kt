package me.martichou.unswayedphotos.ui.home.viewholder

import android.annotation.SuppressLint
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import me.martichou.unswayedphotos.data.model.recycler.HomeDate
import me.martichou.unswayedphotos.databinding.RvHomeDateBinding
import java.text.SimpleDateFormat
import java.util.*

class DateVH(private val binding: RvHomeDateBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("DefaultLocale")
    fun bind(item: HomeDate) {
        binding.separatorDate.setTextFuture(
            PrecomputedTextCompat.getTextFuture(
                SimpleDateFormat("EE d MMM y", Locale.getDefault()).format(item.date)
                    .capitalize(),
                TextViewCompat.getTextMetricsParams(binding.separatorDate), null
            )
        )
        binding.executePendingBindings()
    }
}