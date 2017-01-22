package ecnu.cs14.garagelocation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * An experimental MapView.
 * Created by K on 2017/1/21.
 */

public class MapView extends View {
    private final static String TAG = MapView.class.getName();

    private Paint mPaint;

    public MapView(Context context) {
        super(context);
        initPaint();
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(48);
    }

    private String mString;

    public void setText(String string) {
        Log.i(TAG, "setText: String set:" + string);
        mString = string;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG, "onMeasure: wMode: " + wMode + " hMode: " + hMode + " wSize: " + wSize + " hSize: " + hSize);
        setMeasuredDimension(wSize, hSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: left: " + getLeft() + " top: " + getTop());
        if (null != mString) {
            canvas.drawText(mString, getLeft(), getTop() + 48 * 2, mPaint);
        }
    }
}
