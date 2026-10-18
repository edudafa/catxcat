package com.edudafa.catxcat;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class ScreenTransition {
    public static final int
            NONE = 0,
            FADE_IN = 1,
            FADE_OUT = 2;
    public static int
            alpha,
            state,
            speed;
    static Rect
            rect;
    static Paint
            paint;


    public void initBlackScreen() {
        alpha = 255;
        speed = 20;
        state = NONE;

        paint = new Paint();
        paint.setARGB(255, 21, 5, 28);

        rect = new Rect(0, 0, 160, 120);
    }


    public void update() {
        switch (state){
            case FADE_IN:
                if (alpha > 0) {
                    alpha -= speed;
                    if (alpha <= 0) {
                        alpha = 0;
                        state = NONE;
                        Log.d("ScreenTransition", "FADE_IN OVER");
                    }
                }
                break;
            case FADE_OUT:
                if (alpha < 255)
                    alpha += speed;
                if (alpha >= 255) {
                    alpha = 255;
                    state = NONE;
                    Log.d("ScreenTransition", "FADE_OUT OVER");
                }
                break;
        }
    }

    public void draw(Canvas canvas) {
        paint.setAlpha(alpha);
        canvas.drawRect(rect, paint);
    }

    public void fadeOut(){ state = FADE_OUT; }
    public void fadeIn(){ state = FADE_IN; }
    public boolean isPlaying(){ return state != NONE; }
}