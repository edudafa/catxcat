package com.edudafa.catxcat;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class GraphicEngine extends SurfaceView implements View.OnTouchListener {

    static boolean
            pressed;
    static final int
            gameResolutionX = 160,
            gameResolutionY = 120; //4:3
    static float
            pressedX,
            pressedY,
            zoomX,
            zoomY;
    static Rect
            gameRect;
    Paint
            paint;
    Bitmap
            backgroundTitle,
            foregroundTitle,
            backgroundFight,
            gameOver,
            round,
            fight,
            attackMeter,
            spriteSheet,
            tileSheet,
            gameWindow;
    Bitmap[]
            numbers = new Bitmap[10],
            whiteCatSprites = new Bitmap[13],
            blackCatSprites = new Bitmap[13];
    Canvas
            canvas,
            gameWindowCanvas;
    SurfaceHolder
            surfaceHolder = getHolder();


    public GraphicEngine(Context context) {
        super(context);
        setFocusable(true);
        setOnTouchListener(this);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        GlobalVar.deviceScreenSizeX = displayMetrics.widthPixels;
        GlobalVar.deviceScreenSizeY = displayMetrics.heightPixels;

        zoomX = (float) (GlobalVar.deviceScreenSizeX / gameResolutionX);
        zoomY = (float) (GlobalVar.deviceScreenSizeY / gameResolutionY);

        gameRect = new Rect(
                0,
                0,
                GlobalVar.deviceScreenSizeX,
                GlobalVar.deviceScreenSizeY);

        gameWindow = Bitmap.createBitmap(
                gameResolutionX,
                gameResolutionY,
                Bitmap.Config.ARGB_8888);
        gameWindowCanvas = new Canvas();
        gameWindowCanvas.setBitmap(gameWindow);

        paint = new Paint();
        paint.setColor(Color.CYAN);

        loadGraphics(context);
    }

    public void draw(GameState gameState) {
        if (surfaceHolder.getSurface().isValid()) {
            gameState.draw(gameWindowCanvas);
            Game.screenTransition.draw(gameWindowCanvas);

            canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(gameWindow, null, gameRect, null);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            pressed = false;
            pressedX = -1;
            pressedY = -1;
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            pressed = true;
            pressedX = motionEvent.getX();
            pressedY = motionEvent.getY();
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            pressed = true;
            pressedX = motionEvent.getX();
            pressedY = motionEvent.getY();
        }
        return true;
    }

    void loadGraphics(Context context){
        try {
            AssetManager assetManager = context.getAssets();
            backgroundTitle = BitmapFactory.decodeStream(assetManager.open("graphic/titleBackground.png"));
            foregroundTitle = BitmapFactory.decodeStream(assetManager.open("graphic/titleForeground.png"));
            backgroundFight = BitmapFactory.decodeStream(assetManager.open("graphic/battleBackground.png"));
            spriteSheet = BitmapFactory.decodeStream(assetManager.open("graphic/catSprites.png"));
            tileSheet = BitmapFactory.decodeStream(assetManager.open("graphic/tiles.png"));
        } catch (Exception e) {
            Log.d("GraphicLoader", "couldn't load graphics");
        }

        loadSprites();
        loadTiles();
    }

    void loadSprites() {
        for (int i = 0; i < whiteCatSprites.length - 1; i++) {
            whiteCatSprites[i] = Bitmap.createBitmap(spriteSheet, i * 80, 0, 80, 80);
        }
        whiteCatSprites[12] = Bitmap.createBitmap(spriteSheet, 0, 0, 1, 1);
        for (int i = 0; i < blackCatSprites.length - 1; i++) {
            blackCatSprites[i] = Bitmap.createBitmap(spriteSheet, i * 80, 80, 80, 80);
        }
        blackCatSprites[12] = Bitmap.createBitmap(spriteSheet, 0, 0, 1, 1);
    }

    void loadTiles() {
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Bitmap.createBitmap(tileSheet, i * 8, 0, 8, 8);
        }
        attackMeter = Bitmap.createBitmap(tileSheet, 0, 8, 10, 4);
        round = Bitmap.createBitmap(tileSheet, 16, 8, 40, 8);
        fight = Bitmap.createBitmap(tileSheet, 0, 46, 74, 18);
        gameOver = Bitmap.createBitmap(tileSheet, 0, 16, 62, 30);
    }

    void resume(){
        pressedX = -1;
        pressedY = -1;
    }
}