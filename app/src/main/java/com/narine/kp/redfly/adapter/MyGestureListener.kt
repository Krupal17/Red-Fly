package com.narine.kp.redfly.adapter

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class MyGestureListener(private val recyclerView: RecyclerView) : GestureDetector.SimpleOnGestureListener() {
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        // Handle fling gesture for smooth scrolling
        if (e1 != null) {
            val dx = e2.x - e1.x
            val dy = e2.y - e1.y

            // You can add logic to determine the direction and handle the scroll behavior
            if (Math.abs(dx) > Math.abs(dy)) {
                // Horizontal fling
                recyclerView.smoothScrollBy(dx.toInt(), 0) // Adjust the scroll speed
            } else {
                // Vertical fling
                recyclerView.smoothScrollBy(0, dy.toInt()) // Adjust the scroll speed
            }
            return true
        }
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        // Handle scroll event for smooth interaction
        recyclerView.scrollBy(distanceX.toInt(), distanceY.toInt())
        return true
    }
}
