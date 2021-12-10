package com.udacity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0

    private var baseColor = resources.getColor(R.color.colorPrimary)
    private var loadingColor = resources.getColor(R.color.colorPrimaryDark)
    private var ellipseColor = resources.getColor(R.color.colorAccent)

    private var initText = resources.getString(R.string.button_name)
    private var loadingText = resources.getString(R.string.we_are_loading)
    private var animDuration: Long = 1000
    private var angle = 0f
    private var currentText = ""

    private val loadingPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL // default: FILL
    }
    private val textColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 35f
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        style = Paint.Style.FILL
        typeface = Typeface.SANS_SERIF
    }

    private val textBounds = Rect()
    private val loadingRect = Rect(0, 0, 0, 0)

    private var onEndAnimation: (() -> Unit)? = null


    init {

    }


    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Clicked -> showStartAnimation()
            ButtonState.Loading -> showLoading()
            ButtonState.Completed -> reset()
        }
    }

    private fun reset() {
        buttonState = ButtonState.Clicked
    }

    private fun showLoading() {
        valueAnimator.apply {
            removeAllUpdateListeners()
            removeAllListeners()
            cancel()
            interpolator = AccelerateDecelerateInterpolator()
            setIntValues(widthSize)
            addUpdateListener {
                loadingRect.right = it.animatedValue as Int
                angle = ((it.animatedValue as Int) * 360 / widthSize).toFloat()
                loadingRect.bottom = heightSize
                if (it.animatedValue == widthSize && buttonState == ButtonState.Completed) {
                    valueAnimator.cancel()
                }
                invalidate()
            }
            repeatCount = 0
            addListener(
                onEnd = {
                    buttonState = ButtonState.Completed
                    onEndAnimation?.invoke()
                }
            )
            duration = animDuration
            start()

        }
    }

    private fun showStartAnimation() {
        TODO("Not yet implemented")
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(baseColor)
        loadingPaint.color = loadingColor
        canvas?.drawRect(Rect(0, 0, 0, 0), loadingPaint)
        currentText?.let {
            textColor.getTextBounds(it, 0, it.length, textBounds)
            canvas?.drawText(
                it,
                (widthSize / 2).toFloat(),
                heightSize / 2 - textBounds.exactCenterY(),
                textColor
            )
        }
        loadingPaint.color = ellipseColor

        val start = (widthSize / 2 + textBounds.exactCenterX()) + textBounds.height() / 2
        canvas?.drawArc(
            start,
            (heightSize / 2 - textBounds.height() / 2).toFloat(),
            start + textBounds.height(),
            (heightSize / 2 + textBounds.height() / 2).toFloat(),
            0F,
            angle,
            true,
            loadingPaint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val min: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(min, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        super.performClick()
        reset()
        return true
    }

}