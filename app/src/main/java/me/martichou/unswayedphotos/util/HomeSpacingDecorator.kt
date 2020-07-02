package me.martichou.unswayedphotos.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HomeSpacingDecorator(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val pos = parent.getChildLayoutPosition(view)
        val type = parent.adapter?.getItemViewType(pos)
        if (pos != 0 && type != 2) {
            val prevtype = parent.adapter?.getItemViewType(pos - 1)
            var i = pos
            var lastSpacer = 0
            while (--i >= 0) {
                if (parent.adapter?.getItemViewType(i) == 2) {
                    break
                }
                lastSpacer++
            }
            // Right to the three first child each row
            if (lastSpacer != 4 * pos - 1)
                outRect.right = space
            // Left to the three last child each row
            if (prevtype != 2 && lastSpacer % 4 != 0)
                outRect.left = space
            outRect.top = space * 2
        }
    }

}