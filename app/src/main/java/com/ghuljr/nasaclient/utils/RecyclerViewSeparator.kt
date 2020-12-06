package com.ghuljr.nasaclient.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

//TODO: parse values to from dp to pixels
class RecyclerViewSeparator(
    private val gridWidth: Int,
    private val appMargin: Int,
    private val gridMarginHorizontal: Int,
    private val gridMarginVertical: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view) % gridWidth

        outRect.top = gridMarginVertical

        if(gridWidth == 1) {
            outRect.left = appMargin
            outRect.right = appMargin
        } else {
            with(outRect) {
                when(position) {
                    0 -> {
                        left = appMargin
                        right = gridMarginHorizontal
                    }
                    gridWidth - 1 -> {
                        left = gridMarginHorizontal
                        right = appMargin
                    }
                    else -> {
                        left = gridMarginHorizontal
                        right = gridMarginHorizontal
                    }
                }
            }
        }
    }
}