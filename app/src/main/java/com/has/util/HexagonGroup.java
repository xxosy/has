package com.has.util;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.has.R;
import com.has.data.Sensor;
import com.has.data.Sensors;

public class HexagonGroup extends View {
    private static final String TAG = HexagonGroup.class.getSimpleName();
    public static final float DEFAULT_MAX_HUMIDITY = 100; // Assuming this is km/h and you drive a super-car

    // Speedometer internal state
    private float mMaxHumidity;
    private float mCurrentHumidity[];

    // Scale drawing tools
    private Paint hexagonPaint;
    private Paint borderPaint;
    private Paint readingPaint;
    private Path hexagonPath;
    private Path hexagonBorderPath;
    final RectF oval = new RectF();

    // Drawing colors
    private int BORDER_COLOR = Color.argb(0xff, 0x54, 0x82, 0x35);
    private int INNER_COLOR = Color.argb(255,0x3e,0x3e,0x3e);
    private float SCALE_SIZE = 14f;
    private float READING_SIZE = 60f;

    // Scale configuration
    private float centerX;
    private float centerY;
    private float radius;

    private float[] itemPositionX;
    private float[] itemPositionY;

    private int toggle;
    int size =0;
    public HexagonGroup(Context context){
        super(context);
        Log.d(TAG, "HexagonGroup(Context) called");
        initDrawingTools();
    }

    public void setToggle(int toggle) {
        this.toggle = toggle;
    }

    public HexagonGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "HexagonGroup(Context, AttributeSet) called");
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.HexagonGroup,
                0, 0);
        try{
            BORDER_COLOR = a.getColor(R.styleable.HexagonGroup_borderColor,BORDER_COLOR);
            mMaxHumidity = a.getFloat(R.styleable.HexagonGroup_maxHumidity, DEFAULT_MAX_HUMIDITY);
//            mMaxSpeed = a.getFloat(R.styleable.Speedometer_maxSpeed, DEFAULT_MAX_SPEED);
//            mCurrentSpeed = a.getFloat(R.styleable.Speedometer_currentSpeed, 0);
//            ON_COLOR = a.getColor(R.styleable.Speedometer_onColor, ON_COLOR);
//            OFF_COLOR = a.getColor(R.styleable.Speedometer_offColor, OFF_COLOR);
//            SCALE_COLOR = a.getColor(R.styleable.Speedometer_scaleColor, SCALE_COLOR);
//            SCALE_SIZE = a.getDimension(R.styleable.Speedometer_scaleTextSize, SCALE_SIZE);
//            READING_SIZE = a.getDimension(R.styleable.Speedometer_readingTextSize, READING_SIZE);
        } finally{
            a.recycle();
        }

        initDrawingTools();
    }

    private void initDrawingTools(){
        hexagonPaint = new Paint();
        hexagonPaint.setStyle(Paint.Style.FILL);
        hexagonPaint.setColor(Color.argb(0x33, 0xff, 0xff, 0xff));
        hexagonPaint.setAntiAlias(true);

        borderPaint = new Paint(hexagonPaint);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(BORDER_COLOR);
        borderPaint.setStrokeWidth(25f);

        readingPaint = new Paint(hexagonPaint);
        readingPaint.setStyle(Paint.Style.FILL);
        readingPaint.setTextSize(65f);
        readingPaint.setTypeface(Typeface.SANS_SERIF);
        readingPaint.setColor(Color.WHITE);

        hexagonPath = new Path();
        hexagonBorderPath = new Path();

        itemPositionX =new float[11];
        itemPositionY =new float[11];

    }
    public void initItemPosition(){
        itemPositionX[0] = (float)(centerX-Math.sqrt(3)*radius/2);
        itemPositionY[0] = centerY-radius*3/2;
        itemPositionX[1] = (float) (centerX + Math.sqrt(3) * radius / 2);
        itemPositionY[1] = centerY - radius * 3 / 2;
        itemPositionX[2] = (float)(centerX-Math.sqrt(3)*radius);
        itemPositionY[2] = centerY;
        itemPositionX[4] = (float)(centerX+Math.sqrt(3)*radius);
        itemPositionY[4] = centerY;
        itemPositionX[3] = centerX;
        itemPositionY[3] = centerY;
        itemPositionX[5] = (float) (centerX - Math.sqrt(3) * radius / 2);
        itemPositionY[5] = centerY + radius * 3 / 2;
        itemPositionX[6] = (float) (centerX + Math.sqrt(3) * radius / 2);
        itemPositionY[6] = centerY + radius * 3 / 2;
    }
    public float[] getCurrentHumidity() {
        return mCurrentHumidity;
    }

    public void setCurrentHumidity(float[] mCurrentHumidity) {
        this.mCurrentHumidity = new float[mCurrentHumidity.length];
        for(int i = 0;i<getItemLength();i++) {
            if(mCurrentHumidity[i]==1000) {
                this.mCurrentHumidity[i] = 1000;
                continue;
            }
            else if (mCurrentHumidity[i] > this.mMaxHumidity)
                this.mCurrentHumidity[i] = mMaxHumidity;
            else if (mCurrentHumidity[i] < 0)
                this.mCurrentHumidity[i] = 0;
            else
                this.mCurrentHumidity[i] = mCurrentHumidity[i];
        }
        this.postInvalidate();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        Log.d(TAG, "Size changed to " + width + "x" + height);

        // Setting up the oval area in which the arc will be drawn
        if (width > height){
            radius = height/6;
        }else{
            radius = width/6;
        }

        oval.set(centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		Log.d(TAG, "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
//		Log.d(TAG, "Height spec: " + MeasureSpec.toString(heightMeasureSpec));

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
        super.onDraw(canvas);
        initItemPosition();
//        drawShadow(canvas, (float) (centerX - Math.sqrt(3) * radius / 2), centerY + radius * 3 / 2);
//        drawShadow(canvas, (float) (centerX + Math.sqrt(3) * radius / 2), centerY + radius * 3 / 2);
//        drawShadow(canvas,(float)(centerX+Math.sqrt(3)*radius),centerY);
        for(int i = 0;i<7;i++) {
            drawHexagon(canvas, itemPositionX[i], itemPositionY[i]);
        }
        for(int i = 0;i<getItemLength();i++){
            drawItem(canvas, itemPositionX[i], itemPositionY[i], i);
            if(toggle==0)
                drawReading(canvas,itemPositionX[i], itemPositionY[i],i);
        }
//        drawScale(canvas);
//        drawReading(canvas);
    }

    /**
     * Draws the segments in their OFF state
     * @param canvas
     */
    private void drawHexagon(Canvas canvas,float centerX, float centerY){
        hexagonPath.reset();
        hexagonBorderPath.reset();
        float radiusHexagon = (float)(radius-12.5);
        float triangleHeight = (float) (Math.sqrt(3) * radiusHexagon / 2);
        hexagonPath.moveTo(centerX, centerY + radiusHexagon);
        hexagonPath.lineTo(centerX - triangleHeight, centerY + radiusHexagon / 2);
        hexagonPath.lineTo(centerX - triangleHeight, centerY - radiusHexagon/2);
        hexagonPath.lineTo(centerX, centerY - radiusHexagon);
        hexagonPath.lineTo(centerX + triangleHeight, centerY - radiusHexagon/2);
        hexagonPath.lineTo(centerX + triangleHeight, centerY + radiusHexagon/2);
        hexagonPath.moveTo(centerX, centerY + radiusHexagon);
        canvas.drawPath(hexagonPath, hexagonPaint);
        float radiusBorder = radius;
        float triangleBorderHeight = (float) (Math.sqrt(3) * radiusBorder / 2);
        hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY + radiusBorder / 2);
        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY - radiusBorder / 2);
        hexagonBorderPath.lineTo(centerX, centerY - radiusBorder);
        hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY - radiusBorder / 2);
        hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY + radiusBorder / 2);
        hexagonBorderPath.lineTo(centerX, centerY + radiusBorder);
        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY + radiusBorder / 2);
        canvas.drawPath(hexagonBorderPath, borderPaint);

