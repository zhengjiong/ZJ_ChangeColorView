package com.zj.example.customview.changecolorview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhengjiong on 16/1/30.
 */
public class ChangeColorView extends View {
    private int mColor = 0xff45c01c;
    private Bitmap mIconBitmap;
    private String mText = "消息";
    private int mTextSize = TypedValue.complexToDimensionPixelSize(12, getResources().getDisplayMetrics());
    private int mTextMarginTop;

    private Rect mIconRect;

    private float mAlpha = 0f;


    private Bitmap mAlphaBitmap;
    private Paint mAlphaPaint;

    private Paint mTextPaint;
    private Rect mTextBound;


    public ChangeColorView(Context context) {
        this(context, null);
    }

    public ChangeColorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ChangeColorView);

        mColor = typedArray.getColor(R.styleable.ChangeColorView_changecolor_color, 0xff45c01c);
        mText = typedArray.getString(R.styleable.ChangeColorView_changecolor_text);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.ChangeColorView_changecolor_textsize, TypedValue.complexToDimensionPixelSize(12, getResources().getDisplayMetrics()));
        Drawable iconDrawable = typedArray.getDrawable(R.styleable.ChangeColorView_changecolor_icon);
        mIconBitmap = ((BitmapDrawable)iconDrawable).getBitmap();

        mAlphaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mTextBound = new Rect();
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xff333333);

        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);

        mTextMarginTop = dip2px(getContext(), 5f);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //这个必须加, 不然没有外层的边框
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);

        int alpha = (int) (255 * mAlpha);
        setupBitmap(alpha);

        //画内部的绿色
        canvas.drawBitmap(mAlphaBitmap, 0, 0, null);

        //绘制原文本
        drawSourceText(canvas, alpha);
        //繪製變色文本
        drawTargetText(canvas, alpha);
    }

    /**
     * 绘制原文本
     * @param canvas
     * @param alpha
     */
    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(0xFF333333);
        mTextPaint.setAlpha(255 - alpha);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconRect.bottom + getPaddingTop() + mTextMarginTop;
        canvas.drawText(mText, x, y, mTextPaint);
    }

    /**
     * 绘制变色文本
     * @param canvas
     * @param alpha
     */
    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);

        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconRect.bottom + getPaddingTop() + mTextMarginTop;
        canvas.drawText(mText, x, y, mTextPaint);
    }



    private void setupBitmap(int alpha) {

        mAlphaBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mAlphaBitmap);


        //這裡必須要new一個新的
        mAlphaPaint = new Paint();

        mAlphaPaint.setDither(true);// 防抖动
        mAlphaPaint.setColor(mColor);
        mAlphaPaint.setAlpha(alpha);

        /**
         canvas原有的图片 可以理解为背景 就是dst
         新画上去的图片 可以理解为前景 就是src
         */
        canvas.drawRect(mIconRect, mAlphaPaint);//绘制src(下层图片)
//        mAlphaPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));// 显示上层绘制图片
//        mAlphaPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST));// 显示下层绘制图片
//        mAlphaPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));//正常绘制显示，上下层绘制叠盖。
//        mAlphaPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));//上下层都显示。下层居上显示。
//        mAlphaPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//取两层绘制交集。显示上层。
        mAlphaPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));//取两层绘制交集。显示下层。

        canvas.drawBitmap(mIconBitmap, null, mIconRect, mAlphaPaint);//绘制dst(上层图片)

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int iconWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int iconHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mTextBound.height();

        int rectWidth = Math.min(iconWidth, iconHeight);

        int left = getMeasuredWidth() / 2 - rectWidth / 2 ;
        int top = getMeasuredHeight() / 2 - (rectWidth + mTextBound.height()) / 2;


        mIconRect = new Rect(left, top, left + rectWidth, top + rectWidth);
    }

    public void setAlpha(float mAlpha) {
        this.mAlpha = mAlpha;
        postInvalidate();
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
