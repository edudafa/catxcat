package com.edudafa.catxcat;

import android.app.Activity;
import android.os.Bundle;

public class main extends Activity {
    Game game;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new Game(this);
        setContentView(Game.view);
    }

    public void onStart() { super.onStart(); game.onStart(); }
    public void onResume() { super.onResume(); game.onResume(); }
    public void onPause() { super.onPause(); game.onPause(); }
    public void onStop() { super.onStop(); game.onStop(); }
    public void onDestroy() { super.onDestroy(); game.onDestroy(); }
}