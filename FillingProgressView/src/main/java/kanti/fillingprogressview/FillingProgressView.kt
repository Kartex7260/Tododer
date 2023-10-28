package kanti.fillingprogressview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.view.setPadding
import kotlin.math.min

class FillingProgressView : View {

	private var mProgress: Float = DEFAULT_PROGRESS_VALUE

	private var mDiameter: Float = TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP,
		DEFAULT_DIAMETER_DP,
		context.resources.displayMetrics
	)
	private var mBorderToDiameterRatio: Float = DEFAULT_BORDER_TO_DIAMETER_RATIO

	private val defaultColor = android.R.attr.colorPrimary
	private val mTransparencyModifier = 0.9f
	@ColorInt private var mColor: Int = 0
	private lateinit var mPaintBorder: Paint
	private lateinit var mPaintFill: Paint
	private lateinit var transparencyModifier: TransparencyColorModifier
	private lateinit var darkenModifier: DarkenColorModifier

	var progress: Float
		get() = mProgress
		set(value) {
			require(value in Progress.MIN_VALUE..Progress.MAX_VALUE) {
				"The progress cannot be more than ${Progress.MAX_VALUE} " +
						"or less than ${Progress.MIN_VALUE}. Actual: $value"
			}
			mProgress = value
			computeColorsFromProgress()
			invalidate()
		}

	var color: Int
		get() = mColor
		set(value) {
			mColor = value
			setMainColor(mColor)
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
		init(
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
		init(
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
		init(
			context,
			attrs,
			0,
			0
		)
	}

	constructor(context: Context): super(context) {
		init(
			context,
			null,
			0,
			0
		)
	}

	private fun init(
		context: Context,
		attrs: AttributeSet?,
		defStyleAttr: Int,
		defStyleRes: Int
	) {
		setPadding(TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP,
			16f,
			context.resources.displayMetrics
		).toInt())

		context.theme.obtainStyledAttributes(
			attrs,
			R.styleable.FillingProgressView,
			defStyleAttr,
			defStyleRes
		).apply {
			try {
				mProgress = getFloat(R.styleable.FillingProgressView_progress, DEFAULT_PROGRESS_VALUE)
				mColor = getColor(
					R.styleable.FillingProgressView_colorBorder,
					run {
						val typedValue = TypedValue()
						context.theme.resolveAttribute(defaultColor, typedValue, false)
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
				mPaintBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
					style = Paint.Style.STROKE
					strokeWidth = borderWidth
				}
				darkenModifier = DarkenColorModifier()
				transparencyModifier = TransparencyColorModifier(darkenModifier, mTransparencyModifier)
				mPaintFill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
					style = Paint.Style.FILL
				}
				setMainColor(mColor)
			} finally {
				recycle()
			}
		}
	}

	private fun setMainColor(color: Int) {
		mColor = color
		mPaintBorder.color = mColor
		computeColorsFromProgress()
	}

	private fun computeColorsFromProgress() {
		mPaintBorder.color = darkenModifier.modify(mColor, mProgress)
		mPaintFill.color = transparencyModifier.modify(mColor, mProgress)
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
		val desiredWidth = mDiameter.toInt() + paddingStart + paddingEnd
		val desiredHeight = mDiameter.toInt() + paddingTop + paddingEnd

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