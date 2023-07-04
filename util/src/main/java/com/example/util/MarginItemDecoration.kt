package com.example.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Integer.max

class MarginItemDecoration(
    private val spaceSize: Int,
    private var spanCount: Int = 1,
    private val orientation: Int = GridLayoutManager.VERTICAL
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            val childPosition = parent.getChildAdapterPosition(view)
            if (orientation == GridLayoutManager.VERTICAL) {
                if (childPosition < spanCount) top = 0
                if (childPosition != 0 && childPosition % spanCount == 0) left = spaceSize
            } else {
                if (childPosition < spanCount) left = spaceSize
                if (childPosition % spanCount == 0) top = spaceSize
            }
            right = spaceSize
            bottom = spaceSize
        }
    }
}