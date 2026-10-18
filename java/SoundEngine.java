package com.edudafa.catxcat;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;


public class SoundEngine {
    boolean
            isFadeOutCalled;
    private final int
            SOUND_POOL_SIZE = 6;
    int
            musicStream;
    int[]
            sound,
            SEStream;
    private final float
            SE_VOLUME = 1.0f,
            MUSIC_VOLUME = 1.0f;
    float
            musicVolume;
    SoundPool
            soundPool;


    public SoundEngine(Context context) {
        isFadeOutCalled = false;
        musicVolume = MUSIC_VOLUME;
        sound = new int[SOUND_POOL_SIZE];
        SEStream = new int[SOUND_POOL_SIZE - 1];

        soundPool = new SoundPool.Builder().setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_GAME)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .build())
                .build();
        try {
            AssetManager assetManager = context.getAssets();
            sound[GlobalVar.SOUND_HIT] = soundPool.load(assetManager.openFd("sound/testHit5.wav"), 1);
            sound[GlobalVar.SOUND_GAME_OVER] = soundPool.load(assetManager.openFd("sound/gameOver.wav"), 1);
            sound[GlobalVar.SOUND_MEOW] = soundPool.load(assetManager.openFd("sound/meow.wav"), 1);
            sound[GlobalVar.SOUND_WIN] = soundPool.load(assetManager.openFd("sound/win.wav"), 1);
            sound[GlobalVar.SOUND_TITLE] = soundPool.load(assetManager.openFd("sound/title.wav"), 1);
        } catch (Exception e) { Log.d("SoundEngine", "could not load sounds."); }
    }
    public void destroy() {
        soundPool.release();
    }


    public void playSound(int i) {
        SEStream[i] = soundPool.play(sound[i], SE_VOLUME, SE_VOLUME, 0, 0, 1f);
    }
    public void stopSound(){
        for(int i = 0; i < SEStream.length; i++){
            soundPool.stop(SEStream[i]);
        }
    }
    public void playMusic() {
        Log.d("SoundEngine", "playMusic() called, musicStreamValue = " + musicStream);
        while (musicStream == 0){
            Log.d("SoundEngine", "musicStream = 0");
            musicStream = soundPool.play(sound[GlobalVar.SOUND_TITLE], MUSIC_VOLUME, MUSIC_VOLUME, 0, -1, 1f);
        }
    }
    public void stopMusic(){
        soundPool.stop(musicStream);
        musicStream = 0;
    }
    public void fadeoutMusic() {
        isFadeOutCalled = true;
    }


    void update(){
        if(isFadeOutCalled){
            if(musicStream != 0){
                musicVolume -= 0.2f;
                soundPool.setVolume(musicStream, musicVolume, musicVolume);
                if (musicVolume <= 0.2f){
                    musicVolume = MUSIC_VOLUME;
                    soundPool.stop(musicStream);
                    musicStream = 0;
                    isFadeOutCalled = false;
                    Log.d("SoundEngine", "fadeOut() ended, musicStreamValue = " + musicStream);
                }
            }
        }
    }
}