package com.edudafa.catxcat;


import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;


public class Fight extends GameState{

    final int
            INTRO = 0,
            ROUND = 1,
            PLAYING = 2,
            GAME_OVER = 3;
    int
            ai,
            difficulty,
            fightState,
            roundCount,
            score,
            ticks;
    Paint
            paint;
    Cat
            whiteCat,
            blackCat;
    Input
            inputAtk,
            inputDef;
    Random
            random = new Random();


    void create(){
        inputAtk = new Input(90, 90, 180, 140);
        inputAtk.setEnabled();
        inputDef = new Input(0, 90, 90, 140);
        inputDef.setEnabled();

        whiteCat = new Cat();
        whiteCat.init();
        blackCat = new Cat();
        blackCat.init();

        paint = new Paint();
        paint.setARGB(255, 166, 53, 100);

        fightState = INTRO;
        ticks = 0;
        score = 0;
        roundCount = 1;
        difficulty = 16;

        Game.screenTransition.fadeIn();
    }
    void start() {}
    void resume() {}
    void pause() {}
    void stop() {
        Game.soundEngine.stopSound();
    }
    void update(){
        switch (fightState) {
            case INTRO: playIntro(); break;
            case ROUND: playRound(); break;
            case PLAYING: playing(); break;
            case GAME_OVER: playGameOver(); break;
        }
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(Game.view.backgroundFight, 0, 0, null);
        drawCats(canvas);
        drawAttackMeter(canvas);
        drawPlayerScore(canvas);
        drawLifeBars(canvas);
        switch (fightState){
            case ROUND:
                drawRoundScene(canvas);
                break;
            case GAME_OVER:
                drawGameOverScene(canvas);
                break;
        }
    }


    public void playIntro() {
        ticks++;
        switch (ticks){
            case 1 & 16:
                whiteCat.frameA = 1;
                blackCat.frameA = 1;
                break;
            case 4:
                whiteCat.frameA = 2;
                blackCat.frameA = 2;
                break;
            case 7:
                Game.soundEngine.playSound(GlobalVar.SOUND_MEOW);
                break;
            case 19:
                whiteCat.frameA = 0;
                blackCat.frameA = 0;
                break;
            case 26:
                ticks = 0;
                fightState = ROUND;
                break;
        }
    }
    public void playRound() {
        ticks++;

        whiteCat.update();
        blackCat.update();

        switch (ticks){
            case 10:
                Game.soundEngine.playSound(GlobalVar.SOUND_WIN);
                if (roundCount != 1) {
                    score++;
                    if (score > 99)
                        score = 99;
                    if (score >= GlobalVar.bestScore)
                        GlobalVar.bestScore = score;
                }
                break;
            case 45:
                whiteCat.init();
                blackCat.init();
                if (roundCount == 6)
                    difficulty--;
                if (roundCount == 13)
                    difficulty--;
                if (roundCount == 20)
                    difficulty--;
                break;
            case 60:
                fightState = PLAYING;
                ticks = 0;
                break;
        }
    }
    public void playing() {
        if (inputAtk.isTouched()) {
            GraphicEngine.pressed = true;
            if (whiteCat.state == Cat.STANDING && whiteCat.attackMeter > 0)
                whiteCat.state = Cat.ATTACKING;
        }
        if (inputDef.isTouched()) {
            GraphicEngine.pressed = true;
            if (whiteCat.state == Cat.STANDING)
                whiteCat.state = Cat.EVADING;
        }

        whiteCat.update();

        AILogic(whiteCat.state);
        blackCat.update();

        checkHit();

        whiteCat.fillAttackMeter();
        blackCat.fillAttackMeter();

        checkWinner();
    }
    public void checkHit() {
        if (whiteCat.hit) {
            if (blackCat.state != Cat.EVADING) {
                if (blackCat.state != Cat.GOT_HIT) {
                    blackCat.ticks = 0;
                    blackCat.state = Cat.GOT_HIT;
                    blackCat.damage += 6;
                    whiteCat.hit = false;
                }
            }
        }
        if (blackCat.hit) {
            if (whiteCat.state != Cat.EVADING) {
                if (whiteCat.state != Cat.GOT_HIT) {
                    whiteCat.ticks = 0;
                    whiteCat.state = Cat.GOT_HIT;
                    whiteCat.damage += 6;
                    blackCat.hit = false;
                }
            }
        }
    }
    public void checkWinner() {
        if (whiteCat.damage >= 60)
            fightState = GAME_OVER;
        else if (blackCat.damage >= 60) {
            roundCount++;
            if (roundCount > 99)
                roundCount = 99;
            fightState = ROUND;
        }
    }
    public void playGameOver() {
        ticks++;
        whiteCat.update();
        blackCat.update();

        if (ticks == 25)
            Game.soundEngine.playSound(GlobalVar.SOUND_GAME_OVER);
        if (ticks == 125){
            Game.screenTransition.fadeOut();
            Game.saveGame();
            Game.setNextGameState();
        }
    }
    public void AILogic(int i) {
        if (blackCat.state == Cat.STANDING) {
            if (i != Cat.GOT_HIT) {
                ai = random.nextInt(difficulty);
                if (ai == Cat.STANDING && blackCat.attackMeter > 0)
                    blackCat.state = Cat.ATTACKING;
                if (ai == difficulty - 1)
                    blackCat.state = Cat.EVADING;
            }
        }
    }


