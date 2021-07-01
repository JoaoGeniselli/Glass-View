package com.example.customviews

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.FrameLayout

class GlassView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var fillPercent = 0.5
    private val paint = Paint()
    private val backgroundRect = Rect(0, 0, 0, 0)
    private val percentY: Double get() = height - height * fillPercent
    private val animationDuration: Int

    init {
        setWillNotDraw(false)
        inflate(context, R.layout.view_glass, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.GlassView)
        fillPercent = attributes.getFloat(R.styleable.GlassView_fillPercent, 1f).toDouble()
        animationDuration = attributes.getInt(R.styleable.GlassView_animationDuration, 500)
        val fillColor = attributes.getColor(R.styleable.GlassView_color, Color.RED)
        attributes.recycle()

        paint.apply {
            color = fillColor
            style = Paint.Style.FILL
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startAnimation()
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        backgroundRect.apply {
            left = 0
            right = this@GlassView.width
            bottom = this@GlassView.height
        }
        canvas.drawRect(backgroundRect, paint)
    }

    private fun startAnimation() {
        val topHolder = PropertyValuesHolder.ofInt(PROPERTY_TOP, height, percentY.toInt())
        val animator = ValueAnimator()
        animator.setValues(topHolder)
        animator.duration = animationDuration.toLong()
        animator.addUpdateListener { animation ->
            backgroundRect.top = animation.getAnimatedValue(PROPERTY_TOP) as Int
            invalidate()
        }
        animator.start()
    }

    companion object {
        private const val PROPERTY_TOP = "TOP"
    }
}