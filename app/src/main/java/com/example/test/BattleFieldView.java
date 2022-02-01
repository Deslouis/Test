package com.example.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class BattleFieldView extends View {

    //int startTime = 0;
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

    public BattleFieldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.BattleFieldView, 0, 0);


        if (a.hasValue(R.styleable.BattleFieldView_myDrawable)) {
            background = a.getDrawable(
                    R.styleable.BattleFieldView_myDrawable);
            background.setCallback(this);
        }
        a.recycle();

        for (int i = 0; i < 0; i++) {
            //hand.add(background);
        }


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


    public void AddImage(Drawable image){
        hand.add(image);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        contentWidth = getWidth()-getPaddingLeft()-getPaddingRight();
        contentHeight = getHeight()-getPaddingTop()-getPaddingBottom();
        offset = contentWidth/6;
        handHeight = contentHeight*3/5;

        midZone.moveTo(0,0);
        midZone.lineTo(contentWidth,0);
        midZone.lineTo(contentWidth,handHeight);
        midZone.lineTo(0,handHeight);
        midZone.lineTo(0,0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //long elapsedTime = System.currentTimeMillis() - startTime;



        //canvas.drawPath(path, paint);


        if(showZone){
            canvas.drawPath(midZone, paint);
        }
        //DrawHand
        for (int i = 0; i < hand.size(); i++) {
            drawImage(hand.get(i), i*(contentWidth-2*offset)/(hand.size()-1)+offset, contentHeight*4/5, contentHeight/5.0, canvas);
        }
        if(focus != null){
            drawImage(focus, contentWidth/2, contentHeight/2, contentHeight/2.0, canvas);
        }

        if(selection!=null){
            drawImage(selection, x, y, contentHeight/4.0, canvas);
        }

        this.postInvalidateDelayed( 1000 / framesPerSecond);
    }

    private void drawImage(Drawable image, int x, int y, double size, Canvas canvas){
        Matrix matrix = new Matrix();
        matrix.setRotate(45, x, y);

        float ratio = 5/7f;//(float)image.getIntrinsicWidth()/ image.getIntrinsicHeight();
        image.setBounds((int)(x-size*ratio/2), (int)(y-size/2), (int)(x+size*ratio/2), (int)(y+size/2));
        //canvas.drawBitmap(drawableToBitmap(image), matrix, null);
        image.draw(canvas);


        //Bitmap bitmap = Bitmap.createBitmap((int)(size*ratio), (int)(size), Bitmap.Config.ARGB_8888);
        //Canvas cv = new Canvas(bitmap);
        //image.setBounds(0, 0, cv.getWidth(), cv.getHeight());
        //image.draw(cv);

        //Rect rect = new Rect((int)(x-size*ratio/2), (int)(y-size/2), (int)(x+size*ratio/2), (int)(y+size/2));
        //canvas.drawBitmap(bitmap, null, rect, null);
    }

    private void ShowCardInHand(){
        int index = (x-offset)*hand.size()/(contentWidth-2*offset);
        if(index>=0 && index<hand.size()){
            focus = hand.get(index);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        x=(int)eventX;
        y=(int)eventY;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //path.moveTo(eventX, eventY);

                if(y>handHeight){
                    ShowCardInHand();
                }


                return true;
            case MotionEvent.ACTION_MOVE:
                //path.lineTo(eventX, eventY);

                if(y<handHeight && focus != null){
                    selection=focus;
                    hand.remove(selection);
                    focus = null;
                }else{
                    if(selection==null){
                        ShowCardInHand();
                    }else{
                        showZone= y < handHeight;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                focus = null;
                if(selection!=null){
                    hand.add(selection);
                    selection=null;
                }
                showZone=false;
                break;
            default:
                return false;
        }
        // Schedules a repaint.
        invalidate();
        return true;
    }
    public static Bitmap drawableToBitmap (Drawable drawable) { // https://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}

