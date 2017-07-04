package com.ethanco.lib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import static com.ethanco.lib.utils.Utils.dp2px;
import static com.ethanco.lib.utils.Utils.getColor;

/**
 * Created by EthanCo on 2016/7/25.
 */
public class PasswordInput extends AppCompatEditText {
    private static final String TAG = "Z-SimplePasswordInput";

    //============================= Z-边框 ==============================/
    @ColorInt
    private int borderNotFocusedColor; //边框未选中时的颜色
    @ColorInt
    private int borderFocusedColor; //边框选中时的颜色
    private int borderWidth; //边框宽度


    //============================= Z-圆点 ==============================/
    @ColorInt
    private int dotNotFocusedColor; //圆点未选中时的颜色
    @ColorInt
    private int dotFocusedColor; //圆点选中时的颜色
    private float dotRadius; //圆点半径

    //============================= Z-背景 ==============================/
    @ColorInt
    private int backgroundColor = Color.WHITE; //背景色

    //============================= Z-画笔 ==============================/
    private Paint mBorderPaint; //边框画笔
    private Paint mDotPaint; //圆点画笔
    private Paint mBackgroundPaint; //背景画笔

    //============================= Z-方块 ==============================/
    private int boxCount = 4; //字符方块的数量
    private float boxMarge; //字符方块的marge
    private float boxRadius; //字符方块的边角圆弧
    private float[] scans;  //字符方块缩放比例数组
    private int[] alphas;   //字符方块透明度数组

    //============================= Z-其他 ==============================/
    private int currTextLen = 0; //现在输入Text长度
    private boolean focusColorChangeEnable = true; //获得焦点时颜色是否改变
    private static final InputFilter[] NO_FILTERS = new InputFilter[0];
    private boolean isFinishInflate = false; //inflate layout 是否已结束

    private RectF mBorderRect = new RectF();

    public PasswordInput(Context context) {
        this(context, null);

    }

    public PasswordInput(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取默认属性
        getDefaultVar();
        //初始化自定义属性
        initAttrVar(context, attrs);
        //初始化动画存储数组
        initAnimArr();
        //初始化画笔
        initPaint();
        //初始化EditText
        initView();
    }

    private void getDefaultVar() {
        borderNotFocusedColor = getColor(getContext(), R.color.password_input_border_not_focused);
        borderFocusedColor = getColor(getContext(), R.color.password_input_border_focused);
    }

    private void initAttrVar(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PasswordInput);
        backgroundColor = ta.getColor(R.styleable.PasswordInput_backgroundColor, backgroundColor);
        int focusedColor = ta.getColor(R.styleable.PasswordInput_focusedColor, borderFocusedColor);
        int notFocusedColor = ta.getColor(R.styleable.PasswordInput_notFocusedColor, borderNotFocusedColor);
        boxCount = ta.getInt(R.styleable.PasswordInput_boxCount, boxCount);
        focusColorChangeEnable = ta.getBoolean(R.styleable.PasswordInput_focusColorChangeEnable, true);
        dotRadius = ta.getDimension(R.styleable.PasswordInput_dotRaduis, dp2px(context, 11));
        ta.recycle();

        borderFocusedColor = focusedColor;
        borderNotFocusedColor = notFocusedColor;
        dotFocusedColor = focusedColor;
        dotNotFocusedColor = notFocusedColor;

