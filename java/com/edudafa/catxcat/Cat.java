package com.edudafa.catxcat;


public class Cat {
    boolean
            hit;
    public static final int
            STANDING = 0,
            ATTACKING = 1,
            EVADING = 2,
            GOT_HIT = 3;
    int
            attackMeter,
            attackMeterTicks,
            damage,
            frameA,
            frameB,
            state,
            ticks;


    public void init() {
        hit = false;
        ticks = 0;
        frameA = 0;
        frameB = 12;
        state = 0;
        damage = 0;
        attackMeter = 3;
        attackMeterTicks = 0;
    }


    public void update() {
        this.ticks++;
        switch (state) {
            case STANDING:
                ticks = 0;
                frameA = 0;
                frameB = 12;
                break;
            case ATTACKING:
                attack();
                break;
            case EVADING:
                evade();
                break;
            case GOT_HIT:
                gotHit();
                break;
        }
        fillAttackMeter();
    }


    public void attack() {
        if (ticks == 16) {
            ticks = 0;
            frameA = 0;
            frameB = 12;
            state = 0;
        } else if (ticks == 13) {
            hit = false;
            frameA = 7;
            frameB = 8;
        } else if (ticks == 10) {
            hit = true;
            frameA = 5;
            frameB = 6;
        } else if (ticks == 7) {
            frameA = 3;
            frameB = 4;
        } else if (ticks == 4) {
            frameA = 2;
            attackMeter--;
        } else if (ticks == 1)
            frameA = 1;
    }


    public void evade() {
        if (ticks == 10) {
            frameA = 0;
            ticks = 0;
            state = 0;
        } else if (ticks == 7)
            frameA = 11;
        else if (ticks == 4)
            frameA = 10;
        else if (ticks == 1)
            frameA = 9;
    }


    public void gotHit() {
        if (ticks == 4) {
            ticks = 0;
            frameA = 0;
            frameB = 12;
            state = 0;
        } else if (ticks == 1) {
            frameA = 12;
            frameB = 12;
            Game.soundEngine.playSound(GlobalVar.SOUND_HIT);
        }
    }


    public void fillAttackMeter() {
        if (attackMeter < 3) {
            attackMeterTicks++;
            if (attackMeterTicks == 75) {
                attackMeter++;
                attackMeterTicks = 0;
            }
        }
    }
}