package com.example.util


import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.abs


class ZoomableImageView : AppCompatImageView {

    var scaleFactor = 1.0f
    private val scaleDetector = ScaleGestureDetector(context, ScaleListener())
    private val animationHandler = Handler(Looper.getMainLooper())
    private val expandAnimationRunnable = object : Runnable {
        override fun run() {
            if (scaleFactor <= NORMAL_SCALE_FACTOR) {
                scaleFactor += SCALE_FACTOR_CHANGE
                invalidate()
                animationHandler.postDelayed(this, ANIMATION_DELAY)
            } else {
                scaleFactor = NORMAL_SCALE_FACTOR
                invalidate()
            }
        }
    }

    private val collapseAnimationRunnable = object: Runnable{
        override fun run() {
            if(scaleFactor >= NORMAL_SCALE_FACTOR){
                scaleFactor -= SCALE_FACTOR_CHANGE
                invalidate()
                animationHandler.postDelayed(this, ANIMATION_DELAY)
            } else{
                scaleFactor = NORMAL_SCALE_FACTOR
                invalidate()
            }
        }
    }


    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, styleAttr: Int) : super(context, attributeSet, styleAttr)

    override fun onDraw(canvas: Canvas?) {
        canvas?.save()
        canvas?.scale(scaleFactor, scaleFactor, scaleDetector.focusX, scaleDetector.focusY )
        super.onDraw(canvas)
        canvas?.restore()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event == null) return true
        scaleDetector.onTouchEvent(event)
        if (event.action == MotionEvent.ACTION_UP && scaleFactor < NORMAL_SCALE_FACTOR) {
            animationHandler.removeCallbacks(expandAnimationRunnable)
            animationHandler.postDelayed(expandAnimationRunnable, ANIMATION_DELAY)
        } else if (event.action == MotionEvent.ACTION_UP && scaleFactor > NORMAL_SCALE_FACTOR){
            animationHandler.removeCallbacks(collapseAnimationRunnable)
            animationHandler.postDelayed(collapseAnimationRunnable, ANIMATION_DELAY)
        }
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if(abs(detector.currentSpan - detector.previousSpan) > SENSITIVITY) {
                scaleFactor = max(MIN_SCALE_FACTOR, min(scaleFactor * detector.scaleFactor, MAX_SCALE_FACTOR))
                invalidate()
            }
            return true
        }
    }


    companion object {
        private const val NORMAL_SCALE_FACTOR = 1.0f
        private const val SCALE_FACTOR_CHANGE = 0.02f
        private const val MIN_SCALE_FACTOR = 0.05f
        private const val MAX_SCALE_FACTOR = 5.0f
        private const val ANIMATION_DELAY = 4L
        private const val SENSITIVITY = 10
    }

}