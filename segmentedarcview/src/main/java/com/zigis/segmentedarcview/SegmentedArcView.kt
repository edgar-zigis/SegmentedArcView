package com.zigis.segmentedarcview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.zigis.segmentedarcview.custom.ArcSegment
import com.zigis.segmentedarcview.custom.BlinkAnimationSettings
import com.zigis.segmentedarcview.custom.Coordinate2F
import java.lang.Math.toRadians
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

open class SegmentedArcView : View  {

    //  Setting vars

    var startAngle = 150f
    var sweepAngle = 240f
    var segmentSeparationAngle = 9f
    var segmentThickness = dp(7.5f)

    var title = ""
        set(value) {
            field = value
            invalidate()
        }
    var value = ""
        set(value) {
            field = value
            invalidate()
        }

    var titleColor = Color.parseColor("#525252")
    var valueColor = Color.parseColor("#1A1A1A")

    var titleTypeFace = Typeface.create("sans-serif-light", Typeface.NORMAL)
    var valueTypeface = Typeface.create("sans-serif", Typeface.BOLD)

    var titleTypefaceSize = dp(20f)
    var valueTypefaceSize = dp(55f)

    var blinkAnimationSettings = BlinkAnimationSettings(
        minAlpha = 0.4F,
        maxAlpha = 1F,
        duration = 2000L
    )

    var segments = emptyList<ArcSegment>()
        set(value) {
            field = if (isRTL()) {
                value.reversed()
            } else {
                value
            }
            segmentPaints.clear()
            segmentPaints.addAll(value.map { Paint(Paint.ANTI_ALIAS_FLAG) })

            if (value.any { it.animate }) {
                blinkAnimator?.start()
            }
            invalidate()
        }

    //  Private vars

    private var blinkAnimator: ObjectAnimator? = null
        get() {
            if (field == null) {
                field = ObjectAnimator.ofFloat(
                    this,
                    "blinkFraction",
                    blinkAnimationSettings.minAlpha,
                    blinkAnimationSettings.maxAlpha
                )
            }
            field?.duration = blinkAnimationSettings.duration
            field?.repeatMode = ObjectAnimator.REVERSE
            field?.repeatCount = ObjectAnimator.INFINITE
            return field
        }

    private var blinkFraction = 0f
    private var segmentPaints = mutableListOf<Paint>()

    private var titlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var valuePaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private val titleRect = Rect()
    private val valueRect = Rect()

    //  Constructors

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    //  Initialization

    private fun init(context: Context, attrs: AttributeSet?) {
        if (isInEditMode) return

        val styledAttributes = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SegmentedArcView,
            0,
            0
        )

        title = styledAttributes.getString(R.styleable.SegmentedArcView_title) ?: ""
        value = styledAttributes.getString(R.styleable.SegmentedArcView_value) ?: ""
        titleTypefaceSize = styledAttributes.getFloat(R.styleable.SegmentedArcView_titleTypefaceSize, titleTypefaceSize)
        valueTypefaceSize = styledAttributes.getFloat(R.styleable.SegmentedArcView_valueTypefaceSize, valueTypefaceSize)
        titleColor = styledAttributes.getColor(R.styleable.SegmentedArcView_titleColor, titleColor)
        valueColor = styledAttributes.getColor(R.styleable.SegmentedArcView_valueColor, valueColor)
        startAngle = styledAttributes.getFloat(R.styleable.SegmentedArcView_startAngle, startAngle)
        sweepAngle = styledAttributes.getFloat(R.styleable.SegmentedArcView_sweepAngle, sweepAngle)
        segmentSeparationAngle = styledAttributes.getFloat(R.styleable.SegmentedArcView_segmentSeparationAngle, segmentSeparationAngle)
        segmentThickness = styledAttributes.getFloat(R.styleable.SegmentedArcView_segmentThickness, segmentThickness)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val outerRadius = measuredWidth.toFloat() / 2
        val innerRadius = outerRadius - segmentThickness
        val segmentSweepAngle = (sweepAngle - ((segments.size - 1) * segmentSeparationAngle)) / segments.size

        var angle = startAngle
        segments.mapIndexed { index, arcSegment ->
            drawArcSegment(
                canvas,
                outerRadius,
                outerRadius,
                innerRadius,
                outerRadius,
                angle,
                segmentSweepAngle,
                arcSegment,
                segmentPaints[index]
            )
            angle += (segmentSweepAngle + segmentSeparationAngle)
        }

