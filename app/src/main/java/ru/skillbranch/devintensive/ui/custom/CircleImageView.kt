package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import ru.skillbranch.devintensive.R

/**
 * @author Sergey Susev
 */
class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ImageView(context, attrs, defStyle) {

    private val DEFAULT_BORDER_WIDTH_DIM = 2f
    private val DEFAULT_BORDER_COLOR = Color.WHITE

    private val mDrawableRect = RectF()
    private val mBorderRect = RectF()

    private val mShaderMatrix = Matrix()
    private val mBitmapPaint = Paint()
    private val mBorderPaint = Paint()

    private var mBorderColor = DEFAULT_BORDER_COLOR
    private var mBorderWidth = DEFAULT_BORDER_WIDTH_DIM

    private var mBitmap: Bitmap? = null
    private var mBitmapShader: BitmapShader? = null

    private var mDrawableRadius: Float = 0.toFloat()
    private var mBorderRadius: Float = 0.toFloat()

    private var mColorFilter: ColorFilter? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0)

        mBorderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH_DIM)
        mBorderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)

        a.recycle()

        super.setScaleType(ScaleType.CENTER_CROP)

        setup()
    }

    override fun onDraw(canvas: Canvas) {
        if (mBitmap == null) {
            return
        }

        canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mBitmapPaint)
        if (mBorderWidth > 0) {
            canvas.drawCircle(mBorderRect.centerX(), mBorderRect.centerY(), mBorderRadius, mBorderPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }


    fun setBorderWidth(borderWidth: Float) {
        if (borderWidth == mBorderWidth) {
            return
        }

        mBorderWidth = borderWidth
        setup()
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        initializeBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
    }

    override fun setColorFilter(cf: ColorFilter) {
        if (cf === mColorFilter) {
            return
        }

        mColorFilter = cf
        applyColorFilter()
        invalidate()
    }

    override fun getColorFilter(): ColorFilter? {
        return mColorFilter
    }

    private fun applyColorFilter() {
        mBitmapPaint.colorFilter = mColorFilter
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        return try {

            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    private fun initializeBitmap() {
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    private fun setup() {
        val borderWidthPx = mBorderWidth * context.resources.displayMetrics.density
        if (width == 0 && height == 0) {
            return
        }

        if (mBitmap == null) {
            invalidate()
            return
        }

        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        mBitmapPaint.isAntiAlias = true
        mBitmapPaint.shader = mBitmapShader

        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = mBorderColor
        mBorderPaint.strokeWidth = borderWidthPx

        mBorderRect.set(calculateBounds())
        mBorderRadius =
            Math.min((mBorderRect.height() - borderWidthPx) / 2.0f, (mBorderRect.width() - borderWidthPx) / 2.0f)

        mDrawableRect.set(mBorderRect)
        if (borderWidthPx > 0) {
            mDrawableRect.inset(borderWidthPx - 1.0f, borderWidthPx - 1.0f)
        }
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f)

        applyColorFilter()
        updateShaderMatrix()
        invalidate()
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = Math.min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        mShaderMatrix.set(null)

        if (mBitmap!!.width * mDrawableRect.height() > mDrawableRect.width() * mBitmap!!.height) {
            scale = mDrawableRect.height() / mBitmap!!.height.toFloat()
            dx = (mDrawableRect.width() - mBitmap!!.width * scale) * 0.5f
        } else {
            scale = mDrawableRect.width() / mBitmap!!.width.toFloat()
            dy = (mDrawableRect.height() - mBitmap!!.height * scale) * 0.5f
        }

        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate((dx + 0.5f).toInt() + mDrawableRect.left, (dy + 0.5f).toInt() + mDrawableRect.top)

        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
    }

    @Dimension
    fun getBorderWidth(): Int = mBorderWidth.toInt()

    fun setBorderWidth(@Dimension dp: Int) {
        if (dp == mBorderWidth.toInt()) {
            return
        }

        mBorderWidth = dp.toFloat()
        setup()
    }

    fun getBorderColor(): Int {
        return mBorderColor
    }

    fun setBorderColor(hex: String) {
        val borderColor = Color.parseColor(hex)
        if (borderColor == mBorderColor) {
            return
        }

        mBorderColor = borderColor
        mBorderPaint.color = mBorderColor
        invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        val borderColor = resources.getColor(colorId, context.theme)
        if (borderColor == mBorderColor) {
            return
        }

        mBorderColor = borderColor
        mBorderPaint.color = mBorderColor
        invalidate()
    }
}