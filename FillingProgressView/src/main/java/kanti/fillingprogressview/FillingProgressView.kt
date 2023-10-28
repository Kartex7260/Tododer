package kanti.fillingprogressview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.min

class FillingProgressView : View {

	private var mProgress: Float = DEFAULT_PROGRESS_VALUE

	private var mDiameter: Float = TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP,
		DEFAULT_DIAMETER_DP,
		context.resources.displayMetrics
	)
	private var mBorderToDiameterRatio: Float = DEFAULT_BORDER_TO_DIAMETER_RATIO

	private val mTransparencyModifier = 0.9f
	@ColorInt private var mColorPrimary: Int = 0
	private lateinit var mPaintBorder: Paint
	private lateinit var mPaintFill: Paint
	private lateinit var colorTransparency: ColorTransparency

	var progress: Float
		get() = mProgress
		set(value) {
			require(value in Progress.MIN_VALUE..Progress.MAX_VALUE) {
				"The progress cannot be more than ${Progress.MAX_VALUE} " +
						"or less than ${Progress.MIN_VALUE}. Actual: $value"
			}
			mProgress = value
			initPaintColorFill(mProgress)
			invalidate()
		}

	var color: Int
		get() = mColorPrimary
		set(value) {
			mColorPrimary = value
			initColor(mColorPrimary)
			invalidate()
		}

	var diameter: Float
		get() = mDiameter
		set(value) {
			require(value > 0) {
				"The diameter cannot be less than or equal to 0. Actual: $value"
			}
			mDiameter = value
			requestLayout()
			invalidate()
		}

	var borderWidth: Float
		get() = diameter / borderToDiameterRatio
		set(value) {
			require(value > 0) {
				"The width of the border cannot be greater than 0. Actual: $value"
			}
			borderToDiameterRatio = diameter / value
		}

	var borderToDiameterRatio: Float
		get() = mBorderToDiameterRatio
		set(value) {
			require(value > 0) {
				"The ratio between the border and the diameter must be greater than 0. " +
						"Actual: $value"
			}
			mBorderToDiameterRatio = value
			invalidate()
		}


	constructor(
		context: Context,
		attrs: AttributeSet?,
		defStyleAttr: Int,
		defStyleRes: Int
	): super(context, attrs, defStyleAttr, defStyleRes) {
		initAttrs(
			context,
			attrs,
			defStyleAttr,
			defStyleRes
		)
	}

	constructor(
		context: Context,
		attrs: AttributeSet?,
		defStyleAttr: Int
	): super(context, attrs, defStyleAttr) {
		initAttrs(
			context,
			attrs,
			defStyleAttr,
			0
		)
	}

	constructor(
		context: Context,
		attrs: AttributeSet?
	): super(context, attrs) {
		initAttrs(
			context,
			attrs,
			0,
			0
		)
	}

	constructor(context: Context): super(context) {
		initAttrs(
			context,
			null,
			0,
			0
		)
	}

	private fun initAttrs(
		context: Context,
		attrs: AttributeSet?,
		defStyleAttr: Int,
		defStyleRes: Int
	) {
		context.theme.obtainStyledAttributes(
			attrs,
			R.styleable.FillingProgressView,
			defStyleAttr,
			defStyleRes
		).apply {
			try {
				mProgress = getFloat(R.styleable.FillingProgressView_progress, DEFAULT_PROGRESS_VALUE)
				mColorPrimary = getColor(
					R.styleable.FillingProgressView_colorBorder,
					run {
						val typedValue = TypedValue()
						context.theme.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, false)
						typedValue.data
					}
				)
				mDiameter = getDimension(
					R.styleable.FillingProgressView_diameter,
					mDiameter
				)
				mBorderToDiameterRatio = getFloat(
					R.styleable.FillingProgressView_borderToDiameterRatio,
					DEFAULT_BORDER_TO_DIAMETER_RATIO
				)
				getDimension(
					R.styleable.FillingProgressView_borderWidth,
					-1f
				).also {  borderWidth ->
					if (borderWidth == -1f) {
						return@also
					}
					this@FillingProgressView.borderWidth = borderWidth
				}
				initColor(mColorPrimary)
			} finally {
				recycle()
			}
		}
	}

	private fun initPaintColorFill(progress: Float) {
		mPaintFill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = colorTransparency.transparent(progress)
			style = Paint.Style.FILL
		}
	}

	private fun initColor(clr: Int) {
		mPaintBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = clr
			style = Paint.Style.STROKE
			strokeWidth = borderWidth
		}
		colorTransparency = ColorTransparency(clr, mTransparencyModifier)
		initPaintColorFill(progress)
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		val centerX = width / 2f
		val centerY = height / 2f
		val halfStrokeWidth = borderWidth / 2
		val radius = mDiameter / 2 - halfStrokeWidth

		canvas.drawCircle(
			centerX,
			centerY,
			radius,
			mPaintFill
		)

		canvas.drawCircle(
			centerX,
			centerY,
			radius,
			mPaintBorder
		)
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		val desiredWidth = mDiameter.toInt()
		val desiredHeight = mDiameter.toInt()

		val widthMode = MeasureSpec.getMode(widthMeasureSpec)
		val widthSize = MeasureSpec.getSize(widthMeasureSpec)
		val heightMode = MeasureSpec.getMode(heightMeasureSpec)
		val heightSize = MeasureSpec.getSize(heightMeasureSpec)

		val width = when (widthMode) {
			MeasureSpec.EXACTLY -> widthSize
			MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
			else -> desiredWidth
		}

		val height = when (heightMode) {
			MeasureSpec.EXACTLY -> heightSize
			MeasureSpec.AT_MOST -> min(desiredHeight, heightSize)
			else -> desiredHeight
		}

		setMeasuredDimension(width, height)
	}

	companion object {

		const val DEFAULT_PROGRESS_VALUE = 0.0f
		const val DEFAULT_DIAMETER_DP = 32f
		const val DEFAULT_BORDER_TO_DIAMETER_RATIO = 8f

	}

}