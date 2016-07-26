package com.ethanco.lib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import com.ethanco.lib.utils.DisplayUtil;

/**
 * Created by EthanCo on 2016/7/25.
 */
public class SimplePasswordInput extends EditText {

    private static final String TAG = "Z-";
    //============================= Z-边框 ==============================/
    @ColorInt
    private int borderUncheckedColor = Color.BLACK; //边框未选中时的颜色
    @ColorInt
    private int borderCheckedColor = Color.BLUE; //边框选中时的颜色
    private int borderWidth; //边框宽度


    //============================= Z-圆点 ==============================/
    @ColorInt
    private int dotUncheckedColor = Color.BLACK; //圆点未选中时的颜色
    @ColorInt
    private int dotCheckedColor = Color.BLUE; //圆点选中时的颜色
    //@ColorInt
    //private int dotNotInputColor = Color.TRANSPARENT; //圆点没有输入时的颜色:透明
    private int dotRadius; //圆点半径

    //============================= Z-背景 ==============================/
    @ColorInt
    private int backgroundColor = Color.WHITE; //背景色

    //============================= Z-画笔 ==============================/
    private final Paint mBorderPaint; //边框画笔
    private final Paint mDotPaint; //圆点话题
    private final Paint mBackgroundPaint; //背景画笔

    //============================= Z-其他 ==============================/
    private final int passwordLen = 4;
    private float[] scanArr;
    private float boxMarge = 10;
    private float boxRadius;
    private int currTextLen = 0; //现在输入Text长度

    public SimplePasswordInput(Context context, AttributeSet attrs) {
        super(context, attrs);

        borderWidth = DisplayUtil.dp2px(context, 1);
        boxRadius = DisplayUtil.dp2px(context, 3);

        scanArr = new float[passwordLen];
//        for (int i = 0; i < scanArr.length; i++) {
//            scanArr[i] = 1;
//        }

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStrokeWidth(borderWidth);
        mBorderPaint.setColor(borderUncheckedColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setColor(dotUncheckedColor);
        mDotPaint.setStyle(Paint.Style.FILL);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(backgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        dotRadius = (int) (getHeight() / 5F);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();

        canvas.save();

        //背景
        canvas.drawRect(0, 0, width, height, mBackgroundPaint);

        //边框
        for (int i = 0; i < passwordLen; i++) {
            RectF rect = generationSquareBoxRectF(height, width, i);
            canvas.drawRoundRect(rect, boxRadius, boxRadius, mBorderPaint);
        }

        //圆点
        float cx, cy = height / 2;
        float half = width / passwordLen / 2;
        for (int i = 0; i < passwordLen; i++) {
            cx = width * i / passwordLen + half;
            Log.i(TAG, "onDraw scanArr[" + i + "]: " + scanArr[i]);
            canvas.drawCircle(cx, cy, dotRadius * scanArr[i], mDotPaint);
        }

        canvas.restore();
    }

    @NonNull
    private RectF generationSquareBoxRectF(int height, int width, int i) {
        float boxWidth = (width / passwordLen);
        float boxHeight = height;
        float left = boxMarge + boxWidth * i;
        float right = boxWidth * (i + 1) - boxMarge;
        float top = boxMarge;
        float bottom = boxHeight - boxMarge;

        float min = Math.min(boxWidth, boxHeight);

        float dw = (boxWidth - min) / 2F;
        float dh = (boxHeight - min) / 2F;
        left += dw;
        right -= dw;
        top += dh;
        bottom -= dh;

        return new RectF(left, top, right, bottom);
    }

    //len=6 currTextLen=5 index=5
    //len=5 currTextLen=4 index=4
    //len=4 currTextLen=3 index=3
    //len=3 currTextLen=3 index=3
    //len=2 currTextLen=2 index=2
    //len=1 currTextLen=1 index=1
    //len=0 currTextLen=0 index=0

    //len=6 currTextLen=5 index=5
    //len=5 currTextLen=4 index=4
    //len=4 currTextLen=3 index=3


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return super.onCreateInputConnection(outAttrs);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        Log.i(TAG, "onTextChanged lengthBefore: " + lengthBefore + " lengthAfter:" + lengthAfter);
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (null == scanArr) return;

        final boolean isAdd = lengthAfter - lengthBefore > 0 ? true : false;
        this.currTextLen = text.toString().length();

        final ValueAnimator scanAnim;
        final int index;
        if (isAdd) {
            index = currTextLen - 1;
            scanAnim = ValueAnimator.ofFloat(0F, 1F);
        } else {
            index = currTextLen;
            scanAnim = ValueAnimator.ofFloat(1F, 0F);
        }
        if (scanArr.length >= currTextLen) {
            scanAnim.setDuration(750);
            scanAnim.setRepeatCount(0);
            scanAnim.setInterpolator(new OvershootInterpolator());
            scanAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Log.i(TAG, "onAnimationUpdate index=" + index + ": " + valueAnimator.getAnimatedValue());

                    float value = (float) valueAnimator.getAnimatedValue();

                    scanArr[index] = value;
                    postInvalidate();
                }
            });
            scanAnim.start();
        }

        //invalidate();
        if (null != textLenChangeListen) {
            textLenChangeListen.onTextLenChange(text, currTextLen);
        }

    }

    public interface TextLenChangeListen {
        void onTextLenChange(CharSequence text, int len);
    }

    private TextLenChangeListen textLenChangeListen;

    public void setTextLenChangeListen(TextLenChangeListen lenListen) {
        textLenChangeListen = lenListen;
    }
}
