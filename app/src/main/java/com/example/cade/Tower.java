package com.example.cade;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Tower extends Sprite{
    private boolean placed;
    private boolean upend = false;
    private Atack atack;
    private Rect See;
    private int Rest;
    private int RestVelosity;
    private int Prise;
    public Tower(Bitmap bitmap, int AmountFrames, double x, double y, int ms, boolean placed, Atack atack, int RestVelosity, int Prise) {
        super(x, y, bitmap, AmountFrames, ms, 0.1);
        this.placed = placed;
        this.atack = atack;
        this.RestVelosity = RestVelosity;
        this.Prise = Prise;
    }
    public boolean isUpend(){
        return upend;
    }
    public void setUpend(boolean upend){
        this.upend = upend;
    }
    public boolean isPlaced(){
        return placed;
    }
    public void setPlaced(boolean placed){
        this.placed = placed;
    }
    public Rect getSee(){
        return See;
    }
    public void setSee(Rect rect){
        this.See = rect;
    }
    public Atack getAtack(){
        return atack;
    }
    public void setRest(){
        this.Rest = atack.getTimeForRest();
    }
    public int getRest(){
        return Rest;
    }
    public void setAtackXY(double x, double y){
        this.atack.setX(x);
        this.atack.setY(y);
    }
    public int getPrise(){
        return Prise;
    }

    public void update (int a) {
        Rest = Math.max(Rest - RestVelosity, 0);
        update();
    }
}
