package com.loopeer.android.apps.startuptools.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.loopeer.android.apps.startuptools.R;

/**
 * Created by tudou on 15-3-21.
 */
public class ShapeTextView extends View {
    private static final int DEFAULT_HEIGHT_DP = 10;
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.rgb(56, 43, 100);

    private float default_text_size;
    private float default_triangle_height;

    private Path mBackgroundPath = new Path();

    private Paint mBackgroundPaint;
    private Paint mTextPaint;

    private float mTriangle_height;
    private boolean isShowTriangle;

    private int mBackgroundColor;
    private int mTextColor;

    private String mTextContent = "你好";
    private float mTextStart;
    private float mTextEnd;
    private float mTextSize;

    public ShapeTextView(Context context) {
        this(context, null);
    }

    public ShapeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ShapeTextView,
                defStyleAttr, 0);

        mBackgroundColor = attributes.getColor(R.styleable.ShapeTextView_background_color, DEFAULT_BACKGROUND_COLOR);
        mTextSize = attributes.getDimension(R.styleable.ShapeTextView_text_size, default_text_size);
        mTextColor = attributes.getColor(R.styleable.ShapeTextView_text_color, DEFAULT_TEXT_COLOR);
        mTriangle_height = attributes.getDimension(R.styleable.ShapeTextView_triangle_height, default_triangle_height);

        initializePainters();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateBackground();
        canvas.drawPath(mBackgroundPath, mBackgroundPaint);

        calculateText();
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        //计算文字高度
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        //计算文字baseline
        float textWidth = mTextPaint.measureText(mTextContent);
        float textBaseY = getHeight() - (getHeight() - fontHeight)/2 - fontMetrics.bottom;
        canvas.drawText(mTextContent, getWidth() / 2 - textWidth / 2, textBaseY, mTextPaint);
    }

    private void init() {
        default_text_size = sp2px(20);
        default_triangle_height = dp2px(DEFAULT_HEIGHT_DP);

        mTextColor = DEFAULT_TEXT_COLOR;
        mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
        mTriangle_height = default_triangle_height;
        mTextSize = default_text_size;
    }

    private void initializePainters() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(mBackgroundColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
    }

    private void calculateText() {
        float textWidth = mTextPaint.measureText(mTextContent);
        mTextStart = getWidth() / 2 - textWidth / 2;
        mTextEnd = getWidth() / 2 + textWidth / 2;
    }

    private void calculateBackground() {
        mBackgroundPath.reset();
        mBackgroundPath.setFillType(Path.FillType.EVEN_ODD);
        mBackgroundPath.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CW);
        if (isShowTriangle) {
            mBackgroundPath.moveTo(getWidth() - mTriangle_height, getHeight() / 2);
            mBackgroundPath.lineTo(getWidth(), getHeight() / 2 - mTriangle_height);
            mBackgroundPath.lineTo(getWidth(), getHeight() / 2 + mTriangle_height);
            mBackgroundPath.lineTo(getWidth() - mTriangle_height, getHeight() / 2);
        }
    }

    public void reDraw() {
        invalidate();
    }

    public void showTriangle() {
        isShowTriangle = true;
        invalidate();
    }

    public void hideTriangle() {
        isShowTriangle = false;
        invalidate();
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        mBackgroundPaint.setColor(backgroundColor);
        invalidate();
    }

    public void setText(String text) {
        mTextContent = text;
        invalidate();
    }

    public void setTextColor(int color) {
        mTextColor = color;
        mTextPaint.setColor(mTextColor);
        invalidate();
    }

    public void setTextSize(float size) {
        mTextSize = size;
        mTextPaint.setTextSize(size);
        invalidate();
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

}
