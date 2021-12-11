package com.udacity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.withStyledAttributes
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
    private var currentText = resources.getString(R.string.button_name)

    private val loadingPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL
    }
    private val textColor = Paint().apply {
        isAntiAlias = true
        textSize = 75f
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        style = Paint.Style.FILL
    }

    private val textBounds = Rect()
    private val loadingRect = Rect(0, 0, 0, 0)

    private var onEndAnimation: (() -> Unit)? = null


    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            baseColor = getColor(R.styleable.LoadingButton_baseColor, Color.GREEN)
            animDuration = getInteger(R.styleable.LoadingButton_animDuration, 1000).toLong()
            textColor.textSize =
                getDimensionPixelSize(R.styleable.LoadingButton_android_textSize, 16).toFloat()
        }

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
        loadingRect.right = 0
        angle = 0f
        currentText = initText
        invalidate()
    }

    private fun showLoading() {
        valueAnimator.removeAllUpdateListeners()
        valueAnimator.cancel()
        valueAnimator.removeAllListeners()
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.setIntValues(widthSize)
        valueAnimator.addUpdateListener {
            loadingRect.right = it.animatedValue as Int
            angle = ((it.animatedValue as Int) * 360 / widthSize).toFloat()
            loadingRect.bottom = heightSize
            if (it.animatedValue == widthSize && buttonState == ButtonState.Completed) {
                valueAnimator.cancel()
            }
            invalidate()
        }
        valueAnimator.repeatCount = 0
        valueAnimator.addListener(
            onEnd = {
                buttonState = ButtonState.Completed
                onEndAnimation?.invoke()
            }
        )
        valueAnimator.duration = animDuration
        valueAnimator.start()
    }

    private fun showStartAnimation() {
        buttonState = ButtonState.Loading
        currentText = loadingText
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null) {
            canvas.drawColor(baseColor)
            loadingPaint.color = loadingColor
            canvas.drawRect(loadingRect, loadingPaint)
            currentText.let {
                textColor.getTextBounds(it, 0, it.length, textBounds)
                canvas.drawText(
                    it,
                    (widthSize / 2).toFloat(),
                    heightSize / 2 - textBounds.exactCenterY(),
                    textColor
                )
            }
            loadingPaint.color = ellipseColor
            val start = (widthSize / 2 + textBounds.exactCenterX()) + textBounds.height() / 2
            canvas.drawArc(
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
        buttonState = ButtonState.Clicked
        return true
    }

}