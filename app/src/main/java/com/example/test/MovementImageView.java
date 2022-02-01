package com.example.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class MovementImageView extends View {
    int framesPerSecond = 60;

    private Paint paint = new Paint();
    private Path path = new Path();

    int x, y = 0;

    int contentHeight;
    int contentWidth;

    int offset;

    int handHeight;

    boolean showZone;
    Path midZone = new Path();

    Drawable background = null;
    Drawable focus = null;
    Drawable selection = null;
    ArrayList<Drawable> hand = new ArrayList<>();

    public MovementImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MovementImageView, 0, 0);


        if (a.hasValue(R.styleable.MovementImageView_exampleDrawable)) {
            background = a.getDrawable(
                    R.styleable.MovementImageView_exampleDrawable);
            background.setCallback(this);
        }
        a.recycle();


        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(15f);
        paint.setColor(Color.argb(155, 200, 100, 100));

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        contentWidth = getWidth()-getPaddingLeft()-getPaddingRight();
        contentHeight = getHeight()-getPaddingTop()-getPaddingBottom();
        offset = contentWidth/6;
        handHeight = contentHeight*3/5;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //long elapsedTime = System.currentTimeMillis() - startTime;



        //canvas.drawPath(path, paint);

        if(background!=null){
            drawImage(background, x, y, contentHeight/4.0, canvas);
        }

        this.postInvalidateDelayed( 1000 / framesPerSecond);
    }

    private void drawImage(Drawable image, int x, int y, double size, Canvas canvas){

        float ratio = 5/7f;
        image.setBounds((int)(x-size*ratio/2), (int)(y-size/2), (int)(x+size*ratio/2), (int)(y+size/2));
        image.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        x=(int)eventX;
        y=(int)eventY;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        // Schedules a repaint.
        invalidate();
        return true;
    }
}