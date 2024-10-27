package com.narine.kp.redfly.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun collectInitialPrefetchPositions(
        adapterItemCount: Int,
        layoutPrefetchRegistry: LayoutPrefetchRegistry?
    ) {
        super.collectInitialPrefetchPositions(adapterItemCount, layoutPrefetchRegistry)

        // Define how many items to prefetch
        val itemsToPrefetch = 20 // Number of items to prefetch
        // Get the first visible item position
        val firstVisibleItemPosition = findFirstVisibleItemPosition()

        // Prefetch next items
        for (i in 1..itemsToPrefetch) {
            val nextPosition = firstVisibleItemPosition + i
            if (nextPosition < adapterItemCount) {
                layoutPrefetchRegistry?.addPosition(nextPosition, 1) // Prefetch next item
            }
        }

        // Prefetch previous items
        for (i in 1..itemsToPrefetch) {
            val prevPosition = firstVisibleItemPosition - i
            if (prevPosition >= 0) {
                layoutPrefetchRegistry?.addPosition(prevPosition, 1) // Prefetch previous item
            }
        }
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)

        // Improve performance by disabling predictive item animations
        if (itemCount > 0) {
            val childCount = childCount
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child != null) {
                    child.isDrawingCacheEnabled = true // Enable drawing cache
                }
            }
        }
    }
}

