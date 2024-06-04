package com.example.cade;

import android.graphics.Bitmap;

public class Atack extends Sprite{
    private double Damage;
    private int TimeForRest;
    private int Frame = 1;
    private int AmountFrames;
    public Atack(double Damage, double x, double y, Bitmap bitmap, int AmountFrames, int ms, int TimeForRest){
        super(x, y, bitmap, AmountFrames, ms,  1);
        this.Damage = Damage;
        this.TimeForRest = TimeForRest;
        this.AmountFrames = AmountFrames;
    }
    public int getTimeForRest(){
        return TimeForRest;
    }
    public boolean isAtackOver(){
        return Frame >= AmountFrames;
    }
    public double getDamage(){
        return Damage;
    }
    public void setFrame(int Frame) {
        this.Frame = Frame;
    }
    public int getFrame(){
        return Frame;
    }

    public void update(int a){
        Frame++;
        update();
    }
}