//        canvas.clipPath(hexagonBorderPath, Region.Op.DIFFERENCE);

//        canvas.save();
//        canvas.clipPath(hexagonPath, Region.Op.DIFFERENCE);

//        canvas.save();
    }
    private void drawItem(Canvas canvas,float X, float Y,int i){
        Path path = new Path();
        Paint paint = new Paint(hexagonPaint);
        paint.setColor(Color.argb(255, 0, 191, 255));
        if(mCurrentHumidity[i] == 1000) paint.setColor(Color.argb(255,255,201,14));
        float radiusHexagon = radius-100;
        float triangleHeight = (float) (Math.sqrt(3) * radiusHexagon / 2);
        path.moveTo(X, Y + radiusHexagon);
        path.lineTo(X - triangleHeight, Y + radiusHexagon / 2);
        path.lineTo(X - triangleHeight, Y - radiusHexagon/2);
        path.lineTo(X, Y - radiusHexagon);
        path.lineTo(X + triangleHeight, Y - radiusHexagon/2);
        path.lineTo(X + triangleHeight, Y + radiusHexagon / 2);
        path.moveTo(X, Y + radiusHexagon);
        paint.setAlpha((int)(mCurrentHumidity[i]/100*255));
        canvas.drawPath(path, paint);
    }

    private void drawScale(Canvas canvas) {

    }

    private void drawReading(Canvas canvas, float X, float Y,int i){
        Path path = new Path();
        String message = String.format("%d", (int)this.mCurrentHumidity[i]);
        if(this.mCurrentHumidity[i]==1000){
            message = "NOT";
        }
        float[] widths = new float[message.length()];
        readingPaint.getTextWidths(message, widths);
        float advance = 0;
        for(double width:widths)
            advance += width;
        Log.d(TAG,"advance: "+advance);
        path.moveTo(X - advance/2, Y+30);
        path.lineTo(X + advance/2, Y+30);
        canvas.drawTextOnPath(message, path, 0f, 0f, readingPaint);
    }
    public int getTouchItem(float x, float y){
        float radiusHexagon = (float)(radius-12.5);
        float triangleHeight = (float) (Math.sqrt(3) * radiusHexagon / 2);
        for(int i = 0;i<7;i++){
            if(x>itemPositionX[i]-triangleHeight && x<itemPositionX[i]+triangleHeight
                    && y<itemPositionY[i]+radiusHexagon/2 && y>itemPositionY[i]-radiusHexagon/2){
                if(i<size)
                    return i;
            }
        }
        return 11;
    }
    public int getItemLength(){

        return size;
    }
    public void setCurrentRegion(int size){
        this.size= size;
        mCurrentHumidity = new float[getItemLength()];
        for(int i = 0;i<getItemLength();i++){
            mCurrentHumidity[i] = 100;
        }
        this.invalidate();
    }

}