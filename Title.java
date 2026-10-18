package com.edudafa.catxcat;

import android.graphics.Canvas;
import android.util.Log;


public class Title extends GameState{
    int
            m,
            ticks;
    Input
            playButton;

    void create() {
        m = -16;
        ticks = 0;

        playButton = new Input(20, 20, 160, 115);
        playButton.setEnabled();

        Game.screenTransition.fadeIn();
        Game.soundEngine.playMusic();
    }
    void start() {}
    void resume() {
        Game.soundEngine.playMusic();
    }
    void pause() {
        Game.soundEngine.stopMusic();
    }
    void stop() {}
    public void update() {
        moveBackground();
        if(playButton.isTouched()){
            Log.d("TITLE ", " play button is pressed");
            Game.screenTransition.fadeOut();
            Game.soundEngine.fadeoutMusic();
            Game.setNextGameState();
        }
    }
    public void draw(Canvas canvas){
        drawTitle(canvas);
    }


    void drawTitle(Canvas canvas) {
        try {
            canvas.drawBitmap(Game.view.backgroundTitle, m, 0, null);
            canvas.drawBitmap(Game.view.foregroundTitle, 0, 0, null);
            if (GlobalVar.bestScore >= 10) {
                int un = GlobalVar.bestScore % 10;
                canvas.drawBitmap(Game.view.numbers[(GlobalVar.bestScore - un) / 10], 86, 88, null);
                canvas.drawBitmap(Game.view.numbers[un], 93, 88, null);
            } else {
                canvas.drawBitmap(Game.view.numbers[GlobalVar.bestScore], 93, 88, null);
            }
        } catch (Exception e) {}
    }
    void moveBackground() {
        ticks++;
        if (ticks > 1) {
            ticks = 0;
            m++;
        }
        if (m == 0)
            m = -16;
    }
}