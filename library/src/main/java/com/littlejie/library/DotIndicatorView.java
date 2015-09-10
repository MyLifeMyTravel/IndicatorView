package com.littlejie.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by LittleJie on 2015/9/10.
 */
public class DotIndicatorView extends View {

    private Paint paint;
    private int num;
    private int selectColor, defaultColor;
    //selected position
    private int position;
    //the dot indicator radius
    private float radius;
    //the actual width and height of view
    private int measureWidth, measureHeight;
    //the margin is the first or last dot to side
    private int landScapeMardin, portraitMargin;
    //the margin between two points
    private int dotsMargin;

    public DotIndicatorView(Context context) {
        this(context, null);
    }

    public DotIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.DotIndicatorView);
        //the num of indicator num
        num = typedArray.getInt(R.styleable.DotIndicatorView_dotNum, 0);
        //the indicator color while selected,default selected color is blue
        selectColor = typedArray.getColor(R.styleable.DotIndicatorView_dotSelectColor, Color.BLUE);
        //the indicator color while unselect,default unselected color is white
        defaultColor = typedArray.getColor(R.styleable.DotIndicatorView_dotDefaultColor, Color.WHITE);
        //the selected position of dot,default is 0
        position = typedArray.getInt(R.styleable.DotIndicatorView_dotPosition, 0);
        //default indicator size of dot,default is 10
        radius = typedArray.getFloat(R.styleable.DotIndicatorView_dotSize, 10);

        dotsMargin = typedArray.getInt(R.styleable.DotIndicatorView_dotsMargin, 10);
        paint = new Paint();
    }

    /**
     * 比onDraw先执行
     * <p/>
     * 一个MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求。
     * 一个MeasureSpec由大小和模式组成
     * 它有三种模式：UNSPECIFIED(未指定),父元素部队自元素施加任何束缚，子元素可以得到任意想要的大小;
     * EXACTLY(完全)，父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小；
     * AT_MOST(至多)，子元素至多达到指定大小的值。
     * <p/>
     * 　　它常用的三个函数：
     * 1.static int getMode(int measureSpec):根据提供的测量值(格式)提取模式(上述三个模式之一)
     * 2.static int getSize(int measureSpec):根据提供的测量值(格式)提取大小值(这个大小也就是我们通常所说的大小)
     * 3.static int makeMeasureSpec(int size,int mode):根据提供的大小值和模式创建一个测量值(格式)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        this.measureWidth = measureWidth(widthMeasureSpec);
        this.measureHeight = measureHeight(heightMeasureSpec);
        //calc the margin between two points
        landScapeMardin = (this.measureWidth - num * 2 * ((int) radius)
                - (num - 1) * dotsMargin) / 2;
        portraitMargin = (this.measureHeight - 2 * (int) radius) / 2;
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = (int) radius * num + getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);// 60,480
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = (int) radius * 2 + getPaddingTop() + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDots(canvas, num, position);
    }

    private void drawDots(Canvas canvas, int num, int position) {
        paint.reset();
        for (int i = 0; i < num; i++) {
            if (i == position) {
                paint.setColor(selectColor);
            } else {
                paint.setColor(defaultColor);
            }
            //计算圆的左侧边距
            int cx = landScapeMardin + (2 * i + 1) * (int) radius + i * dotsMargin;
            canvas.drawCircle(cx, portraitMargin + radius, radius, paint);
        }
    }

    /**
     * while indicator change use this method
     *
     * @param position
     */
    public void onPageIndicatorChange(int position) {
        this.position = position;
        invalidate();
    }

    public int getIndicatorSize() {
        return num;
    }
}
