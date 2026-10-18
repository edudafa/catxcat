package com.edudafa.catxcat;

import android.graphics.Canvas;

public abstract class GameState {

    abstract void create();
    abstract void start();
    abstract void resume();
    abstract void pause();
    abstract void stop();
    abstract void update();
    abstract void draw(Canvas canvas);
}