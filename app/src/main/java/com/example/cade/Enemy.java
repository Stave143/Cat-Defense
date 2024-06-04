package com.example.cade;

import android.graphics.Bitmap;

public class Enemy extends Sprite{
    private double Speed;
    private double HP;
    public Enemy(double Speed, Bitmap bitmap, int AmountFrames, double x, double y, double HP, int ms) {
        super(x, y, bitmap, AmountFrames, ms, 0.1);
        this.Speed = Speed;
        this.HP = HP;
    }
    public void setHP(double HP){
        this.HP = HP;
    }
    public double getHP(){
        return HP;
    }

    public void update (int a) {
        update();

        setX(getX() + Speed);
    }

}
