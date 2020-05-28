package me.martichou.unswayed.ui.folder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingDecorator(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = space
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = space * 2
        } else {
            outRect.left = space
        }
    }
}