    public void drawPlayerScore(Canvas canvas) {
        if (score >= 10) {
            int un = score % 10;
            canvas.drawBitmap(Game.view.numbers[(score - un) / 10], 72, 4, null);
            canvas.drawBitmap(Game.view.numbers[un], 80, 4, null);
        } else {
            canvas.drawBitmap(Game.view.numbers[score], 76, 4, null);
        }
    }
    public void drawLifeBars(Canvas canvas) {
        canvas.drawRect((float) (whiteCat.damage + 9), 2, 69, 6, paint);
        canvas.drawRect(91, 2, (float) (151 - blackCat.damage), 6, paint);
    }
    public void drawRoundScene(Canvas canvas) {
        if (ticks > 45) {
            canvas.drawBitmap(Game.view.fight, 43, 32, null);
        } else if (ticks > 10) {
            canvas.drawBitmap(Game.view.round, 57, 37, null);
            if (roundCount >= 10) {
                int un = roundCount % 10;
                canvas.drawBitmap(Game.view.numbers[(roundCount - un) / 10], 95, 37, null);
                canvas.drawBitmap(Game.view.numbers[un], 102, 37, null);
            } else {
                canvas.drawBitmap(Game.view.numbers[roundCount], 95, 37, null);
            }
        }
    }
    public void drawGameOverScene(Canvas canvas) {
        if (ticks > 25)
            canvas.drawBitmap(Game.view.gameOver, 49, 34, null);
    }
    public void drawAttackMeter(Canvas canvas) {
        if (whiteCat.attackMeter >= 3)
            canvas.drawBitmap(Game.view.attackMeter, 29, 9, null);
        if (whiteCat.attackMeter >= 2)
            canvas.drawBitmap(Game.view.attackMeter, 44, 9, null);
        if (whiteCat.attackMeter >= 1)
            canvas.drawBitmap(Game.view.attackMeter, 59, 9, null);
        if (blackCat.attackMeter >= 3)
            canvas.drawBitmap(Game.view.attackMeter, 121, 9, null);
        if (blackCat.attackMeter >= 2)
            canvas.drawBitmap(Game.view.attackMeter, 106, 9, null);
        if (blackCat.attackMeter >= 1)
            canvas.drawBitmap(Game.view.attackMeter, 91, 9, null);
    }
    public void drawCats(Canvas canvas) {
        canvas.drawBitmap(Game.view.whiteCatSprites[whiteCat.frameA], 0, 24, null);
        canvas.drawBitmap(Game.view.blackCatSprites[blackCat.frameA], 80, 24, null);
        canvas.drawBitmap(Game.view.whiteCatSprites[whiteCat.frameB], 80, 24, null);
        canvas.drawBitmap(Game.view.blackCatSprites[blackCat.frameB], 0, 24, null);
    }
}
