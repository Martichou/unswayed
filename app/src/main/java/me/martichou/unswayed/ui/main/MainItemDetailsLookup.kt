package me.martichou.unswayed.ui.main

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class MainItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            val vh = recyclerView.getChildViewHolder(view)
            // Workaround for now, will need to find a way to avoid user selecting viewType 2
            return if (vh is ImagesAdapter.ViewHolder) {
                vh.getItemDetails()
            } else {
                null
            }
        }
        return null
    }
}