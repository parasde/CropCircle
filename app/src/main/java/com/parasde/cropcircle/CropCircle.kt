package com.parasde.cropcircle

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

class CropCircle : ImageView, View.OnTouchListener {

    var downX = 0.0F
    var downY = 0.0F

    init {
        setOnTouchListener(this)
        setBackgroundResource(R.drawable.circle)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                animate()
                    .x(event.rawX + downX)
                    .y(event.rawY + downY)
                    .setDuration(0).start()
            }
            MotionEvent.ACTION_DOWN -> {
                downX = x - event.rawX
                downY = y - event.rawY
            }
            else -> return false
        }

        return true
    }
}
