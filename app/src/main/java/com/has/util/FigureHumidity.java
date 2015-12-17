package com.has.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class FigureHumidity extends View implements HumidityChangedListener{
    private static final String TAG = FigureHumidity.class.getSimpleName();
    public static final float DEFAULT_MAX_HUMIDITY = 100;

    //internal state
    private int mMaxHumidity;
    private int mCurrentHumidity;

    //Drawing tools
    private Paint onMarkPaint;
    private Paint offMarkPaint;
    private Paint shadowPaint;
    private Paint readingPaint;
    private Path onPath;
    private Path offPath;
    final RectF oval = new RectF();
    //Drawing colors
    private int ON_COLOR = Color.argb(255,0x1A,0xBA,0xEA);
    private int OFF_COLOR = Color.argb(255,0xEA,0xEA,0xEA);
    private int SHADOW_COLOR = Color.argb(64,0x33,0x33,0x33);

    //Scale configuration
    private float centerX;
    private float centerY;
    private float radius;

    public FigureHumidity(Context context) {
        super(context);
        Log.d(TAG, "Speedometer(Context) called");
    }

    public FigureHumidity(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCurrentHumidity = 50;
        mMaxHumidity = 100;
        initDrawingTools();
    }

    public FigureHumidity(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initDrawingTools(){
        onMarkPaint = new Paint();
        onMarkPaint.setStyle(Paint.Style.STROKE);
        onMarkPaint.setColor(ON_COLOR);
        onMarkPaint.setStrokeWidth(60f);
        //onMarkPaint.setShadowLayer(5f, 0f, 0f, ON_COLOR);
        onMarkPaint.setAntiAlias(true);

        offMarkPaint = new Paint(onMarkPaint);
        offMarkPaint.setColor(OFF_COLOR);
        //offMarkPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //offMarkPaint.setShadowLayer(10f, 0f, 0f, OFF_COLOR);
        //offMarkPaint.setShadowLayer(3f, 0f, 0f, Color.WHITE);
        shadowPaint = new Paint(offMarkPaint);
        shadowPaint.setColor(SHADOW_COLOR);

        offMarkPaint.setShadowLayer(10f, 0f, 0f, SHADOW_COLOR);

        readingPaint = new Paint(offMarkPaint);
        readingPaint.setStyle(Paint.Style.FILL);
        readingPaint.setTextSize(60f);
        readingPaint.setStrokeWidth(2f);
        readingPaint.setTypeface(Typeface.SANS_SERIF);
        readingPaint.setColor(Color.BLACK);

        onPath = new Path();
        offPath = new Path();
    }
    public void setHumidity(float mCurrentHumidity) {
        if(mCurrentHumidity > this.mMaxHumidity)
            this.mCurrentHumidity = mMaxHumidity;
        else if(mCurrentHumidity < 0)
            this.mCurrentHumidity = 0;
        else
            this.mCurrentHumidity = (int) mCurrentHumidity;
    }
    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        Log.d(TAG, "Size changed to " + width + "x" + height);

        // Setting up the oval area in which the arc will be drawn
        if (width > height){
            radius = height*4/10;
        }else{
            radius = width*4/10;
        }
        oval.set(centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int chosenWidth = chooseDimension(widthMode, widthSize);
        int chosenHeight = chooseDimension(heightMode, heightSize);

        int chosenDimension = Math.min(chosenWidth, chosenHeight);
        centerX = chosenDimension / 2;
        centerY = chosenDimension / 2;
        setMeasuredDimension(chosenDimension, chosenDimension);
    }
    private int chooseDimension(int mode, int size) {
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
            return size;
        } else { // (mode == MeasureSpec.UNSPECIFIED)
            return getPreferredSize();
        }
    }

    // in case there is no size specified
    private int getPreferredSize() {
        return 300;
    }
    @Override
    public void onDraw(Canvas canvas){
        drawScaleBackground(canvas);
        drawScale(canvas);
        //drawLegend(canvas);
        drawReading(canvas);
    }

    private void drawScaleBackground(Canvas canvas){
        Log.d(TAG, "drawScaleBackground");
        offPath.reset();
        oval.set(centerX + 5 - radius,
                centerY + 10 - radius,
                centerX + 5 + radius,
                centerY + 10 + radius);
        offPath.addArc(oval, 0, 360);
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setColor(SHADOW_COLOR);
        canvas.drawPath(offPath, shadowPaint);

        offPath.reset();
        oval.set(centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius);
        offPath.addArc(oval, 0, 360);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setColor(Color.WHITE);
        canvas.drawPath(offPath, shadowPaint);

        offPath.reset();
        oval.set(centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius);
        offPath.addArc(oval, 0, 360);
        canvas.drawPath(offPath, offMarkPaint);
    }

    private void drawScale(Canvas canvas){
        onPath.reset();
        oval.set(centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius);
        onPath.addArc(oval, -90, (float)mCurrentHumidity/100*360);
        canvas.drawPath(onPath, onMarkPaint);
    }
    private void drawReading(Canvas canvas){
        Path path = new Path();
        Log.i("bbbbbb", String.valueOf(this.mCurrentHumidity));
        String message = String.format("%d", (int)this.mCurrentHumidity);
        message+="%";
        float[] widths = new float[message.length()];
        readingPaint.getTextWidths(message, widths);

        float advance = 0;
        for(double width:widths)
            advance += width;
        Log.d(TAG,"advance: "+advance);
        path.moveTo(centerX - advance / 2, centerY + advance / 6);
        path.lineTo(centerX + advance / 2, centerY + advance / 6);
        canvas.drawTextOnPath(message, path, 0f, 0f, readingPaint);
        path.close();
    }
    @Override
    public void onHumidityChanged(float newHumidityValue) {
        this.setHumidity(newHumidityValue);
        this.postInvalidate();
    }
}
