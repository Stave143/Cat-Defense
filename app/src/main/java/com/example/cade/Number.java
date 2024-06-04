package com.example.cade;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Number{
    private double x;
    private double y;
    private int number;
    public Number(double x, double y, int number){
        this.x = x;
        this.y = y;
        this.number = number;
    }
    public int getNumber(){
        return number;
    }
    public void setNumber(int number){
        this.number = number;
    }
    public void draw(Canvas canvas){
        Paint p = new Paint();
        p.setTextSize(50f);
        canvas.drawText( String.valueOf(number),  (float) x,(float) y, p);
    }
}