        borderWidth = dp2px(context, 1);
        boxRadius = dp2px(context, 3);
        boxMarge = dp2px(context, 3);
    }

    private void initAnimArr() {
        scans = new float[boxCount];
        alphas = new int[boxCount];
        for (int i = 0; i < alphas.length; i++) {
            alphas[i] = 255;
        }
    }

    private void initPaint() {
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStrokeWidth(borderWidth);
        mBorderPaint.setColor(borderNotFocusedColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setColor(dotNotFocusedColor);
        mDotPaint.setStyle(Paint.Style.FILL);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(backgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
    }

    private void initView() {
        setCursorVisible(false); //光标不可见
        setInputType(InputType.TYPE_CLASS_NUMBER); //设置输入的是数字
        //设置输入最大长度
        setMaxLen(boxCount);
        setTextIsSelectable(false);//设置文字不可选中
    }

    private void setMaxLen(int maxLength) {
        if (maxLength >= 0) {
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        } else {
            setFilters(NO_FILTERS);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();

        canvas.save();

        //绘制背景
        drawBackGround(canvas, height, width);
        //绘制边框
        drawBorder(canvas, height, width);
        //绘制圆点
        drawDot(canvas, height, width);

        canvas.restore();
    }

    private void drawBackGround(Canvas canvas, int height, int width) {
        canvas.drawRect(0, 0, width, height, mBackgroundPaint);
    }

    private void drawBorder(Canvas canvas, int height, int width) {
        for (int i = 0; i < boxCount; i++) {
            RectF rect = generationSquareBoxRectF(height, width, i);
            canvas.drawRoundRect(rect, boxRadius, boxRadius, mBorderPaint);
        }
    }

    private void drawDot(Canvas canvas, int height, int width) {
        float cx, cy = height / 2;
        float half = width / boxCount / 2;
        for (int i = 0; i < boxCount; i++) {
            mDotPaint.setAlpha(alphas[i]);
            cx = width * i / boxCount + half;
            Log.i(TAG, "onDraw scans[" + i + "]: " + scans[i]);
            canvas.drawCircle(cx, cy, dotRadius * scans[i], mDotPaint);
        }
    }

    @NonNull
    private RectF generationSquareBoxRectF(int height, int width, int i) {
        float boxWidth = (width / boxCount);
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

        mBorderRect.set(left, top, right, bottom);
        return mBorderRect;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        isFinishInflate = true;
    }

    @Override
    protected MovementMethod getDefaultMovementMethod() {
        //关闭 copy/paste/cut 长按文字菜单，使文字不可长按选中
        //Note: 需 setTextIsSelectable(false) 才会生效
        return null;
    }

    @Override
    protected void onTextChanged(CharSequence text, final int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (start != text.length()) {
            moveCursorToTheEnd();
        }

//        Log.i(TAG, "onTextChanged currTextLen:" + currTextLen+" id:"+getId());
//        if (null == scans) return;
//        Log.i(TAG, "==>onTextChanged currTextLen:" + currTextLen+" id:"+getId());
//        if (isFristChangeText) {
//            isFristChangeText = false;
//            return;
//        }

        if (!isFinishInflate) {
            return;
        }

        this.currTextLen = text.toString().length();
        final boolean isAdd = lengthAfter - lengthBefore > 0;

//        new Thread() {
//            @Override
//            public void run() {
//                Log.i(TAG, "currTextLen:" + currTextLen);
//                for (int i = 0; i < currTextLen; i++) {
//                    scans[i] = 1;
//                }
//                postInvalidate();
//            }
//        }.start();

        startTextChangedAnim(isAdd);
        //通知TextChangeListen
        notifyTextChangeListener(text);
    }

    /**
     * 开始TextChanged动画
     *
     * @param isAdd true:字符从小到大 (增加) false:字符从大到小 (删除)
     */
    private void startTextChangedAnim(boolean isAdd) {
        final ValueAnimator scanAnim;
        final ValueAnimator alphaAnim;
        final int index;
        if (isAdd) {
            index = currTextLen - 1;
            scanAnim = ValueAnimator.ofFloat(0F, 1F);
            alphaAnim = ValueAnimator.ofInt(0, 255);
        } else {
            index = currTextLen;
            scanAnim = ValueAnimator.ofFloat(1F, 0F);
            alphaAnim = ValueAnimator.ofInt(255, 0);
        }

        if (scans.length >= currTextLen) {

            scanAnim.setDuration(750);
            scanAnim.setRepeatCount(0);
            scanAnim.setInterpolator(new OvershootInterpolator());
            scanAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float scale = (float) valueAnimator.getAnimatedValue();
                    scans[index] = scale;
                    postInvalidate();
                }
            });

            alphaAnim.setDuration(750);
            alphaAnim.setRepeatCount(0);
            alphaAnim.setInterpolator(new LinearInterpolator());
            alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int alpha = (int) valueAnimator.getAnimatedValue();
                    alphas[index] = alpha;
                    postInvalidate();
                }
            });

            scanAnim.start();
            alphaAnim.start();
        }
    }

    private void notifyTextChangeListener(CharSequence text) {
        if (null != textLenChangeListener) {
            textLenChangeListener.onTextLenChange(text, currTextLen);
        }
    }

    @Override
    protected void onFocusChanged(final boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if (focused) {
            moveCursorToTheEnd();
        }

        if (focusColorChangeEnable) {
            startFocusChangedAnim(focused);
        }
    }

    /**
     * 开始FocusChanged动画
     *
     * @param focused 是否获得焦点
     */
    private void startFocusChangedAnim(final boolean focused) {
        final ValueAnimator scanAnim;

        scanAnim = ValueAnimator.ofFloat(1F, 0.1F, 1F);

        scanAnim.setDuration(750);
        scanAnim.setRepeatCount(0);
        scanAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        scanAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float scale = (float) valueAnimator.getAnimatedValue();
                for (int i = 0; i < scans.length; i++) {
                    if (scans[i] != 0) {
                        scans[i] = scale;
                    }
                }
                if (scale <= 0.15) {
                    if (focused) {
                        mBorderPaint.setColor(borderFocusedColor);
                        mDotPaint.setColor(dotFocusedColor);
                    } else {
                        mBorderPaint.setColor(borderNotFocusedColor);
                        mDotPaint.setColor(dotNotFocusedColor);
                    }
                }
                postInvalidate();
            }
        });
        scanAnim.start();
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);

        if (selEnd != getText().length()) {
            moveCursorToTheEnd();
        }
    }

    private void moveCursorToTheEnd() {
        setSelection(getText().length());
    }

    public interface TextLenChangeListener {
        void onTextLenChange(CharSequence text, int len);
    }

    private TextLenChangeListener textLenChangeListener;

    /**
     * 设置Text长度改变监听
     *
     * @param lenListener 监听
     */
    public void setTextLenChangeListener(TextLenChangeListener lenListener) {
        textLenChangeListener = lenListener;
    }

    public void setBorderNotFocusedColor(@ColorInt int borderNotFocusedColor) {
        this.borderNotFocusedColor = borderNotFocusedColor;
    }

    public void setBorderFocusedColor(@ColorInt int borderFocusedColor) {
        this.borderFocusedColor = borderFocusedColor;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public void setDotNotFocusedColor(@ColorInt int dotNotFocusedColor) {
        this.dotNotFocusedColor = dotNotFocusedColor;
    }

    public void setDotFocusedColor(@ColorInt int dotFocusedColor) {
        this.dotFocusedColor = dotFocusedColor;
    }

    public void setDotRadius(float dotRadius) {
        this.dotRadius = dotRadius;
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBoxCount(int boxCount) {
        this.boxCount = boxCount;
    }

    public void setBoxMarge(float boxMarge) {
        this.boxMarge = boxMarge;
    }

    public void setBoxRadius(float boxRadius) {
        this.boxRadius = boxRadius;
    }

    public void setFocusColorChangeEnable(boolean focusColorChangeEnable) {
        this.focusColorChangeEnable = focusColorChangeEnable;
    }
}
