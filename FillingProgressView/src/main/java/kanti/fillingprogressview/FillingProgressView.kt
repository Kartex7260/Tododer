package kanti.fillingprogressview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.min
import kotlin.properties.Delegates

class FillingProgressView : View {

	private var mProgress: Float by Delegates.notNull()

	private var mDiameter: Float by Delegates.notNull()
	private var mBorderToDiameterRatio: Float by Delegates.notNull()

	private var mMaxValueTransparency: Float by Delegates.notNull()
	@ColorInt private var mColorAccent: Int = Color.BLACK
	@ColorInt private var mColorOnSurface: Int = Color.BLACK
	private lateinit var mPaintBorderEmpty: Paint
	private lateinit var mPaintBorderFull: Paint
	private lateinit var mPaintBody: Paint
	private lateinit var transparencyModifierBorder: TransparencyColorModifier
	private lateinit var transparencyModifierBody: TransparencyColorModifier

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

	var colorAccent: Int
		get() = mColorAccent
		set(value) {
			mColorAccent = value
			computeColorsFromProgress()
			invalidate()
		}

	var colorOnSurface: Int
		get() = mColorOnSurface
		set(value) {
			mColorOnSurface = value
			mPaintBorderEmpty.color = value
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

	var maxValueTransparency: Float
		get() = mMaxValueTransparency
		set(value) {
			require(value in Progress.MIN_VALUE..Progress.MAX_VALUE) {
				"The transparency modifier cannot be more than ${Progress.MAX_VALUE} " +
						"or less than ${Progress.MIN_VALUE}. Actual: $value"
			}
			mMaxValueTransparency = value
			transparencyModifierBorder.modifierMaxColor = mMaxValueTransparency
			computeColorsFromProgress()
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
			R.style.kanti_fillingprogressview_FillingProgressView
		)
	}

	constructor(
		context: Context,
		attrs: AttributeSet?
	): super(context, attrs) {
		init(
			context,
			attrs,
			R.style.kanti_progress,
			R.style.kanti_fillingprogressview_FillingProgressView
		)
	}

	constructor(context: Context): super(context) {
		init(
			context,
			null,
			R.style.kanti_progress,
			R.style.kanti_fillingprogressview_FillingProgressView
		)
	}

	private fun init(
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
				mProgress = clippingValue(getFloat(R.styleable.FillingProgressView_progress, 0f))
				mColorAccent = getColor(
					R.styleable.FillingProgressView_colorAccent,
					mColorAccent
				)
				mColorOnSurface = getColor(
					R.styleable.FillingProgressView_colorOnSurface,
					mColorOnSurface
				)
				mDiameter = getDimension(
					R.styleable.FillingProgressView_diameter,
					100f
				).let { diameter ->
					if (diameter == 0f)
						return@let 1f
					clippingValue(diameter, mxv = Float.MAX_VALUE)
				}
				mBorderToDiameterRatio = getFloat(
					R.styleable.FillingProgressView_borderToDiameterRatio,
					9f
				).let { ratio ->
					if (ratio <= 0)
						return@let 1f
					ratio
				}
				getDimension(
					R.styleable.FillingProgressView_borderWidth,
					-1f
				).also {  borderWidth ->
					if (borderWidth == -1f) {
						return@also
					}
					this@FillingProgressView.borderWidth = borderWidth
				}
				mMaxValueTransparency = getFloat(
					R.styleable.FillingProgressView_maxValueTransparency,
					0.9f
				).let { mvt -> clippingValue(mvt) }
				mPaintBorderFull = Paint(Paint.ANTI_ALIAS_FLAG).apply {
					style = Paint.Style.STROKE
					strokeWidth = borderWidth
				}
				mPaintBorderEmpty = Paint(Paint.ANTI_ALIAS_FLAG).apply {
					style = Paint.Style.STROKE
					strokeWidth = borderWidth
					color = mColorOnSurface
				}
				transparencyModifierBody = TransparencyColorModifier(
					modifierMaxColor = mMaxValueTransparency
				)
				transparencyModifierBorder = TransparencyColorModifier()
				mPaintBody = Paint(Paint.ANTI_ALIAS_FLAG).apply {
					style = Paint.Style.FILL
				}
				computeColorsFromProgress()
			} finally {
				recycle()
			}
		}
	}

	private fun computeColorsFromProgress() {
		mPaintBorderFull.color = transparencyModifierBorder.modify(mColorAccent, mProgress)
		mPaintBody.color = transparencyModifierBody.modify(mColorAccent, mProgress)
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		val centerX = width / 2f
		val centerY = height / 2f
		val halfOfBorder = borderWidth / 2
		val radius = mDiameter / 2 - halfOfBorder

		canvas.drawCircle(
			centerX,
			centerY,
			radius,
			mPaintBody
		)

		canvas.drawCircle(
			centerX,
			centerY,
			radius,
			mPaintBorderEmpty
		)

		canvas.drawCircle(
			centerX,
			centerY,
			radius,
			mPaintBorderFull
		)
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		val desiredWidth = mDiameter.toInt() + paddingStart + paddingEnd
		val desiredHeight = mDiameter.toInt() + paddingTop + paddingBottom

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

	private fun clippingValue(
		value: Float,
		mxv: Float = Progress.MAX_VALUE,
		mnv: Float = Progress.MIN_VALUE
	): Float {
		if (value > mxv)
			return mxv
		else if (value < mnv)
			return mnv
		return value
	}

}