        drawTitleText(canvas)
        drawValueText(canvas)
    }

    //  Blink

    fun getBlinkFraction(): Float {
        return blinkFraction
    }

    fun setBlinkFraction(blinkFraction: Float) {
        this.blinkFraction = blinkFraction
        invalidate()
    }

    //  Texts

    @Suppress("DEPRECATION")
    private fun drawTitleText(canvas: Canvas) {
        titlePaint.color = titleColor
        titlePaint.typeface = titleTypeFace
        titlePaint.textSize = titleTypefaceSize
        titlePaint.getTextBounds(title, 0, title.length, titleRect)

        val textLayout = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            val builder = StaticLayout.Builder.obtain(title, 0, title.length, titlePaint, width)
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
            builder.build()
        } else {
            StaticLayout(title, titlePaint, width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false)
        }

        canvas.save()

        canvas.translate(0f, measuredHeight / 4.8f)
        textLayout.draw(canvas)

        canvas.restore()
    }

    @Suppress("DEPRECATION")
    private fun drawValueText(canvas: Canvas) {
        valuePaint.color = valueColor
        valuePaint.typeface = valueTypeface
        valuePaint.textSize = valueTypefaceSize
        valuePaint.getTextBounds(value, 0, value.length, valueRect)

        val textLayout = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            val builder = StaticLayout.Builder.obtain(value, 0, value.length, valuePaint, width)
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
            builder.build()
        } else {
            StaticLayout(value, valuePaint, width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false)
        }

        canvas.save()

        canvas.translate(0f, measuredHeight / 2f - valueRect.height() / 1.5f)
        textLayout.draw(canvas)

        canvas.restore()
    }

    //  Segment math

    private fun drawArcSegment(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        rInn: Float,
        rOut: Float,
        startAngle: Float,
        sweepAngle: Float,
        segment: ArcSegment,
        paint: Paint
    ) {
        val outerRect = RectF(cx - rOut, cy - rOut, cx + rOut, cy + rOut)
        val innerRect = RectF(cx - rInn, cy - rInn, cx + rInn, cy + rInn)
        val segmentPath = Path()

        val start = toRadians(startAngle.toDouble())
        val startX = (cx + rInn * cos(start)).toFloat()
        val startY = (cy + rInn * sin(start)).toFloat()

        segmentPath.moveTo(startX, startY)
        segmentPath.lineTo(
            (cx + rOut * cos(start)).toFloat(),
            (cy + rOut * sin(start)).toFloat()
        )
        segmentPath.arcTo(outerRect, startAngle, sweepAngle)

        val end = toRadians((startAngle + sweepAngle).toDouble())
        val endX = (cx + rInn * cos(end)).toFloat()
        val endY = (cy + rInn * sin(end)).toFloat()

        segmentPath.lineTo(endX, endY)
        segmentPath.arcTo(innerRect, startAngle + sweepAngle, -sweepAngle)
        segmentPath.close()

        roundCorner(
            segmentPath,
            Coordinate2F((cx + rOut * cos(start)).toFloat(), (cy + rOut * sin(start)).toFloat()),
            Coordinate2F(startX, startY)
        )
        roundCorner(
            segmentPath,
            Coordinate2F(endX, endY),
            Coordinate2F((cx + rOut * cos(end)).toFloat(), (cy + rOut * sin(end)).toFloat())
        )

        paint.apply {
            val gradient = LinearGradient(
                startX,
                startY,
                endX,
                endY,
                intArrayOf(segment.gradientStart, segment.gradientEnd),
                null,
                Shader.TileMode.CLAMP
            )
            shader = gradient
            if (segment.animate) {
                alpha = (blinkFraction * 255f).toInt()
            }
        }

        canvas.drawPath(segmentPath, paint)
    }

    private fun roundCorner(path: Path, startCoordinate: Coordinate2F, endCoordinate: Coordinate2F) {
        val radius = segmentThickness / 2
        val sweepCoordinate = getMiddleCoordinate(startCoordinate, endCoordinate)

        path.moveTo(sweepCoordinate.x, sweepCoordinate.y)

        val endOval = RectF()
        endOval[sweepCoordinate.x - radius, sweepCoordinate.y - radius, sweepCoordinate.x + radius] = sweepCoordinate.y + radius

        val startAngleEnd = (180 / Math.PI * atan2(
            startCoordinate.y - endCoordinate.y,
            startCoordinate.x - endCoordinate.x
        )).toFloat()

        path.arcTo(endOval, startAngleEnd, -180f)
        path.close()
    }

    private fun getMiddleCoordinate(start: Coordinate2F, end: Coordinate2F): Coordinate2F {
        val measurePath = Path()
        measurePath.moveTo(start.x, start.y)
        measurePath.lineTo(end.x, end.y)

        val pathMeasure = PathMeasure(measurePath, false)
        val coordinates = floatArrayOf(0f, 0f)
        pathMeasure.getPosTan(pathMeasure.length * 0.5f, coordinates, null)

        return Coordinate2F(coordinates[0], coordinates[1])
    }

    //  Helper methods

    private fun isRTL(): Boolean {
        return resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    private fun dp(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }
}