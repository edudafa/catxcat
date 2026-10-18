package com.edudafa.catxcat;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class GlobalVar {
    public static int
            bestScore = 0,
            deviceScreenSizeX,
            deviceScreenSizeY;
    public static final int
            SOUND_HIT = 1,
            SOUND_GAME_OVER = 2,
            SOUND_MEOW = 3,
            SOUND_WIN = 4,
            SOUND_TITLE = 5;
    final String
            fileName = "catxcat.save";
    File file;


    public void saveData(Context context) {
        file = new File(context.getFilesDir(), fileName);
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, 0);
            fileOutputStream.write(bestScore);
            fileOutputStream.close();
        } catch (Exception e) {}
    }


    public void loadData(Context context) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            bestScore = fileInputStream.read();
            fileInputStream.close();
        } catch (Exception e) {
            bestScore = 0;
        }
        Log.d("GlobalVar","loadData() called, bestScore = " + bestScore);
    }
}