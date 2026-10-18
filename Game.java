package com.edudafa.catxcat;

import android.content.Context;


public class Game implements Runnable{
    boolean
            isRunning;
    static boolean
            isNextGameStateToBeSet,
            isGameToBeSaved;
    static final int
            TITLE = 0,
            FIGHT = 1;
    int
            nextGameState;
    static final long
            gameLoopTime = 25;
    long
            endLoop,
            startLoop,
            wait;
    Context context;
    Thread thread;
    GameState gameState;
    GlobalVar globalVar;
    static SoundEngine soundEngine;
    static GraphicEngine view;
    static ScreenTransition screenTransition;


    public Game(Context context){
        this.context = context;

        globalVar = new GlobalVar();
        globalVar.loadData(context);

        soundEngine = new SoundEngine(context);
        view = new GraphicEngine(context);

        screenTransition = new ScreenTransition();
        screenTransition.initBlackScreen();

        nextGameState = TITLE;
        setGameState();
    }


    void onStart(){}
    void onResume(){
        gameState.resume();
        view.resume();
        thread = new Thread(this);
        thread.start();
        isRunning = true;
    }
    void onPause(){
        gameState.pause();
        isRunning = false;
    }
    void onStop(){
        gameState.stop();
        isRunning = false;
    }
    void onDestroy(){
        soundEngine.destroy();
        isRunning = false;
    }


    public void updateGame() {
        gameState.update();
        screenTransition.update();
        soundEngine.update();

        if(ScreenTransition.state == ScreenTransition.NONE)
            if(isNextGameStateToBeSet)
                setGameState();

        if(isGameToBeSaved)
            globalVar.saveData(context);
    }


    static void setNextGameState(){
        isNextGameStateToBeSet = true;
    }
    void setGameState(){
        isNextGameStateToBeSet = false;
        if(nextGameState == TITLE) {
            gameState = new Title();
            nextGameState = FIGHT;
        } else {
            gameState = new Fight();
            nextGameState = TITLE;
        }
        gameState.create();
    }
    static void saveGame(){
        isGameToBeSaved = true;
    }


    public void run() {
        while (isRunning) {
            startLoop = System.nanoTime();

            updateGame();
            view.draw(gameState);

            endLoop = (System.nanoTime() - startLoop) / 1000000;

            if (endLoop < gameLoopTime) {
                wait = gameLoopTime - endLoop;
                try {
                    Thread.sleep(wait);
                } catch (Exception e) { }
            }
        }
    }
}